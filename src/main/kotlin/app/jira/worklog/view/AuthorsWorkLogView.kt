package app.jira.worklog.view

import app.jira.worklog.view.viewmodel.AuthorsWorkLogViewModel
import app.util.width
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object AuthorsWorkLogView {
    fun show(authorWorkLogs: List<AuthorsWorkLogViewModel>) {
        authorWorkLogs.forEach {
            println("\n${it.author}  ${it.totalTimeSpent.timeSpentFormat()}")

            val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")

            it.workLogList.forEach {
                val date = it.date.format(dateFormatter)
                println("  $date")

                it.workLogList.forEach {
                    println("    ${it.timeSpent}\ton ${it.issueSummary}")
                }
            }
        }
    }

    private fun Long.timeSpentFormat() = LocalTime.ofSecondOfDay(this)
            .format(DateTimeFormatter.ofPattern("HH:mm:ss"))
}