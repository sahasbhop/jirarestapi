package app.jira.session.service

import app.jira.session.model.SessionRequest
import app.jira.session.model.SessionResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

// Reference: https://developer.atlassian.com/cloud/jira/platform/rest/#api-api-2-worklog-list-post
interface SessionService {
    @POST("rest/auth/1/session")
    fun cookieBasedAuthentication(@Body request: SessionRequest): Observable<SessionResponse>
}