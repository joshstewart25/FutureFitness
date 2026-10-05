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