package bare.bones.project.base

import bare.bones.project.base.rx.RxPresenter

abstract class BasePresenter<V : BaseView> : RxPresenter<V>()