@file:Suppress("unused")

package au.sjowl.app.base

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.graphics.scale
import java.io.File
import java.io.InputStream
import kotlin.math.max

object ResourcesUtils {
    fun getResourceAsString(resourceId: String): String {
        return this::class.java.classLoader
            .getResourceAsStream(resourceId)
            .bufferedReader(Charsets.UTF_8).use {
                it.readText()
            }
    }

    fun getResourceAsLines(resourceId: String): List<String> {
        return this::class.java.classLoader
            .getResourceAsStream(resourceId)
            .bufferedReader(Charsets.UTF_8).use {
                it.lineSequence().toList()
            }
    }

    fun getFile(resourceId: String): File {
        return File(
            this::class.java.classLoader
                .getResource(resourceId).file
        )
    }
}

fun File.copyInputStream(inputStream: InputStream?): File {
    inputStream?.use { input ->
        this.outputStream().use { fileOut ->
            input.copyTo(fileOut)
        }
    }
    return this
}

/**
 * Saves file at localFileImageUrl to File and returns it.
 * If url is not local, for example, http://..., result is null
 */
fun Context.urlToFile(localFileImageUrl: String?, tempName: String = "temp"): File? {
    return if (localFileImageUrl != null) {
        try {
            File(cacheDir, tempName)
                .copyInputStream(
                    contentResolver.openInputStream(Uri.parse(localFileImageUrl))
                        ?: error("No file $localFileImageUrl")
                )
        } catch (e: Exception) {
            loge(e)
            null
        }
    } else {
        null
    }
}

/**
 * Saves image from url to image with max size side = sizeMax px
 */
fun Context.imageUrlTo2mbFile(
    localFileImageUrl: String?,
    tempName: String = "temp",
    sizeMax: Int = 300
): File? {
    return if (localFileImageUrl != null) {
        try {
            File(cacheDir, tempName).apply {
                val b = BitmapFactory
                    .decodeStream(contentResolver.openInputStream(Uri.parse(localFileImageUrl)))

                /** sizeMax * sizeMax * 2 bytes / 1048576 bytes < 2mb */
                val scale = 1f * max(b.width, b.height) / 1024.coerceAtMost(sizeMax)

                logd("resize image to ${(b.width / scale).toInt()}, ${(b.height / scale).toInt()}")

                b.scale((b.width / scale).toInt(), (b.height / scale).toInt())
                    .compress(Bitmap.CompressFormat.PNG, 100, outputStream())
            }
        } catch (e: Exception) {
            loge(e)
            null
        }
    } else {
        null
    }
}
