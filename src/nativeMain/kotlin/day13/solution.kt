package day13

import utils.*

val input = input("day13/input.txt")
    .takeWhile { it.isNotBlank() }
    .map { it.split(",") }
    .map {
        Coordinates(it[0].trim().toInt(), it[1].trim().toInt())
    }
    .toSet()

data class Fold(val axis: Char, val n: Int)

val folds = input("day13/input.txt")
    .takeLastWhile { it.isNotBlank() }
    .map { it.takeLastWhile { it != ' ' } }
    .map { it.split("=") }
    .map {
        Fold(it.first().trim()[0], it[1].trim().toInt())
    }

fun main() {
    println(ex1())
    ex2().forEach {
        println(it)
    }
}

fun ex1(): Int {
    return (fold(input, folds[0]).size)
}

fun ex2(): Matrix<Char> {
    var res = input.toSet()
    folds.forEach {
        res = fold(res, it)
    }

    // create matrix from res
    val maxX = res.maxOf { it.x }
    val maxY = res.maxOf { it.y }

    return matrixOf(maxY + 1, maxX + 1) { Mutable(0) }.apply {
        res.forEach {
            this.get(it.second, it.first).v = 1
        }
    }.map {
        it.map { if (it.v == 1) '$' else ' ' }
    }
}

fun fold(t: Set<Coordinates>, f: Fold): Set<Coordinates> {
    return when (f.axis) {
        'x' -> {
            t.map {
                if (it.x < f.n) {
                    it // stays the same
                } else {
                    Coordinates(f.n - (it.x - f.n), it.y)
                }
            }.toSet()
        }
        'y' -> {
            t.map {
                if (it.y < f.n) {
                    it
                } else {
                    Coordinates(it.x, f.n - (it.y - f.n))
                }
            }.toSet()
        }
        else -> throw IllegalArgumentException("only x/y axis allowed")
    }
}
