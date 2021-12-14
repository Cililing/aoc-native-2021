package day1

import utils.input

val input = input("day1/input.txt")
    .filter { it.isNotBlank() }
    .map {
        it.trim().toInt()
    }

fun main() {
    // 1

    input.zipWithNext().count { it.second > it.first }.let { println(it) }

    // 2
    input.asSequence() // for performance
        .zipWithNext()
        .zipWithNext()
        // res := ((n, n+1), (n+1, n+2)), but we want get the sum of n, n+1, n+2
        .map { it.first.first + it.first.second + it.second.second }
        .zipWithNext()
        .count { it.second > it.first }
        .let { println(it) }
}
