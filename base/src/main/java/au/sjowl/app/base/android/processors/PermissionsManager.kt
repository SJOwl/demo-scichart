package au.sjowl.app.base.android.processors

import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import au.sjowl.app.base.asString
import au.sjowl.app.base.logd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class GrantResult(
    val permission: String,
    val granted: Boolean
)

class PermissionsManager(
    private val context: Application
) : ActivityProcessor() {

    private val channel = Channel<GrantResult>()

    private val code = ActivityRequestCodes.PERMISSION

    override fun processResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean = false

    fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PERMISSION_GRANTED
    }

    suspend fun requestPermissions(permission: String): GrantResult =
        withContext(Dispatchers.Main) {
            logd("Requesting permission: $permission")
            return@withContext when {
                hasPermission(permission) -> {
                    logd("Already have this permission!")
                    GrantResult(permission, true)
                }
                else -> {
                    activity?.let {
                        ActivityCompat.requestPermissions(
                            it,
                            arrayOf(permission),
                            code
                        )
                    }
                    fragment?.requestPermissions(arrayOf(permission), code)
                    channel.receive()
                }
            }
        }

    fun processResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        GlobalScope.launch(Dispatchers.Main) {
            logd("processResult(): requestCode= $requestCode, permissions: ${permissions.asString()}, grantResults: ${grantResults.asString()}")
            for ((index, permission) in permissions.withIndex()) {
                val granted = grantResults[index] == PERMISSION_GRANTED
                val result = GrantResult(permission, granted)
                logd("Permission grant result: $result")
                channel.send(result)
            }
        }
    }
}
