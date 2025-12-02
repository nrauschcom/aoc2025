package shared.io

import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.buffer
import okio.use

/**
 * IO Service for local usage and testing.
 *
 * Input is read from /input/dayXX.txt
 *
 * Output and Debugging Information is delivered to Standard Out.
 */
class LocalIOService() : IOService() {
    override fun lines(day: Int): Sequence<String> {
        val resourcePath = "./inputs/day$day.txt".toPath()
        FileSystem.SYSTEM.source(resourcePath).use { fileSource ->
            fileSource.buffer().use { bufferedFileSource ->
                return generateSequence { bufferedFileSource.readUtf8Line() }
            }
        }
    }

    override fun out(output: String) {
        println("=== $output ===");
    }

    override fun debug(output: String) {
        println("# $output");
    }
}