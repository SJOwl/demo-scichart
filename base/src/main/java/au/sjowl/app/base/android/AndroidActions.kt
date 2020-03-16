package au.sjowl.app.base.android

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import au.sjowl.app.base.logd

fun openUrl(context: Context, url: String) {
    logd("open url = \'$url\'")
    ContextCompat.startActivity(context, Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }, null)
}
