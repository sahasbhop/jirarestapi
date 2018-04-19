package app.jira.session.model

data class SessionResponse(
        val session: NameValue,
        val loginInfo: LoginInfo
)