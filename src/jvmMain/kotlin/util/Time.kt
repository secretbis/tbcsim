package util

actual object Time {
    actual fun currentTimeMillis(): Long {
        return System.currentTimeMillis()
    }
}
