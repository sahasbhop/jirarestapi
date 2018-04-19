package app.jira.search.repository

import app.ApplicationContext
import app.jira.search.model.SearchRequest
import app.jira.search.model.SearchResponse
import app.jira.search.service.SearchService
import app.retrofit.service
import io.reactivex.Observable

class SearchRepository(private val context: ApplicationContext) {

    fun search(jql: String, maxResults: Int): Observable<SearchResponse> {
        return context.service<SearchService>().search(SearchRequest(jql, maxResults))
    }
}