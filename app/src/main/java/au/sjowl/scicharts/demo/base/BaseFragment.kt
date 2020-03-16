package au.sjowl.scicharts.demo.base

import au.sjowl.scicharts.demo.base.rx.RxFragment

abstract class BaseFragment<P : BasePresenter<V>, V : BaseView> : RxFragment<P, V>()
