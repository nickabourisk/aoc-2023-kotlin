val regexSpaces = """\s+""".toRegex()

data class ScratchCard (val cardNumber: Int, val winningNumbers: List<Int>, val ourNumbers: List<Int>) {
    fun matchingNumbers() = ourNumbers.count { winningNumbers.contains(it) }
    // TODO: make this more elegant and don't call `matchingNumbers()` twice...
    fun score() = if (matchingNumbers() == 0) 0 else 1 shl (matchingNumbers() - 1)
}

fun main() {
    fun parseTicket(line: String): ScratchCard {
        val scratchTokens  = line.split(":")
        val numberTokens   = scratchTokens[1].trim().split(" | ")
        val cardNumber     = scratchTokens[0].replace("Card ", "").trim().toInt()
        val winningNumbers = numberTokens[0].trim().split(regexSpaces).map { it.toInt() }
        val ourNumbers     = numberTokens[1].trim().split(regexSpaces).map { it.toInt() }
        return ScratchCard(cardNumber, winningNumbers, ourNumbers)
    }

    fun parseTickets(input: List<String>) = input.map { parseTicket(it) }

    fun part1(input: List<String>) = parseTickets(input).sumOf { it.score() }

    fun part2(input: List<String>): Int {
        val tickets = parseTickets(input)
        val ticketMap = HashMap<Int, Int>()
        tickets.forEach { ticketMap[it.cardNumber] = 1 } // we have one of every ticket to start
        for (ticket in tickets) {
            val instances = ticketMap[ticket.cardNumber]!!
            for (nextCard in ticket.cardNumber + 1..ticket.cardNumber + ticket.matchingNumbers()) {
                ticketMap[nextCard] = ticketMap[nextCard]!! + instances
            }
        }
        return ticketMap.values.sum()
    }

    val testInput = readInput("Day04_test1")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
