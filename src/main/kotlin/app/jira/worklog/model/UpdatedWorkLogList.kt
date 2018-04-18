package app.jira.worklog.model

data class UpdatedWorkLogList(
        val values: List<UpdatedWorkLog>,
        val since: Long,
        val until: Long,
        val self: String,
        val nextPage: String,
        val lastPage: Boolean
)