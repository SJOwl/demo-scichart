package au.sjowl.scicharts.demo.base

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.annotation.VisibleForTesting
import androidx.annotation.VisibleForTesting.PRIVATE
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import au.sjowl.app.base.asString
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.Single.just
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

data class GrantResult(
    val permission: String,
    val granted: Boolean
)

interface PermissionsManager {

    fun onGrantResult(): Observable<GrantResult>

    fun attach(activity: Activity)

    fun hasPermission(permission: String = Manifest.permission.WRITE_EXTERNAL_STORAGE): Boolean

    fun requestPermission(permission: String = Manifest.permission.WRITE_EXTERNAL_STORAGE, waitForGranted: Boolean = false): Single<GrantResult>

    fun processResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)

    fun detach(activity: Activity)
}

class RealPermissionsManager(
    private val context: Application,
    private val mainScheduler: Scheduler
) : PermissionsManager {

    @VisibleForTesting(otherwise = PRIVATE)
    var activity: Activity? = null

    private val relay = PublishSubject.create<GrantResult>()

    override fun onGrantResult(): Observable<GrantResult> = relay.share().observeOn(mainScheduler)

    override fun attach(activity: Activity) {
        Timber.d("attach(): $activity")
        this.activity = activity
    }

    override fun requestPermission(permission: String, waitForGranted: Boolean): Single<GrantResult> =
        requestPermission(REQUEST_CODE, permission, waitForGranted)

    override fun processResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Timber.d("processResult(): requestCode= %d, permissions: %s, grantResults: %s",
            requestCode, permissions.asString(), grantResults.asString())
        for ((index, permission) in permissions.withIndex()) {
            val granted = grantResults[index] == PERMISSION_GRANTED
            val result =
                GrantResult(permission, granted)
            Timber.d("Permission grant result: %s", result)
            relay.onNext(result)
        }
    }

    override fun detach(activity: Activity) {
        // === is referential equality - returns true if they are the same instance
        if (this.activity === activity) {
            Timber.d("detach(): $activity")
            this.activity = null
        }
    }

    override fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PERMISSION_GRANTED
    }

    private fun requestPermission(code: Int, permission: String, waitForGranted: Boolean): Single<GrantResult> {
        Timber.d("Requesting permission: %s", permission)
        if (hasPermission(permission)) {
            Timber.d("Already have this permission!")
            return just(
                GrantResult(
                    permission,
                    true
                ).also {
                relay.onNext(it)
            })
        }

        val attachedTo = activity ?: throw IllegalStateException("Not attached")
        ActivityCompat.requestPermissions(attachedTo, arrayOf(permission), code)
        return onGrantResult()
            .filter { it.permission == permission }
            .filter {
                if (waitForGranted) {
                    // If we are waiting for granted, only allow emission if granted is true
                    it.granted
                } else {
                    // Else continue
                    true
                }
            }
            .take(1)
            .singleOrError()
    }

    companion object {
        @VisibleForTesting(otherwise = PRIVATE)
        const val REQUEST_CODE = 69
    }
}
