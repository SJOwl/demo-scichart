package au.sjowl.app.base.prefs

import android.content.Context
import android.content.SharedPreferences

fun Context.getPrivateSharedPreferences(): SharedPreferences {
    return this.getSharedPreferences("$packageName:preference", Context.MODE_PRIVATE)
}

fun <T> Context.setProperty(key: String, value: T) {
    val spe = getPrivateSharedPreferences().edit()
    when (value) {
        is Boolean -> spe.putBoolean(key, value as Boolean).apply()
        is Float -> spe.putFloat(key, value as Float).apply()
        is Int -> spe.putInt(key, value as Int).apply()
        is Long -> spe.putLong(key, value as Long).apply()
        is String -> spe.putString(key, value as String).apply()
        else -> throw IllegalStateException("This type can not be put in bundle")
    }
}

fun <T> Context.getProperty(key: String, defaultValue: T): T {

    val sp = getPrivateSharedPreferences()
    return when (defaultValue) {
        is Boolean -> sp.getBoolean(key, defaultValue)
        is Float -> sp.getFloat(key, defaultValue)
        is Int -> sp.getInt(key, defaultValue)
        is Long -> sp.getLong(key, defaultValue)
        is String -> sp.getString(key, defaultValue)
        else -> throw IllegalArgumentException("This type is not supported by shared prefs")
    } as T
}
