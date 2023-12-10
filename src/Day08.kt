import kotlin.math.max

fun lowestCommonMultiple(a: Long, b: Long): Long {
    val larger = max(a, b)
    val MAX_LCM = a * b
    var lcm = larger
    while (lcm <= MAX_LCM) {
        if (lcm % a == 0L && lcm % b == 0L) return lcm
        lcm += larger
    }
    return MAX_LCM
}
data class NetworkMap(val sequence: String, val map: Map<String, Pair<String, String>>) {
    private fun nextLocation(location: String, step: Int): String {
        return when (sequence[step % sequence.length]) {
            'L'  -> map[location]!!.first
            else -> map[location]!!.second
        }
    }

    fun stepsUntil(startLocation: String, targetLocations: List<String>, startStep: Int = 0): Int {
        var step     = startStep
        var location = startLocation
        while (location !in targetLocations)
            location = nextLocation(location, step++)
        return step - startStep
    }

    fun stepsUntil(startLocation: String, targetLocation: String, startStep: Int = 0): Int {
        return stepsUntil(startLocation, listOf(targetLocation), startStep)
    }
}

fun main() {
    fun parseNetworkMap(input: List<String>): NetworkMap {
        val networkMap = (2..<input.size)
            .map { input[it] }
            .associate {
                val (key, left, right) = it.split(" = ", ", ")
                key to Pair(left.removePrefix("("), right.removeSuffix(")"))
            }
        return NetworkMap(sequence=input[0], networkMap)
    }

    fun part1(input: List<String>) = parseNetworkMap(input).stepsUntil("AAA", "ZZZ")

    fun part2(input: List<String>): Long {
        val networkMap      = parseNetworkMap(input)
        val targetLocations = networkMap.map.keys.filter { it.endsWith("Z") }
        val startLocations  = networkMap.map.keys.filter { it.endsWith("A") }.toMutableList()

        return startLocations
            .map { networkMap.stepsUntil(it, targetLocations).toLong() }
            .reduce { lcm, i -> lowestCommonMultiple(lcm, i) }
    }

    // Test inputs
    val testInput1 = readInput("Day08_test1")
    val testInput2 = readInput("Day08_test2")
    val testInput3 = readInput("Day08_test3")
    val input      = readInput("Day08")

    check(part1(testInput1) == 2)
    check(part1(testInput2) == 6)
    check(part2(testInput3) == 6L)

    println("Part1: ${part1(input)}")
    println("Part2: ${part2(input)}")
}
