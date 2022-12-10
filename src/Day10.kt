import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val needSave = setOf(20, 60, 100, 140, 180, 220)
        var x = 1
        var cyc = 0
        return input.sumOf { s ->
            var ret = 0
            if(s.contains("noop")) {
                cyc++
                if (needSave.contains(cyc)) {
                    ret = x * cyc
                }
            } else {
                cyc++
                if (needSave.contains(cyc)) {
                    ret = x * cyc
                }
                cyc++
                if (needSave.contains(cyc)) {
                    ret = x * cyc
                }
                x += s.takeLastWhile { it != ' ' }.toInt()
            }
            ret
        }
    }

    fun part2(input: List<String>): List<String> {
        var sprite = ".".repeat(1000) + "#".repeat(3) + ".".repeat(1000)
        var result = ""
        var pos = 1000
        input.forEach { s ->
            result += sprite[pos]
            pos = (pos - 999) % 40 + 1000
            if(s.contains("addx")) {
                val move = s.takeLastWhile { it != ' ' }.toInt()
                result += sprite[pos]
                pos = (pos - 999) % 40 + 1000
                sprite = when {
                    move > 0 -> ".".repeat(move) + sprite.dropLast(move)
                    move < 0 -> sprite.drop(abs(move)) + ".".repeat(abs(move))
                    else -> sprite
                }
            }
        }
        return result.chunked(40)
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13140)

    val expectedAns = listOf(
        "##..##..##..##..##..##..##..##..##..##..",
        "###...###...###...###...###...###...###.",
        "####....####....####....####....####....",
        "#####.....#####.....#####.....#####.....",
        "######......######......######......####",
        "#######.......#######.......#######....."
    )
    check(part2(testInput) == expectedAns)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input).joinToString("\n"))
}