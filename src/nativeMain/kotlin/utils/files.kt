package utils

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toKString
import platform.posix.fclose
import platform.posix.fgets
import platform.posix.fopen

/**
 * @param path file location relative to kotlin dir
 */
fun input(path: String): List<String> {
    val result = mutableListOf<String>()

    val f = fopen("./src/nativeMain/kotlin/$path", "r") ?: throw IllegalArgumentException("cannot open file")

    try {
        memScoped {
            val readBufferLength = 64 * 1024
            val buffer = allocArray<ByteVar>(readBufferLength)
            var line = fgets(buffer, readBufferLength, f)?.toKString()
            while (line != null) {
                result.add(line)
                line = fgets(buffer, readBufferLength, f)?.toKString()
            }
        }
    } finally {
        fclose(f)
    }

    return result.toList()
}
