import shared.io.IOService
import kotlin.math.abs
import kotlin.math.sign

class Day1(day: Int, io: IOService) : Day<List<Int>>(day, io) {
    override fun prepareInput(): List<Int> {
        return mapLines { line -> line.substring(1).toInt() * (if (line[0] == 'R') 1 else -1) }.toList()
    }

    override fun part1(input: List<Int>) {
        var zeroes = 0;
        var current = 50;

        for (line in mapLines { line -> line.substring(1).toInt() * (if (line[0] == 'R') 1 else -1) }) {
            current = (current + line).mod(100) // .mod will never result in negative numbers
            if (current == 0) zeroes++
        }
        out(zeroes.toString())
    }

    override fun part2(input: List<Int>) {
        var zeroes = 0;
        var current = 50;

        for (line in mapLines { line -> line.substring(1).toInt() * (if (line[0] == 'R') 1 else -1) }) {
            val theoretical = (current + line)
            zeroes += abs(theoretical) / 100 + if (current != 0 && theoretical.sign != current.sign) 1 else 0;
            current = theoretical.mod(100)
        }
        out(zeroes.toString())
    }

    override fun runFast() {
        var zeroes1 = 0;
        var zeroes2 = 0;
        var current = 50;

        for (line in mapLines { line -> line.substring(1).toInt() * (if (line[0] == 'R') 1 else -1) }) {
            val theoretical = (current + line)
            zeroes2 += abs(theoretical) / 100 + if (current != 0 && theoretical.sign != current.sign) 1 else 0;
            current = theoretical.mod(100)
            if (current == 0) zeroes1++
        }
        out("$zeroes1, $zeroes2")
    }
}