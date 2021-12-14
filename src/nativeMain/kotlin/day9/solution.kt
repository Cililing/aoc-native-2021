package day9

import utils.*

val input = utils.input("day9/input.txt")
    .map { it.split("").filter { it.isNotBlank() }.map { it.trim().toInt() } }

fun main() {
    println(ex1())
    println(ex2())
}

data class Basin(val value: Int, var visitId: Int? = null) {
    override fun toString(): String {
        return "$value:${visitId ?: "N"}"
    }
}

fun ex2(): Int {
    val n = input.map {
        it.map {
            Basin(it, null)
        }
    }

    var visitorId = 0

    // at first assign ids to basin-base-points
    n.forEachIndexed { a, b, v ->
        val adj = n.adjacent(a, b)
        if (adj.values().map { it.value }.all { it > v.value }) {
            n.expand(Coordinates(a, b), visitorId++)
        }
    }

    val groups = n.flatten().filter { it.visitId != null }.groupBy { it.visitId }

    return groups.values.map { it.size }.sorted().takeLast(3)
        .reduce { a, b -> a * b }
}

fun Matrix<Basin>.expand(point: Coordinates, visitId: Int) {
    val p = this.get(point)
    if (p.value == 9) {
        return
    }
    p.visitId = visitId

    // visit all points without visitId
    this.adjacent(point.x, point.y)
        .filter { it.second.visitId == null }
        .forEach { expand(it.first, visitId) }
}

fun ex1(): Int {
    val res = mutableListOf<Int>()
    input.forEachIndexed { a, b, v ->
        val adj = input.adjacent(a, b)
        if (adj.values().all { it > v }) {
            res.add(v)
        }
    }
    return res.sumOf { it + 1 }
}
