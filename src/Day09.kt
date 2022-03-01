fun main() {

    fun getAdjacentCoord(x: Int, y: Int, maxX: Int, maxY: Int): List<Coordinate> {
        val neighbours = mutableListOf<Coordinate>()
        if (x > 0) {
            neighbours.add(Coordinate(x - 1, y))
        }
        if (y > 0) {
            neighbours.add(Coordinate(x, y - 1))
        }
        if (x < maxX) {
            neighbours.add(Coordinate(x + 1, y))
        }
        if (y < maxY) {
            neighbours.add(Coordinate(x, y + 1))
        }
        return neighbours
    }

    fun part1(input: List<String>): Int {
        val board = input.map { row ->
            row.toCharArray().map { it.toString().toInt() }
        }

        val mins = mutableListOf<Int>()
        board.forEachIndexed { y, row ->
            row.forEachIndexed { x, height ->
                val neighbours = getAdjacentCoord(x, y, row.size - 1, input.size - 1)
                if (neighbours.all { board[it.y][it.x] > height }) {
                    mins.add(height)
                }
            }
        }
        return mins.sumOf { it + 1 }
    }

    fun getBasin(board: List<List<Int>>, coordinate: Coordinate, basin: MutableList<Coordinate>) {
        val neighbours = getAdjacentCoord(coordinate.x, coordinate.y, board[0].size - 1, board.size - 1)
        if (basin.contains(coordinate) || board[coordinate.y][coordinate.x] == 9) return
        basin.add(coordinate)
        neighbours.forEach { getBasin(board, it, basin) }
    }

    fun part2(input: List<String>): Int {
        val board = input.map { row ->
            row.toCharArray().map { it.toString().toInt() }
        }

        val basins = mutableListOf<List<Coordinate>>()

        board.forEachIndexed { y, row ->
            row.forEachIndexed { x, height ->
                val coordinate = Coordinate(x, y)
                if (basins.none { it.contains(coordinate) }) {
                    val basin = mutableListOf<Coordinate>()
                    getBasin(board, coordinate, basin)
                    basins.add(basin)
                }
            }
        }

        basins.sortBy { it.size }
        val (a, b, c) = basins.takeLast(3).map { it.size }
        return a * b * c
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
