import java.util.*

fun main() {
    fun part1(input: List<String>): Int {
        val lava = input.map { it.split(',').toList().map { it.toInt() } }
        val dirs = listOf(
            listOf(1, 0, 0),
            listOf(0, 1, 0),
            listOf(0, 0, 1),
            listOf(-1, 0, 0),
            listOf(0, -1, 0),
            listOf(0, 0, -1),
        )
        val lavaSet = lava.toSet()
        return lava.sumOf { pos ->
            dirs.count {
                val find = listOf(pos[0] + it[0], pos[1] + it[1], pos[2] + it[2])
                !lavaSet.contains(find)
            }
        }
    }

    fun part2(input: List<String>): Int {
        val lava = input.map { it.split(',').toList().map { it.toInt() } }
        val dirs = listOf(
            listOf(1, 0, 0),
            listOf(0, 1, 0),
            listOf(0, 0, 1),
            listOf(-1, 0, 0),
            listOf(0, -1, 0),
            listOf(0, 0, -1),
        )
        val lavaSet = lava.toMutableSet()

        for(x in (0..21)) {
            for(y in (0..21)) {
                for(z in (0..21)) {
                    val pos = listOf(x, y, z)
                    if(lavaSet.contains(pos))
                        continue
                    val vis = mutableSetOf(pos)
                    val q = LinkedList<List<Int>>()
                    q.addFirst(pos)
                    var cnt = 4000
                    while(cnt > 0) {
                        if(q.isEmpty()) break;
                        val nw = q.last
                        q.removeLast()
                        dirs.forEach {
                            val nxt = listOf(nw[0] + it[0], nw[1] + it[1], nw[2] + it[2])
                            if(!lavaSet.contains(nxt) && !vis.contains(nxt)) {
                                q.addFirst(nxt)
                                vis.add(nxt)
                                cnt--
                            }
                        }
                    }
                    if(cnt > 0) {
                        lavaSet.addAll(vis)
                    }
                }
            }
        }

        return lava.sumOf { pos ->
            dirs.count {
                val find = listOf(pos[0] + it[0], pos[1] + it[1], pos[2] + it[2])
                !lavaSet.contains(find)
            }
        }
    }

    val testInput = readInput("Day18_test")
    check(part1(testInput) == 64)
    check(part2(testInput) == 58)

    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}