package bare.bones.project.base

import bare.bones.project.base.rx.RxFragment

abstract class BaseFragment<P : BasePresenter<V>, V : BaseView> : RxFragment<P, V>()