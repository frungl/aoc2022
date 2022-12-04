fun main() {
    fun part1(input: List<String>): Int {
        return input.count { str ->
            val (l, r) = str.split(',')
            val (la, lb) = l.split('-').map { it.toInt() }
            val (ra, rb) = r.split('-').map { it.toInt() }
            (la <= ra && rb <= lb) || (ra <= la && lb <= rb)
        }
    }

    fun part2(input: List<String>): Int {
        return input.count { str ->
            val (l, r) = str.split(',')
            val (la, lb) = l.split('-').map { it.toInt() }
            val (ra, rb) = r.split('-').map { it.toInt() }
            !(la > rb || ra > lb)
        }
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
