fun main() {
    fun cost(ch: Char): Int {
        return when(ch) {
            in 'a'..'z' -> ch - 'a' + 1
            else -> ch - 'A' + 27
        }
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { str ->
            val (l, r) = str.chunked(str.length / 2)
            l.toCharArray().distinct().sumOf { ch ->
                when (r.contains(ch)) {
                    true -> cost(ch)
                    else -> 0
                }
            }
        }
    }

    fun part2(input: List<String>): Int {
        return input.chunked(3).sumOf { bp ->
            bp[0].toCharArray().distinct().sumOf { ch ->
                when (bp[1].contains(ch) && bp[2].contains(ch)) {
                    true -> cost(ch)
                    else -> 0
                }
            }
        }
    }

    fun part1Beauty(input: List<String>): Int {
        return input.map { str ->
            val (l, r) = str.chunked(str.length / 2).map { it.toSet() }
            (l intersect r).single()
        }.sumOf { cost(it) }
    }

    fun part2Beauty(input: List<String>): Int {
        return input.chunked(3) { ch ->
            val (a, b, c) = ch.map { it.toSet() }
            (a intersect b intersect c).single()
        }.sumOf { cost(it) }
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
    println(part1Beauty(input))
    println(part2Beauty(input))
}
