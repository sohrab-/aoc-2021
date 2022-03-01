import java.lang.RuntimeException
import kotlin.math.ceil
import kotlin.math.truncate

private abstract class Node {
    lateinit var parent: Branch
}
private class Leaf(var value: Int): Node() {
    override fun toString() = value.toString()
}

private class Branch: Node {
    var left: Node
        set(node) {
            node.parent = this
            field = node
        }
    var right: Node
        set(node) {
            node.parent = this
            field = node
        }

    constructor(left: Node, right: Node) {
        this.left = left
        this.right = right
    }

    override fun toString() = "[$left,$right]"
}

private val rootBranch = Branch(Leaf(-1), Leaf(-1))

fun main() {

    fun parse(iterator: Iterator<Char>): Node {
        val char = iterator.next()
        return if (char == '[') {
            val left = parse(iterator)
            iterator.next() // skip comma
            val right = parse(iterator)
            iterator.next() // skip ]
            Branch(left, right)
        } else if (char.isDigit()) {
            Leaf(char.toString().toInt())
        } else {
            throw RuntimeException("wtf")
        }
    }

//    val x = parse("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]".iterator())

    fun findLeftDeepest(node: Node, depth: Int): Branch? {
        when(node) {
            is Leaf -> return if (depth > 4) {
                node.parent
            } else {
                null
            }
            is Branch -> {
                val found = findLeftDeepest(node.left, depth + 1)
                if (found != null) return found
                return findLeftDeepest(node.right, depth + 1)
            }
            else -> throw RuntimeException("Another child class?? $node")
        }
    }

    // TODO need to go left then right

    fun findLeft(node: Branch): Leaf? {
        if (node.parent == rootBranch) return null

        var current: Node = node
        while (current.parent.left == current) {
            current = current.parent
//            println("Going up to $current")
        }
        current = (current as Branch).parent.left
        while (current is Branch) {
            current = current.right
//            println("Going down to $current")
        }
        return (current as Leaf)

//        return when (node.parent.left) {
//            node -> findLeft(node.parent) // go up
//            is Branch -> findLeft(node.parent.left as Branch)
//            is Leaf -> node.parent.left as Leaf
//            else -> throw RuntimeException("Another child class?? $node")
//        }
    }

    // TODO need to go right then left

    fun findRight(node: Branch): Leaf? {
        if (node.parent == rootBranch) return null

        var current: Node = node
        while (current.parent.right == current) {
            current = current.parent
//            println("Going up to $current")
        }
        current = (current as Branch).parent.right
        while (current is Branch) {
            current = (current as Branch).left
//            println("Going down to $current")
        }
        return (current as Leaf)

//        if (node.parent == rootBranch) return null
//
//        return when (node.parent.right) {
//            node -> findRight(node.parent) // go up
//            is Branch -> findRight(node.parent.right as Branch)
//            is Leaf -> node.parent.right as Leaf
//            else -> throw RuntimeException("Another child class?? $node")
//        }
    }

    fun explode(node: Branch) {
        val leftNeighbour = findLeft(node)
        if (leftNeighbour != null) {
            leftNeighbour.value += (node.left as Leaf).value
        }

        val rightNeighbour = findRight(node)
        if (rightNeighbour != null) {
            rightNeighbour.value += (node.right as Leaf).value
        }

        if (node.parent.left == node) {
            node.parent.left = Leaf(0)
        } else {
            node.parent.right = Leaf(0)
        }
    }

//    val tree = parse("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]".iterator())
//    val node = findLeftDeepest(tree, 0)
//    explode(node as Branch)
//    println(tree)

    fun findLeftLargest(node: Node): Leaf? {
        when(node) {
            is Leaf -> return if (node.value >= 10) {
                node
            } else {
                null
            }
            is Branch -> {
                val found = findLeftLargest(node.left)
                if (found != null) return found
                return findLeftLargest(node.right)
            }
            else -> throw RuntimeException("Another child class?? $node")
        }
    }

    fun split(leaf: Leaf) {
        val branch = Branch(
            Leaf(truncate(leaf.value / 2.0).toInt()),
            Leaf(ceil(leaf.value / 2.0).toInt())
        )

        if (leaf.parent.left == leaf) {
            leaf.parent.left = branch
        } else {
            leaf.parent.right = branch
        }
    }

//    val tree = parse("[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]".iterator())
//    tree.parent = rootBranch
//    val node1 = findLeftDeepest(tree, 0)
//    explode(node1 as Branch)
//    println(tree)
//    val node2 = findLeftDeepest(tree, 0)
//    explode(node2 as Branch)
//    println(tree)
//    val node3 = findLeftLargest(tree)
//    split(node3 as Leaf)
//    println(tree)
//    val node4 = findLeftLargest(tree)
//    split(node4 as Leaf)
//    println(tree)
//    val node5 = findLeftDeepest(tree, 0)
//    explode(node5 as Branch)
//    println(tree)

    fun getMagnitude(node: Node): Int {
        return when (node) {
            is Leaf -> node.value
            is Branch -> getMagnitude(node.left) * 3 + 2 * getMagnitude(node.right)
            else -> throw RuntimeException("Another child class?? $node")
        }
    }

    fun add(a: Node, b: Node): Branch {
        val sum = Branch(a, b)
        sum.parent = rootBranch

        // reduce
        var noAction = false
        while(!noAction) {
            // explode
            val foundExplody = findLeftDeepest(sum, 0)
            if (foundExplody != null) {
                explode(foundExplody)
                continue
            }

            // split
            val foundSplitty = findLeftLargest(sum)
            if (foundSplitty != null) {
                split(foundSplitty)
                continue
            }
            noAction = true
        }

        return sum
    }

    fun part1(input: List<String>): Int {
        val sum = input.map { parse(it.iterator()) }.reduce { a, b -> add(a, b) }
        return getMagnitude(sum)
    }

//    part1(listOf("[[[[4,3],4],4],[7,[[8,4],9]]]", "[1,1]"))

    fun part2(input: List<String>): Int {
        return input.indices.flatMap { a ->
            input.indices.flatMap { b -> listOf(Pair(a, b), Pair(b, a)) }
        }.filter { (a, b) -> a != b }
            .map { (a, b) ->
                getMagnitude(add(parse(input[a].iterator()), parse(input[b].iterator())))
            }.maxOf { it }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
//    check(part1(testInput) == 4140)
    check(part2(testInput) == 3993)

    val input = readInput("Day18")
//    println(part1(input))
    println(part2(input))
}
