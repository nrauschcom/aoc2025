import shared.data.Range
import shared.io.IOService
import kotlin.math.ceil
import kotlin.math.pow

class Day2(day: Int, io: IOService) : Day<List<Range>>(day, io) {
    override fun part1(input: List<Range>) {
        var sum = 0L;
        for (range in input) {
            val full = range.from.toString();
            io.debug("= Range $range");
            var currentHalf = if (full.length % 2 == 0) full.take(ceil(full.length / 2.0).toInt()).toLong() else 10f.pow(ceil(full.length / 2.0).toInt() - 1).toLong()
            do {
                val current = (currentHalf.toString() + currentHalf).toLong();
                io.debug("$current (${current <= range.to})")
                if (current < range.from) {
                    currentHalf++;
                } else if (current <= range.to) {
                    sum += current
                    currentHalf++;
                }
                else break
            } while (true);
        }
        out(sum.toString());
    }

    override fun part2(input: List<Range>) {
        var sum = 0L;
        for (range in input) {
            val used = mutableListOf<Long>()
            val full = range.from.toString();
            io.debug("= Range $range");
            for (n in 2..range.to.toString().length) {
                io.debug("n = $n")
                var currentHalf = if (full.length % n == 0) full.take(ceil(full.length / n.toFloat()).toInt())
                    .toLong() else 10f.pow(ceil(full.length / n.toFloat()).toInt() - 1).toLong()
                do {
                    val current = currentHalf.toString().repeat(n).toLong();
                    io.debug("$current (${current <= range.to})")
                    if (current < range.from) {
                        currentHalf++;
                    } else if (current <= range.to) {
                        if (!used.contains(current)) {
                            sum += current
                            used.add(current)
                        }
                        currentHalf++;
                    } else break
                } while (true);
            }
        }
        out(sum.toString());
    }

    override fun runFast() {
        var sum1 = 0L;
        var sum2 = 0L;
        this.oneLine().split(",").map {
            val (start, end) = it.split("-").map { it.toLong() }
            Range(start, end)
        }.forEach {
            val used = mutableListOf<Long>()
            val full = it.from.toString();
            for (n in 2..it.to.toString().length) {
                var currentHalf = if (full.length % n == 0) full.take(ceil(full.length / n.toFloat()).toInt())
                    .toLong() else 10f.pow(ceil(full.length / n.toFloat()).toInt() - 1).toLong()
                do {
                    val current = currentHalf.toString().repeat(n).toLong();
                    if (current < it.from) {
                        currentHalf++;
                    } else if (current <= it.to) {
                        if (!used.contains(current)) {
                            if (n == 2) sum1 += current;
                            sum2 += current
                            used.add(current)
                        }
                        currentHalf++;
                    } else break
                } while (true);
            }
        }
        out("$sum1, $sum2");
    }

    override fun prepareInput(): List<Range> {
        return this.oneLine().split(",").map {
            val (start, end) = it.split("-").map { it.toLong() }
            Range(start, end)
        }
    }
}