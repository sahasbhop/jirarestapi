package app.jira.search.model

data class SearchRequest(val jql: String, val maxResults: Int = 500) {
    @Suppress("unused")
    val fields = arrayOf("summary", "labels", "updated")
}