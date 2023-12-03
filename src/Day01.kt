fun main() {
    fun part1(inputLines: List<String>): Int {
        var sum = 0
        for (input in inputLines) {
            sum += calcCalibrationPart1(input)
        }
        return sum
    }

    fun part2(inputLines: List<String>): Int {
        var sum = 0
        for (input in inputLines) {
            sum += calcCalibrationPart2(input)
        }
        return sum
    }

    check(142 == part1(readInput("Day01_test1")))
    check(281 == part2(readInput("Day01_test2")))
    check(55712 == part1(readInput("Day01")))
    check(55413 == part2(readInput("Day01")))
}

val digitStrings = listOf(
    "zero", // it appears none of the inputs use zero so lets add this here to make indexing consistent instead of off-by-one
    "one",
    "two",
    "three",
    "four",
    "five",
    "six",
    "seven",
    "eight",
    "nine"
)

const val digitChars = "0123456789"



fun calcCalibrationPart1(input: String): Int {
    val digitText = input.filter { it.isDigit() }
    return 10 * digitText.first().digitToInt() + digitText.last().digitToInt()
}

fun calcCalibrationPart2(input: String): Int {
    return 10 * findFirstDigit(input) + findLastDigit(input)
}

fun findFirstDigit(input: String): Int {
    var digitIndex = -1
    var minIndex = Int.MAX_VALUE
    for ((index, digitString) in digitStrings.withIndex()) {
        var indexOf = input.indexOf(digitString)
        if (indexOf != -1 && indexOf < minIndex) {
            minIndex = indexOf
            digitIndex = index
        }
        indexOf = input.indexOf(digitChars[index])
        if (indexOf != -1 && indexOf < minIndex) {
            minIndex = indexOf
            digitIndex = index
        }
    }
    if (digitIndex == -1) throw RuntimeException("Couldn't parse any first digits from input string [$input]")

    return digitIndex
}

fun findLastDigit(input: String): Int {
    var digitIndex = -1
    var maxIndex = -1
    for ((index, digitString) in digitStrings.withIndex()) {
        var indexOf = input.lastIndexOf(digitString)
        if (indexOf != -1 && indexOf > maxIndex) {
            maxIndex = indexOf
            digitIndex = index
        }
        indexOf = input.lastIndexOf(digitChars[index])
        if (indexOf != -1 && indexOf > maxIndex) {
            maxIndex = indexOf
            digitIndex = index
        }
    }
    if (digitIndex == -1) throw RuntimeException("Couldn't parse any last digits from input string [$input]")

    return digitIndex
}
