fun computeDiffList(list: List<Long>) = list.windowed(2).map { it[1] - it[0] }
fun isListAllZeros (list: List<Long>) = list.all { it == 0L }

// TODO: convert this into functional
fun predictNextValue(list: List<Long>): Long {
    val listOfLists = ArrayList<List<Long>>()

    listOfLists.add(list)
    var lastList = list
    while (!isListAllZeros(lastList)) {
        lastList = computeDiffList(lastList)
        listOfLists.add(lastList)
    }
    return listOfLists.sumOf { it.last() }
}

fun main() {
    fun part1(input: List<String>): Long {
        val sequences = ArrayList<List<Long>>()
        input.forEach { line ->
            sequences.add(line.split(" ").map { it.toLong() })
        }
        return sequences.sumOf { predictNextValue(it) }
    }

    fun part2(input: List<String>): Long {
        return 0L
    }

    val testInput1 = readInput("Day09_test1")
    val input      = readInput("Day09")
    check(part1(testInput1) == 114L)

    part1(input).println()
    //part2(input).println()
}
