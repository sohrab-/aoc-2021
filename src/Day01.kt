fun main() {
    fun part1(input: List<String>): Int {
        var previous = -1
        var increased = 0
        for (x in input) {
            if (previous != -1 && x.toInt() > previous) {
                increased++
            }
            previous = x.toInt()
        }
        return increased
    }

    fun part2(input: List<String>): Int {
        var previous = -1
        var increased = 0
        for (i in input.indices) {
            if (i + 2 >= input.size) break

            // using windowed() would have been cheating, right?
            val sum = input[i].toInt() + input[i+1].toInt() + input[i+2].toInt()
            if (previous != -1 && sum > previous) {
                increased++
            }
            previous = sum
        }
        return increased
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
