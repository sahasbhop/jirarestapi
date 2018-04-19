package app.jira.search.service

import app.jira.search.model.SearchRequest
import app.jira.search.model.SearchResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface SearchService {
    @POST("rest/api/2/search")
    fun search(@Body request: SearchRequest): Observable<SearchResponse>
}