package util

import kotlin.js.Date

actual object Time {
    actual fun currentTimeMillis(): Long {
        return Date().getTime().toLong()
    }
}
