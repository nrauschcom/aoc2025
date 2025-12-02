package shared.io

/**
 * StdIO Service for use in Advent of Tryhard Benchmarking.
 * Input is delivered over Standard Input after a "handshake" message is sent by the program.
 *
 * Output is delivered to Standard Out.
 * Debug messages are delivered to Standard Out.
 */
class StdIOService: IOService() {
    override fun lines(day: Int): Sequence<String> {
        println("Hi Kai");
        return generateSequence { readlnOrNull() };
    }

    override fun out(output: String) {
        println(output);
    }

    override fun debug(output: String) {
        println(output);
    }
}