fun main() {
    fun parse(input: List<String>): Pair<List<String>, List<List<List<String>>>> {
        val numbers = input.first().split(",")
        val boards = input.drop(1).chunked(6).map { lines ->
            lines.drop(1).map { line ->
                line.split("\\s+".toRegex()).filter { it.trim().isNotEmpty() }
            }
        }
        return Pair(numbers, boards)
    }

    fun score(board: List<List<String>>, matchedNumbers: ArrayList<String>): Int {
        val unmatchedSum = board
            .flatten()
            .subtract(matchedNumbers.toSet())
            .sumOf { it.toInt() }
        return unmatchedSum * matchedNumbers.last().toInt()
    }

    fun play(input: List<String>, winFirst: Boolean = true): Int {
        val (numbers, boards) = parse(input)

        val matchedNumbers = Array(boards.size) { ArrayList<String>() }
        val matchedRows = Array(boards.size) { intArrayOf(0, 0, 0, 0, 0) }
        val matchedColumns = Array(boards.size) { intArrayOf(0, 0, 0, 0, 0) }

        val winningBoardIndexes = ArrayList<Int>()

        run game@{
            numbers.forEach { number ->
                boards.forEachIndexed { boardIndex, board ->
                    if (winningBoardIndexes.contains(boardIndex)) return@forEachIndexed
                    board.forEachIndexed { rowIndex, row ->
                        val found = row.indexOf(number)
                        if (found != -1) {
                            matchedNumbers[boardIndex].add(row[found])
                            matchedRows[boardIndex][rowIndex] += 1
                            matchedColumns[boardIndex][found] += 1
                        }
                    }
                    if (matchedRows[boardIndex].any { it == 5 } ||
                        matchedColumns[boardIndex].any { it == 5 }) {
                        winningBoardIndexes.add(boardIndex)
                        if (winFirst) return@game
                    }
                }
            }
        }

        return score(boards[winningBoardIndexes.last()], matchedNumbers[winningBoardIndexes.last()])
    }

    fun part1(input: List<String>): Int = play(input)

    fun part2(input: List<String>): Int  = play(input, false)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
