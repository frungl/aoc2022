data class Blizzard(val pos: Pair<Int, Int>, val move: Pair<Int, Int>)

@OptIn(ExperimentalStdlibApi::class)
fun main() {
    fun parseInput(input: List<String>): List<Blizzard> {
        val tiles = mapOf(
            '>' to Pair(0, 1),
            '<' to Pair(0, -1),
            '^' to Pair(-1, 0),
            'v' to Pair(1, 0)
        )
        val blizzards = mutableListOf<Blizzard>()
        input.forEachIndexed { x, str ->
            str.forEachIndexed { y, c ->
                if (c in tiles)
                    blizzards.add(Blizzard(x to y, tiles[c]!!))
            }
        }
        return blizzards
    }

    fun part1(input: List<String>): Int {
        val n = input.size - 2
        val m = input[0].length - 2
        val time = n * m / n.toBigInteger().gcd(m.toBigInteger()).toInt()
        var blizzards = parseInput(input)
        val positions = MutableList(time) { mutableSetOf<Pair<Int, Int>>() }
        positions[0] = blizzards.map { it.pos }.toMutableSet()
        for(i in 1 ..< time) {
            val newBlizzards = mutableListOf<Blizzard>()
            for((pos, dir) in blizzards) {
                var newPos = pos.first + dir.first to pos.second + dir.second
                if (newPos.first == n + 1) newPos = 1 to newPos.second
                if (newPos.first == 0) newPos = n to newPos.second
                if (newPos.second == m + 1) newPos = newPos.first to 1
                if (newPos.second == 0) newPos = newPos.first to m
                newBlizzards.add(Blizzard(newPos, dir))
            }
            blizzards = newBlizzards;
            positions[i] = blizzards.map { it.pos }.toMutableSet()
        }
        val possible = mutableSetOf<Pair<Int, Int>> ()
        possible.addAll((1..n).flatMap { x -> (1..m).map { y -> x to y } })
        possible.add(0 to 1)
        possible.add(n + 1 to m)
        var dp = MutableList(n + 2) { MutableList(m + 2) { false } }
        var count = 0
        dp[0][1] = true

        while (true) {
            val ind = count % positions.size
            val newDp = MutableList(n + 2) { MutableList(m + 2) { false } }
            for(x in 0 .. n+1) {
                for(y in 0 .. m+1) {
                    if (!dp[x][y])
                        continue
                    for((dx, dy) in listOf(Pair(0, 0), Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0))) {
                        val nx = x + dx
                        val ny = y + dy
                        if (nx to ny in possible && nx to ny !in positions[ind])
                            newDp[nx][ny] = true
                    }
                }
            }
            if (newDp[n + 1][m]) {
                return count
            }
            count++
            dp = newDp
        }
    }

    fun part2(input: List<String>): Int {
        val n = input.size - 2
        val m = input[0].length - 2
        val time = n * m / n.toBigInteger().gcd(m.toBigInteger()).toInt()
        var blizzards = parseInput(input)
        val positions = MutableList(time) { mutableSetOf<Pair<Int, Int>>() }
        positions[0] = blizzards.map { it.pos }.toMutableSet()
        for(i in 1 ..< time) {
            val newBlizzards = mutableListOf<Blizzard>()
            for((pos, dir) in blizzards) {
                var newPos = pos.first + dir.first to pos.second + dir.second
                if (newPos.first == n + 1) newPos = 1 to newPos.second
                if (newPos.first == 0) newPos = n to newPos.second
                if (newPos.second == m + 1) newPos = newPos.first to 1
                if (newPos.second == 0) newPos = newPos.first to m
                newBlizzards.add(Blizzard(newPos, dir))
            }
            blizzards = newBlizzards;
            positions[i] = blizzards.map { it.pos }.toMutableSet()
        }
        val possible = mutableSetOf<Pair<Int, Int>> ()
        possible.addAll((1..n).flatMap { x -> (1..m).map { y -> x to y } })
        possible.add(0 to 1)
        possible.add(n + 1 to m)
        var dp = MutableList(n + 2) { MutableList(m + 2) { false } }
        var count = 0
        dp[0][1] = true

        var state = 0

        while (true) {
            val ind = count % positions.size
            var newDp = MutableList(n + 2) { MutableList(m + 2) { false } }
            for(x in 0 .. n+1) {
                for(y in 0 .. m+1) {
                    if (!dp[x][y])
                        continue
                    for((dx, dy) in listOf(Pair(0, 0), Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0))) {
                        val nx = x + dx
                        val ny = y + dy
                        if (nx to ny in possible && nx to ny !in positions[ind])
                            newDp[nx][ny] = true
                    }
                }
            }
            if (newDp[n + 1][m]) {
                if (state == 0){
                    state = 1
                    newDp = MutableList(n + 2) { MutableList(m + 2) { false } }
                    newDp[n + 1][m] = true
                }
                else if (state == 2)
                    return count
            }
            if (newDp[0][1] && state == 1) {
                state = 2
                newDp = MutableList(n + 2) { MutableList(m + 2) { false } }
                newDp[0][1] = true
            }
            count++
            dp = newDp
        }
    }

    val testInput = readInput("Day24_test")
    check(part1(testInput) == 18)
    check(part2(testInput) == 54)

    val input = readInput("Day24")
    println(part1(input))
    println(part2(input))
}