import kotlin.math.abs

val regexPart   = """(\d+)""".toRegex()
val regexSymbol = """([^0-9.])""".toRegex()
val regexGear   = """(\*)""".toRegex()

data class Part(val row: Int, val colRange: IntRange, val value: Int) {
    fun intersects(symbol: Symbol): Boolean {
        if (abs(row - symbol.row) > 1) return false
        return (symbol.col >= colRange.first - 1 && symbol.col <= colRange.last + 1)
    }
}

data class Symbol(val row: Int, val col: Int, val symbol: Char) {
    fun intersects(part: Part) = part.intersects(this)
}

fun main() {
    fun part1(input: List<String>): Int {
        val validParts = HashSet<Part>()

        val parts   = ArrayList<Part>()
        val symbols = ArrayList<Symbol>()

        for ((row, line) in input.withIndex()) {
            regexPart.findAll(line).forEach {
                parts.add(Part(row, it.range, it.value.toInt()))
            }
            regexSymbol.findAll(line).forEach {
                check(it.value.first() == it.value.last())
                symbols.add(Symbol(row, it.range.first, it.value.first()))
            }
        }

        for (symbol in symbols) {
            for (part in parts) {
                if (symbol.intersects(part)) validParts.add(part)
            }
        }
        return validParts.sumOf {it.value}
    }

    fun part2(input: List<String>): Int {
        val parts = ArrayList<Part>()
        val gears = ArrayList<Symbol>()

        for ((row, line) in input.withIndex()) {
            regexPart.findAll(line).forEach {
                parts.add(Part(row, it.range, it.value.toInt()))
            }
            regexGear.findAll(line).forEach {
                check(it.value.first() == it.value.last())
                gears.add(Symbol(row, it.range.first, it.value.first()))
            }
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

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test1")
    val test1 = part1(testInput)
    test1.println()
    check(test1 == 4361)

    val input = readInput("Day03")
    val part1 = part1(input)
    part1.println()
    check(part1 == 554003)

    val test2 = part2(testInput)
    test2.println()
    check(test2 == 467835)

    val part2 = part2(input)
    part2.println()
    check(part2 == 87263515)
}