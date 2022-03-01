
fun main() {

    fun parse(input: List<String>):  List<Pair<Coordinate, Coordinate>> =
        input.map { line ->
            val (start, end) = line.split(" -> ").map {
                val (x, y) = it.split(",").map(String::toInt)
                Coordinate(x, y)
            }
            Pair(start, end)
        }

    fun range(a: Int, b: Int) = if (a <= b) a..b else a downTo b

    fun traverseSimple(start: Coordinate, end: Coordinate): List<Coordinate> {
        return if (start.x == end.x) { // horizontal
            range(start.y, end.y).map { Coordinate(start.x, it) }
        } else if (start.y == end.y) { // vertical
            range(start.x, end.x).map { Coordinate(it, start.y) }
        } else {
            emptyList() // ignore the rest for now
        }
    }

    fun traverseComplex(start: Coordinate, end: Coordinate): List<Coordinate> {
        return if (start.x == end.x) { // horizontal
            range(start.y, end.y).map { Coordinate(start.x, it) }
        } else if (start.y == end.y) { // vertical
            range(start.x, end.x).map { Coordinate(it, start.y) }
        } else {
            range(start.x, end.x).zip(range(start.y, end.y))
                .map { (x,y) -> Coordinate(x, y) }
        }
    }

    fun solve(input: List<String>, traverser: (Coordinate, Coordinate) -> List<Coordinate>): Int {
        val plane = mutableMapOf<Coordinate, Int>()

        parse(input).forEach { (start, end) ->
            traverser(start, end).forEach {
                plane[it] = plane.getOrDefault(it, 0) + 1
            }
        }

        return plane.count { (_, count) -> count > 1 }
    }

    fun part1(input: List<String>): Int = solve(input, ::traverseSimple)

    fun part2(input: List<String>): Int = solve(input, ::traverseComplex)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
