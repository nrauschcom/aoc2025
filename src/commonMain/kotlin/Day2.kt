import shared.data.Constants.Companion.POW10
import shared.data.Range
import shared.io.IOService
import kotlin.Long.Companion.MAX_VALUE
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.time.measureTime

class Day2(day: Int, io: IOService) : Day<List<Range>>(day, io) {
    override fun part1(input: List<Range>) {
        var sum = 0L;
        for (range in input) {
            val full = range.from.toString();
            io.debug("= Range $range");
            var currentHalf = if (full.length % 2 == 0) full.take(ceil(full.length / 2.0).toInt()).toLong() else 10f.pow(ceil(full.length / 2.0).toInt() - 1).toLong()
            do {
                val current = (currentHalf.toString() + currentHalf).toLong();
                if (current < range.from) {
                    currentHalf++;
                } else {
                    io.debug("$current (${current <= range.to})")
                    if (current <= range.to) {
                        sum += current
                        currentHalf++;
                    }
                    else break
                }
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
                    if (current < range.from) {
                        currentHalf++;
                    } else {
                        io.debug("$current (${current <= range.to})")
                        if (current <= range.to) {
                            if (!used.contains(current)) {
                                sum += current
                                used.add(current)
                            }
                            currentHalf++;
                        }
                        else break
                    }
                } while (true);
            }
        }
        out(sum.toString());
    }

    override fun runFast() {
        var sum1 = 0L;
        var sum2 = 0L;
        // preparation
        val steps = mutableListOf<MutableList<Long>>()
        val reps = mutableListOf<MutableList<Int>>()
        val sets = mutableListOf<MutableSet<Long>>()
        steps.add(mutableListOf(0L));
        reps.add(mutableListOf(1));
        for (i in 1..12) {
            val smallSteps = mutableListOf<Long>(0L);
            val smallReps = mutableListOf<Int>();
            for (j in 1..12) {
                val tmp = POW10[ceil(i / j.toDouble()).toInt() - 1]
                try {
                    val step = tmp.toString().repeat(j).toLong() / tmp;
                    smallSteps.add(step)
                } catch (e: NumberFormatException) {
                    smallSteps.add(MAX_VALUE)
                }
            }
            steps.add(smallSteps)
            for (j in 2..i) {
                if (i % j == 0 || (i-1) % j == 0) smallReps.add(j)
            }
            reps.add(smallReps);
        }
        for (i in 0..100) {
            sets.add(HashSet<Long>(256))
        }
        io.debug("Preparation done")
        io.debug("Steps: $steps")
        io.debug("Reps: $reps")
        val fast = measureTime {
            var lineNum = 0;
            this.oneLine().split(",").forEach {
                val (startString, endString) = it.split("-")
                val startLen = startString.length
                val start = startString.toLong()
                val end = endString.toLong()
                val set = sets[lineNum]
                for (rep in reps[endString.length]) {
                    val step = steps[startString.length][rep];
                    if (step > end) continue
                    val currentHalf =
                        if (startLen % rep == 0) startString.take(startLen / rep)
                            .toInt() else POW10[startLen / rep]
                            .toInt()
                    var current = currentHalf.toString().repeat(rep).toLong()
                    if (current < start) current += step;
                    val num = if (current <= end) floor(
                        max(
                            0,
                            min(end, POW10[current.toString().length]) - current
                        ).toFloat() / step
                    ).toInt() + 1 else 0;
                    if (rep == 2) sum1 += (num * (num - 1) / 2) * step + num * current;
                    for (i in 0..<num) {
                        if (!set.contains(current + i * step)) {
                            val c = current + i * step
                            set.add(c)
                            sum2 += c;
                        }
                        //else io.debug("DUPLICATE n = $rep, ${current + i * step}, i = $i");
                    }
                }
                lineNum++;
            }
        }
        out("$sum1, $sum2")
        out("$fast")
    }

    override fun prepareInput(): List<Range> {
        return this.oneLine().split(",").map {
            val (start, end) = it.split("-").map { it.toLong() }
            Range(start, end)
        }
    }
}
