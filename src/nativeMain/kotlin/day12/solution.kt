package day12

import utils.isLowerCase
import utils.isUpperCase

val input = utils.input("day12/input.txt")
    .map {
        val n = it.split("-")
        // map two-ways as it's a unidirectional graph
        listOf(n[0] to n[1], n[1] to n[0]) //
    }
    .flatten()
    .groupBy { it.first }
    .mapValues { it.value.map { it.second } }

fun main() {
    println(getPaths(::selectSmallAtMostOnce).size)
    println(getPaths(::selectSingleSmallTwice).size)
}

fun getPaths(selector: (List<String>) -> List<List<String>>): List<List<String>> {
    val startingPaths = input.getOrElse("start") { throw IllegalArgumentException("no start entry") }
        .map { listOf("start") + listOf(it) }

    val finishedPaths = mutableListOf<List<String>>()
    var currentPaths = startingPaths

    while (currentPaths.isNotEmpty()) {
        val partial = currentPaths.groupBy { it.last() == "end" }

        val finished = partial[true] ?: listOf()
        finishedPaths.addAll(finished)

        currentPaths = (partial[false] ?: listOf()).map {
            selector(it)
        }.flatten()
    }

    return finishedPaths
}

fun selectSingleSmallTwice(current: List<String>): List<List<String>> {
    val thiz = input[current.last()]!!
    val possible = thiz.map { current + it }

    val filter = filter@{ v: List<String> ->
        val groups = v.groupBy { it }
        val lowerCaseCounters = groups
            .filter { it.key.isLowerCase() && it.key != "start" && it.key != "end" }
            .mapValues { it.value.size }

        // verify max only one entry has more than one occurrence
        if (lowerCaseCounters.filterValues { it > 1 }.size > 1) {
            return@filter false
        }

        groups.all {
            if (it.key.trim().isUpperCase()) {
                return@all true
            } else if (it.key.trim() == "start" || it.key.trim() == "end") {
                return@all it.value.size < 2 // cannot visit start/end twice
            } else if (it.key.trim().isLowerCase()) {
                return@all it.value.size <= 2 // and verify it's up to 2 occurrences
            } else {
                throw IllegalArgumentException("only upper/lower case keys allowed")
            }
        }
    }

    return possible.filter(filter)
}

fun selectSmallAtMostOnce(current: List<String>): List<List<String>> {
    val thiz = input[current.last()]!!
    val possible = thiz.map { current + it }

    val filter = { v: List<String> ->
        v.groupBy { it }.all {
            if (it.key.trim().isUpperCase()) {
                true
            } else if (it.key.trim().isLowerCase()) {
                it.value.size < 2
            } else {
                throw IllegalArgumentException("only upper/lower case keys allowed ${it.key}")
            }
        }
    }

    // filter those which has two same entries
    return possible.filter(filter)
}
