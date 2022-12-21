data class Monkey(val left: String?, val operation: String?, val right: String?, var number: Long) {
    init {
        if (left == null || right == null || operation == null) {
            require(left == null && right == null && operation == null)
        }
    }

    val hasNumber: Boolean
        get() = left == null && right == null && operation == null
}
fun main() {
    fun applyOp(left: Long, right: Long, op: String): Long {
        return when (op) {
            "+" -> left + right
            "-" -> left - right
            "*" -> left * right
            "/" -> left / right
            else -> throw IllegalArgumentException("Unknown op $op")
        }
    }

    fun dfs(graph: Map<String, Monkey>, monkeyName: String): Long {
        val monkey = graph[monkeyName] ?: throw IllegalArgumentException("Unknown monkey $monkeyName")
        if (monkey.hasNumber) {
            return monkey.number
        }
        val left = dfs(graph, monkey.left ?: throw IllegalArgumentException("No left for $monkeyName"))
        val right = dfs(graph, monkey.right ?: throw IllegalArgumentException("No right for $monkeyName"))
        val result = applyOp(left, right, monkey.operation ?: throw IllegalArgumentException("No op for $monkeyName"))
        monkey.number = result
        return result
    }

    fun parse(input: List<String>): Map<String, Monkey> = input.map {
        val name = it.substringBefore(":")
        if ((it.toSet() intersect setOf('+', '-', '/', '*')).isNotEmpty()) {
            val splited = it.substringAfter(": ").split(" ")
            val left = splited[0]
            val operation = splited[1]
            val right = splited[2]
            name to Monkey(left, operation, right, 0)
        } else {
            name to Monkey(null, null, null, it.substringAfter(": ").toLong())
        }
    }.toMap()

    fun part1(input: List<String>): Long {
        val tree = parse(input)
        return dfs(tree, "root")
    }

    fun reverseOpLeft(op: String, right: Long, need: Long): Long {
        return when (op) {
            "+" -> need - right
            "-" -> need + right
            "*" -> need / right
            "/" -> need * right
            else -> throw IllegalArgumentException("Unknown op $op")
        }
    }

    fun reverseOpRight(op: String, left: Long, need: Long): Long {
        return when (op) {
            "+" -> need - left
            "-" -> left - need
            "*" -> need / left
            "/" -> left / need
            else -> throw IllegalArgumentException("Unknown op $op")
        }
    }

    fun goToHuman(tree: Map<String, Monkey>, monkeyName: String): String? {
        if (monkeyName == "humn") {
            return ""
        }
        val monkey = tree[monkeyName] ?: throw IllegalArgumentException("Unknown monkey $monkeyName")
        if (monkey.hasNumber) {
            return null
        }
        val left = goToHuman(tree, monkey.left ?: throw IllegalArgumentException("No left for $monkeyName"))
        val right = goToHuman(tree, monkey.right ?: throw IllegalArgumentException("No right for $monkeyName"))
        if (left != null) {
            return "L$left"
        } else if (right != null) {
            return "R$right"
        }
        return null
    }

    fun part2(input: List<String>): Long {
        val tree = parse(input)
        dfs(tree, "root")
        val path = goToHuman(tree, "root") ?: throw IllegalArgumentException("No path to human")
        var monkeyName = "root"
        var needValue = 0L
        path.forEach { next ->
            if (monkeyName == "humn") {
                return@forEach
            }
            val monkey = tree[monkeyName] ?: throw IllegalArgumentException("Unknown monkey $monkeyName")
            val op = monkey.operation ?: throw IllegalArgumentException("No op for $monkeyName")
            val left = monkey.left ?: throw IllegalArgumentException("No left for $monkeyName")
            val right = monkey.right ?: throw IllegalArgumentException("No right for $monkeyName")
            val monkeyLeft = tree[left] ?: throw IllegalArgumentException("Unknown monkey $left")
            val monkeyRight = tree[right] ?: throw IllegalArgumentException("Unknown monkey $right")
            if (monkeyName == "root") {
                needValue = if (next == 'L') {
                    monkeyRight.number
                } else {
                    monkeyLeft.number
                }
            } else {
                needValue = if (next == 'L') {
                    reverseOpLeft(op, monkeyRight.number, needValue)
                } else {
                    reverseOpRight(op, monkeyLeft.number, needValue)
                }
            }
            monkeyName = if (next == 'L') {
                left
            } else {
                right
            }
        }
        return needValue
    }

    val testInput = readInput("Day21_test")
    check(part1(testInput) == 152L)
    check(part2(testInput) == 301L)

    val input = readInput("Day21")
    println(part1(input))
    println(part2(input))
}