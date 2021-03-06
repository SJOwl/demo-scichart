package au.sjowl.app.base.navigation

import java.util.HashMap
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router

class LocalCiceroneHolder {

    private val containers: HashMap<String, Cicerone<Router>> = HashMap()

    fun getCicerone(containerTag: String): Cicerone<Router> {
        if (!containers.containsKey(containerTag)) {
            containers[containerTag] = Cicerone.create()
        }
        return containers[containerTag] as Cicerone<Router>
    }
}
