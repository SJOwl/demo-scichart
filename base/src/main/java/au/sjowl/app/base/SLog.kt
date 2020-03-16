@file:Suppress("unused")

package au.sjowl.app.base

import android.annotation.SuppressLint
import au.sjowl.app.base.SLog.getMessage
import java.text.SimpleDateFormat
import java.util.GregorianCalendar
import java.util.Locale
import timber.log.Timber

object SLog {

    const val TAG_DEFAULT = ""

    private val calendar = GregorianCalendar()

    @SuppressLint("ConstantLocale")
    private val locale = Locale.getDefault()

    private val dateFormatDay = SimpleDateFormat("hh:mm:ss.SSS", locale)

    fun time(): String {
        calendar.timeInMillis = System.currentTimeMillis()
        return dateFormatDay.format(calendar.time)
    }

    inline fun getMessage(message: String): String {
        return "isj | ${time()} |${"%-33s".format("@${Thread.currentThread().name}")}| $message"
    }

    inline fun i(tag: String, message: String): Unit = Timber.i(getMessage(message))
    inline fun v(tag: String, message: String): Unit = Timber.v(getMessage(message))
    inline fun d(tag: String, message: String): Unit = Timber.d(getMessage(message))
    inline fun w(tag: String, message: String): Unit = Timber.w(getMessage(message))
    inline fun e(tag: String, message: String): Unit = Timber.e(getMessage(message))

    inline fun i(message: String) = i(TAG_DEFAULT, message)
    inline fun v(message: String) = v(TAG_DEFAULT, message)
    inline fun d(message: String) = d(TAG_DEFAULT, message)
    inline fun w(message: String) = w(TAG_DEFAULT, message)
    inline fun e(message: String) = e(TAG_DEFAULT, message)

    inline fun e(tag: String?, message: Throwable?) = e(tag.orEmpty(), message.toString())
}

fun logThread(message: String = "") = SLog.d("$message Thread is ${Thread.currentThread().name}")

inline fun logi(message: String?) = SLog.i(message.toString())
inline fun logv(message: String?) = SLog.v(message.toString())
inline fun logd(message: String?) = SLog.d(message.toString())
inline fun logw(message: String?) = SLog.w(message.toString())
inline fun loge(message: String?) = SLog.e(message.toString())

inline fun logi(formatter: String, vararg args: Any) =
    if (args.isEmpty()) SLog.i(formatter) else SLog.i(formatter.format(args))

inline fun logv(formatter: String, vararg args: Any) =
    if (args.isEmpty()) SLog.v(formatter) else SLog.v(formatter.format(args))

inline fun logd(formatter: String, vararg args: Any) =
    if (args.isEmpty()) SLog.d(formatter) else SLog.d(formatter.format(args))

inline fun logw(formatter: String, vararg args: Any) =
    if (args.isEmpty()) SLog.w(formatter) else SLog.w(formatter.format(args))

inline fun loge(formatter: String, vararg args: Any) =
    if (args.isEmpty()) SLog.e(formatter) else SLog.e(formatter.format(args))

inline fun loge(t: Throwable) = Timber.e(t)

inline fun logi(list: List<Any>, message: String = "") =
    SLog.i("list $message: ${list.joinToString(",")}")

inline fun logv(list: List<Any>, message: String = "") =
    SLog.v("list $message: ${list.joinToString(",")}")

inline fun logd(list: List<Any>, message: String = "") =
    SLog.d("list $message: \n${list.joinToString("\n")}")

inline fun logw(list: List<Any>, message: String = "") =
    SLog.w("list $message: ${list.joinToString(",")}")

inline fun loge(list: List<Any>, message: String = "") =
    SLog.e("list $message: ${list.joinToString(",")}")

private const val formatter = "lifecycle |%-30s|%-20s|%s"
fun logLifecycle(entity: Any?, method: String) {
    if (entity != null) logv(formatter.format(entity::class.java.simpleName, method, entity))
    else logv(formatter.format("null", method, entity))
}

fun logt(message: String?) = println(getMessage(message.toString()))
fun logtx(message: String?) = println(message.toString())
