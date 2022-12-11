import java.math.BigInteger

@OptIn(ExperimentalStdlibApi::class)
fun main() {
    fun String.takeLastInt() =
        takeLastWhile { it != ' ' }.toInt()

    fun String.takeLastBigInteger() =
        takeLastWhile { it != ' ' }.toBigInteger()

    fun String.evalInt(): Int {
        val tmp = this.split(' ')
        return when {
            tmp.contains("+") -> tmp.first().toInt() + tmp.last().toInt()
            else -> tmp.first().toInt() * tmp.last().toInt()
        }
    }

    fun String.evalBigInteger(): BigInteger {
        val tmp = this.split(' ')
        return when {
            tmp.contains("+") -> tmp.first().toBigInteger() + tmp.last().toBigInteger()
            else -> tmp.first().toBigInteger() * tmp.last().toBigInteger()
        }
    }

    fun String.eval(num: Int) =
        replace("old", num.toString()).evalInt()

    fun String.eval(num: BigInteger) =
        replace("old", num.toString()).evalBigInteger()

    fun part1(input: List<String>): Int {
        val monkeys = input
            .chunked(7)
            .map { it.drop(1) }
        val items = monkeys.map {
            it[0].substringAfter(": ")
                .split(", ")
                .map(String::toInt)
                .toMutableList()
        }
        val operation = monkeys.map {
            it[1].substringAfter("= ")
        }
        val test = monkeys.map {
            it[2].takeLastInt()
        }
        val go = monkeys.map {
            it[3].takeLastInt() to it[4].takeLastInt()
        }
        val n = monkeys.size
        val inspects = MutableList(n) { 0 }
        repeat(20) {
            (0..<n).forEach { i ->
                items[i].forEach { item ->
                    inspects[i]++
                    val newItem = operation[i].eval(item) / 3
                    when (newItem % test[i] == 0) {
                        true -> items[go[i].first].add(newItem)
                        else -> items[go[i].second].add(newItem)
                    }
                }
                items[i].clear()
            }
        }
        val (a, b) = inspects.sorted().takeLast(2)
        return a * b
    }

    fun part2(input: List<String>): BigInteger {
        val monkeys = input
            .chunked(7)
            .map { it.drop(1) }
        val items = monkeys.map {
            it[0].substringAfter(": ")
                .split(", ")
                .map(String::toBigInteger)
                .toMutableList()
        }
        val operation = monkeys.map {
            it[1].substringAfter("= ")
        }
        val test = monkeys.map {
            it[2].takeLastBigInteger()
        }
        val go = monkeys.map {
            it[3].takeLastInt() to it[4].takeLastInt()
        }
        val mod = test.reduce { acc, int -> acc * int }
        val n = monkeys.size
        val inspects = MutableList(n) { 0.toBigInteger() }
        repeat(10000) {
            (0..<n).forEach { i ->
                items[i]
                    .forEach { item ->
                    inspects[i]++
                    val newItem = operation[i].eval(item) % mod
                    when (newItem % test[i] == 0.toBigInteger()) {
                        true -> items[go[i].first].add(newItem)
                        else -> items[go[i].second].add(newItem)
                    }
                }
                items[i].clear()
            }
        }
        val (a, b) = inspects.sorted().takeLast(2)
        return a * b
    }

    val testInput = readInput("Day11_test")
    check(part1(testInput) == 10605)
    check(part2(testInput) == 2713310158.toBigInteger())

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}