package day6

val input = utils.input("day6/input.txt")
    .first()
    .split(",")
    .map { it.trim().toInt() }

const val resetValue = 6 // value when up to reset
const val initValue = 8 // value when added to list

fun main() {
    ex2(80)
    ex2(256)
}

private fun ex2(days: Int = 256) {
    var totalUnderflow = 0L
    var currentValues = mapOf<Int, Long?>(
        0 to input.count { it == 0 }.toLong(),
        1 to input.count { it == 1 }.toLong(),
        2 to input.count { it == 2 }.toLong(),
        3 to input.count { it == 3 }.toLong(),
        4 to input.count { it == 4 }.toLong(),
        5 to input.count { it == 5 }.toLong(),
        6 to input.count { it == 6 }.toLong(),
        7 to input.count { it == 7 }.toLong(),
        8 to input.count { it == 8 }.toLong()
    )

    repeat((1..days).count()) {
        val underflow = currentValues[0]
        totalUnderflow = totalUnderflow.plus(underflow ?: 0)

        currentValues = currentValues.mapValues {
            when (it.key) {
                8 -> currentValues[0]

                6 -> {
                    val lastDay = currentValues[7] ?: 0
                    val zeroDay = currentValues[0] ?: 0

                    lastDay + zeroDay
                }

                else -> currentValues[it.key + 1]
            }
        }

        var total = 0L
        currentValues.values.forEach {
            total += it ?: 0L
        }
    }
    println(totalUnderflow)
}

private fun ex1() {
    val days = 80
    var list = input

    var totalUnderflows = 0

    (1..days).forEach {
        var underflow = 0
        list = list
            .map {
                it - 1
            }
            .map {
                when (it) {
                    -1 -> {
                        underflow++
                        resetValue
                    }
                    else -> it
                }
            }

        val newValues = (0 until underflow).map { initValue }

        list = list + newValues
        totalUnderflows += underflow

        // println("after $it days: $list (total: ${list.sum()})")
    }

    println(totalUnderflows)
}
