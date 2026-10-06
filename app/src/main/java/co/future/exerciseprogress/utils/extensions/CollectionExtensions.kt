package co.future.exerciseprogress.utils.extensions

fun <T> Collection<T>.safeGet(index: Int): T? {
    return if (index in 0 until size) {
        elementAtOrNull(index)
    } else {
        null
    }
}

fun <T> Collection<T>.isNotEmpty(): Boolean {
    return !isEmpty()
}

// Removes the item if it is in the set, adds it if it is not.
fun <T> Set<T>.toggled(item: T): Set<T> {
    return if (item in this) this - item else this + item
}