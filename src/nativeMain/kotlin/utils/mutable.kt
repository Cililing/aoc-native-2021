// ktlint-disable filename
package utils

data class Mutable<T>(var v: T) {
    override fun toString(): String {
        return v.toString()
    }
}
