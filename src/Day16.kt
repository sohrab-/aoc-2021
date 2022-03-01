import java.lang.Exception
import java.lang.IndexOutOfBoundsException
import java.lang.RuntimeException
import java.util.NoSuchElementException

private fun <T> Iterator<T>.next(n: Long): String {
    var results = ""
    (0 until n).forEach { _ -> results += this.next() }
    return results
}
private fun Iterator<Char>.nextBinary(n: Long) = this.next(n).toLong(2)

private open class Packet(val version: Long)
private class Literal(version: Long, val value: Long): Packet(version) {
    override fun toString() = "Literal[v$version]($value)"
}
private class Operator(version: Long, val type: Long, val subPackets: List<Packet>): Packet(version) {
    override fun toString() = "Operator[v$version](type: $type, subPackets: $subPackets)"
}

private fun parseLiteral(iterator: Iterator<Char>): Long {
    var literal = ""
    do {
        val block = iterator.next(5)
        literal += block.drop(1)
    } while(block.first() == '1')
    return literal.toLong(2)
}

private fun parseOperator(iterator: Iterator<Char>): List<Packet> {
    val packets = mutableListOf<Packet>()
    when(iterator.next()) {
        '0' -> {
            val theRest = iterator.next(iterator.nextBinary(15)).iterator()

            do {
                val packet = parse(theRest)?.also { packets.add(it) }
            } while(packet != null)
        }
        '1' -> {
            (0 until iterator.nextBinary(11)).forEach { _ ->
                parse(iterator)?.also { packets.add(it) }
            }
        }
        else -> throw RuntimeException("Something has gone horribly wrong")
    }
    return packets
}

private fun parse(iterator: Iterator<Char>): Packet? {
    return try {
        val version = iterator.nextBinary(3)
        when(val type = iterator.nextBinary(3)) {
            4L -> Literal(version, parseLiteral(iterator))
            else -> Operator(version, type, parseOperator(iterator))
        }
    } catch (e: Exception) {
        when(e) {
            is NoSuchElementException, is IndexOutOfBoundsException -> null
            else -> throw e
        }
    }
}

fun main() {

    fun part1(input: String): Long {
        val iterator = input.flatMap {
            Integer.toBinaryString(Integer.parseInt(it.toString(), 16))
                .padStart(4, '0').asIterable()
        }.iterator()

        val packet = parse(iterator) ?: throw RuntimeException("Packet invalid")

        println(packet)

        fun getVersion(packet: Packet): Long = when(packet) {
            is Literal -> packet.version
            is Operator -> packet.version + packet.subPackets.sumOf(::getVersion)
            else -> throw RuntimeException("Wtf $packet")
        }

        return getVersion(packet)
    }

    fun part2(input: String): Long {
        val iterator = input.flatMap {
            Integer.toBinaryString(Integer.parseInt(it.toString(), 16))
                .padStart(4, '0').asIterable()
        }.iterator()

        val packet = parse(iterator) ?: throw RuntimeException("Packet invalid")

        println(packet)

        fun getValue(packet: Packet): Long = when(packet) {
            is Literal -> packet.value
            is Operator -> when(packet.type) {
                0L -> packet.subPackets.sumOf(::getValue)
                1L -> packet.subPackets.fold(1) { acc, x -> acc * getValue(x) }
                2L -> packet.subPackets.minOf(::getValue)
                3L -> packet.subPackets.maxOf(::getValue)
                5L -> if (getValue(packet.subPackets[0]) > getValue(packet.subPackets[1])) 1 else 0
                6L -> if (getValue(packet.subPackets[0]) < getValue(packet.subPackets[1])) 1 else 0
                7L -> if (getValue(packet.subPackets[0]) == getValue(packet.subPackets[1])) 1 else 0
                else -> throw RuntimeException("Wtf $packet operator")
            }
            else -> throw RuntimeException("Wtf $packet")
        }

        return getValue(packet)
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day16_test")

//    check(part1("D2FE28") == 6)
//    check(part1("38006F45291200") == 9)
//    check(part1("EE00D40C823060") == 14)
//    check(part1("8A004A801A8002F478") == 16)
//    check(part1("620080001611562C8802118E34") == 12)
//    check(part1("C0015000016115A2E0802F182340") == 23)
//    check(part1("A0016C880162017C3686B18A3D4780") == 31)

//    check(part2("C200B40A82") == 3L)
//    check(part2("04005AC33890") == 54L)
//    check(part2("880086C3E88112") == 7L)
//    check(part2("CE00C43D881120") == 9L)
//    check(part2("D8005AC2A8F0") == 1L)
//    check(part2("F600BC2D8F") == 0L)
//    check(part2("9C005AC2F8F0") == 0L)
//    check(part2("9C0141080250320F1802104A08") == 1L)

    val input = readInput("Day16").first()
//    println(part1(input))
    println(part2(input))
}
