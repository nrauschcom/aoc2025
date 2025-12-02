package shared.io

abstract class IOService() {
    abstract fun lines(day: Int): Sequence<String>;
    abstract fun out(output: String);
    abstract fun debug(output: String);
}