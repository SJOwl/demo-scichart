package au.sjowl.app.base.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

interface FragmentTransactionCommand {
    fun execute(
        currentFragment: Fragment?,
        nextFragment: Fragment?,
        fragmentTransaction: FragmentTransaction
    )
}
