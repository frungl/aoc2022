import kotlin.math.max

@OptIn(ExperimentalStdlibApi::class)
fun main() {
    fun part1(input: List<String>): Int {
        val name = input.map { it.substringAfter("Valve ").substringBefore(' ') }
        val flow = input.map { it.substringAfter("rate=").substringBefore(';').toInt() }
        val leads = input.map { it.split(' ').drop(9).map { str ->
            when {
                str.last() == ',' -> str.dropLast(1)
                else -> str
            }
        }}
        val n = name.size
        val ind = mutableMapOf<String, Int>()
        name.forEachIndexed { i, s -> ind[s] = i }
        val can = mutableListOf<Int>()
        (0..<n).forEach { if (flow[it] != 0) can.add(it) }
        val mask = (1 shl can.size)
        val dp =
            MutableList(31) { MutableList(n) { MutableList(mask) { -1 } } }
        dp[0][ind["AA"]!!][0] = 0
        var ans = 0

        val go = leads.map { it.map { s -> ind[s]!! } }

        for (time in (0..<30)) {
            for (v in (0..<n)) {
                for (lmask in (0..<mask)) {
                    if (dp[time][v][lmask] == -1)
                        continue

                    val nval = dp[time][v][lmask] + (0..<can.size).sumOf {
                        when {
                            ((lmask shr it) and 1) == 1 -> flow[can[it]]
                            else -> 0
                        }
                    }

                    if (flow[v] != 0) {
                        val pos = can.binarySearch(v)
                        if (((lmask shr pos) and 1) == 0) {
                            val nmask = lmask or (1 shl pos)
                            if (dp[time + 1][v][nmask] < nval) {
                                dp[time + 1][v][nmask] = nval
                                ans = max(ans, nval)
                            }
                        }
                    }

                    go[v].forEach {
                        if (dp[time + 1][it][lmask] < nval) {
                            dp[time + 1][it][lmask] = nval
                            ans = max(ans, nval)
                        }
                    }
                }
            }
        }
        return ans
    }

    fun part2(input: List<String>): Int {
        val name = input.map { it.substringAfter("Valve ").substringBefore(' ') }
        val flow = input.map { it.substringAfter("rate=").substringBefore(';').toInt() }
        val leads = input.map { it.split(' ').drop(9).map { str ->
            when {
                str.last() == ',' -> str.dropLast(1)
                else -> str
            }
        }}
        val n = name.size
        val ind = mutableMapOf<String, Int>()
        name.forEachIndexed { i, s -> ind[s] = i }
        val can = mutableListOf<Int>()
        (0..<n).forEach { if (flow[it] != 0) can.add(it) }
        val mask = (1 shl can.size)
        val dp =
            MutableList(27) { MutableList(n) { MutableList(n) { mutableMapOf<Int, Int>() } } }
        dp[0][ind["AA"]!!][ind["AA"]!!][0] = 0
        var ans = 0

        val go = leads.map { it.map { s -> ind[s]!! } }

        for (time in (0..<26)) {
            for (v in (0..<n)) {
                for (u in (0..<n)) {
                    val tmp = dp[time][v][u].keys.sortedDescending()
                    val was = mutableListOf<Int>()
                    for (lmask in tmp) {
                        var uniq = true
                        for (w in was) {
                            if (w and lmask == lmask) {
                                uniq = false
                                break;
                            }
                        }
                        if (!uniq)
                            continue
                        was.add(lmask)
                        val nval = dp[time][v][u][lmask]!! + (0..<can.size).sumOf {
                            when {
                                ((lmask shr it) and 1) == 1 -> flow[can[it]]
                                else -> 0
                            }
                        }

                        var nmask = lmask
                        if (flow[v] != 0) {
                            val pos = can.binarySearch(v)
                            nmask = nmask or (1 shl pos)
                            val tmask = lmask or (1 shl pos)
                            go[u].forEach { nu ->
                                if ((dp[time + 1][v][nu][tmask] ?: -1) < nval) {
                                    dp[time + 1][v][nu][tmask] = nval
                                    ans = max(ans, nval)
                                }
                            }
                        }
                        if (flow[u] != 0) {
                            val pos = can.binarySearch(u)
                            nmask = nmask or (1 shl pos)
                            val tmask = lmask or (1 shl pos)
                            go[v].forEach { nv ->
                                if ((dp[time + 1][nv][u][tmask] ?: -1) < nval) {
                                    dp[time + 1][nv][u][tmask] = nval
                                    ans = max(ans, nval)
                                }
                            }
                        }
                        if ((dp[time + 1][v][u][nmask] ?: -1) < nval) {
                            dp[time + 1][v][u][nmask] = nval
                            ans = max(ans, nval)
                        }

                        go[v].forEach { nv ->
                            go[u].forEach { nu ->
                                if ((dp[time + 1][nv][nu][lmask] ?: -1) < nval) {
                                    dp[time + 1][nv][nu][lmask] = nval
                                    ans = max(ans, nval)
                                }
                            }
                        }
                    }
                    dp[time][v][u].clear()
                }
            }
        }
        return ans
    }

    val testInput = readInput("Day16_test")
    check(part1(testInput) == 1651)
    check(part2(testInput) == 1707)

    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
}