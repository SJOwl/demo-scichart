package au.sjowl.app.base.prefs

import android.content.Context
import au.sjowl.app.base.view.bg
import com.google.gson.Gson
import kotlin.reflect.KProperty

@Suppress("unused", "UNCHECKED_CAST")
class TypedPrefDelegate<T : Any?>(
    private val context: Context,
    private val gson: Gson,
    private val key: String,
    private val clazz: Class<T>,
    private val producer: (() -> T)
) {

    private var v: T? = null

    operator fun getValue(s: Any, property: KProperty<*>): T {
        return when (v) {
            null -> {
                val json = context.getProperty(key, "null")
                return gson.fromJson(json, clazz) ?: producer.invoke()
            }
            else -> v as T
        }
    }

    operator fun setValue(s: Any, property: KProperty<*>, value: T?) {
        v = value
        bg {
            context.setProperty(key, gson.toJson(value))
        }
    }
}
