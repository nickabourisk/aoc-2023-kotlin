fun main() {
    fun part1(input: List<String>): Int {
        val times     = input[0].removePrefix("Time:")     .trim().split("\\s+".toRegex()).map { it.toInt() }
        val distances = input[1].removePrefix("Distance: ").trim().split("\\s+".toRegex()).map { it.toInt() }
        val pairs = times.zip(distances)

        return pairs
            .map {(t, d) ->
                (1..<t).count {
                    (t-it)*it > d         // distance(t_wait) = (t_total - t_wait) * speed; speed = t_wait
                }
            }
            .reduce { acc, i -> acc * i } // product of all race results
    }

    fun part2(input: List<String>): Int {
        val time = input[0].removePrefix("Time:")    .replace(" ", "").toLong()
        val dist = input[1].removePrefix("Distance:").replace(" ", "").toLong()

        return (1..<time).count {(time-it)*it > dist}
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test1")
    check(part1(testInput) == 288)
    check(part2(testInput) == 71503)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}