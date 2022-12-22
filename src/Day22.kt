import kotlin.math.max
import kotlin.math.min

enum class Direction {
    UP, DOWN, LEFT, RIGHT;

    private fun turnLeft() = when (this) {
        UP -> LEFT
        DOWN -> RIGHT
        LEFT -> DOWN
        RIGHT -> UP
    }

    private fun turnRight() = when (this) {
        UP -> RIGHT
        DOWN -> LEFT
        LEFT -> UP
        RIGHT -> DOWN
    }

    fun turn(direction: Direction) = when (direction) {
        LEFT -> turnLeft()
        RIGHT -> turnRight()
        else -> throw IllegalArgumentException("Can only turn left or right")
    }

    fun toInt() = when (this) {
        UP -> 3
        DOWN -> 1
        LEFT -> 2
        RIGHT -> 0
    }
}

data class GoInfo(val graph: Set<Pair<Int, Int>>, val rangesRow: Map<Int, Pair<Int, Int>>, val rangesCol: Map<Int, Pair<Int, Int>>)

data class NewSide(val v: Pair<Int, Int>, val d: Direction)

fun main() {
    fun parseGraph(input: List<String>): GoInfo {
        val set = mutableSetOf<Pair<Int, Int>>()
        val rangesRow = mutableMapOf<Int, Pair<Int, Int>>()
        val rangesCol = mutableMapOf<Int, Pair<Int, Int>>()
        input.forEachIndexed { x, str ->
            str.forEachIndexed { y, c ->
                if (c == '.') {
                    set.add(Pair(x, y))
                }
                if (c != ' ') {
                    rangesRow[x] = rangesRow.getOrDefault(x, Pair(y, y)).let {
                        min(y, it.first) to max(y, it.second)
                    }
                    rangesCol[y] = rangesCol.getOrDefault(y, Pair(x, x)).let {
                        min(x, it.first) to max(x, it.second)
                    }
                }
            }
        }
        return GoInfo(set, rangesRow, rangesCol)
    }

    fun parsePath(path: String): List<Pair<Direction, Int>> {
        var temp = path
        val list = mutableListOf<Pair<Direction, Int>>()
        var nowDir = Direction.RIGHT
        while (temp.isNotEmpty()) {
            val num = temp.takeWhile { it.isDigit() }.toInt()
            temp = temp.dropWhile { it.isDigit() }
            list.add(nowDir to num)
            if (temp.isNotEmpty()) {
                nowDir = when (temp.take(1)) {
                    "L" -> Direction.LEFT
                    "R" -> Direction.RIGHT
                    else -> throw IllegalArgumentException("Invalid input")
                }
                temp = temp.drop(1)
            }
        }
        return list
    }

    fun part1(input: List<String>): Int {
        val (graph, path) = input.filter { it.isNotEmpty() }.partition { !it[0].isDigit() }
        val (graphSet, goRow, goCol) = parseGraph(graph)
        val pathList = parsePath(path.single())
        var v = graphSet.filter { it.first == 0 }.minBy { it.second }
        var lastDir = Direction.UP
        pathList.forEach {
            val (dir, num) = it
            lastDir = lastDir.turn(dir)
            repeat(num) {
                val next = when (lastDir) {
                    Direction.UP -> {
                        val (edgeUp, edgeDown) = goCol[v.second] ?: throw IllegalArgumentException("Column ranges is not found")
                        when (edgeUp <= v.first - 1) {
                            true -> v.first - 1
                            false -> edgeDown
                        } to v.second
                    }
                    Direction.DOWN -> {
                        val (edgeUp, edgeDown) = goCol[v.second] ?: throw IllegalArgumentException("Column ranges is not found")
                        when (edgeDown >= v.first + 1) {
                            true -> v.first + 1
                            false -> edgeUp
                        } to v.second
                    }
                    Direction.LEFT -> {
                        val (edgeLeft, edgeRight) = goRow[v.first] ?: throw IllegalArgumentException("Row ranges is not found")
                        v.first to when (edgeLeft <= v.second - 1) {
                            true -> v.second - 1
                            false -> edgeRight
                        }
                    }
                    Direction.RIGHT -> {
                        val (edgeLeft, edgeRight) = goRow[v.first] ?: throw IllegalArgumentException("Row ranges is not found")
                        v.first to when (edgeRight >= v.second + 1) {
                            true -> v.second + 1
                            false -> edgeLeft
                        }
                    }
                }
                v = when (graphSet.contains(next)) {
                    true -> next
                    false -> v
                }
            }
        }
        return (v.first + 1) * 1000 + (v.second + 1) * 4 + lastDir.toInt()
    }

    fun buildNextSide(): Map<Pair<Int, Int>, NewSide> {
        val map = mutableMapOf<Pair<Int, Int>, NewSide>()

        val a1 = (0..49).map { it to 150 }
        val b1 = (100..149).map { NewSide(it to 99, Direction.LEFT) }.asReversed()
        map.putAll(a1.zip(b1))

        val a2 = (50..99).map { it to 100 }
        val b2 = (100..149).map { NewSide(49 to it, Direction.UP) }
        map.putAll(a2.zip(b2))

        val a3 = (150..199).map { it to 50 }
        val b3 = (50..99).map { NewSide(149 to it, Direction.UP) }
        map.putAll(a3.zip(b3))

        val a4 = (0..49).map { 200 to it }
        val b4 = (100..149).map { NewSide(0 to it, Direction.DOWN) }
        map.putAll(a4.zip(b4))

        val a5 = (50..99).map { -1 to it }
        val b5 = (150..199).map { NewSide(it to 0, Direction.RIGHT) }
        map.putAll(a5.zip(b5))

        val a6 = (0..49).map { 99 to it }
        val b6 = (50..99).map { NewSide(it to 50, Direction.RIGHT) }
        map.putAll(a6.zip(b6))

        val a7 = (0..49).map { it to 49 }
        val b7 = (100..149).map { NewSide(it to 0, Direction.RIGHT) }.asReversed()
        map.putAll(a7.zip(b7))

        val a8 = (100..149).map { it to 100 }
        val b8 = (0..49).map { NewSide(it to 149, Direction.LEFT) }.asReversed()
        map.putAll(a8.zip(b8))

        val a9 = (100..149).map { 50 to it }
        val b9 = (50..99).map { NewSide(it to 99, Direction.LEFT) }
        map.putAll(a9.zip(b9))

        val a10 = (50..99).map { 150 to it }
        val b10 = (150..199).map { NewSide(it to 49, Direction.LEFT) }
        map.putAll(a10.zip(b10))

        val a11 = (100..149).map { -1 to it }
        val b11 = (0..49).map { NewSide(199 to it, Direction.UP) }
        map.putAll(a11.zip(b11))

        val a12 = (150..199).map { it to -1 }
        val b12 = (50..99).map { NewSide(0 to it, Direction.DOWN) }
        map.putAll(a12.zip(b12))

        val a13 = (50..99).map { it to 49 }
        val b13 = (0..49).map { NewSide(100 to it, Direction.DOWN) }
        map.putAll(a13.zip(b13))

        val a14 = (100..149).map { it to -1 }
        val b14 = (0..49).map { NewSide(it to 50, Direction.RIGHT) }.asReversed()
        map.putAll(a14.zip(b14))

        return map
    }

    fun part2(input: List<String>): Int {
        val (graph, path) = input.filter { it.isNotEmpty() }.partition { !it[0].isDigit() }
        val graphSet = parseGraph(graph).graph
        val pathList = parsePath(path.single())
        val nextSide = buildNextSide()
        var v = graphSet.filter { it.first == 0 }.minBy { it.second }
        var lastDir = Direction.UP
        pathList.forEach {
            val (dir, num) = it
            lastDir = lastDir.turn(dir)
            repeat(num) {
                val maybeNext = when (lastDir) {
                    Direction.UP -> v.first - 1 to v.second
                    Direction.DOWN ->  v.first + 1 to v.second
                    Direction.LEFT -> v.first to v.second - 1
                    Direction.RIGHT -> v.first to v.second + 1
                }
                val (nextV, nextDir) = if (nextSide.containsKey(maybeNext)) {
                    Pair(
                        nextSide[maybeNext]?.v ?: throw IllegalArgumentException("Point is not found"),
                        nextSide[maybeNext]?.d ?: throw IllegalArgumentException("Direction is not found")
                    )
                } else {
                    maybeNext to lastDir
                }
                if (graphSet.contains(nextV)) {
                    v = nextV
                    lastDir = nextDir
                }
            }
        }
        return (v.first + 1) * 1000 + (v.second + 1) * 4 + lastDir.toInt()
    }

    val testInput = readInput("Day22_test")
    check(part1(testInput) == 6032)
//    check(part2(testInput) == 0)

    val input = readInput("Day22")
    println(part1(input))
    println(part2(input))
}