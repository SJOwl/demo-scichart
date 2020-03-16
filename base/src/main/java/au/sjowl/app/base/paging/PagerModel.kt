package au.sjowl.app.base.paging

import com.google.gson.annotations.SerializedName

data class PagerModel(
    @SerializedName("pagesCount") var pagesCount: Int = 0,
    @SerializedName("pageNumber") var pageNumber: Int = 1,
    @SerializedName("pageSize") val pageSize: Int = 20
) {
    fun canLoadMore() = pageNumber < pagesCount - 1

    fun applyDefaults() {
        pagesCount = 2
        pageNumber = 0
    }

    fun isFirstPage() = pageNumber == 1
    fun isSecondPage() = pageNumber == 2
}
