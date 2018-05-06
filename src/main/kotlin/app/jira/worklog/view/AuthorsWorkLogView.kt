package app.jira.worklog.view

import app.ApplicationContext
import app.jira.worklog.view.viewmodel.AuthorsWorkLogViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object AuthorsWorkLogView {
    fun show(context: ApplicationContext, authorWorkLogs: List<AuthorsWorkLogViewModel>) {
        summary(context, authorWorkLogs)
        detailed(authorWorkLogs)
    }

    private fun detailed(authorWorkLogs: List<AuthorsWorkLogViewModel>) {
        println("h2. Details")
        authorWorkLogs
                .sortedBy { it.author.toLowerCase() }
                .forEach {
                    println("h3. ${it.author.toUpperCase()} - ${it.totalTimeSpent.timeSpentFormat()}")

                    val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")

                    it.workLogList
                            .sortedBy { it.date }
                            .forEach {
                                val date = it.date.format(dateFormatter)
                                println("* $date ${it.totalTimeSpent.timeSpentFormat()}")

                                it.workLogList.forEach {
                                    println("** ${it.timeSpent} on ${it.issueSummary.replace("[", "(").replace("]", ")")}")
                                }
                                println()
                            }
                }
    }

    private fun summary(context: ApplicationContext, authorWorkLogs: List<AuthorsWorkLogViewModel>) {
        val numberOfWorkingPersonExpect = context.numberOfWorkingPerson
        val numberOfWorkingPersonActual = authorWorkLogs.size

        val numberOfWorkingDaysExpect = context.numberOfWorkingDays
        val numberOfWorkingDaysActual = authorWorkLogs.fold(mutableSetOf<LocalDate>()) { acc, it ->
            acc.addAll(it.workLogList.map { it.date })
            acc
        }.size

        val totalTimeSpent = authorWorkLogs.fold(0L) { acc, model ->
            return@fold acc + model.totalTimeSpent
        }

        val averageTimeSpentPerPersonPerDayString = Math.round(totalTimeSpent.toDouble() / numberOfWorkingPersonActual / numberOfWorkingDaysActual).timeSpentFormat()

        println("""

            h2. Summary
            * Number of working days: $numberOfWorkingDaysActual / $numberOfWorkingDaysExpect
            * Number of working person: $numberOfWorkingPersonActual / $numberOfWorkingPersonExpect
            * Total time spent: ${totalTimeSpent.timeSpentFormat()} hrs
            * Avg.  time spent: $averageTimeSpentPerPersonPerDayString hrs (per person per day)

                """.trimIndent()
        )
    }

    // credit: https://stackoverflow.com/a/6118983
    private fun Long.timeSpentFormat(): String {
        val hours = this / 3600
        val minutes = (this % 3600) / 60
        val seconds = this % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}