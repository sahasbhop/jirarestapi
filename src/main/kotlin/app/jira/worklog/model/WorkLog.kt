package app.jira.worklog.model

data class WorkLog(
        val self: String,
        val author: Author,
        val updateAuthor: Author,
        val comment: String,
        val updated: String,
        val started: String,
        val timeSpent: String,
        val timeSpentSeconds: Long,
        val id: String,
        val issueId: String)