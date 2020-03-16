package au.sjowl.app.base.navigation

interface BackButtonListener {
    /**
     * @return true if consumed. Fragments which are roots of tab supposed to return true.
     * Other child fragments should return false
     */
    fun onBackPressed(): Boolean
}
