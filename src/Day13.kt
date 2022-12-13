class Block : Comparable<Block> {
    private var value: Int? = null
    private val inside = mutableListOf<Block>()

    private fun isLast() = value != null
    fun set(x: Int) {
        value = x
    }
    fun add(x: Block) {
        inside.add(x)
    }

    override fun compareTo(other: Block): Int {
        if (isLast() && other.isLast()) {
            return value!!.compareTo(other.value!!)
        }
        if (!isLast() && !other.isLast()) {
            inside.indices.forEach {
                if (other.inside.size == it) {
                    return 1
                }
                val res = inside[it].compareTo(other.inside[it])
                if (res != 0)
                    return res
            }
            if (other.inside.size > inside.size) {
                return -1
            }
        } else if (isLast()) {
            val tmp = Block()
            tmp.add(this)
            return tmp.compareTo(other)
        } else {
            val tmp = Block()
            tmp.add(other)
            return this.compareTo(tmp)
        }
        return 0
    }

    override fun toString(): String {
        if (value != null) {
            return value.toString()
        }
        return inside.joinToString(prefix = "[", postfix = "]", separator = ", ")
    }
}

fun parse(str: String): Block {
    val here = Block()
    if(str.startsWith('[')) {
        val tmp = str.substring(1, str.length - 1)
        var cnt = 0
        var spos = 0
        for (i in tmp.indices) {
            cnt += when {
                tmp[i] == '[' -> 1
                tmp[i] == ']' -> -1
                else -> 0
            }
            if (cnt == 0 && (i + 1 == tmp.length || tmp[i + 1] == ',')) {
                here.add(parse(tmp.substring(spos, i+1)))
                spos = i + 2
            }
        }
    } else {
       if (str.isNotEmpty())
           here.set(str.toInt())
       else
           here.set(-1000)
    }
    return here
}

fun main() {
    fun part1(input: List<String>): Int {
        var ind = 0
        return input.chunked(3).sumOf { arr ->
            ind++
            val s = arr[0]
            val t = arr[1]
            when {
                parse(s) < parse(t) -> ind
                else -> 0
            }
        }
    }

    fun part2(input: List<String>): Int {
        val f = parse("[[2]]")
        val s = parse("[[6]]")
        val tmp = ((input + "[[2]]") + "[[6]]")
            .filter { it.isNotEmpty() }
            .map { parse(it) }
            .sorted()
        return (tmp.binarySearch(f) + 1) * (tmp.binarySearch(s) + 1)
    }

    val testInput = readInput("Day13_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 140)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}