@file:Suppress("unused")

package au.sjowl.app.base

fun IntArray.asString(): String {
    return joinToString(separator = ", ", prefix = "[", postfix = "]")
}

fun LongArray.asString(): String {
    return joinToString(separator = ", ", prefix = "[", postfix = "]")
}

fun <T> Array<T>.asString(): String {
    return joinToString(separator = ", ", prefix = "[", postfix = "]")
}

fun <K, V> MutableMap<K, V>.putIfAbsentt(key: K, value: V): V? {
    var v: V? = get(key)
    if (v == null) {
        v = put(key, value)
    }
    return v
}

fun List<*>.contentEquals(other: List<*>): Boolean {
    if (size != other.size) return false
    var i = 0
    while (i < size - 1) {
        if (get(i) != other[i]) return false
        i++
    }
    return true
}

inline fun <T, R> List<T>.listMap(transform: (List<T>) -> List<R>): List<R> {
    return transform(this)
}

inline fun <T, R> Iterable<T>.mapMutable(transform: (T) -> R): MutableList<R> {
    return mapTo(ArrayList<R>(if (this is Collection<*>) this.size else 10), transform)
}
