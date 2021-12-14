package utils

typealias Row<T> = List<T>
typealias MutableRow<T> = MutableList<T>

typealias Matrix<T> = List<Row<T>>
typealias MutableMatrix<T> = MutableList<MutableList<T>>

fun <T> matrixOf(rows: List<T>): Matrix<T> = listOf(rows)

fun <T> mutableMatrixOf(size: Int, initialValue: T): MutableMatrix<T> {
    return MutableList(size) {
        MutableList(size) { initialValue }
    }
}

/**
 * @param a number of rows
 * @param b number of items in a single row
 */
fun <T> matrixOf(a: Int, b: Int, initialValue: () -> T): Matrix<T> {
    return List(a * b) { initialValue() }.chunked(b)
}

/**
 * @param a number of items in a single row (applicable to all rows)
 */
fun <T> matrixOf(a: Int, vararg elements: T): Matrix<T> = elements.toList().chunked(a)

fun <T> Matrix<T>.get(a: Int, b: Int): T = this[a][b]

fun <T> Matrix<T>.get(c: Coordinates): T = this.get(c.x, c.y)

fun <T> Matrix<T>.forEachIndexed(v: (a: Int, b: Int, v: T) -> Unit) {
    this.forEachIndexed { index1, row ->
        row.forEachIndexed { index2, n ->
            v(index1, index2, n)
        }
    }
}

typealias Adjacent<T> = List<Pair<Coordinates, T>>

fun <T> Adjacent<T>.positions() = this.map { it.first }
fun <T> Adjacent<T>.values() = this.map { it.second }

/**
 * Returns list of coordinates and values
 *
 * Not working for matrix of size 1
 */
fun <T> Matrix<T>.adjacent(a: Int, b: Int, includeDiagonals: Boolean = false): List<Pair<Coordinates, T>> {
    val positions = listOf(
        (a - 1) to b,
        (a + 1) to b,
        a to (b - 1),
        a to (b + 1)
    )

    return positions.mapNotNull {
        if (it.first >= 0 && it.first <= this.size - 1 &&
            it.second >= 0 && it.second <= this[0].size - 1
        ) {
            Coordinates(it.first, it.second) to this[it.first][it.second]
        } else null
    } + if (includeDiagonals) this.diagonalAdjacent(a, b) else listOf()
}

fun <T> Matrix<T>.diagonalAdjacent(a: Int, b: Int): List<Pair<Coordinates, T>> {
    val diagonalPositions = listOf(
        (a - 1) to (b - 1),
        (a + 1) to (b + 1),
        (a - 1) to (b + 1),
        (a + 1) to (b - 1)
    )

    return diagonalPositions.mapNotNull {
        if (it.first >= 0 && it.first <= this.size - 1 &&
            it.second >= 0 && it.second <= this[0].size - 1
        ) {
            Coordinates(it.first, it.second) to this[it.first][it.second]
        } else null
    }
}

/**
 * Transposes matrix
 */
@Deprecated("WTF: not working in kotlin native", replaceWith = ReplaceWith("transpose()"))
fun <T> List<List<T>>.trans(): List<List<T>> {
    // don't know why, but not working in kotlin native :(
    return (this[0].indices).map { i ->
        this.map { it[i] }
    }
}

/**
 *
 */
fun <T> List<List<T>>.transpose(): List<List<T>> {
    val result = (first().indices).map { mutableListOf<T>() }.toMutableList()
    forEach { list -> result.zip(list).forEach { it.first.add(it.second) } }
    return result
}

/**
 * Applies XOR operator on each element on the list
 */
fun List<Boolean>.revBitMask() = this.map { !it }
