package shared.data

/**
 * Data Structure to store a range of numbers.
 * Implemented as data class with little to no overhead.
 */
data class Range(val from: Long, val to: Long) {
    override fun toString(): String {
        return "$from->$to"
    }
}
