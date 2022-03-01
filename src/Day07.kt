import kotlin.math.absoluteValue

fun main() {

    fun part1(input: List<String>): Int {
        val positions = input[0].split(",").map(String::toInt)
        val fuels = (positions.minOf { it }..positions.maxOf { it }).map { position ->
            positions.sumOf { (it - position).absoluteValue }
        }
        return fuels.minOf { it }
    }

    fun part2(input: List<String>): Int {
        val positions = input[0].split(",").map(String::toInt)
        val fuels = (positions.minOf { it }..positions.maxOf { it }).map { position ->
            positions.sumOf {
                val n = (it - position).absoluteValue
                n * (n + 1) / 2
            }
        }
        return fuels.minOf { it }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
