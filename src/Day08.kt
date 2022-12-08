@OptIn(ExperimentalStdlibApi::class)
fun main() {
    fun List<String>.getRow(i: Int): String =
        this[i]

    fun List<String>.getCol(j: Int): String =
        joinToString("") { it[j].toString() }

    fun isCovered(s: String, pos: Int): Boolean {
        val list = s.toCharArray().map { it.digitToInt() }
        return list.take(pos).max() >= list[pos] &&
                list[pos] <= list.takeLast(list.size - pos - 1).max()
    }

    fun List<Int>.aboba(b: Int): Int = when {
        isEmpty() -> 0
        max() < b -> size
        else -> takeWhile { it < b }.size + 1
    }

    fun countCnt(s: String, pos: Int): Int {
        val list = s.toCharArray().map { it.digitToInt() }
        return list.take(pos).asReversed().aboba(list[pos]) *
                list.takeLast(list.size - pos - 1).aboba(list[pos])
    }

    fun part1(input: List<String>): Int {
        val height = input.size
        val width = input[0].length
        return height * width - (1..<height-1).sumOf { h ->
            (1..<width-1).count { w ->
                isCovered(input.getRow(h), w) && isCovered(input.getCol(w), h)
            }
        }
    }

    fun part2(input: List<String>): Int {
        val height = input.size
        val width = input[0].length
        return (0..<height).maxOf { h ->
            (0 ..< width).maxOf { w ->
                countCnt(input.getRow(h), w) * countCnt(input.getCol(w), h)
            }
        }
    }

    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}