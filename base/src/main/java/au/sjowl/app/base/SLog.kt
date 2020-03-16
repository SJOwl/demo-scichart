package au.sjowl.app.base

import android.util.Log

object SLog {

    inline fun i(tag: String, message: String) {
        if (BuildConfig.BUILD_TYPE == "debug") Log.i("isj_$tag", getMessage(message))
    }

    inline fun v(tag: String, message: String) {
        if (BuildConfig.BUILD_TYPE == "debug") Log.v("isj_$tag", getMessage(message))
    }

    inline fun d(tag: String, message: String) {
        if (BuildConfig.BUILD_TYPE == "debug") Log.d("isj_$tag", getMessage(message))
    }

    inline fun w(tag: String, message: String) {
        if (BuildConfig.BUILD_TYPE == "debug") Log.w("isj_$tag", getMessage(message))
    }

    inline fun e(tag: String, message: String) {
        if (BuildConfig.BUILD_TYPE == "debug") Log.e("isj_$tag", getMessage(message))
    }

    inline fun getMessage(message: String) = "${"%-33s".format("@${Thread.currentThread().name}")}| $message"

    inline fun i(message: String) = i("", message)
    inline fun v(message: String) = v("", message)
    inline fun d(message: String) = d("", message)
    inline fun w(message: String) = w("", message)
    inline fun e(message: String) = e("", message)
    inline fun e(tag: String?, message: Throwable?) = e(tag.orEmpty(), message.toString())
}

fun logThread(message: String = "") = SLog.d("$message Thread is ${Thread.currentThread().name}")

inline fun slog(tag: String, message: String) = SLog.d(tag, message)
inline fun slog(message: String) = SLog.d(message)

private const val formatter = "lifecycle |%-30s|%-15s|%s"
fun logLifecycle(entity: Any?, method: String) {
    if (entity != null) SLog.v(formatter.format(entity::class.java.simpleName, method, entity))
    else SLog.v(formatter.format("null", method, entity))
}
