package au.sjowl.app.base.prefs

import android.content.Context
import au.sjowl.app.base.view.bg
import kotlin.reflect.KProperty

class CachedSharedPrefsDelegate<T>(
    val context: Context,
    val key: String,
    private val defaultValue: T
) {

    private var v: T? = null

    @Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
    operator fun getValue(
        s: Any,
        property: KProperty<*>
    ): T {
        return when (v) {
            null -> when (defaultValue) {
                is Boolean -> context.getProperty(key, defaultValue)
                is Float -> context.getProperty(key, defaultValue)
                is Int -> context.getProperty(key, defaultValue)
                is Long -> context.getProperty(key, defaultValue)
                is String -> context.getProperty(key, defaultValue)
                else -> throw IllegalStateException("This type can not be put in bundle")
            } as T
            else -> v as T
        }
    }

    operator fun setValue(
        s: Any,
        property: KProperty<*>,
        value: T
    ) {
        v = value

        bg {
            when (defaultValue) {
                is Boolean -> context.setProperty(key, value as Boolean)
                is Float -> context.setProperty(key, value as Float)
                is Int -> context.setProperty(key, value as Int)
                is Long -> context.setProperty(key, value as Long)
                is String -> context.setProperty(key, value as String)
                else -> throw IllegalStateException("This type can not be put in bundle")
            }
        }
    }
}
