fun main() {

    fun part1(input: List<String>): Int {
        val algorithm = input.first()

        var lightPixels = input.drop(2)
            .flatMapIndexed { y, row ->
                row.mapIndexed { x, pixel ->
                    if (pixel == '#') Coordinate(x, y) else null
                }.filterNotNull()
            }.toSet()

        fun printImage() {
            println()
            val minCoordinate = Coordinate(lightPixels.minOf { it.x }, lightPixels.minOf { it.y })
            val maxCoordinate = Coordinate(lightPixels.maxOf { it.x }, lightPixels.maxOf { it.y })

            (minCoordinate.y - 2 .. maxCoordinate.y + 2).forEach { y ->
                (minCoordinate.x - 2..maxCoordinate.x + 2).forEach { x ->
                    print(if (Coordinate(x, y) in lightPixels) '#' else '.')
                }
                println()
            }
        }

        printImage()

        (0 .. 1).forEach { i ->
            val minCoordinate = Coordinate(lightPixels.minOf { it.x }, lightPixels.minOf { it.y })
            val maxCoordinate = Coordinate(lightPixels.maxOf { it.x }, lightPixels.maxOf { it.y })

            val newLightPixels = mutableSetOf<Coordinate>()

            (minCoordinate.x - 2 .. maxCoordinate.x + 2).forEach { x ->
                (minCoordinate.y - 2 .. maxCoordinate.y + 2).forEach { y ->
                    val coordinate = Coordinate(x, y)
                    val default = if (algorithm.first() == '.') '.'
                        else if (i % 2 == 0) '.' else '#'

                    val binary = listOf(
                        Coordinate(-1, -1), Coordinate(0, -1), Coordinate(1, -1),
                        Coordinate(-1, 0), Coordinate(0, 0), Coordinate(1, 0),
                        Coordinate(-1, 1), Coordinate(0, 1), Coordinate(1, 1),
                    ).map { offset ->
                        if (coordinate + offset in lightPixels) '1'
                        else if (
                            (coordinate.x !in minCoordinate.x..maxCoordinate.x ||
                            coordinate.y !in minCoordinate.y..maxCoordinate.y) &&
                            default == '#'
                        ) '1'
                        else '0'
                    }.joinToString("")

                    if (algorithm[Integer.parseInt(binary, 2)] == '#') {
                        newLightPixels.add(Coordinate(x, y))
                    }
                }
            }

            lightPixels = newLightPixels

            printImage()
        }

        return lightPixels.size
    }


    fun part2(input: List<String>): Int {
        TODO()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20_test")
//    check(part1(testInput) == 35)
//    check(part2(testInput) == 3993)

    val input = readInput("Day20")
    println(part1(input))
//    println(part2(input))
}
