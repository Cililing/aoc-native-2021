package day8

import utils.requireNonEqual
import kotlin.IllegalArgumentException

data class Input(
    val patterns: List<String>,
    val output: List<String>
)

val input = utils.input("day8/input.txt")
    .filter { it.isNotBlank() }
    .map { it.split("|") }
    .map { it ->
        Input(
            it[0].split(" ").filter { it.isNotEmpty() },
            it[1].split(" ").filter { it.isNotEmpty() }
        )
    }

fun main() {
    println(ex1())
    println(ex2())
}

fun ex1(): Int {
    return input
        .map { it.output }
        .flatten()
        .mapNotNull(::convertToFixedNumber)
        .count()
}

fun ex2(): Int {
    return input.sumOf {
        val mappings = guessMappings(it.patterns.map { it.toList() })

        it.output.map {
            mapToDigit(mappings, it.toList())
        }.joinToString("").trim().toInt()
    }
}

/**
 * It's not guessing, but the "algorithm" here... xD
 * but... it's working :)
 *
 * Probably operating on bit-masks would be much better.
 */
fun guessMappings(patterns: List<List<Char>>, printer: ((Any?) -> Unit)? = null): Map<Char, Char> {

    val n1 = patterns.find { it.count() == 2 }!!
    val n4 = patterns.find { it.count() == 4 }!!
    val n7 = patterns.find { it.count() == 3 }!!
    val n8 = patterns.find { it.count() == 7 }!!

    // using 1 and 7 determine original aaa signal
    // then we can apply all mappings
    val aaa = (n7 - n1)
        .first()
    printer?.invoke("aaa found: $aaa")
    requireNonEqual(aaa)

    // using 4 and 9 we can determine ggg singal
    val n9 = patterns.filter { it.count() == 6 }
        .filter { it.containsAll(n4) }
        .first { it.contains(aaa) }
    val ggg = (n9 - n4).first { it != aaa }
    printer?.invoke("ggg found: $ggg")
    requireNonEqual(aaa, ggg)

    // using 4 and 8 we can determine eee signal
    val eee = n8.filter { it != aaa }
        .filter { it != ggg }
        .minus(n4)
        .first()
    printer?.invoke("eee found: $eee")
    requireNonEqual(aaa, ggg, eee)

    // using 4 and 3 we can determine bbb signal
    // to find 3: must contains inside 3 elements and two of them must be in n1
    // the set difference is bbb
    val n3 = patterns.filter { it.count() == 5 }
        .first { it.containsAll(n1) }
    val bbb = (n4 - n3).first()
    printer?.invoke("bbb found: $bbb")
    requireNonEqual(aaa, ggg, eee, bbb)

    // by filtering data from 4 we can determine ddd signal
    val ddd = n4.filter { it != bbb }
        .first { !n1.contains(it) }
    printer?.invoke("ddd found: $ddd")
    requireNonEqual(aaa, ggg, eee, bbb, ddd)

    // by filtering data from 6 we can determine fff signal
    val n6 = patterns.asSequence().filter { it.count() == 6 }
        .filter { it.contains(aaa) }
        .filter { it.contains(bbb) }
        .filter { it.contains(ddd) }
        .filter { it.contains(eee) }
        .first { it.contains(ggg) }
    val fff = n6.asSequence().filter { it != aaa }
        .filter { it != bbb }
        .filter { it != ddd }
        .filter { it != eee }
        .first { it != ggg }

    printer?.invoke("fff found: $fff")
    requireNonEqual(aaa, ggg, eee, bbb, ddd, fff)

    // finally, find ccc signal
    val ccc = n8
        .first { !n6.contains(it) }
    printer?.invoke("ccc found: $ccc")
    requireNonEqual(aaa, ggg, eee, bbb, ddd, fff, ccc)

    return mapOf(
        aaa to 'a',
        bbb to 'b',
        ccc to 'c',
        ddd to 'd',
        eee to 'e',
        fff to 'f',
        ggg to 'g'
    )
}

fun mapToDigit(mappings: Map<Char, Char>, s: List<Char>): Int {
    val mapped = s.mapNotNull {
        mappings.getOrElse(it) {
            null
        }
    }.sorted()

    return when {
        // 0
        mapped.count() == 6 && mapped.containsAll("abcefg".toList()) -> 0
        // 1
        mapped.count() == 2 -> 1
        // 2
        mapped.count() == 5 && mapped.containsAll("acdeg".toList()) -> 2
        // 3
        mapped.count() == 5 && mapped.containsAll("acdfg".toList()) -> 3
        // 4
        mapped.count() == 4 -> 4
        // 5
        mapped.count() == 5 && mapped.containsAll("abdfg".toList()) -> 5
        // 6
        mapped.count() == 6 && mapped.containsAll("abdefg".toList()) -> 6
        // 7
        mapped.count() == 3 -> 7
        // 8
        mapped.count() == 7 -> 8
        // 9
        mapped.count() == 6 && mapped.containsAll("abcdfg".toList()) -> 9
        else -> throw IllegalArgumentException()
    }
}

// if a number of unique letters is in (2, 3, 4, 7) converts it to a proper number
fun convertToFixedNumber(s: String): Int? {
    return when (s.length) {
        2 -> 1
        3 -> 7
        4 -> 4
        7 -> 8
        else -> null
    }
}
