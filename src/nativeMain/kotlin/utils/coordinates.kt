package utils

typealias Coordinates = Pair<Int, Int>

val Coordinates.x: Int
    get() = this.first

val Coordinates.y: Int
    get() = this.second
