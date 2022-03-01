private data class Player(val position: Int, val score: Int)
private data class GameSnapshot(
    val p1Turn: Boolean,
    val p1: Player,
    val p2: Player,
    val multiplier: Int
) {
    override infix operator fun equals(other: Any?): Boolean {
        if (other == null || other !is GameSnapshot) return false
        return this.p1Turn == other.p1Turn &&
                this.p1 == other.p1 &&
                this.p2 == other.p2
    }
}

fun main() {

    fun part1(p1Start: Int, p2Start: Int): Int {
        var p1Position = p1Start
        var p2Position = p2Start
        var p1Score = 0
        var p2Score = 0
        var lastDieRoll = 100
        var dieRolls = 0

        fun nextRoll(): Int {
            dieRolls++
            lastDieRoll = lastDieRoll % 100 + 1
            print("$lastDieRoll,")
            return lastDieRoll
        }

        while (true) {
            p1Position = (p1Position + nextRoll() + nextRoll() + nextRoll() - 1) % 10 + 1
            p1Score += p1Position
            println("  P1: $p1Position - $p1Score")
            if (p1Score >= 1000) break

            p2Position = (p2Position + nextRoll() + nextRoll() + nextRoll() - 1) % 10 + 1
            p2Score += p2Position
            println("  P2: $p2Position - $p2Score")
            if (p2Score >= 1000) break
        }

        println(dieRolls)
        return p1Score.coerceAtMost(p2Score) * dieRolls
    }


    fun part2(p1Start: Int, p2Start: Int): Long {
        val games = mutableListOf(GameSnapshot(
            true,
            Player(p1Start, 0),
            Player(p2Start, 0),
            1
        ))

        val combinations = listOf(
            listOf(1, 1, 1), listOf(1, 1, 2), listOf(1, 1, 3),
            listOf(1, 2, 1), listOf(1, 2, 2), listOf(1, 2, 3),
            listOf(1, 3, 1), listOf(1, 3, 2), listOf(1, 3, 3),
            listOf(2, 1, 1), listOf(2, 1, 2), listOf(2, 1, 3),
            listOf(2, 2, 1), listOf(2, 2, 2), listOf(2, 2, 3),
            listOf(2, 3, 1), listOf(2, 3, 2), listOf(2, 3, 3),
            listOf(3, 1, 1), listOf(3, 1, 2), listOf(3, 1, 3),
            listOf(3, 2, 1), listOf(3, 2, 2), listOf(3, 2, 3),
            listOf(3, 3, 1), listOf(3, 3, 2), listOf(3, 3, 3),
        ).groupingBy { it.sum() }.eachCount()

        var p1Wins = 0L
        var p2Wins = 0L

        while(games.isNotEmpty()) {
            println(games.size)
            val game = games.removeFirst()

            // generate next frontier for the game
            val (wonGames, continuingGames)  = combinations.map { (rollSum, multiplier) ->
                if (game.p1Turn) {
                    val position = (game.p1.position + rollSum - 1) % 10 + 1
                    GameSnapshot(
                        false,
                        Player(position, game.p1.score + position),
                        game.p2,
                        game.multiplier * multiplier
                    )
                } else {
                    val position = (game.p2.position + rollSum - 1) % 10 + 1
                    GameSnapshot(
                        true,
                        game.p1,
                        Player(position, game.p2.score + position),
                        game.multiplier * multiplier
                    )
                }
            }.partition { if (game.p1Turn) it.p1.score >= 21 else it.p2.score >= 21 }

            // add wins
            val wins = wonGames.sumOf { it.multiplier }
            if (game.p1Turn) p1Wins += wins else p2Wins += wins

            if (games.size == 5) {
                println("???")
            }

            // dedup what's already in the list
            games.addAll(continuingGames.filter { x ->
                val i = games.indexOfFirst { it == x }
                if (i == -1) return@filter true

                val previous = games.removeAt(i)
                games.add(i, previous.copy(multiplier = previous.multiplier * x.multiplier))
                false
            })
        }

        println("P1: $p1Wins, P2: $p2Wins")

        return p1Wins.coerceAtLeast(p2Wins)
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day21_test")
//    check(part1(4, 8) == 739785)
    check(part2(4, 8) == 444356092776315)

//    val input = readInput("Day21")
//    println(part1(5, 9))
//    println(part2(5, 9))
}

// 231956413737
// 444356092776315