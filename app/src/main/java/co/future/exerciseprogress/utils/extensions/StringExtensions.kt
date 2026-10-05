package co.future.exerciseprogress.utils.extensions

import java.util.UUID

fun String.isNilUUIDString(): Boolean {
    return this == UUID_NIL_STRING
}

fun String?.isEmpty(): Boolean {
    return this == null || this.isEmpty()
}

fun String?.isNotEmpty(): Boolean {
    return !isEmpty()
}

fun String?.nilIfEmpty(): String? {
    return if (isNotEmpty()) this else null
}

const val UUID_NIL_STRING = "00000000-0000-0000-0000-000000000000"

val nilUUID: UUID
    get() = UUID.fromString(nilUUIDString)

val nilUUIDString: String
    get() = "00000000-0000-0000-0000-000000000000"