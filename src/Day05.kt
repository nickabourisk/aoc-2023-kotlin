import kotlin.collections.ArrayList

data class CategoryRange(val diff: Long, val range: LongRange) {
    fun contains(value: Long) = range.contains(value)
    fun mapRange(otherRange: LongRange) = otherRange.first + diff..otherRange.last + diff
}

fun isLeftOf(range1: LongRange, range2: LongRange) = range1.last < range2.first
fun isRightOf(range1: LongRange, range2: LongRange) = range1.first > range2.last
fun isFullyWithin(range1: LongRange, range2: LongRange) = range1.first >= range2.first && range1.last <= range2.last
fun isSpanningAcross(range1: LongRange, range2: LongRange) = isFullyWithin(range2, range1)
fun isLeftStraddling(range1: LongRange, range2: LongRange) = range2.contains(range1.last) && range1.first < range2.first
fun isRightStraddling(range1: LongRange, range2: LongRange) = range2.contains(range1.first) && range1.last > range2.last


fun createCategoryRange(startDst: Long, startSrc: Long, length: Long) =
    CategoryRange(startDst - startSrc, LongRange(startSrc, startSrc + length - 1))

fun parseCategoryRange(line: String): CategoryRange {
    val (startDst, startSrc, length) = line.split(" ").map { it.toLong() }
    return createCategoryRange(startDst, startSrc, length)
}

// A Category Map consists of a collection of CategoryRange's
class CategoryMap(val srcText: String, val dstText: String) {
    val ranges = ArrayList<CategoryRange>()

    fun add(range: CategoryRange) = ranges.add(range)

    // TODO: convert this to a functional one-liner
    fun map(value: Long): Long {
        for (range in ranges) {
            if (range.contains(value)) return value + range.diff
        }
        return value
    }
}

fun main() {
    // Parse Category Maps. Skip the first two lines (list of seeds and empty line)
    fun parseCategoryMaps(input: List<String>): ArrayList<CategoryMap> {
        val listOfMaps = ArrayList<CategoryMap>()
        val regexMapLine = """(?<src>[a-z]+)-to-(?<dst>[a-z]+) map:""".toRegex()
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
        return listOfMaps
    }

    fun part1(input: List<String>): Int {
        val seeds = input[0]
                           .removePrefix("seeds: ")
                           .split(" ")
                           .map { it.toLong() }

        val listOfMaps = parseCategoryMaps(input)

        // TODO: convert this into a functional one-liner
        val newSeeds = seeds.toMutableList()
        for (categoryMap in listOfMaps) {
            for ((index, newSeed) in newSeeds.withIndex()) {
                newSeeds[index] = categoryMap.map(newSeed)
            }
        }
        return newSeeds.min().toInt()
    }

    fun part2(input: List<String>): Long {
        val seedRanges = input[0]
                           .removePrefix("seeds: ")
                           .split(" ")
                           .map { it.toLong() }
                           .chunked(2)
                           .map { (start, length) ->
                               LongRange(start, start + length - 1)
                           }

        val listOfMaps = parseCategoryMaps(input)
        var currentSeedRanges = seedRanges
        for (i in listOfMaps.indices) {
            currentSeedRanges = leftToRightMapping(currentSeedRanges, listOfMaps[i].ranges)
        }
        return currentSeedRanges.minOf { it.first }
    }

    val testInput = readInput("Day05_test1")
    check(part1(testInput) == 35)
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

// There are 6 cases when examining whether the source and destination ranges overlap:
//   1. source outside (left  of destination)
//   2. source outside (right of destination)
//   3. source inside (of destination)
//   4. source includes (destination; i.e. destination wholly inside source)
//   5. source straddles start of destination
//   6. source straddles end   of destination
// In cases 1-3, we don't straddle any destination boundaries so can return the original
// If we straddle only one side of destination (start or end), we'll split our range into 2 sub-ranges
// If source includes destination, we'll split our range into 3 sub-ranges

// I've also verified that none of the category mapping ranges overlap at each layer and the initial seeds don't either
fun leftToRightMapping(immutableSrcRanges: List<LongRange>, dstCatRanges: List<CategoryRange>): ArrayList<LongRange> {
    val srcRanges = immutableSrcRanges.sortedBy { it.first }
    val dstRanges = dstCatRanges.sortedBy { it.range.first }

    var upto = srcRanges[0].first
    var end  = srcRanges[0].last
    var i = 0
    var j = 0

    val mappedRanges = ArrayList<LongRange>()
    while (true) {
        var doMoveSrc = false
        var doMoveDst = false
        val range = LongRange(upto, end)
        val other = dstRanges[j].range

        if (isRightOf(range, other)) {
            doMoveDst = true
        }
        else if (isLeftOf(range, other)) {
            mappedRanges.add(range)
            doMoveSrc = true
        } else if (isFullyWithin(range, other)) {
            mappedRanges.add(dstRanges[j].mapRange(range))
            doMoveSrc = true
        } else if (isLeftStraddling(range, other)) {
            // add the range before destination starts
            mappedRanges.add(LongRange(upto, other.first - 1))
            // add the mapped range inside destination
            mappedRanges.add(dstRanges[j].mapRange(LongRange(other.first, end)))
            doMoveSrc = true
        } else if (isRightStraddling(range, other)) {
            // add the mapped range inside destination
            mappedRanges.add(dstRanges[j].mapRange(LongRange(upto, other.last)))
            // We don't know whether the rest of the source range will overlap with the next destination range or not.
            // Process it on the next loop iteration
            upto = other.last + 1
            doMoveDst = true
        }
        else if (isSpanningAcross(range, other)) {
            // add the range before destination starts
            mappedRanges.add(LongRange(upto, other.first - 1))
            // add the mapped range inside destination
            mappedRanges.add(dstRanges[j].mapRange(LongRange(other.first, other.last)))
            // We don't know whether the rest of the source range will overlap with the next destination range or not.
            // Process it on the next loop iteration
            upto = other.last + 1
            doMoveDst = true
        }
        else throw RuntimeException("Unhandled range vs range comparison case!")

        // Move Source or Destination range we're considering
        if (doMoveSrc) {
            if (i++ == srcRanges.size - 1) break
            upto = srcRanges[i].first
            end  = srcRanges[i].last
        }
        if (doMoveDst) {
            if (j++ == dstCatRanges.size - 1) {
                // no more destination ranges, add the rest of the source ranges
                mappedRanges.add(LongRange(upto, end))
                srcRanges.subList(i, srcRanges.size).forEach { mappedRanges.add(it) }
                break
            }
        }
    }
    return mappedRanges
}

