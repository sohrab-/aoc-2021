import java.util.*

fun main() {

    fun part1(input: List<String>): Int {
        val board = input.map { line ->
            line.toCharArray().map { it.toString().toInt() }
        }

        fun getNeighbours(coordinate: Coordinate): List<Coordinate> {
            val neighbours = mutableListOf<Coordinate>()
            if (coordinate.x > 0) {
                neighbours.add(Coordinate(coordinate.x - 1, coordinate.y))
            }
            if (coordinate.y > 0) {
                neighbours.add(Coordinate(coordinate.x, coordinate.y - 1))
            }
            if (coordinate.x < board.first().size - 1) {
                neighbours.add(Coordinate(coordinate.x + 1, coordinate.y))
            }
            if (coordinate.y < board.size - 1) {
                neighbours.add(Coordinate(coordinate.x, coordinate.y + 1))
            }
            return neighbours
        }

        val queue = PriorityQueue<Pair<Coordinate, Int>>(compareBy { it.second })
        queue.add(Pair(Coordinate(0, 0), 0))

        val risks = MutableList(board.size) {
            MutableList(board.first().size) { Int.MAX_VALUE }
        }

        do {
            val (current, risk) = queue.remove()
            getNeighbours(current).filter { coordinate ->
                risks[coordinate.y][coordinate.x] > risk + board[coordinate.y][coordinate.x]
            }.forEach { coordinate ->
                risks[coordinate.y][coordinate.x] = risk + board[coordinate.y][coordinate.x]
                queue.add(Pair(coordinate, risks[coordinate.y][coordinate.x]))
            }
        } while (queue.isNotEmpty())

        return risks.last().last()
    }

    fun part2(input: List<String>): Int {
        val board = input.map { line ->
            line.toCharArray().map { it.toString().toInt() }
        }

        val realBoardXSize = board.first().size * 5
        val realBoardYSize = board.size * 5

        fun realRisk(coordinate: Coordinate): Int {
            val og = board[coordinate.y % board.size][coordinate.x % board.first().size]
            val risk = og + coordinate.y / board.size + coordinate.x / board.first().size
            return if (risk < 10) risk else risk % 9
        }

        fun getNeighbours(coordinate: Coordinate): List<Coordinate> {
            val neighbours = mutableListOf<Coordinate>()
            if (coordinate.x > 0) {
                neighbours.add(Coordinate(coordinate.x - 1, coordinate.y))
            }
            if (coordinate.y > 0) {
                neighbours.add(Coordinate(coordinate.x, coordinate.y - 1))
            }
            if (coordinate.x < realBoardXSize - 1) {
                neighbours.add(Coordinate(coordinate.x + 1, coordinate.y))
            }
            if (coordinate.y < realBoardYSize - 1) {
                neighbours.add(Coordinate(coordinate.x, coordinate.y + 1))
            }
            return neighbours
        }

        val queue = PriorityQueue<Pair<Coordinate, Int>>(compareBy { it.second })
        queue.add(Pair(Coordinate(0, 0), 0))

        val risks = MutableList(realBoardYSize) {
            MutableList(realBoardXSize) { Int.MAX_VALUE }
        }

        do {
            val (current, risk) = queue.remove()
            getNeighbours(current).filter { coordinate ->
                risks[coordinate.y][coordinate.x] > risk + realRisk(coordinate)
            }.forEach { coordinate ->
                risks[coordinate.y][coordinate.x] = risk + realRisk(coordinate)
                queue.add(Pair(coordinate, risks[coordinate.y][coordinate.x]))
            }
        } while (queue.isNotEmpty())

        return risks.last().last()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
//    check(part1(testInput) == 40)
    check(part2(testInput) == 315)

    val input = readInput("Day15")
//    println(part1(input))
    println(part2(input))
}
