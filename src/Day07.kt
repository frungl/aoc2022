data class Node(var weight: Int, val go: MutableMap<String, Int>, val par: Int)
fun main() {
    fun parseToTree(input: List<String>): MutableList<Node> {
        val graph = mutableListOf(Node(0, emptyMap<String, Int>().toMutableMap(), -1))
        var pos = 0
        input.drop(1).forEach { cmd ->
            if (cmd.startsWith("$")) {
                if(cmd.contains("cd")) {
                    val to = cmd.takeLastWhile { it != ' ' }
                    pos = if (to == "..") {
                        graph[pos].par
                    } else {
                        graph[pos].go[to]!!
                    }
                }
            } else {
                if(cmd.contains("dir")) {
                    val to = cmd.takeLastWhile { it != ' ' }
                    val ind = graph.size
                    graph[pos].go[to] = ind
                    graph.add(Node(0, emptyMap<String, Int>().toMutableMap(), pos))
                } else {
                    graph[pos].weight += cmd.takeWhile { it != ' ' }.toInt()
                }
            }
        }
        return graph
    }

    fun dfs(v: Int, graph: MutableList<Node>, res: MutableList<Int>): Int {
        val weight = graph[v].weight + graph[v].go.values.fold(0) { acc, next ->
            acc + dfs(next, graph, res)
        }
        res.add(weight)
        return weight
    }

    fun part1(input: List<String>): Int {
        val graph = parseToTree(input)
        val sizes = emptyList<Int>().toMutableList()
        dfs(0, graph, sizes)
        return sizes.filter { it <= 100000 }.sum()
    }

    fun part2(input: List<String>): Int {
        val graph = parseToTree(input)
        val sizes = emptyList<Int>().toMutableList()
        dfs(0, graph, sizes)
        val need = 30000000 - (70000000 - sizes.last())
        return sizes.partition { it < need }.second.min()
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}