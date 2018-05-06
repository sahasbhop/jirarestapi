package app

import app.jira.search.model.SearchBetween
import app.jira.search.model.SearchFrom
import app.jira.search.model.SearchPeriod
import com.esotericsoftware.yamlbeans.YamlReader
import com.sun.org.apache.xpath.internal.operations.Bool
import com.sun.tools.corba.se.idl.constExpr.BooleanOr
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

data class ApplicationContext(
        var url: String,
        var username: String,
        var password: String,
        var projectName: String,
        var searchPeriodFrom: String,
        var searchPeriodTo: String,
        var maxConcurrentRequest: Int,
        var numberOfWorkingDays: Int,
        var numberOfWorkingPerson: Int,
        var debug: Boolean
) {

    @Suppress("unused") // requested by YamlReader lib
    constructor() : this("", "", "", "", "", "", 1, 1, 1, true)

    companion object {
        fun read(resourceFileName: String): ApplicationContext {
            val file = File("resources/$resourceFileName")
            if (!file.exists()) throw FileNotFoundException("$file does not exists!")
            return YamlReader(FileReader(file)).read(ApplicationContext::class.java)
        }
    }

    fun searchPeriod(): SearchPeriod {
        val from = try {
            LocalDate.parse(searchPeriodFrom, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        } catch (e: DateTimeParseException) {
            null
        }
        val to = try {
            LocalDate.parse(searchPeriodTo, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        } catch (e: DateTimeParseException) {
            null
        }
        return when {
            from != null && to != null -> SearchBetween(from, to)
            from != null -> SearchFrom(from)
            else -> throw IllegalArgumentException("Invalid search period! Please specify search period in format yyyy-MM-dd")
        }
    }
}