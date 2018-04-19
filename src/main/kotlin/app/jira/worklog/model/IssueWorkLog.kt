package app.jira.worklog.model

data class IssueWorkLog(val maxResults: Int, val total: Int, val worklogs: List<WorkLog>)