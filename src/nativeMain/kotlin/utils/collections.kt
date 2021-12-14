package utils

fun <T> requireNonEqual(vararg elements: T) {
    if (elements.count() != elements.toSet().count()) {
        throw IllegalStateException("all elements must be unique")
    }
}

class Stack<T> {
    private val internal = mutableListOf<T>()

    fun push(item: T) {
        internal.add(item)
    }

    fun pop(): T {
        return internal.removeLast()
    }

    fun <R> map(function: (T) -> R): List<R> {
        // returns a list applying the mapping to each element
        return internal.toList().map(function)
    }
}