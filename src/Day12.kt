import java.util.*

fun main() {
    fun bfs(graph: List<String>, sX: Int, sY: Int, eX: Int, eY: Int): Int {
        val n = graph.size
        val m = graph[0].length
        val dist = mutableMapOf<Pair<Int, Int>, Int>()
        val q = LinkedList<Pair<Int, Int>>()
        dist[sX to sY] = 0
        q.addFirst(sX to sY)

        val go = listOf(
            0 to 1,
            1 to 0,
            0 to -1,
            -1 to 0
        )

        while (!q.isEmpty()) {
            val (x, y) = q.last
            q.removeLast()
            for ((dx, dy) in go) {
                if(x + dx < 0 || x + dx >= n) continue
                if(y + dy < 0 || y + dy >= m) continue
                if(dist.containsKey(x + dx to y + dy)) continue
                val ch = graph[x + dx][y + dy]
                if (ch - graph[x][y] > 1) continue
                dist[x + dx to y + dy] = dist[x to y]!! + 1
                q.addFirst(x + dx to y + dy)
            }
        }
        return dist[eX to eY] ?: 1000000000
    }

    fun part1(input: List<String>): Int {
        val sX = input.indexOfFirst { it.contains('S') }
        val sY = input[sX].indexOfFirst { it == 'S' }
        val eX = input.indexOfFirst { it.contains('E') }
        val eY = input[eX].indexOfFirst { it == 'E' }

        val graph = input.map { it.replace('S', 'a').replace('E', 'z') }

        return bfs(graph, sX, sY, eX, eY)
    }

    fun part2(input: List<String>): Int {
        val eX = input.indexOfFirst { it.contains('E') }
        val eY = input[eX].indexOfFirst { it == 'E' }

        val graph = input.map { it.replace('S', 'a').replace('E', 'z') }

        val posX = graph.mapIndexedNotNull { i, s -> if(s.contains('a')) i else null }
        return posX.flatMap { graph[it].mapIndexedNotNull { i, c -> if(c == 'a') (it to i) else null } }.minOf {
            val (sX, sY) = it
            bfs(graph, sX, sY, eX, eY)
        }
    }

    val testInput = readInput("Day12_test")
    check(part1(testInput) == 31)
    check(part2(testInput) == 29)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}