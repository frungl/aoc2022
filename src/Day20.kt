import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val list = MutableList (input.size) { i -> input[i].toInt() to i }
        val orig = list.toList()
        for ((i, index) in orig) {
            val pos = list.indexOf(i to index)
            var next = 0
            if (i < 0) {
                if(pos >= -i) {
                    next = pos + i
                } else {
                    next = (list.size - 1) - abs(i + pos) % (list.size - 1)
                }
            } else {
                if(i <= (list.size - pos - 1)) {
                    next = pos + i
                } else {
                    next = (i - (list.size - pos - 1)) % (list.size - 1)
                }
            }
            list.removeAt(pos)
            list.add(next, i to index)
        }
        val n = list.indexOfFirst { it.first == 0 }
        return list[(n + 1000) % list.size].first + list[(n + 2000) % list.size].first + list[(n + 3000) % list.size].first
    }

    fun part2(input: List<String>): Long {
        val list = MutableList (input.size) { i -> input[i].toLong() * 811589153L to i }
        val orig = list.toList()
        repeat(10) {
            for ((i, index) in orig) {
                val pos = list.indexOf(i to index)
                var next = 0L
                if (i < 0) {
                    if(pos >= -i) {
                        next = pos + i
                    } else {
                        next = (list.size - 1L) - abs(i + pos) % (list.size - 1L)
                    }
                } else {
                    if(i <= (list.size - pos - 1)) {
                        next = pos + i
                    } else {
                        next = (i - (list.size - pos - 1L)) % (list.size - 1L)
                    }
                }
                list.removeAt(pos)
                list.add(next.toInt(), i to index)
            }
        }
        val n = list.indexOfFirst { it.first == 0L }
        return list[(n + 1000) % list.size].first + list[(n + 2000) % list.size].first + list[(n + 3000) % list.size].first
    }

    val testInput = readInput("Day20_test")
    check(part1(testInput) == 3)
    check(part2(testInput) == 1623178306L)

    val input = readInput("Day20")
    println(part1(input))
    println(part2(input))
}