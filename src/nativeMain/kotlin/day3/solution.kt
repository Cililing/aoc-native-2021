package day3

import utils.input
import utils.revBitMask
import utils.transpose

val input = input("day3/input.txt")
    .filter { it.isNotBlank() }
    .map { line ->
        line.map { it == '1' }
    }

fun main() {
    val result = mutableListOf<Boolean>()

    val trans = input.transpose()
    trans.forEach { v ->
        val zeros = v.count { !it }
        val ones = v.count { it }
        when {
            zeros == ones -> {
                throw IllegalArgumentException("cannot be equal")
            }
            zeros > ones -> {
                result.add(false)
            }
            else -> {
                result.add(true)
            }
        }
    }

    val binaryToDec = { i: List<Boolean> ->
        i.joinToString(separator = "") {
            if (it) "1" else "0"
        }.toInt(2)


    }

    println(binaryToDec.invoke(result) * binaryToDec.invoke(result.revBitMask()))

    // part 2
    val ox = input.reduceByBit { zeros, ones ->
        when {
            ones > zeros -> true
            zeros > ones -> false
            else -> true
        }
    }
    val co2 = input.reduceByBit { zeros, ones ->
        when {
            ones < zeros -> true
            zeros < ones -> false
            else -> false
        }
    }

    println(binaryToDec.invoke(ox) * binaryToDec.invoke(co2))
}

fun List<List<Boolean>>.reduceByBit(f: (zeros: Int, ones: Int) -> Boolean): List<Boolean> {
    var res = this.toList()
    var index = 0

    while (res.size != 1) {
        val trans = res.transpose()
        val zeros = trans[index].count { !it }
        val ones = trans[index].count { it }

        val v = f(zeros, ones)
        res = res.filter { it[index] == v }

        index++
    }
    return res[0]
}
