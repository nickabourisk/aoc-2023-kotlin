fun computeDiffList(list: List<Long>) = list.windowed(2).map { it[1] - it[0] }
fun isListAllZeros (list: List<Long>) = list.all { it == 0L }

// TODO: compute this using functional patterns only
fun computeListOfDiffLists(list: List<Long>): ArrayList<List<Long>> {
    val listOfLists = ArrayList<List<Long>>()

    listOfLists.add(list)
    var lastList = list
    while (!isListAllZeros(lastList)) {
        lastList = computeDiffList(lastList)
        listOfLists.add(lastList)
    }
    return listOfLists
}

fun predictNextValue(list: List<Long>) = computeListOfDiffLists(list).sumOf { it.last() }
fun predictPrevValue(list: List<Long>) = computeListOfDiffLists(list).reversed().map { it.first() }.reduce { acc, it -> it - acc }

fun main() {
    fun part1(input: List<String>) = input.map { line -> line.split(" ").map { it.toLong() } }.sumOf { predictNextValue(it) }
    fun part2(input: List<String>) = input.map { line -> line.split(" ").map { it.toLong() } }.sumOf { predictPrevValue(it) }

    val testInput1 = readInput("Day09_test1")
    val input      = readInput("Day09")
    check(part1(testInput1) == 114L)
    check(part2(testInput1) == 2L)

    part1(input).println()
    part2(input).println()
}
