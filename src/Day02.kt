data class RGBSample (var red: Int = 0, val green: Int = 0, val blue: Int = 0) {
    fun isPossible (threshold: RGBSample): Boolean {
        if (red   > threshold.red)   return false
        if (green > threshold.green) return false
        if (blue  > threshold.blue)  return false
        return true
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val possibleGameIDs = HashSet<Int>()
        val RGB_THRESHOLD = RGBSample(12, 13, 14)

        for (line in input) {
            val tokens = line.split(":")
            val gameID = tokens[0].replace("Game", "").trim().toInt()
            possibleGameIDs.add(gameID)
            for (sample in tokens[1].split(";")) {
                if (!possibleGameIDs.contains(gameID)) continue
                for (color in sample.split(",")) {
                    if (!possibleGameIDs.contains(gameID)) continue
                    var red = 0
                    var green = 0
                    var blue = 0
                    if (color.contains("red")) {
                        red = color.replace("red", "").trim().toInt()
                    } else if (color.contains("green")) {
                        green = color.replace("green", "").trim().toInt()
                    } else if (color.contains("blue")) {
                        blue = color.replace("blue", "").trim().toInt()
                    }
                    val rgbSample = RGBSample(red, green, blue)

                    if (!rgbSample.isPossible(RGB_THRESHOLD)) {
                        possibleGameIDs.remove(gameID)
                    }
                }
            }
        }
        return possibleGameIDs.sum()
    }

    fun part2(input: List<String>): Int {
        var sum = 0
        for (line in input) {
            val tokens = line.split(":")
            val gameID = tokens[0].replace("Game", "").trim().toInt()
            var r_min = 0
            var g_min = 0
            var b_min = 0
            for (sample in tokens[1].split(";")) {
                for (color in sample.split(",")) {
                    if (color.contains("red")) {
                        val red = color.replace("red", "").trim().toInt()
                        if (red > r_min) r_min = red
                    } else if (color.contains("green")) {
                        val green = color.replace("green", "").trim().toInt()
                        if (green > g_min) g_min = green
                    } else if (color.contains("blue")) {
                        val blue = color.replace("blue", "").trim().toInt()
                        if (blue > b_min) b_min = blue
                    }
                }
            }
            sum += r_min * g_min * b_min
        }
        return sum
    }

    val testInput = readInput("Day02_test1")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
