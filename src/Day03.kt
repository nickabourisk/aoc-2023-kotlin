import kotlin.math.abs

val regexPart   = """(\d+)""".toRegex()
val regexSymbol = """([^0-9.])""".toRegex()
val regexGear   = """(\*)""".toRegex()

data class Part(val row: Int, val colRange: IntRange, val value: Int) {
    fun intersects(symbol: Symbol): Boolean {
        return abs(row - symbol.row) <= 1
            && symbol.col >= colRange.first - 1
            && symbol.col <= colRange.last  + 1
    }
}

data class Symbol(val row: Int, val col: Int, val symbol: Char) {
    fun intersects(part: Part) = part.intersects(this)
}

fun main() {
    fun part1(input: List<String>): Int {
        val validParts = HashSet<Part>()
        val parts      = ArrayList<Part>()
        val symbols    = ArrayList<Symbol>()

        for ((row, line) in input.withIndex()) {
            regexPart.findAll(line).forEach {parts.add(Part(row, it.range, it.value.toInt())) }
            regexSymbol.findAll(line).forEach {symbols.add(Symbol(row, it.range.first, it.value.first())) }
        }
        for (symbol in symbols) {
            for (part in parts) {
                if (symbol.intersects(part)) validParts.add(part)
            }
        }
        return validParts.sumOf { it.value }
    }

    fun part2(input: List<String>): Int {
        val parts = ArrayList<Part>()
        val gears = ArrayList<Symbol>()

        for ((row, line) in input.withIndex()) {
            regexPart.findAll(line).forEach {parts.add(Part(row, it.range, it.value.toInt())) }
            regexGear.findAll(line).forEach {gears.add(Symbol(row, it.range.first, it.value.first())) }
        }

        var sum = 0
        for (gear in gears) {
            val tentativeParts = ArrayList<Int>()
            for (part in parts) {
                if (gear.intersects(part)) tentativeParts.add(part.value)
            }
            if (tentativeParts.size == 2) {
                sum += tentativeParts[0] * tentativeParts[1]
            }
        }
        return sum
    }

    val testInput = readInput("Day03_test1")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}