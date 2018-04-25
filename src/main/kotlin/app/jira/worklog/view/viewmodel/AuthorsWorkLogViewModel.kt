package app.jira.worklog.view.viewmodel

data class AuthorsWorkLogViewModel(
        val author: String,
        val workLogList: List<DailyWorkLogsViewModel>,
        val totalTimeSpent: Long)