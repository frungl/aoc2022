fun main() {
    val figures = listOf(
        listOf(-1 to 0, 0 to 0, 1 to 0, 2 to 0),
        listOf(0 to 0, -1 to 1, 0 to 1, 1 to 1, 0 to 2),
        listOf(-1 to 0, 0 to 0, 1 to 0, 1 to 1, 1 to 2),
        listOf(-1 to 0, -1 to 1, -1 to 2, -1 to 3),
        listOf(-1 to 0, 0 to 0, -1 to 1, 0 to 1)
    )

    operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
        return first + other.first to second + other.second
    }

    fun List<Pair<Int, Int>>.applyFigure(pos: Pair<Int, Int>): List<Pair<Int, Int>> {
        val tmp = map { it + pos }
        val mn = tmp.minBy { it.first }.first
        val mx = tmp.maxBy { it.first }.first
        return when(mn < 0 || mx > 6) {
            true -> this
            else -> tmp
        }
    }

    fun part1(input: List<String>): Int {
        val go = input[0]
        val set = mutableSetOf<Pair<Int, Int>>()
        (0..6).forEach { set.add(it to - 1) }
        val down = 0 to -1
        val left = -1 to 0
        val right = 1 to 0
        var nowCord = 3 to 3
        var nowPos = 0
        var nowInd = 0

        repeat(2022) {
            var nowFigure = figures[nowInd].applyFigure(nowCord)
            while (true) {
                val tmp = when (go[nowPos]) {
                    '>' -> nowFigure.applyFigure(right)
                    '<' -> nowFigure.applyFigure(left)
                    else -> throw Exception("Error")
                }
                if ((tmp intersect set).isEmpty())
                    nowFigure = tmp
                nowPos = (nowPos + 1) % go.length
                if((nowFigure.applyFigure(down) intersect set).isNotEmpty())
                    break
                nowFigure = nowFigure.applyFigure(down)
            }
            set.addAll(nowFigure)
            nowCord = 3 to (set.maxBy { it.second }.second + 4)
            nowInd = (nowInd + 1) % figures.size
        }
        return set.maxBy { it.second }.second + 1
    }

    fun part2(input: List<String>): Long {
        val need: Long = 1_000_000_000_000L
        val go = input[0]
        val set = mutableSetOf<Pair<Int, Int>>()
        (0..6).forEach { set.add(it to - 1) }
        val down = 0 to -1
        val left = -1 to 0
        val right = 1 to 0
        var nowCord = 3 to 3
        var nowPos = 0
        var nowInd = 0

        val fiveSet = mutableMapOf<Int, Pair<Int, Set<Pair<Int, Int>>>>()
        val heights = mutableListOf<Int>()
        var lastHeight = 0
        var cnt = 0
        while(true) {
            val nowSet = mutableSetOf<Pair<Int, Int>>()
            repeat(5) {
                var nowFigure = figures[nowInd].applyFigure(nowCord)
                while (true) {
                    val tmp = when (go[nowPos]) {
                        '>' -> nowFigure.applyFigure(right)
                        '<' -> nowFigure.applyFigure(left)
                        else -> throw Exception("Error")
                    }
                    if ((tmp intersect set).isEmpty())
                        nowFigure = tmp
                    nowPos = (nowPos + 1) % go.length
                    if((nowFigure.applyFigure(down) intersect set).isNotEmpty())
                        break
                    nowFigure = nowFigure.applyFigure(down)
                }
                set.addAll(nowFigure)
                nowSet.addAll(nowFigure)
                val mxH = set.maxBy { it.second }.second
                heights.add(mxH + 1)
                nowCord = 3 to (mxH + 4)
                nowInd = (nowInd + 1) % figures.size
            }
            if (fiveSet[nowPos] == null) {
                fiveSet[nowPos] = cnt to nowSet.toSet()
            } else {
                val (oldCnt, oldFiveSet) = fiveSet[nowPos]!!
                val mnNow = nowSet.minBy { it.second }.second
                val mnOld = oldFiveSet.minBy { it.second }.second
                val newSet = nowSet.map { it.first to it.second - mnNow + mnOld }.toSet()
                if (oldFiveSet == newSet) {
                    val needCnt = (need - oldCnt) / (cnt - oldCnt)
                    val needPos = (need - oldCnt) % (cnt - oldCnt)
                    return needCnt * (lastHeight - mnOld) + heights[oldCnt + needPos.toInt() - 1]
                }
            }
            lastHeight = heights.last()
            cnt += 5
        }
        return -1L
    }

    val testInput = readInput("Day17_test")
    check(part1(testInput) == 3068)
    check(part2(testInput) == 1_514_285_714_288L)

    val input = readInput("Day17")
    println(part1(input))
    println(part2(input))
}