import kotlin.math.abs
import kotlin.math.sign

typealias Point = Pair<Int, Int>

@OptIn(ExperimentalStdlibApi::class)
fun main() {
    fun direction(main: Point, other: Point): Point {
        val dx = main.first - other.first
        val dy = main.second - other.second
        return when((abs(dx) - abs(dy)).sign) {
            0 -> dx.sign to dy.sign
            1 -> dx.sign to 0
            else -> 0 to dy.sign
        }
    }

    fun move(head: Point, tail: Point, move: Point): Pair<Point, Point> {
        val newHead = head + move
        val possibleMove = listOf(
            Point(0, 1),
            Point(0, -1),
            Point(1, 0),
            Point(-1, 0),
            Point(1, 1),
            Point(1, -1),
            Point(-1, 1),
            Point(-1, -1),
            Point(0, 0)
        )
        val possibleTailPos = possibleMove.map { newHead + it }
        if(possibleTailPos.contains(tail)) {
            return Pair(newHead, tail)
        }
        val newTail = newHead - direction(newHead, tail)
        return Pair(newHead, newTail)
    }

    fun getMoveDir(ch: String): Point {
        return when(ch) {
            "U" -> Point(0, 1)
            "D" -> Point(0, -1)
            "L" -> Point(-1, 0)
            "R" -> Point(1, 0)
            else -> throw IllegalArgumentException("Unknown direction $ch")
        }
    }

    fun part1(input: List<String>): Int {
        var headPos = Point(0, 0)
        var tailPos = Point(0, 0)
        val vis = mutableSetOf<Point>()
        vis.add(tailPos)
        input.map { it.split(' ').toList() }.forEach {
            val (dir, dist) = it
            val move = getMoveDir(dir)
            repeat(dist.toInt()) {
                val (newHead, newTail) = move(headPos, tailPos, move)
                headPos = newHead
                tailPos = newTail
                vis.add(tailPos)
            }
        }
        return vis.size
    }

    fun part2(input: List<String>): Int {
        val snake = MutableList(10) {
            Point(0, 0)
        }
        val vis = mutableSetOf<Point>()
        vis.add(snake.last())
        input.map { it.split(' ').toList() }.forEach {
            val (dir, dist) = it
            val move = getMoveDir(dir)
            repeat(dist.toInt()) {
                var lastMove = move
                for(i in 1..<snake.size) {
                    val (newHead, newTail) = move(snake[i - 1], snake[i], lastMove)
                    lastMove = newTail - snake[i]
                    snake[i - 1] = newHead
                    if(i == snake.size - 1 || lastMove == Point(0, 0)) {
                        snake[i] = newTail
                        break
                    }
                }
                vis.add(snake.last())
            }
        }
        return vis.size
    }

    val testInput = readInput("Day09_test")
    val testInput2 = readInput("Day09_test2")
    check(part1(testInput) == 13)
    check(part2(testInput) == 1)
    check(part2(testInput2) == 36)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}

private operator fun Point.plus(move: Point): Point {
    return Point(first + move.first, second + move.second)
}

private operator fun Point.minus(move: Point): Point {
    return Point(first - move.first, second - move.second)
}
