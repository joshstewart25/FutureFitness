package co.future.exerciseprogress.utils.extensions

fun <T> Set<T>.toggled(item: T): Set<T> {
    return if (item in this) {
        this - item
    } else {
        this + item
    }
}
