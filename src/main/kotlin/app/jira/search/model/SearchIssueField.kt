package app.jira.search.model

data class SearchIssueField(val summary: String, val labels: List<String>, val updated: String)