fun main() {
    fun part1(input: List<String>): Int {
        return input.fold(mutableListOf(0)) { temp, el ->
            if (el.isBlank()) {
                temp.add(0)
            } else {
                temp[temp.size - 1] += el.toInt()
            }
            temp
        }.max()
    }

    fun part2(input: List<String>): Int {
        return input.fold(mutableListOf(0)) { temp, el ->
            if (el.isBlank()) {
                temp.add(0)
            } else {
                temp[temp.size - 1] += el.toInt()
            }
            temp
        }.sorted().takeLast(3).sum()
    }

    fun beauty(input: List<String>): List<Int> =
        input.joinToString(separator="\n").trim().split("\n\n").map { it.lines().sumOf(String::toInt) }.sortedDescending()

    fun part1Beauty(input: List<String>): Int =
        beauty(input).first()

    fun part2Beauty(input: List<String>): Int =
        beauty(input).take(3).sum()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
    println(part1Beauty(input))
    println(part2Beauty(input))
}
