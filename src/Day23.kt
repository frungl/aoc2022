fun main() {
    fun parseToSet(input: List<String>): Set<Pair<Int, Int>> {
        val set = mutableSetOf<Pair<Int, Int>>()
        input.forEachIndexed { i, s -> 
            s.forEachIndexed { j, c ->
                if (c == '#')
                    set.add(Pair(i, j))
            }
        }
        return set
    }

    val go = listOf(
        listOf(-1 to 0, -1 to -1, -1 to 1),
        listOf(1 to 0, 1 to -1, 1 to 1),
        listOf(0 to -1, -1 to -1, 1 to -1),
        listOf(0 to 1, -1 to 1, 1 to 1),

        listOf(-1 to 0, -1 to -1, -1 to 1),
        listOf(1 to 0, 1 to -1, 1 to 1),
        listOf(0 to -1, -1 to -1, 1 to -1),
        listOf(0 to 1, -1 to 1, 1 to 1),
    )

    val aroundPos = listOf(
        -1 to 0, 1 to 0, 0 to -1, 0 to 1,
        -1 to -1, -1 to 1, 1 to -1, 1 to 1
    )

    fun testPrint(set: Set<Pair<Int, Int>>) {
        val minX = set.minOf { it.first }
        val maxX = set.maxOf { it.first }
        val minY = set.minOf { it.second }
        val maxY = set.maxOf { it.second }
        val w = maxX - minX + 1
        val h = maxY - minY + 1
        val map = MutableList(w) { CharArray(h) { '.' } }
        set.forEach { pos ->
            map[pos.first - minX][pos.second - minY] = '#'
        }
        map.forEach { println(String(it)) }
        println()
    }

    fun part1(input: List<String>): Int {
        var set = parseToSet(input)
        var goPos = 0
        repeat(10) {
            val nextPos = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()
            val cnt = mutableMapOf<Pair<Int, Int>, Int>()
            set.forEach { pos ->
                var allAroundEmpty = true
                aroundPos.forEach { around ->
                    val newPos = pos.first + around.first to pos.second + around.second
                    if (set.contains(newPos))
                        allAroundEmpty = false
                }
                if (!allAroundEmpty) {
                    for (i in 0..3) {
                        var allEmpty = true
                        go[goPos + i].forEach { move ->
                            val newPos = Pair(pos.first + move.first, pos.second + move.second)
                            if (set.contains(newPos))
                                allEmpty = false
                        }
                        if (allEmpty) {
                            val next = Pair(pos.first + go[goPos + i][0].first, pos.second + go[goPos + i][0].second)
                            nextPos[pos] = next
                            cnt[next] = cnt.getOrDefault(next, 0) + 1
                            break
                        }
                    }
                }
            }
            val newSet = mutableSetOf<Pair<Int, Int>>()
            for(pos in set) {
                if (nextPos.containsKey(pos)) {
                    val next = nextPos[pos] ?: error("no next")
                    if (cnt[next] == 1) {
                        newSet.add(next)
                        continue
                    }
                }
                newSet.add(pos)
            }
            set = newSet
            goPos = (goPos + 1) % 4
        }
        val minX = set.minOf { it.first }
        val maxX = set.maxOf { it.first }
        val minY = set.minOf { it.second }
        val maxY = set.maxOf { it.second }
        val w = maxX - minX + 1
        val h = maxY - minY + 1
        return w * h - set.size
    }

    fun part2(input: List<String>): Int {
        var set = parseToSet(input)
        var goPos = 0
        for(i in 1..5000) {
            val nextPos = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()
            val cnt = mutableMapOf<Pair<Int, Int>, Int>()
            set.forEach { pos ->
                var allAroundEmpty = true
                aroundPos.forEach { around ->
                    val newPos = pos.first + around.first to pos.second + around.second
                    if (set.contains(newPos))
                        allAroundEmpty = false
                }
                if (!allAroundEmpty) {
                    for (i in 0..3) {
                        var allEmpty = true
                        go[goPos + i].forEach { move ->
                            val newPos = Pair(pos.first + move.first, pos.second + move.second)
                            if (set.contains(newPos))
                                allEmpty = false
                        }
                        if (allEmpty) {
                            val next = Pair(pos.first + go[goPos + i][0].first, pos.second + go[goPos + i][0].second)
                            nextPos[pos] = next
                            cnt[next] = cnt.getOrDefault(next, 0) + 1
                            break
                        }
                    }
                }
            }
            val newSet = mutableSetOf<Pair<Int, Int>>()
            for(pos in set) {
                if (nextPos.containsKey(pos)) {
                    val next = nextPos[pos] ?: error("no next")
                    if (cnt[next] == 1) {
                        newSet.add(next)
                        continue
                    }
                }
                newSet.add(pos)
            }
            if (set == newSet) {
                return i
            }
            set = newSet
            goPos = (goPos + 1) % 4
        }
        return -1
    }

    val testInput = readInput("Day23_test")
    check(part1(testInput) == 110)
    check(part2(testInput) == 20)

    val input = readInput("Day23")
    println(part1(input))
    println(part2(input))
}