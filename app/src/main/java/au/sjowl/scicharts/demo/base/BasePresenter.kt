package au.sjowl.scicharts.demo.base

import au.sjowl.scicharts.demo.base.rx.RxPresenter

abstract class BasePresenter<V : BaseView> : RxPresenter<V>()
