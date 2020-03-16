package au.sjowl.app.base.paging

abstract class PagerDelegate {

    open var isLoadingNewPage = false

    var pagerModel = PagerModel()

    abstract fun requestNextPage()

    abstract fun requestNewQuery()
}
