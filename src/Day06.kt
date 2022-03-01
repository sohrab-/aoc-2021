fun main() {
    // I _could_ refactor all those magic numbers but nah

    // dumb implementation
    fun part1(input: List<String>): Int {
        val fishes = input[0].split(",").map(String::toInt).toMutableList()
        repeat(80) {
            repeat(fishes.size) { i -> // don't go beyond the fishes available this round
                if (fishes[i] == 0) {
                    fishes.add(8)
                    fishes[i] = 6
                } else {
                    fishes[i] -= 1
                }
            }
        }

        return fishes.size
    }

    // less dumb implementation
    fun part2(input: List<String>): Long {
        var counters = LongArray(9) { 0 }
        input[0].split(",").map(String::toInt)
            .forEach { counters[it] = counters[it] + 1 }

        repeat(256) {
            val snapshot = counters.copyOf()

            (8 downTo 1).forEach {
                counters[it - 1] = snapshot[it]
            }
            counters[6] += snapshot[0]
            counters[8] = snapshot[0]
        }
        return counters.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 5934)
    check(part2(testInput) == 26984457539)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
