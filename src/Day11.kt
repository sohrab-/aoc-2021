fun main() {

    fun getAdjacentCoord(coordinate: Coordinate, maxX: Int, maxY: Int): List<Coordinate> {
        val neighbours = mutableListOf<Coordinate>()
        if (coordinate.x > 0) {
            neighbours.add(Coordinate(coordinate.x - 1, coordinate.y))
        }
        if (coordinate.y > 0) {
            neighbours.add(Coordinate(coordinate.x, coordinate.y - 1))
        }
        if (coordinate.x < maxX) {
            neighbours.add(Coordinate(coordinate.x + 1, coordinate.y))
        }
        if (coordinate.y < maxY) {
            neighbours.add(Coordinate(coordinate.x, coordinate.y + 1))
        }

        if (coordinate.x > 0 && coordinate.y > 0) {
            neighbours.add(Coordinate(coordinate.x - 1, coordinate.y - 1))
        }
        if (coordinate.x > 0 && coordinate.y < maxY) {
            neighbours.add(Coordinate(coordinate.x - 1, coordinate.y + 1))
        }
        if (coordinate.x < maxY && coordinate.y > 0) {
            neighbours.add(Coordinate(coordinate.x + 1, coordinate.y - 1))
        }
        if (coordinate.x < maxY && coordinate.y < maxY) {
            neighbours.add(Coordinate(coordinate.x + 1, coordinate.y + 1))
        }

        return neighbours
    }

    fun part1(input: List<String>): Int {
        val board = input.map { line ->
            line.toCharArray().map { it.toString().toInt() }.toMutableList()
        }.toMutableList()

        var flashes = 0

        (0 until 100).forEach { step ->
            board.indices.forEach { row ->
                board[row].indices.forEach { col ->
                    board[row][col]++
                }
            }

            val flashed = mutableMapOf<Coordinate, Boolean>()

            fun maybeFlash(coordinate: Coordinate): Boolean {
                if (board[coordinate.y][coordinate.x] <= 9 ||
                    flashed.getOrDefault(coordinate, false)) return false

                println("Flashing $coordinate")
                flashed[coordinate] = true
                flashes++

                getAdjacentCoord(coordinate, board[0].size - 1, board.size - 1).forEach { neighbour ->
                    board[neighbour.y][neighbour.x]++
                }

                return true
            }

            var changes: Boolean
            do {
                changes = false
                board.indices.forEach { y ->
                    board[y].indices.forEach { x ->
                        changes = changes || maybeFlash(Coordinate(x, y))
                    }
                }
            } while (changes)

            flashed.keys.forEach { board[it.y][it.x] = 0 }

            println("After Step ${step + 1}:")
            board.forEach { println(it.joinToString("")) }
            println()
        }

        return flashes
    }

    fun part2(input: List<String>): Int {
        val board = input.map { line ->
            line.toCharArray().map { it.toString().toInt() }.toMutableList()
        }.toMutableList()

        var step = 1
        while (true) {

            board.indices.forEach { row ->
                board[row].indices.forEach { col ->
                    board[row][col]++
                }
            }

            val flashed = mutableMapOf<Coordinate, Boolean>()

            fun maybeFlash(coordinate: Coordinate): Boolean {
                if (board[coordinate.y][coordinate.x] <= 9 ||
                    flashed.getOrDefault(coordinate, false)) return false

                println("Flashing $coordinate")
                flashed[coordinate] = true

                getAdjacentCoord(coordinate, board[0].size - 1, board.size - 1).forEach { neighbour ->
                    board[neighbour.y][neighbour.x]++
                }

                return true
            }

            var changes: Boolean
            do {
                changes = false
                board.indices.forEach { y ->
                    board[y].indices.forEach { x ->
                        changes = changes || maybeFlash(Coordinate(x, y))
                    }
                }
            } while (changes)

            flashed.keys.forEach { board[it.y][it.x] = 0 }

            if (flashed.size == board.size * board[0].size) return step

            println("After Step ${step + 1}:")
            board.forEach { println(it.joinToString("")) }
            println()

            step++
        }
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
//    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)

    val input = readInput("Day11")
//    println(part1(input))
    println(part2(input))
}
