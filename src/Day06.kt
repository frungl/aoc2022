fun main() {
    fun part1(input: List<String>): Int {
        val str = input.first()
        val tmp = str.mapIndexed { index, _ ->
            when(str.length - index >= 4) {
                true -> str.substring(index, index + 4)
                else -> ""
            }
        }.dropLast(3)
        return 4 + tmp.indexOfFirst {
            it.toCharArray().distinct().size == 4
        }
    }

    fun part2(input: List<String>): Int {
        val str = input.first()
        val tmp = str.mapIndexed { index, _ ->
            when(str.length - index >= 14) {
                true -> str.substring(index, index + 14)
                else -> ""
            }
        }.dropLast(13)
        return 14 + tmp.indexOfFirst {
            it.toCharArray().distinct().size == 14
        }
    }

    fun beautiful(input: List<String>, size: Int): Int =
        input.first().windowed(size).indexOfFirst { it.toSet().size == size } + size

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 19)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
    println(beautiful(input, 4))
    println(beautiful(input, 14))
}