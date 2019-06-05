package bare.bones.project.app

import android.os.StrictMode
import androidx.multidex.MultiDexApplication
import bare.bones.project.BuildConfig
import bare.bones.project.BuildConfig.DEBUG
import com.facebook.stetho.Stetho
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class MainApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        if (DEBUG) {
            Timber.plant(Timber.DebugTree())
            Stetho.initializeWithDefaults(this@MainApp)
        } else {
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

    private fun strictMode() {
        if (!BuildConfig.DEBUG) return

        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
            .detectAll()
            .penaltyLog()
            .build()
        )
        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
            .detectAll()
            .detectLeakedSqlLiteObjects()
            .detectLeakedClosableObjects()
            .penaltyLog()
            .build()
        )
    }
}