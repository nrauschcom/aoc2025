import shared.io.IOService
import shared.io.LocalIOService
import shared.io.StdIOService
import kotlin.system.exitProcess
import kotlin.time.measureTime

fun main(args: Array<String>) {
    val io: IOService? = if ("local".equals(System.getenv("IO"))) LocalIOService()
    else StdIOService();

    val DAY = 3;
    val dayInstance = Day3 (DAY, io!!);

    val execTime = measureTime {
        // local flag will run the parts separately with debug output, fast mode will try to use runFast()
        if ("true".equals(System.getenv("DEBUG")))
            dayInstance.run()
        else
            dayInstance.runFast()
    }

    io.out("Program Execution took $execTime")
}

actual fun exitApplication() {
    exitProcess(0)
}

actual fun sleep(durationNanos: Long) {
    Thread.sleep(durationNanos / 1_000_000)
}

actual fun printFlush(line: String) {
    println(line)
}

actual fun readStdIn(): List<String> {
    return generateSequence(::readlnOrNull).toList()
}
