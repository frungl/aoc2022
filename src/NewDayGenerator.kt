import java.io.File

fun main() {
    println("Enter number of the day:")
    val dayNumber = readln()
    val name = "Day" + dayNumber.padStart(2, '0')
    File("src","$name.txt").writeText("")
    File("src", "${name}_test.txt").writeText("")
    File("src", "$name.kt").writeText("""
        fun main() {
            fun part1(input: List<String>): Int {
                return 0
            }

            fun part2(input: List<String>): Int {
                return 0
            }

            val testInput = readInput("${name}_test")
            check(part1(testInput) == 0)
            check(part2(testInput) == 0)

            val input = readInput("$name")
            println(part1(input))
            println(part2(input))
        }
    """.trimIndent())
}