package bare.bones.project.app.navigation

interface BackButtonListener {
    /**
     * @return true if consumed. Fragments which are roots of tab supposed to return true.
     * Other child fragments should return false
     */
    fun onBackPressed(): Boolean
}