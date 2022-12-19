import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalStdlibApi::class)
fun main() {
    fun buy(have: List<Int>, cost: List<Int>): List<Int> {
        if((0..3).any { have[it] < cost[it] })
            return listOf()
        return List(have.size) { ind -> have[ind] - cost[ind] }
    }

    fun String.takeInt() = takeWhile { it != ' ' }.toInt()
    fun String.takeLastInt() = takeLastWhile { it != ' ' }.toInt()

    fun part1(input: List<String>): Int {
        return input.sumOf { str ->
            val idx = str.substringBefore(':').takeLastInt()
            val ore = listOf(str.substringAfter("ore robot costs ").takeInt() , 0, 0, 0)
            val clay = listOf(str.substringAfter("clay robot costs ").takeInt(), 0, 0, 0)
            val obsidian= listOf(str.substringAfter("obsidian robot costs ").takeInt(), str.substringBefore(" clay.").takeLastInt(), 0, 0)
            val geode = listOf(str.substringAfter("geode robot costs ").takeInt(), 0, str.substringBefore(" obsidian.").takeLastInt(), 0)

            val mxOre = max(ore[0], max(clay[0], max(obsidian[0], geode[0])))
            val mxClay = min(10, obsidian[1])

            val dp = MutableList(25) { mutableMapOf<List<Int>, MutableSet<List<Int>>>() }
            dp[0][listOf(1, 0, 0, 0)] = mutableSetOf(listOf(0, 0, 0, 0))
            var ans = 0
            for (time in (1..24)) {
                for ((state, values) in dp[time - 1]) {
                    for (value in values) {
                        val tryGeode = buy(value, geode)
                        if (tryGeode.isNotEmpty()) {
                            val newState = state.toMutableList()
                            newState[3]++
                            val newValue = List(value.size) { ind -> tryGeode[ind] + state[ind] }
                            ans = max(ans, newValue[3])
                            if (dp[time][newState.toList()] == null) dp[time][newState.toList()] = mutableSetOf()
                            dp[time][newState.toList()]!!.add(newValue)
                        }

                        val tryObsidian = buy(value, obsidian)
                        if (tryObsidian.isNotEmpty()) {
                            val newState = state.toMutableList()
                            newState[2]++
                            val newValue = List(value.size) { ind -> tryObsidian[ind] + state[ind] }
                            ans = max(ans, newValue[3])
                            if (dp[time][newState.toList()] == null) dp[time][newState.toList()] = mutableSetOf()
                            dp[time][newState.toList()]!!.add(newValue)
                        }

                        val tryClay = buy(value, clay)
                        if (tryClay.isNotEmpty()) {
                            val newState = state.toMutableList()
                            newState[1]++
                            if(newState[1] <=  mxClay) {
                                val newValue = List(value.size) { ind -> tryClay[ind] + state[ind] }
                                ans = max(ans, newValue[3])
                                if (dp[time][newState.toList()] == null) dp[time][newState.toList()] = mutableSetOf()
                                dp[time][newState.toList()]!!.add(newValue)
                            }
                        }

                        val tryOre = buy(value, ore)
                        if (tryOre.isNotEmpty()) {
                            val newState = state.toMutableList()
                            newState[0]++
                            if(newState[0] <= mxOre) {
                                val newValue = List(value.size) { ind -> tryOre[ind] + state[ind] }
                                ans = max(ans, newValue[3])
                                if (dp[time][newState.toList()] == null) dp[time][newState.toList()] = mutableSetOf()
                                dp[time][newState.toList()]!!.add(newValue)
                            }
                        }

                        if (tryOre.isEmpty() || tryClay.isEmpty() || tryObsidian.isEmpty() || tryGeode.isEmpty()) {
                            val newValue = List(value.size) { ind -> value[ind] + state[ind] }
                            ans = max(ans, newValue[3])
                            if (dp[time][state] == null) dp[time][state] = mutableSetOf()
                            dp[time][state]!!.add(newValue)
                        }
                    }
                }
            }
            ans * idx
        }
    }

    fun part2(input: List<String>): Long {
        return input.take(3).fold(1L) { acc, str ->
            val idx = str.substringBefore(':').takeLastInt()
            val ore = listOf(str.substringAfter("ore robot costs ").takeInt() , 0, 0, 0)
            val clay = listOf(str.substringAfter("clay robot costs ").takeInt(), 0, 0, 0)
            val obsidian= listOf(str.substringAfter("obsidian robot costs ").takeInt(), str.substringBefore(" clay.").takeLastInt(), 0, 0)
            val geode = listOf(str.substringAfter("geode robot costs ").takeInt(), 0, str.substringBefore(" obsidian.").takeLastInt(), 0)

            val mxOre = max(ore[0], max(clay[0], max(obsidian[0], geode[0])))
            val mxClay = min(10, obsidian[1])
            val mxObs= min(12, geode[2])
            val mxGeode = 12

            val dp = MutableList(33) {
                MutableList(mxOre + 1) {
                    MutableList(mxClay + 1) {
                        MutableList(mxObs + 1) {
                            MutableList(mxGeode + 1) {
                                mutableSetOf<List<Int>>()
                            }
                        }
                    }
                }
            }
            dp[0][1][0][0][0] = mutableSetOf(listOf(0, 0, 0, 0))
            var ans = 0
            var mxO = 1
            var mxC = 0
            var mxB = 0
            var mxG = 0
            var list = mutableSetOf(listOf(1, 0, 0, 0))
            for (time in (1..32)) {
                val prepare = list.sortedWith(object : Comparator <List<Int>> {
                    override fun compare (p0: List<Int>, pi: List<Int>) : Int {
                        if (p0[3] < pi[3])
                            return -1
                        if (p0[3] > pi[3])
                            return 1
                        if (p0[2] < pi[2])
                            return -1
                        if (p0[2] > pi[2])
                            return 1
                        if (p0[1] < pi[1])
                            return -1
                        if (p0[1] > pi[1])
                            return 1
                        if (p0[0] < pi[0])
                            return -1
                        if (p0[0] > pi[0])
                            return 1
                        return 0
                    }
                }).takeLast(10000)
                list.clear()

                for ((o, c, b, g) in prepare) {

                    val needSee = dp[time - 1][o][c][b][g].sortedWith(object : Comparator <List<Int>> {
                        override fun compare (p0: List<Int>, pi: List<Int>) : Int {
                            if (p0[3] < pi[3])
                                return -1
                            if (p0[3] > pi[3])
                                return 1
                            if (p0[2] < pi[2])
                                return -1
                            if (p0[2] > pi[2])
                                return 1
                            if (p0[1] < pi[1])
                                return -1
                            if (p0[1] > pi[1])
                                return 1
                            if (p0[0] < pi[0])
                                return -1
                            if (p0[0] > pi[0])
                                return 1
                            return 0
                        }
                    }).takeLast(5000)

                    for (value in needSee) {
                        val tryGeode = buy(value, geode)
                        if (g < mxGeode && tryGeode.isNotEmpty()) {
                            val state = listOf(o, c, b, g)
                            val newValue = List(value.size) { ind -> tryGeode[ind] + state[ind] }
                            ans = max(ans, newValue[3])
                            dp[time][o][c][b][g+1].add(newValue)
                            list.add(listOf(o, c, b, g + 1))
                            mxG = max(mxG, g + 1)
                        }

                        val tryObsidian = buy(value, obsidian)
                        if (b < mxObs && tryObsidian.isNotEmpty()) {
                            val state = listOf(o, c, b, g)
                            val newValue = List(value.size) { ind -> tryObsidian[ind] + state[ind] }
                            ans = max(ans, newValue[3])
                            dp[time][o][c][b+1][g].add(newValue)
                            list.add(listOf(o, c, b + 1, g))
                            mxB = max(mxB, b + 1)
                        }

                        val tryClay = buy(value, clay)
                        if (c < mxClay && tryClay.isNotEmpty()) {
                            val state = listOf(o, c, b, g)
                            val newValue = List(value.size) { ind -> tryClay[ind] + state[ind] }
                            ans = max(ans, newValue[3])
                            dp[time][o][c+1][b][g].add(newValue)
                            list.add(listOf(o, c + 1, b, g))
                            mxC = max(mxC, c + 1)
                        }

                        val tryOre = buy(value, ore)
                        if (o < mxOre && tryOre.isNotEmpty()) {
                            val state = listOf(o, c, b, g)
                            val newValue = List(value.size) { ind -> tryOre[ind] + state[ind] }
                            ans = max(ans, newValue[3])
                            dp[time][o+1][c][b][g].add(newValue)
                            list.add(listOf(o + 1, c, b, g))
                            mxO = max(mxO, o + 1)
                        }

                        if (tryOre.isEmpty() || tryClay.isEmpty() || tryObsidian.isEmpty() || tryGeode.isEmpty()) {
                            val state = listOf(o, c, b, g)
                            val newValue = List(value.size) { ind -> value[ind] + state[ind] }
                            ans = max(ans, newValue[3])
                            dp[time][o][c][b][g].add(newValue)
                            list.add(listOf(o, c, b, g))
                        }
                        if (list.size > 20000) {
                            list = list.sortedWith(object : Comparator <List<Int>> {
                                override fun compare (p0: List<Int>, pi: List<Int>) : Int {
                                    if (p0[3] < pi[3])
                                        return -1
                                    if (p0[3] > pi[3])
                                        return 1
                                    if (p0[2] < pi[2])
                                        return -1
                                    if (p0[2] > pi[2])
                                        return 1
                                    if (p0[1] < pi[1])
                                        return -1
                                    if (p0[1] > pi[1])
                                        return 1
                                    if (p0[0] < pi[0])
                                        return -1
                                    if (p0[0] > pi[0])
                                        return 1
                                    return 0
                                }
                            }).takeLast(10000).toMutableSet()
                        }
                    }
                }
            }
            acc * ans
        }
    }

    val testInput = readInput("Day19_test")
    check(part1(testInput) == 33)
    check(part2(testInput) == 3472L)

    val input = readInput("Day19")
    println(part1(input))
    println(part2(input))
}