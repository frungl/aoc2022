import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

typealias Coordinate = Pair<Int, Int>

fun main() {
    fun parse(input: List<String>): List<Pair<Coordinate, Coordinate>> =
        input.asSequence()
            .map { it.split(": ").toList() }
            .map { it[0].substringAfter("at ") to it[1].substringAfter("at ") }
            .map { it.first.split(", ").toList() to it.second.split(", ").toList()}
            .map {
                Pair(
                    it.first[0].substringAfter('=').toInt() to it.first[1].substringAfter('=').toInt(),
                    it.second[0].substringAfter('=').toInt() to it.second[1].substringAfter('=').toInt()
                )
            }.toList()

    fun distance(a: Coordinate, b: Coordinate): Int =
        abs(a.first - b.first) + abs(a.second - b.second)

    fun part1(input: List<String>, layer: Int): Int {
        val data = parse(input)
        val used = mutableSetOf<Int>()
        val beacons = mutableSetOf<Int>()
        data.forEach {
            val (s, b) = it
            val dst = distance(s, b)
            val size = dst - abs(layer - s.second)
            if (size >= 0) {
                used.addAll(s.first - size .. s.first + size)
                if (b.second == layer)
                    beacons.add(b.first)
            }
        }
        return used.size - beacons.size
    }

    fun part2(input: List<String>, limit: Int): Long {
        var x = -1
        var y = -1
        val data = parse(input)
        (0..limit).forEach { layer ->
            val used = mutableListOf<Pair<Int, Int>>()
            data.forEach {
                val (s, b) = it
                val dst = distance(s, b)
                val size = dst - abs(layer - s.second)
                if (size >= 0) {
                    used.add(max(0, s.first - size) to min(limit, s.first + size))
                }
            }
            used.add(-1 to -1)
            used.add(limit + 1 to limit + 1)
            used.sortBy { it.first }
            var mx = -1
            used.windowed(2) {
                val (f, s) = it
                mx = max(mx, f.second)
                if (mx + 1 < s.first) {
                    x = mx + 1
                    y = layer
                }
            }
        }
        return x.toLong() * 4_000_000L + y.toLong()
    }

    val testInput = readInput("Day15_test")
    check(part1(testInput, 10) == 26)
    check(part2(testInput, 20) == 56000011L)

    val input = readInput("Day15")
    println(part1(input, 2_000_000))
    println(part2(input, 4_000_000))
}