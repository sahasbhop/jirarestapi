package app

import app.jira.search.usecase.SearchIssueUseCase
import app.jira.util.jiraDateTime
import app.jira.worklog.model.WorkLog
import app.util.resetTime
import app.util.width
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

fun main(args: Array<String>) {
    val context = ApplicationContext.read("yml/application.yml")
    println(context)

    val sinceDateTime = OffsetDateTime.now().minusDays(0).resetTime()
    println("Query work log since: ${sinceDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)}")

    SearchIssueUseCase(context)
            .execute("LBC", updatedFrom = sinceDateTime.toLocalDate())
            .subscribe({
                it.forEach {
                    val timestamp = it.fields.updated.jiraDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                    println("$timestamp ${it.key} ${it.fields.summary}")
                }
            }, { error ->
                println("onError - ${error.message}")
                error.printStackTrace()
            })

//    LoginUseCase(context).execute()
//            .flatMap { GetAuthorWorkLogsUseCase(context).execute(sinceDateTime) }
//            .subscribe({
//                printAuthorAndWorkLog(it)
//            }, { error ->
//                println("onError - ${error.message}")
//            })
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