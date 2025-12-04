package shared.io

import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM
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
    override fun lines(day: Int): List<String> {
        val resourcePath = "./inputs/day$day.txt".toPath()
        val seq = mutableListOf<String>()
        FileSystem.SYSTEM.source(resourcePath).use { fileSource ->
            fileSource.buffer().use { bufferedFileSource ->
                var line: String?;
                do {
                    line = bufferedFileSource.readUtf8Line()
                    if (line != null)
                        seq.add(line)
                } while (line != null)
            }
        }
        return seq
    }

    override fun out(output: String) {
        println("=== $output ===");
    }

    override fun debug(output: Any) {
        println("# $output");
    }
}
