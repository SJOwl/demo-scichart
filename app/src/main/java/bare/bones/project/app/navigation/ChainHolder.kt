package bare.bones.project.app.navigation

import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference

interface ChainHolder {
    val chain: List<WeakReference<Fragment>>
}