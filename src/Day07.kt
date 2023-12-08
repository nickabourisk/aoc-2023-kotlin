enum class Strength {  // example  maxOfLabel  uniqueLabel
    FIVE_OF_A_KIND,    // AAAAA       5           1
    FOUR_OF_A_KIND,    // AA8AA       4           2
    FULL_HOUSE,        // 23332       3           2
    THREE_OF_A_KIND,   // TTT98       3           3
    TWO_PAIR,          // 23432       2           3
    ONE_PAIR,          // A23A4       2           4
    HIGH_CARD          // 23456       1           5
}

class Hand (private val handString: String, private val hasWildcards: Boolean = false): Comparable<Hand> {
    private val strength = evaluate(handString)

    private fun labelOrdinal(char: Char) =
        if (hasWildcards) "AKQT98765432J".indexOf(char)
        else              "AKQJT98765432".indexOf(char)

    override fun compareTo(other: Hand): Int {
        var cmp = this.strength.ordinal - other.strength.ordinal
        if (cmp != 0) return cmp
        for ((i, char) in handString.withIndex()) {
            cmp = labelOrdinal(char) - labelOrdinal(other.handString[i])
            if (cmp != 0) return cmp
        }
        return 0 // only reach if both hands are equal
    }

    private fun evaluate(handStr: String): Strength {
        if (handStr == "JJJJJ") return Strength.FIVE_OF_A_KIND // edge-case

        val map = HashMap<Char, Int>()
        handStr.forEach {
            if (!hasWildcards || it != 'J')
                map[it] = map.getOrDefault(it, 0) + 1
        }
        val uniqueCount = map.keys.size
        val maxOfLabel  = map.values.max() + if (hasWildcards) handStr.count { it == 'J' } else 0

        return when {
            maxOfLabel == 5                     -> Strength.FIVE_OF_A_KIND
            maxOfLabel == 4                     -> Strength.FOUR_OF_A_KIND
            maxOfLabel == 1                     -> Strength.HIGH_CARD
            maxOfLabel == 3 && uniqueCount == 2 -> Strength.FULL_HOUSE
            maxOfLabel == 3 && uniqueCount == 3 -> Strength.THREE_OF_A_KIND
            maxOfLabel == 2 && uniqueCount == 3 -> Strength.TWO_PAIR
            maxOfLabel == 2 && uniqueCount == 4 -> Strength.ONE_PAIR
            else -> throw RuntimeException("Unhandled hand evaluation!")
        }
    }
}

fun main() {
    fun computeWinnings(input: List<String>, hasWildcards: Boolean): Int {
        return input
            .map {
                val (handTxt, bidTxt) = it.split(" ")
                Pair(Hand(handTxt, hasWildcards), bidTxt.toInt())
            }
            .sortedBy { it.first }
            .reversed()
            .mapIndexed { i, (_, bid) -> (i+1) * bid }
            .sum()
    }
    fun part1(input: List<String>) = computeWinnings(input, hasWildcards=false)
    fun part2(input: List<String>) = computeWinnings(input, hasWildcards=true)

    val testInput = readInput("Day07_test1")
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput("Day07")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
