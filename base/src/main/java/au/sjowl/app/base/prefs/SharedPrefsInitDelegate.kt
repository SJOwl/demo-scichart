package au.sjowl.app.base.prefs

import android.content.Context
import kotlin.reflect.KProperty

class SharedPrefsInitDelegate<T>(
    val context: Context,
    val key: String,
    val defaultValue: T
) {

    private val value = when (defaultValue) {
        is Boolean -> context.getProperty(key, defaultValue)
        is Float -> context.getProperty(key, defaultValue)
        is Int -> context.getProperty(key, defaultValue)
        is Long -> context.getProperty(key, defaultValue)
        is String -> context.getProperty(key, defaultValue)
        else -> throw IllegalStateException("This type can not be put in bundle")
    } as T

    @Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
    operator fun getValue(
        s: Any,
        property: KProperty<*>
    ): T {
        return value
    }

    operator fun setValue(
        s: Any,
        property: KProperty<*>,
        value: T
    ) {
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
