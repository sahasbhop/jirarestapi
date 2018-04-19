package app

import com.esotericsoftware.yamlbeans.YamlReader
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader

data class ApplicationContext(
        var url: String,
        var username: String,
        var password: String) {

    @Suppress("unused") // requested by YamlReader lib
    constructor() : this("", "", "")

    companion object {
        fun read(resourceFileName: String): ApplicationContext {
            val file = File("resources/$resourceFileName")
            if (!file.exists()) throw FileNotFoundException("$file does not exists!")
            return YamlReader(FileReader(file)).read(ApplicationContext::class.java)
        }
    }
}