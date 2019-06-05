package au.sjowl.app.base

import android.content.Context
import android.net.Uri
import android.speech.tts.TextToSpeech
import android.util.TypedValue
import androidx.core.content.ContextCompat
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.util.Locale

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

fun Context.readFileToString(path: String): String {
    val file = File(path)
    return file.readText()
}

fun Context.getTTS(lang: String): TextToSpeech? {
    var textToSpeech: TextToSpeech? = null
    textToSpeech = TextToSpeech(this, TextToSpeech.OnInitListener { status ->
        if (status == TextToSpeech.SUCCESS) {
            when (textToSpeech?.setLanguage(Locale(lang))) {
                TextToSpeech.LANG_MISSING_DATA,
                TextToSpeech.LANG_NOT_SUPPORTED -> SLog.e("Language is not supported")
                else -> {
                }
            }
        } else {
            SLog.e("Init of TTS failed")
        }
    })
    return textToSpeech
}

fun Context.getColorCompat(id: Int) = ContextCompat.getColor(this, id)

val Context.statusBarHeight: Int
    get() {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId)
        }
        return result
    }

fun Context.getColorFromAttr(attrId: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attrId, typedValue, true)
    return typedValue.data
}