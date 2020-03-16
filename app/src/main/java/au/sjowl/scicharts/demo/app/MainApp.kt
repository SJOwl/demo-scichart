package au.sjowl.scicharts.demo.app

import android.os.StrictMode
import androidx.multidex.MultiDexApplication
import au.sjowl.app.base.view.bg
import au.sjowl.scicharts.demo.BuildConfig
import com.facebook.stetho.Stetho
import com.scichart.charting.visuals.SciChartSurface
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class MainApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        bg { license() }
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Stetho.initializeWithDefaults(this@MainApp)
        }

        startKoin {
            if (BuildConfig.DEBUG) {
                androidLogger(Level.DEBUG)
            }
            androidContext(this@MainApp)
            modules(appComponent)
        }

        strictMode()
    }

    private fun license() {
        SciChartSurface.setRuntimeLicenseKey(BuildConfig.SciChartLicenseKey)
    }

    private fun strictMode() {
        if (!BuildConfig.DEBUG) return

        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()
        )
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectAll()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .build()
        )
    }
}
