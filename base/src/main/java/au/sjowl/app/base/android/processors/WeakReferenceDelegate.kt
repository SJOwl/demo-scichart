package au.sjowl.app.base.android.processors

import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

class WeakReferenceDelegate<T> {

    private var weakRef: WeakReference<T>? = null

    operator fun getValue(s: Any, property: KProperty<*>): T? = weakRef?.get()

    operator fun setValue(s: Any, property: KProperty<*>, value: T?) {
        if (value != null) {
            weakRef = WeakReference(value)
        } else {
            weakRef?.clear()
        }
    }
}
