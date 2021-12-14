package day10

import utils.Stack

val input = utils.input("day10/input.txt")
    .map { it.toCharArray().toList() }

fun main() {
    println(ex1())
    println(ex2())
}

fun ex2(): Any {
    // convert lines to stack, discard corrupted
    val incomplete = input.mapNotNull {
        val stack = Stack<Char>()
        it.forEach {
            when (it) {
                '(', '{', '[', '<' -> stack.push(it)
                ')', '}', ']', '>' -> {
                    val s = stack.pop()
                    if (!isClosing(s, it)) {
                        // broken line, discard
                        return@mapNotNull null
                    }
                }
            }
        }
        stack
        // this way we get not-closed items
    }.map {
        it.map { getClosing(it) }
            .reversed()
            .map {
                when (it) {
                    // must be long due to overflow in case of Int
                    ')' -> 1L
                    ']' -> 2L
                    '}' -> 3L
                    '>' -> 4L
                    else -> throw IllegalArgumentException("must be one of allowed characters")
                }
            }.reduce { acc, i ->
                acc * 5 + i
            }
    }.sorted()

    return incomplete[incomplete.size / 2]
}

fun ex1(): Int {
    val invalidCharacters = mutableListOf<Char>()
    val processSingle = { l: List<Char> ->
        val stack = Stack<Char>()
        l.forEach {
            when (it) {
                '(', '{', '[', '<' -> stack.push(it)
                ')', '}', ']', '>' -> {
                    val s = stack.pop()
                    if (!isClosing(s, it)) {
                        invalidCharacters.add(it)
                        return@forEach
                    }
                }
            }
        }
    }
    input.forEach(processSingle)
    return invalidCharacters.sumOf {
        @Suppress("USELESS_CAST") // Overload resolution ambiguity.
        when (it) {
            ')' -> 3
            ']' -> 57
            '}' -> 1197
            '>' -> 25137
            else -> throw IllegalArgumentException("must be one of allowed characters")
        } as Int
    }
}

fun isClosing(o: Char, c: Char): Boolean {
    return o == '(' && c == ')' || o == '[' && c == ']' || o == '{' && c == '}' || o == '<' && c == '>'
}

fun getClosing(c: Char): Char {
    return when (c) {
        '<' -> '>'
        '{' -> '}'
        '(' -> ')'
        '[' -> ']'
        else -> throw IllegalArgumentException("must be one of allowed characters")
    }
}
