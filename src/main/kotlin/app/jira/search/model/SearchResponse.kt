package app.jira.search.model

data class SearchResponse(val maxResults: Int, val total: Int, val issues: List<SearchIssue>)