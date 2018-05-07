package app.jira.worklog.mapping

import app.jira.issue.model.Issue
import app.jira.search.model.SearchBetween
import app.jira.search.model.SearchFrom
import app.jira.search.model.SearchPeriod
import app.jira.util.jiraDateTime
import app.jira.worklog.model.WorkLog
import app.jira.worklog.view.viewmodel.AuthorsWorkLogViewModel
import app.jira.worklog.view.viewmodel.DailyWorkLogsViewModel
import app.jira.worklog.view.viewmodel.WorkLogViewModel
import app.util.atZeroHours
import app.util.inBangkok

object WorkLogsMapper {
    fun authorsWorkLogsMap(workLogs: List<WorkLog>, issues: Map<String, Issue>): List<AuthorsWorkLogViewModel> {
        return workLogs
                .groupBy { it.updateAuthor.name }
                .map { (authorName, authorsWorkLogList) ->
                    val dailyWorksLogList = authorsWorkLogList
                            .groupBy { it.started.jiraDateTime().toLocalDate() }
                            .map { (date, workLogList) ->
                                val workLogInDateList = workLogList.map {
                                    val issue = issues[it.issueId]
                                    WorkLogViewModel(it.timeSpent, issue?.summary ?: it.issueId)
                                }
                                val totalTimeSpentInDate = workLogList.fold(0L) { acc, it -> acc + it.timeSpentSeconds }
                                DailyWorkLogsViewModel(date, workLogInDateList, totalTimeSpentInDate)
                            }
                    val totalTimeSpent = authorsWorkLogList.fold(0L) { acc, it -> acc + it.timeSpentSeconds}
                    AuthorsWorkLogViewModel(authorName, dailyWorksLogList, totalTimeSpent)
                }
    }

    fun workLogsInPeriod(workLogs: List<WorkLog>, searchPeriod: SearchPeriod): List<WorkLog> {
        return workLogs.filter {
            val updated = it.started.jiraDateTime()
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
}