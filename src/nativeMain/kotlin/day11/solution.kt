package day11

import utils.*

val input = utils.input("day11/input.txt")
    .map {
        it.split("")
            .filter { it.isNotBlank() }
            .map {
                it.trim().toInt()
            }
    }

fun main() {
    println(ex1())
    println(ex2())
}

fun ex2(): Int {
    val mutable = input.map { it.map { Mutable(it) } }

    var index = 0
    while (true) {
        mutable.forEachIndexed { a, b, _ ->
            mutable.increaseAndExpand(a, b)
        }

        if (mutable.sumOf { it.count { it.v >= 10 } } == 100) {
            return index + 1 // include this step
        }

        mutable.forEachIndexed { _, _, v ->
            if (v.v >= 10) {
                v.v = 0
            }
        }
        index++
    }
}

fun ex1(): Int {
    val mutable = input.map { it.map { Mutable(it) } }
    return (0..99).sumOf {
        mutable.forEachIndexed { a, b, _ ->
            mutable.increaseAndExpand(a, b)
        }
        val sum = mutable.sumOf {
            it.count { it.v >= 10 }
        }
        mutable.forEachIndexed { _, _, v ->
            // reset 10 to 0
            if (v.v >= 10) {
                v.v = 0
            }
        }
        sum
    }
}

fun Matrix<Mutable<Int>>.increaseAndExpand(a: Int, b: Int) {
    val v = this.get(a, b)
    // increase the value here
    if (++v.v != 10) {
        return
    }

    // we reached over 9, so we have to "expand" all our adjacent numbers
    this.adjacent(a, b, true).forEach {
        increaseAndExpand(it.first.x, it.first.y)
    }
}
