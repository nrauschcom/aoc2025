import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.cValue
import platform.posix.nanosleep
import platform.posix.timespec
import shared.io.IOService
import shared.io.LocalIOService
import shared.io.StdIOService
import kotlin.time.measureTime

@OptIn(ExperimentalForeignApi::class)
fun main(args: Array<String>) {
    val io: IOService? = if (args.contains("--local")) LocalIOService()
        else StdIOService();

    // Wait a little to finish startup, will increase runtime a bit
    nanosleep(cValue<timespec> {
        tv_sec = 0
        tv_nsec = 250000000
    }, null);

    val DAY = 2;
    val dayInstance = Day2(DAY, io!!);

    val execTime = measureTime {
        // slow flag will run the parts separately with debug output, fast mode will try to use runFast()
        if (args.contains("--slow"))
            dayInstance.run()
        else
            dayInstance.runFast()
    }

    io.out("Program Execution took $execTime")
}