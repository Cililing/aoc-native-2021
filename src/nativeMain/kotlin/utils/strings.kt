package utils

fun String.isUpperCase(): Boolean {
    return this.all { it.isUpperCase() }
}

fun String.isLowerCase(): Boolean {
    return this.all { it.isLowerCase() }
}
