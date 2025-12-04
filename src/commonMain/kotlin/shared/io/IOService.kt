package shared.io

abstract class IOService() {
    abstract fun lines(day: Int): List<String>;
    abstract fun out(output: String);
    abstract fun debug(output: Any);
}
