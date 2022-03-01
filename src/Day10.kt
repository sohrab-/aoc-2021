import java.lang.RuntimeException
import java.util.ArrayDeque

fun main() {

    val lookup = mapOf(
        ')' to '(',
        ']' to '[',
        '}' to '{',
        '>' to '<',
    )

    val pointLookup1 = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137
    )

    fun match(line: String): Char {
        val stack = ArrayDeque<Char>()

        line.toCharArray().forEach { x ->
            if (lookup.values.contains(x)) {
                stack.push(x)
            } else if (lookup.keys.contains(x)) {
                if (stack.peek() != lookup[x]) {
                    return x
                }
                stack.pop()
            }
        }

        if (stack.isEmpty()) return '*'
        return '~'
    }

    fun part1(input: List<String>): Int {
        return input.map(::match)
            .filter { it != '~' && it != '*' }
            .map { pointLookup1[it] ?: throw RuntimeException("Something's wrong in part1: $it") }
            .sumOf { it }
    }

    /////////////////////

    val reverseLookup = lookup.entries.associate { (k, v) -> v to k }

    val pointLookup2 = mapOf(
        ')' to 1,
        ']' to 2,
        '}' to 3,
        '>' to 4
    )

    fun complete(line: String): String {
        val stack = ArrayDeque<Char>()

        line.toCharArray().forEach { x ->
            if (lookup.values.contains(x)) {
                stack.push(x)
            } else if (lookup.keys.contains(x)) {
                if (stack.peek() != lookup[x]) {
                    return ""
                }
                stack.pop()
            }
        }

        if (stack.isEmpty()) return ""

        return stack
            .map { reverseLookup[it] ?: throw RuntimeException("Something's wrong in complete: $it") }
            .joinToString("")
    }

    fun part2(input: List<String>): Long {
        val scores = input.map(::complete)
            .filter { it.isNotEmpty() }
            .map {
                it.fold(0L) { acc, c ->
                    acc * 5 + (pointLookup2[c] ?: throw RuntimeException("Something's wrong part2: $it"))
                }
            }

        return scores.sorted()[scores.size / 2]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957L)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
