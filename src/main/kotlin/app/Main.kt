package app

import app.jira.issue.model.Issue
import app.jira.search.model.SearchBetween
import app.jira.search.model.SearchFrom
import app.jira.search.model.SearchIssue
import app.jira.search.model.SearchPeriod
import app.jira.search.usecase.SearchIssueUseCase
import app.jira.session.usecase.LoginUseCase
import app.jira.util.jiraDateTime
import app.jira.worklog.mapping.AuthorWorkLogsMapper
import app.jira.worklog.model.WorkLog
import app.jira.worklog.usecase.GetIssueWorkLogsUseCase
import app.util.atZeroHours
import app.util.inBangkok
import app.util.resetTime
import app.util.width
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

fun main(args: Array<String>) {
    val context = ApplicationContext.read("yml/application.yml")
    println(context)

    val sinceDateTime = OffsetDateTime.now().minusDays(3).resetTime()
    println("Query work log since: ${sinceDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)}")

    val searchPeriod: SearchPeriod = SearchFrom(sinceDateTime.toLocalDate())

    LoginUseCase(context).execute()
            .flatMap { SearchIssueUseCase(context).execute("LBC", searchPeriod) }
            // .doOnNext { searchResult -> printSearchIssues(searchResult) }
            .map { it.associateBy({ it.id }, { Issue(it.id, it.fields.summary) }) }
            .flatMap { issues ->
                GetIssueWorkLogsUseCase(context)
                        .execute(issues.map { (id, _) -> id })
                        .map { workLogsInPeriod(it, searchPeriod) }
                        .map { AuthorWorkLogsMapper.map(it) }
                        .map { list -> list to issues }

            }
            .subscribe({ (authorWorkLogs, issues) ->
                printAuthorAndWorkLog(authorWorkLogs, issues)
            }, { error ->
                println("onError - ${error.message}")
            })
}

private fun workLogsInPeriod(workLogs: List<WorkLog>, searchPeriod: SearchPeriod): List<WorkLog> {
    return workLogs.filter {
        val updated = it.updated.jiraDateTime()
        when (searchPeriod) {
            is SearchFrom -> {
                updated.isAfter(searchPeriod.from.atZeroHours().inBangkok())
            }
            is SearchBetween -> {
                updated.isAfter(searchPeriod.from.atZeroHours().inBangkok())
                        && updated.isBefore(searchPeriod.to.atZeroHours().inBangkok())
            }
        }
    }
}

private fun printSearchIssues(it: List<SearchIssue>) {
    it.forEach {
        val timestamp = it.fields.updated.jiraDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        println("$timestamp ${it.key} ${it.fields.summary}")
    }
}

private fun printAuthorAndWorkLog(authorWorkLogsMap: Map<String, List<WorkLog>>, issues: Map<String, Issue>? = emptyMap()) {
    authorWorkLogsMap.forEach { author, workLogList ->
        println("\n$author")

        workLogList.forEach {
            val issueId = it.issueId
            val issueSummary = issues?.get(issueId)?.summary ?: issueId
            val timestamp = it.updated.jiraDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            println("\t$timestamp\tspent${it.timeSpent.width(6)}\ton issue: $issueSummary")
        }
    }
}