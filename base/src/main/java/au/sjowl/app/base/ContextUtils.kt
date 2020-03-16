@file:Suppress("unused")

package au.sjowl.app.base

import android.app.NotificationManager
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.util.TypedValue
import android.widget.ImageView
import androidx.core.content.ContextCompat
import au.sjowl.app.base.glide.GlideApp
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

fun Context.readToString(resource: Int): String {
    var res: String? = null
    val inputStream = resources.openRawResource(resource)
    val baos = ByteArrayOutputStream()
    val b = ByteArray(1)
    try {
        while (inputStream.read(b) != -1) {
            baos.write(b)
        }
        res = baos.toString()
        inputStream.close()
        baos.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return res ?: throw IllegalStateException("Could not get string from raw file")
}

fun Context.readTextFromUri(uri: Uri): String {
    val stringBuilder = StringBuilder()
    contentResolver.openInputStream(uri)?.use { inputStream ->
        val reader = BufferedReader(InputStreamReader(inputStream))
        var line: String? = reader.readLine()
        while (line != null) {
            stringBuilder.append(line)
            line = reader.readLine()
        }
    }
    return stringBuilder.toString()
}

fun readFileToString(path: String): String {
    val file = File(path)
    return file.readText()
}

fun Context.getTTS(lang: String): TextToSpeech? {
    var textToSpeech: TextToSpeech? = null
    textToSpeech = TextToSpeech(this, TextToSpeech.OnInitListener { status ->
        if (status == TextToSpeech.SUCCESS) {
            when (textToSpeech?.setLanguage(Locale(lang))) {
                TextToSpeech.LANG_MISSING_DATA,
                TextToSpeech.LANG_NOT_SUPPORTED -> loge("Language is not supported")
                else -> {
                }
            }
        } else {
            loge("Init of TTS failed")
        }
    })
    return textToSpeech
}

fun Context.getColorCompat(id: Int): Int = ContextCompat.getColor(this, id)

val Context.statusBarHeight: Int
    get() {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

fun Context.getColorFromAttr(attrId: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attrId, typedValue, true)
    return typedValue.data
}

fun Context.copyToClipboard(text: String) {
    (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).primaryClip =
        ClipData.newPlainText("data", text)
}

fun Context.colorStateList(id: Int): ColorStateList =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        resources.getColorStateList(id, theme)
    } else {
        resources.getColorStateList(id)
    }

fun ImageView.setTintList(id: Int) {
    imageTintList = context.colorStateList(id)
}

fun Context.isPackageInstalled(targetPackage: String): Boolean {
    val pm = packageManager
    try {
        val info = pm.getPackageInfo(targetPackage, PackageManager.GET_META_DATA)
    } catch (e: PackageManager.NameNotFoundException) {
        return false
    }
    return true
}

fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager: ConnectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnected
}

fun Context.jobScheduler() =
    (applicationContext.getSystemService(JobService.JOB_SCHEDULER_SERVICE) as JobScheduler)

fun Context.audioManager() = (getSystemService(Context.AUDIO_SERVICE) as AudioManager)
fun Context.notificationManager() =
    (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)

fun batteryOptimizationsTurnOff(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val intent = Intent()
        val packageName = context.packageName
        ContextCompat.getSystemService(context, PowerManager::class.java)?.let { pm ->
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.data = Uri.parse("package:$packageName")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                ContextCompat.startActivity(context, intent, null)
            }
        }
    }
}

suspend fun loadBitmap(context: Context, url: String): Bitmap =
    suspendCancellableCoroutine { cont ->
        cont.invokeOnCancellation {
        }
        GlideApp.with(context)
            .asBitmap()
            .load(url)
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    cont.resume(resource)
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    cont.resumeWithException(Exception("Bitmap load failed"))
                }
            })
    }
