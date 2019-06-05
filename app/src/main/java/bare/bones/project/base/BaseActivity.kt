package bare.bones.project.base

import android.os.Bundle
import bare.bones.project.base.rx.RxActivity
import org.koin.android.ext.android.inject

abstract class BaseActivity<P : BasePresenter<V>, V : BaseView> : RxActivity<P, V>() {

    val permissionsManager: PermissionsManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionsManager.attach(this)
    }

    override fun onDestroy() {
        permissionsManager.detach(this)
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionsManager.processResult(requestCode, permissions, grantResults)
    }
}