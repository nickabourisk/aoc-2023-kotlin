enum class Strength {  // example  maxLabel  uniqueLabel
    FIVE_OF_A_KIND,    // AAAAA       5           1
    FOUR_OF_A_KIND,    // AA8AA       4           2
    FULL_HOUSE,        // 23332       3           2
    THREE_OF_A_KIND,   // TTT98       3           3
    TWO_PAIR,          // 23432       2           3
    ONE_PAIR,          // A23A4       2           4
    HIGH_CARD          // 23456       1           5
}

const val LABELS = "AKQJT98765432"
fun labelOrdinal(char: Char) = LABELS.indexOf(char)

class Hand(private val handString: String): Comparable<Hand> {
    private val strength: Strength = computeStrength(handString)

    override fun toString() = handString
    override fun compareTo(other: Hand): Int {
        var cmp = this.strength.ordinal - other.strength.ordinal
        if (cmp != 0) return cmp
        for ((i, char) in handString.withIndex()) {
            cmp = labelOrdinal(char) - labelOrdinal(other.handString[i])
            if (cmp != 0) return cmp
        }
        return cmp
    }
}

fun computeStrength(handStr: String): Strength {
    val map = HashMap<Char, Int>()
    handStr.forEach {
        map[it] = map.getOrDefault(it, 0) + 1
    }
    val uniqueCount = map.keys.size
    val maxOfLabel = map.values.max()

    // NGA TODO: replace these with 'when' clauses
    if (maxOfLabel == 5) return Strength.FIVE_OF_A_KIND
    if (maxOfLabel == 4) return Strength.FOUR_OF_A_KIND
    if (maxOfLabel == 1) return Strength.HIGH_CARD
    if (maxOfLabel == 3) {
        if      (uniqueCount == 2) return Strength.FULL_HOUSE
        else if (uniqueCount == 3) return Strength.THREE_OF_A_KIND
        else throw RuntimeException("Should never get here!")
    }
    check(maxOfLabel == 2)
    if (uniqueCount == 3) return Strength.TWO_PAIR
    if (uniqueCount == 4) return Strength.ONE_PAIR

    throw RuntimeException("Should never get here!")
}

fun main() {
    fun part1(input: List<String>): Int {
        return input
            .map {
                val (handTxt, bidTxt) = it.split(" ")
                Pair(Hand(handTxt), bidTxt.toInt())
            }
            .sortedBy { it.first }
            .reversed()
            .mapIndexed { i, (_, bid) -> (i+1) * bid }
            .sum()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test1")
    check(part1(testInput) == 6440)

    val input = readInput("Day07")
    part1(input).println()
    //part2(input).println()
}
