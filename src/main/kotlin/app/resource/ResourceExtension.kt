package app.resource

import com.esotericsoftware.yamlbeans.YamlReader
import java.io.File
import java.io.FileReader

fun String.asExternalResourceFile(): File = File("resources/$this")

fun File.asYmlMap(): Map<*, *> {
    return try {
        YamlReader(FileReader(this)).read() as Map<*, *>
    } catch (e: Exception) {
        println("Read YAML Error!")
        throw e
    }
}