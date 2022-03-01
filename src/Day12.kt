fun main() {

    fun part1(input: List<String>): Int {
        val edges = mutableMapOf<String, List<String>>()
        input.forEach { line ->
            val (start, end) = line.split("-")
            edges[start] = edges.getOrDefault(start, emptyList()) + end
            edges[end] = edges.getOrDefault(end, emptyList()) + start
        }

        val paths = mutableListOf<Array<String>>()

        fun dfs(start: String, end: String, path: Array<String>) {
            if (start == end) {
                paths.add(path + end)
                return
            }
            edges[start]
                ?.filter { it[0].isUpperCase() || !path.contains(it) }
                ?.forEach {
                dfs(it, end, path + start)
            }
        }

        dfs("start", "end", emptyArray())

        return paths.size
    }

    fun part2(input: List<String>): Int {
        val edges = mutableMapOf<String, List<String>>()
        input.forEach { line ->
            val (start, end) = line.split("-")
            edges[start] = edges.getOrDefault(start, emptyList()) + end
            edges[end] = edges.getOrDefault(end, emptyList()) + start
        }

        val paths = mutableListOf<Array<String>>()

        fun dfs(start: String, end: String, path: Array<String>) {
//            println("$start -> $end | ${path.toList()}")
            if (start == end) {
                paths.add(path + end)
                return
            }

            val canVisitTwice = path
                .filter { it[0].isLowerCase() && it != "start" && it != "end" }
                .groupingBy { it }
                .eachCount()
                .values
                .none { it > 1 }

            edges[start]
                ?.filter {
                    it[0].isUpperCase() ||
                    !path.contains(it) || (
                        canVisitTwice &&
                        it != "start"
                    )
                }
                ?.forEach { next ->
                    if ((path + next).filter { it[0].isLowerCase() }
                        .groupingBy { it }.eachCount().values.filter { it > 1 }.size > 1) {
                        println("wtf")
                    }
                    dfs(next, end, path + start)
                }
        }

        dfs("start", "end", emptyArray())

        // TODO why?!
        val x = paths.filter {
            it.filter { it[0].isLowerCase() }
                .groupingBy { it }.eachCount().values.filter { it > 1 }.size > 1
        }.size

        return paths.size - x
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 10)
//    check(part2(testInput) == 36)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
