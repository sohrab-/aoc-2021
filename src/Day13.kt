import java.lang.RuntimeException

fun main() {

    fun part1(input: List<String>): Int {
        val blocks = mutableListOf<Coordinate>()
        val instructions = mutableListOf<Pair<String, Int>>()

        // parse
        val inputIterator = input.listIterator()
        while (inputIterator.hasNext()) {
            val line = inputIterator.next()
            if (line.isEmpty()) break
            val (x, y) = line.split(",").map(String::toInt)
            blocks.add(Coordinate(x, y))
        }
        while(inputIterator.hasNext()) {
            val line = inputIterator.next()
            val match = Regex("fold along ([^=]+)=(\\d+)").find(line)
                ?: throw RuntimeException("Cannot match $line")
            val (axis, value) = match.destructured
            instructions.add(Pair(axis, value.toInt()))
        }

       return instructions.take(1).sumOf { (axis, value) ->
           val newBlocks = blocks.map {
               if (axis == "x" && it.x <= value ||
                       axis == "y" && it.y <= value) {
                   it
               } else if (axis == "x")
                   Coordinate(value * 2 - it.x, it.y)
               else
                   Coordinate(it.x, value * 2 - it.y)
           }.toSet()
           newBlocks.size
       }
    }

    fun printBoard(blocks: Set<Coordinate>) {
        (0..10).forEach { y ->
            (0..80).forEach { x ->
                if (blocks.contains(Coordinate(x, y))) {
                    print("#")
                } else {
                    print(".")
                }
            }
            println()
        }
    }

    fun part2(input: List<String>) {
        var blocks = mutableSetOf<Coordinate>()
        val instructions = mutableListOf<Pair<String, Int>>()

        // parse
        val inputIterator = input.listIterator()
        while (inputIterator.hasNext()) {
            val line = inputIterator.next()
            if (line.isEmpty()) break
            val (x, y) = line.split(",").map(String::toInt)
            blocks.add(Coordinate(x, y))
        }
        while(inputIterator.hasNext()) {
            val line = inputIterator.next()
            val match = Regex("fold along ([^=]+)=(\\d+)").find(line)
                ?: throw RuntimeException("Cannot match $line")
            val (axis, value) = match.destructured
            instructions.add(Pair(axis, value.toInt()))
        }

        var foldedBlock = blocks.toSet()
        instructions.forEach { (axis, value) ->
            println("fold along $axis=$value")
            foldedBlock = foldedBlock.map {
                if (axis == "x") {
                    if (it.x < value) it
                    else Coordinate(value * 2 - it.x, it.y)
                } else {
                    if (it.y < value) it
                    else Coordinate(it.x, value * 2 - it.y)
                }
            }.toSet()

        }

        // 6 -fold 5-> 4
        printBoard(foldedBlock)


    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
//    check(part1(testInput) == 17)
//    part2(testInput)
//    check(part2(testInput) == 36)

    val input = readInput("Day13")
//    println(part1(input))
    part2(input)
//    println(part2(input))
}
