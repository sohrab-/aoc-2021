fun main() {

    fun part1(input: List<String>): Int {
        val template = input.first().toList()
        val rules = input.drop(2).map { line ->
            val (left, right) = line.split(" -> ")
            left to right.toCharArray().first()
        }.toMap()

        println(rules)

        var polymer = template
        (1..10).forEach { step ->
            val newPolymer = mutableListOf<Char>()
//            println(polymer.windowed(2, 1))
            newPolymer.add(polymer.first())
            polymer.windowed(2, 1).forEach { (left, right) ->
                newPolymer.add(rules.getOrDefault("$left$right", '?'))
                newPolymer.add(right)
            }
            polymer = newPolymer
//            println("Step $step")
//            println(polymer.joinToString(""))
        }

        val counts = polymer.groupingBy { it }.eachCount().toList().sortedBy { it.second }

        println(counts)

        return counts.last().second - counts.first().second
    }

    fun part2(input: List<String>): Long {
        val rules = input.drop(2).map { line ->
            val (left, right) = line.split(" -> ")
            left to right.toCharArray().first()
        }.toMap()

        println(rules)

        var pairMap = input.first().windowed(2, 1).associateWith { 1L }
        val singleMap = input.first().groupingBy { it }.eachCount()
            .map { it.key to it.value.toLong() }.toMap().toMutableMap()
        (1..40).forEach { step ->
            val newPairMap = pairMap.toMutableMap()
            pairMap.forEach { (pair, count) ->
                val middle = rules.getOrDefault(pair, '?')

                singleMap[middle] = singleMap.getOrDefault(middle, 0) + count

                val newPair1 = "${pair[0]}$middle"
                val newPair2 = "$middle${pair[1]}"

                newPairMap[pair] = newPairMap.getOrDefault(pair, 0) - count
                newPairMap[newPair1] = newPairMap.getOrDefault(newPair1, 0) + count
                newPairMap[newPair2] = newPairMap.getOrDefault(newPair2, 0) + count
            }
            pairMap = newPairMap

            println("Step $step")
            println(pairMap)
            println(singleMap)
        }

        println(singleMap.maxOf { it.value })
        println(singleMap.minOf { it.value })

        return singleMap.maxOf { it.value } - singleMap.minOf { it.value }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
//    check(part1(testInput) == 1588)
//    check(part2(testInput) == 2188189693529)

    val input = readInput("Day14")
//    println(part1(input))
    println(part2(input))
}
