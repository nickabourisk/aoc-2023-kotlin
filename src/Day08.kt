fun main() {
    fun part1(input: List<String>): Int {
        val sequence = input[0]

        val networkMap = (2..<input.size)
            .map {
                input[it]
            }
            .associate {
                val (key, left, right) = it.split(" = ", ", ")
                key to Pair(left.removePrefix("("), right.removeSuffix(")"))
            }

        var step     = 0
        var location = "AAA"
        while (location != "ZZZ") {
            val direction = sequence[step % sequence.length]
            location = when (direction) {
                'L' -> networkMap[location]!!.first
                'R' -> networkMap[location]!!.second
                else -> throw RuntimeException("Sequence should only have 'L' and 'R' directions!")
            }
            step++
        }
        return step
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput1 = readInput("Day08_test1")
    val testInput2 = readInput("Day08_test2")
    val input      = readInput("Day08")

    check(part1(testInput1) == 2)
    check(part1(testInput2) == 6)
    println("Part1: ${part1(input)}")
    //println("Part2: ${part2(input)}")
}
