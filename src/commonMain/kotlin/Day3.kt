import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking
import shared.data.Constants.Companion.POW10
import shared.io.IOService
import shared.multithreaded.forEachParallel
import kotlin.concurrent.atomics.AtomicLong
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.time.measureTime

class Day3(day: Int, io: IOService) : Day<List<List<Int>>>(day, io) {
    override fun part1(input: List<List<Int>>) {
        var sum = 0L;
        for (row in input) {
            io.debug(row)
            var highestIdx = 1;
            for (i in 0..row.lastIndex - 1) {
                if (row[i] > row[highestIdx]) highestIdx = i;
            }
            val d1 = row[highestIdx]
            val i1 = highestIdx + 1
            highestIdx = i1
            for (i in i1..row.lastIndex) {
                if (row[i] > row[highestIdx]) highestIdx = i;
            }
            io.debug(highestIdx)
            io.debug(i1)

            sum += d1 * 10 + row[highestIdx]
        }
        out(sum.toString());
    }

    override fun part2(input: List<List<Int>>) {
        var sum = 0L;
        for (row in input) {
            var localSum = 0L;
            io.debug(row)
            var highestIdx: Int;
            var prevHighestIdx = -1;
            for (j in 11 downTo 0) {
                highestIdx = prevHighestIdx + 1
                for (i in highestIdx..row.lastIndex - j) {
                    if (row[i] > row[highestIdx]) highestIdx = i;
                }
                sum += POW10[j] * row[highestIdx];
                localSum += POW10[j] * row[highestIdx];
                prevHighestIdx = highestIdx;
                io.debug("j = $j, highestIdx = $highestIdx, num = ${row[highestIdx]}, localSum = $localSum")
            }
        }
        out(sum.toString());
    }

    @OptIn(ExperimentalAtomicApi::class, DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    override fun runFast() {
        var sum1 = AtomicLong(0L);
        var sum2 = AtomicLong(0L);
        val multi = 4;
        val threadPool = newFixedThreadPoolContext(multi, "tp");
        // time for threadpool init
        sleep(500_000_000);
        var threadLines: List<List<String>>;
        val inputtime = measureTime {
            val allLines = lines();
            threadLines = allLines.chunked(allLines.count() / multi);
        }
        val all = measureTime {
            runBlocking(threadPool) {
                threadLines.forEachParallel { lines ->
                    val thread = measureTime {
                        lines.forEach {
                            val smallLine = IntArray(12)
                            var localSum = 0L
                            val row = it.toCharArray().map { it.digitToInt() }
                            var highestIdx: Int;
                            var prevHighestIdx = -1;
                            for (j in 11 downTo 0) {
                                highestIdx = prevHighestIdx + 1
                                for (i in highestIdx..99 - j) {
                                    if (row[i] > row[highestIdx]) highestIdx = i;
                                    if (row[i] == 9) break;
                                }
                                localSum += POW10[j] * row[highestIdx];
                                smallLine[11 - j] = row[highestIdx]
                                prevHighestIdx = highestIdx;
                            }
                            sum2.fetchAndAdd(localSum);
                            // Part 1 now on the reduced set of numbers
                            highestIdx = 0;
                            for (i in 0..10) {
                                if (smallLine[i] > smallLine[highestIdx]) highestIdx = i;
                                if (smallLine[i] == 9) break;
                            }
                            val d1 = smallLine[highestIdx]
                            val i1 = highestIdx + 1
                            highestIdx = i1
                            for (i in i1..11) {
                                if (smallLine[i] > smallLine[highestIdx]) highestIdx = i;
                            }
                            sum1.fetchAndAdd(d1 * 10L + smallLine[highestIdx])
                        }
                    }
                    out("Thread took $thread")
                }

            }
        }
        out("$sum1, $sum2 ($all)");
        out("Input took $inputtime")
    }

    override fun prepareInput(): List<List<Int>> {
        return this.lines().map {
            it.toCharArray().map { it.digitToInt() }
        }.toCollection(ArrayList())
    }
}
