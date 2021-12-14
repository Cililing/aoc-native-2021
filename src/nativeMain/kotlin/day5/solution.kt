package day5

import utils.Coordinates
import utils.mutableMatrixOf
import utils.x
import utils.y

val input: List<Pair<Coordinates, Coordinates>> = utils.input("day5/input.txt")
    .map {
        it.split("->").map { it.trim() }
    }
    .map {
        Pair(it[0], it[1])
    }
    .map {
        val first = it.first.split(",")
        val second = it.second.split(",")

        Pair(
            first[0].trim().toInt() to first[1].trim().toInt(),
            second[0].trim().toInt() to second[1].trim().toInt()
        )
    }

fun main() {
    val maxSize = input.map {
        listOf(it.first.first, it.first.second, it.second.first, it.second.second)
    }.flatten().maxOrNull()!!

    val vertical = input.filter { it.isVertical() }.map {
        if (it.first.first < it.second.first) it else Pair(it.second, it.first)
    }
    val horizontal = input.filter { it.isHorizontal() }.map {
        if (it.first.second < it.second.second) it else Pair(it.second, it.first)
    }

    val m = mutableMatrixOf(maxSize + 1, 0)

    // apply vertical
    vertical.forEach {
        // apply only first cord
        val x = it.first.second
        val yStart = it.first.first
        val yEnd = it.second.first

        (yStart..yEnd).forEach {
            m[x][it]++
        }
    }
    horizontal.forEach {
        val y = it.first.first
        val xStart = it.first.second
        val xEnd = it.second.second

        (xStart..xEnd).forEach {
            m[it][y]++
        }
    }

    println(m.flatten().count { it >= 2 })

    // apply also diagonals
    val diagonal = input.filter { it.isDiagonal() && !it.isHorizontal() && !it.isVertical() }
    diagonal.forEach {
        val cords = it.getDiagonalCoords()
        cords.forEach {
            m[it.second][it.first]++
        }
    }

    println(m.flatten().count { it >= 2 })
}

fun Pair<Coordinates, Coordinates>.isVertical(): Boolean {
    return this.first.second == this.second.second
}

fun Pair<Coordinates, Coordinates>.isHorizontal(): Boolean {
    return this.first.first == this.second.first
}

fun Pair<Coordinates, Coordinates>.isDiagonal(): Boolean {
    val dy: Int = this.first.second - this.second.second
    val dx: Int = this.first.first - this.second.first
    return dx == 0 || dy == 0 || dx == dy || dx == -dy
}

fun Pair<Coordinates, Coordinates>.getDiagonalCoords(): List<Coordinates> {
    val res = mutableListOf<Coordinates>()
    var current = this.first

    // diagonal x increasing, y increasing
    if (second.x > first.x && second.y >= first.y) {
        do {
            res.add(current)
            current = current.copy(current.first + 1, current.second + 1)
        } while (current != this.second)
        res.add(current) // include the last one

        return res
    }

    // diagonal x increasing, y decreasing
    if (second.x > first.x && second.y <= first.y) {
        do {
            res.add(current)
            current = current.copy(current.first + 1, current.second - 1)
        } while (current != this.second)
        res.add(current)

        return res
    }

    // diagonal x decreasing, y increasing
    if (second.x <= first.x && second.y > first.y) {
        do {
            res.add(current)
            current = current.copy(current.first - 1, current.second + 1)
        } while (current != this.second)
        res.add(current)

        return res
    }

    // diagonal x decreasing, y decreasing
    if (second.x <= first.x && second.y < first.y) {
        do {
            res.add(current)
            current = current.copy(current.first - 1, current.second - 1)
        } while (current != this.second)
        res.add(current)

        return res
    }

    return emptyList()
}
