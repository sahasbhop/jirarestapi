package app.jira.session.model

data class LoginInfo(
        val failedLoginCount: Int,
        val loginCount: Int,
        val lastFailedLoginTime: String,
        val previousLoginTime: String
)