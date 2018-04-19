package app.jira.session.repository

import app.ApplicationContext
import app.jira.session.model.SessionRequest
import app.jira.session.model.SessionResponse
import app.jira.session.service.SessionService
import app.retrofit.service
import io.reactivex.Observable

class SessionRepository(private val context: ApplicationContext) {

    fun cookieBasedAuthentication(username: String, password: String): Observable<SessionResponse> {
        val request = SessionRequest(username, password)
        return context.service<SessionService>().cookieBasedAuthentication(request)
    }
}