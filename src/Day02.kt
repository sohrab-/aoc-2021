fun main() {
    fun part1(input: List<String>): Int {
        var horizontal = 0
        var depth = 0
        for (command in input) {
            val (direction, amount) = command.split(" ")
            when(direction) {
                "forward" -> { horizontal += amount.toInt() }
                "up" -> { depth -= amount.toInt() }
                "down" -> { depth += amount.toInt() }
            }
        }

        return horizontal * depth
    }

    fun part2(input: List<String>): Int {
        var horizontal = 0
        var depth = 0
        var aim = 0

        for (command in input) {
            val (direction, amount) = command.split(" ")
            when(direction) {
                "forward" -> {
                    horizontal += amount.toInt()
                    depth += aim * amount.toInt()
                }
                "up" -> { aim -= amount.toInt() }
                "down" -> { aim += amount.toInt() }
            }
        }

        return horizontal * depth
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
