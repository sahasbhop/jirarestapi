package app.jira.session.usecase

import app.ApplicationContext
import app.jira.session.repository.SessionRepository
import io.reactivex.Observable

class LoginUseCase(private val context: ApplicationContext) {

    fun execute(): Observable<Unit> {
        return SessionRepository(context)
                .cookieBasedAuthentication(context.username, context.password)
                .map { Unit }
    }
}