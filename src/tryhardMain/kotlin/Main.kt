import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UnsafeNumber
import kotlinx.cinterop.cValue
import platform.posix.nanosleep
import platform.posix.timespec
import shared.io.IOService
import shared.io.LocalIOService
import shared.io.StdIOService
import kotlin.system.exitProcess
import kotlin.time.measureTime
import platform.posix.*
import kotlinx.cinterop.*
import kotlinx.cinterop.internal.ConstantValue
import platform.posix.fflush
import platform.posix.stdout

@OptIn(ExperimentalForeignApi::class, UnsafeNumber::class)
fun main(args: Array<String>) {
    val io: IOService? = if ("local".equals(getenv("IO")?.toKString())) LocalIOService()
        else StdIOService();

    // Wait a little to finish startup, will increase runtime a bit
    sleep(250000000);

    val DAY = 3;
    val dayInstance = Day3(DAY, io!!);

    val execTime = measureTime {
        // slow flag will run the parts separately with debug output, fast mode will try to use runFast()
        if ("true".equals(getenv("DEBUG")?.toKString()))
            dayInstance.run()
        else
            dayInstance.runFast()
    }

    io.out("Program Execution took $execTime")
}

actual fun exitApplication() {
    exitProcess(0)
}

@OptIn(UnsafeNumber::class, ExperimentalForeignApi::class)
actual fun sleep(durationNanos: Long) {
    val sec = durationNanos / 1_000_000_000;
    nanosleep(cValue<timespec> {
        tv_sec = sec
        tv_nsec = 500_000_000
    }, null);
}

@OptIn(ExperimentalForeignApi::class)
actual fun printFlush(line: String) {
    fprintf(stdout, line + '\n')
    fflush(stdout)
}

@OptIn(ExperimentalForeignApi::class, UnsafeNumber::class)
actual fun readStdIn(): List<String> {
    val INITIAL_BUFFER_SIZE = 4096

    val lines = mutableListOf<String>()

    memScoped {
        val buffer = allocArray<ByteVar>(INITIAL_BUFFER_SIZE)

        while (fgets(buffer, INITIAL_BUFFER_SIZE, stdin) != null) {
            val length = strlen(buffer.toKString()).toInt()

            if (length > 0 && buffer[length - 1].toInt().toChar() != '\n' && feof(stdin) != 0) {
                val builder = StringBuilder(buffer.toKString())

                while (true) {
                    val bytesRead = fgets(buffer, INITIAL_BUFFER_SIZE, stdin) ?: break
                    builder.append(buffer.toKString())

                    val currentLength = strlen(buffer.toKString()).toInt()
                    if (currentLength > 0 && buffer[currentLength - 1].toInt().toChar() == '\n') {
                        break // Found the end of the line
                    }
                }
                lines.add(builder.toString().trimEnd('\r', '\n'))

            } else {
                lines.add(buffer.toKString().trimEnd('\r', '\n'))
            }
        }
    }

    return lines
}
