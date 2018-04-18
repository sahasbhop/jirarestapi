package app

import app.jira.util.jiraDateTime
import app.jira.worklog.model.WorkLog
import app.jira.worklog.usecase.GetAuthorWorkLogs
import app.util.width
import app.util.zeroHoursInBangkok
import java.time.format.DateTimeFormatter

fun main(args: Array<String>) {
    val context = ApplicationContext.read("yml/application.yml")
    println(context)

    GetAuthorWorkLogs(context).execute("2018-04-15".zeroHoursInBangkok())
            .subscribe({
                printAuthorAndWorkLog(it)
            })
}

private fun printAuthorAndWorkLog(authorWorkLogsMap: Map<String, List<WorkLog>>) {
    authorWorkLogsMap.forEach { author, workLogList ->
        println("\n$author")

        workLogList.forEach {
            val timestamp = it.updated.jiraDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            println("\t$timestamp\tspent${it.timeSpent.width(6)}\ton issue: ${it.issueId}")
        }
    }
}