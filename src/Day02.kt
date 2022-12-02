fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf {
            val (f, s) = it.split(' ')
            val fVal = when(f) {
                "A" -> 0
                "B" -> 1
                "C" -> 2
                else -> -1
            }
            val sVal = when(s) {
                "X" -> 0
                "Y" -> 1
                "Z" -> 2
                else -> -1
            }
            1 + sVal + when(sVal) {
                fVal -> 3
                (fVal + 1) % 3 -> 6
                else -> 0
            }
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf {
            val (f, s) = it.split(' ')
            val fVal = when(f) {
                "A" -> 0
                "B" -> 1
                "C" -> 2
                else -> -1
            }
            val sVal = when(s) {
                "X" -> 0
                "Y" -> 3
                "Z" -> 6
                else -> -1
            }
            sVal + 1 + when(sVal) {
                0 -> (fVal + 2) % 3
                3 -> fVal
                6 -> (fVal + 1) % 3
                else -> -1
            }
        }
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}