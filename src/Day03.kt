fun main() {
    fun part1(input: List<String>): Int {
        val bitSummary = Array(input[0].length) { IntArray(2) { 0 } }

        input.forEach { reading ->
            reading.forEachIndexed { j, bit ->
                bitSummary[j][bit.toString().toInt()] += 1
            }
        }

        val gamma = bitSummary.joinToString("") { if (it[0] > it[1]) "0" else "1" }
        // TODO just flip the bit instead of this rubbish
        val epsilon = bitSummary.joinToString("") { if (it[0] < it[1]) "0" else "1" }

        return Integer.parseInt(gamma, 2) * Integer.parseInt(epsilon, 2)
    }

    fun reduce(input: List<String>, index: Int, comp: (Int, Int) -> Boolean): List<String> {
        if (input.size == 1) return input
        val(zeroes, ones) = input.partition { it[index] == '0' }
        return reduce(if (comp(zeroes.size, ones.size)) zeroes else ones, index + 1, comp)
    }

    // TODO do this with a binary tree - you animal!

    fun part2(input: List<String>): Int {
        // TODO I hate myself
        val oxygen = reduce(input, 0) { a, b -> a > b }[0]
        val co2 = reduce(input, 0) { a, b -> a <= b }[0]
        return Integer.parseInt(oxygen, 2) * Integer.parseInt(co2, 2)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
