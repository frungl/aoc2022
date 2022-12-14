import kotlin.math.max
import kotlin.math.sign

typealias Coord = Pair<Int, Int>

private operator fun Coord.plus(other: Coord): Coord {
    return first + other.first to second + other.second
}

fun main() {
    val tryGo = listOf(0 to 1, -1 to 1, 1 to 1)

    fun tryAdd(now: Coord, used: MutableSet<Coord>): Coord {
        if (now.second == 1000)
            return now
        val pos = tryGo.map{ it + now }
        if (pos.all { used.contains(it) })
            return now
        return tryAdd(pos.first { !used.contains(it) }, used)
    }

    fun MutableSet<Coord>.addLine(from: Coord, to: Coord) {
        var last = from
        val move = (to.first - last.first).sign to (to.second - last.second).sign
        add(last)
        while (last != to) {
            last += move
            add(last)
        }
    }

    fun part1(input: List<String>): Int {
        val set = mutableSetOf<Coord>()
        input.forEach { line ->
            val crd = line.split("->")
                .map { it.trim() }
                .map {
                    val (a, b) = it.split(',')
                    a.toInt() to b.toInt()
                }
            crd.windowed(2) {
                set.addLine(it[0], it[1])
            }
        }
        var cnt = 0
        while (true) {
            val new = tryAdd(500 to 0, set)
            if (new.second == 1000)
                break
            set.add(new)
            cnt++
        }
        return cnt
    }

    fun part2(input: List<String>): Int {
        val set = mutableSetOf<Coord>()
        var mx = 0
        input.forEach { line ->
            val crd = line.split("->")
                .map { it.trim() }
                .map {
                    val (a, b) = it.split(',')
                    mx = max(mx, b.toInt() + 2)
                    a.toInt() to b.toInt()
                }
            crd.windowed(2) {
                set.addLine(it[0], it[1])
            }
        }
        set.addLine(-1000 to mx, 1000 to mx)
        var cnt = 0
        while (!set.contains(500 to 0)) {
            val new = tryAdd(500 to 0, set)
            set.add(new)
            cnt++
        }
        return cnt
    }

    val testInput = readInput("Day14_test")
    check(part1(testInput) == 24)
    check(part2(testInput) == 93)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}