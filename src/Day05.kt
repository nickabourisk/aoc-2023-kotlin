data class CategoryRange(val diff: Long, val range: LongRange) {
    fun contains(value: Long) = range.contains(value)
    fun adjust(value: Long) = if (contains(value)) value + diff else value
}

fun createCategoryRange(startDst: Long, startSrc: Long, length: Long) =
    CategoryRange(startDst - startSrc, LongRange(startSrc, startSrc + length - 1))

fun parseCategoryRange(line: String): CategoryRange {
    val (startDst, startSrc, length) = line.split(" ").map { it.toLong() }
    return createCategoryRange(startDst, startSrc, length)
}

// A Category Map consists of a collection of CategoryRange's
class CategoryMap(val srcText: String, val dstText: String) {
    private val list = ArrayList<CategoryRange>()

    fun add(range: CategoryRange) = list.add(range)
    fun contains(value: Long) = list.forEach { list.contains(it) }

    // TODO: convert this to a functional one-liner
    fun map(value: Long): Long {
        for (range in list) {
            if (range.contains(value)) return value + range.diff
        }
        return value
    }
}

fun main() {
    val regexMapLine = """(?<src>[a-z]+)-to-(?<dst>[a-z]+) map:""".toRegex()

    fun part1(input: List<String>): Int {
        val seeds = input[0].replace("seeds: ", "").trim().split(" ").map {it.toLong()}
        val listOfMaps = ArrayList<CategoryMap>()

        // Parse Category Maps. Skip the first two lines (list of seeds and empty line)
        for (index in 2..<input.size) {
            val line = input[index]
            if (line.isNotBlank()) {
                val matchResult = regexMapLine.find(line)
                if (matchResult != null) {
                    listOfMaps.add(
                        CategoryMap(
                            matchResult.groups["src"]?.value!!,
                            matchResult.groups["dst"]?.value!!))
                }
                else listOfMaps.last().add(parseCategoryRange(line))
            }
        }

        // TODO: convert this into a functional one-liner
        val newSeeds = seeds.toMutableList()
        for (categoryMap in listOfMaps) {
            for ((index, newSeed) in newSeeds.withIndex()) {
                newSeeds[index] = categoryMap.map(newSeed)
            }
        }
        return newSeeds.min().toInt()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("Day05_test1")
    check(part1(testInput) == 35)
    //check(part2(testInput) == 1)

    val input = readInput("Day05")
    part1(input).println()
//    part2(input).println()
}
