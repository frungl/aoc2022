fun main() {
    fun parseStacks(i: List<String>): MutableList<MutableList<String>> {
        val tmp = i.map {
            it.padEnd(i.last().length, ' ')
                .chunked(1)
                .filterIndexed { index, _ -> index % 4 == 1 }
        }.dropLast(1)
        val res = MutableList<MutableList<String>>(tmp[0].size) { mutableListOf() }
        tmp.asReversed().fold(res) { set, el ->
            set.mapIndexed { index, chars ->
                if(el[index].isNotBlank())
                    chars.add(el[index])
                chars
            }.toMutableList()
        }
        return res
    }

    fun parseQuery(q: List<String>): List<List<Int>> {
        return q.fold(mutableListOf<List<Int>>()) { acc, s ->
            acc.add(s.split(' ').filter { it.toIntOrNull() != null }.map { it.toInt() })
            acc
        }.toList()
    }

    fun part1(input: List<String>): String {
        val (q, i) = input.filter { it.isNotBlank() }.partition { it.contains("move") }
        val stacks = parseStacks(i)
        val queries = parseQuery(q)
        queries.forEach {
            val (cnt, from, to) = it
            stacks[to - 1].addAll(stacks[from - 1].takeLast(cnt).asReversed())
            stacks[from - 1] = stacks[from - 1].dropLast(cnt).toMutableList()
        }
        return stacks.fold("") { str, it ->
            str + it.last()
        }
    }

    fun part2(input: List<String>): String {
        val (q, i) = input.filter { it.isNotBlank() }.partition { it.contains("move") }
        val stacks = parseStacks(i)
        val queries = parseQuery(q)
        queries.forEach {
            val (cnt, from, to) = it
            stacks[to - 1].addAll(stacks[from - 1].takeLast(cnt))
            stacks[from - 1] = stacks[from - 1].dropLast(cnt).toMutableList()
        }
        return stacks.fold("") { str, it ->
            str + it.last()
        }
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}