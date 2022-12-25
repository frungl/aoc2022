fun main() {
    fun Long.toSnafu(): String {
        var s = ""
        var n = this
        while (n > 0L) {
            val rem = n % 5L
            var res = n / 5L
            if (rem < 3L)
                s += rem.toString()
            else {
                if (rem == 3L)
                    s += "="
                else
                    s += "-"
                res++
            }
            n = res
        }
        return s.reversed()
    }

    fun String.fromSnafu(): Long {
        var n = 0L
        var m = 1L
        this.reversed().forEach {
            n += when (it) {
                '=' -> m * -2L
                '-' -> m * -1L
                else -> m * it.toString().toLong()
            }
            m *= 5L
        }
        return n
    }

    fun part1(input: List<String>): String {
        val sum = input.sumOf { it.fromSnafu() }
        return sum.toSnafu()
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day25_test")
    check(part1(testInput) == "2=-1=0")
    check(part2(testInput) == 0)

    val input = readInput("Day25")
    println(part1(input))
    println(part2(input))
}