import java.lang.RuntimeException

private data class Entry(val digits: List<String>, val outputs: List<String>)

fun main() {

    fun part1(input: List<String>): Int {
        val entries = input.map { line ->
            val (digits, output) = line.split(" | ").map { it.split(" ") }
            Entry(digits, output)
        }

        return entries.sumOf { entry ->
            entry.outputs.filter { it.length in arrayOf(2, 3, 4, 7) }.size
        }
    }

    // 0: abcefg  (6)
    // 1: cf      (2)
    // 2: acdeg   (5)
    // 3: acdfg   (5)
    // 4: bcdf    (4)
    // 5: abdfg   (5)
    // 6: abdefg  (6)
    // 7: acf     (3)
    // 8: abcdefg (7)
    // 9: abcdfg  (6)

    // a: 8
    // b: 6 <-
    // c: 8
    // d: 7
    // e: 4 <-
    // f: 9 <-
    // g: 7


    fun part2(input: List<String>): Int {
        val entries = input.map { line ->
            val (digits, output) = line.split(" | ").map { it.split(" ") }
            Entry(digits, output)
        }

        return entries.sumOf { entry ->
            val occurrences = entry.digits.joinToString("").groupingBy { it }.eachCount()
            val eChar = occurrences.filter { it.value == 4 }.keys.first()
            val bChar = occurrences.filter { it.value == 6 }.keys.first()
            val fChar = occurrences.filter { it.value == 9 }.keys.first()
            val cChar = entry.digits.first { it.length == 2 }.replace(fChar.toString(), "")
            entry.outputs.joinToString("") { output ->
                when (output.length) {
                    2 -> "1"
                    3 -> "7"
                    4 -> "4"
                    5 ->  // 2, 3, 5
                        if (output.contains(eChar)) "2"
                        else if (output.contains(bChar)) "5"
                        else "3"
                    6 ->  // 0, 6, 9
                        if (!output.contains(eChar)) "9"
                        else if (output.contains(cChar)) "0"
                        else "6"
                    7 -> "8"
                    else -> throw RuntimeException("Didn't match")
                }
            }.toInt()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
