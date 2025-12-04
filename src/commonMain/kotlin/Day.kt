import shared.io.IOService
import kotlin.time.measureTime

abstract class Day<T>(protected val day: Int, protected val io: IOService) {
    abstract fun part1(input: T);
    abstract fun part2(input: T);

    abstract fun prepareInput(): T;

    fun run() {
        var input: T;

        val prepareTime = measureTime {
            input = this.prepareInput();
        }
        io.debug("Preparing Input took $prepareTime");

        val part1Time = measureTime {
            this.part1(input);
        }
        io.debug("Running Part 1 took $part1Time");

        val part2Time = measureTime {
            this.part2(input);
        }
        io.debug("Running Part 2 took $part2Time");
    }

    open fun runFast() {
        this.run();
    }

    protected fun oneLine(): String {
        return io.lines(day).first();
    }

    protected fun lines(): List<String> {
        return io.lines(day)
    }

    protected fun <T> mapLines(mapper: (String) -> T): List<T> {
        return io.lines(day).map(mapper)
    }

    protected fun out(output: String) {
        io.debug("")
        io.out(output)
        io.debug("")
    }
}
