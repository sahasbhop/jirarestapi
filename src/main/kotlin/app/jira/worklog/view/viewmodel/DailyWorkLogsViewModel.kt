package app.jira.worklog.view.viewmodel

import java.time.LocalDate

data class DailyWorkLogsViewModel(
        val date: LocalDate,
        val workLogList: List<WorkLogViewModel>,
        val totalTimeSpent: Long)