package app

import app.resource.asExternalResourceFile
import app.resource.asYmlMap

data class ApplicationContext(
        val jiraUrl: String,
        val jiraUsername: String,
        val jiraPassword: String,
        val jSessionId: String,
        val token: String) {

    companion object {
        fun default(): ApplicationContext {
            val configuration = "yml/application.yml".asExternalResourceFile().asYmlMap()
            return ApplicationContext(
                    jiraUrl = configuration["jira.url"] as String,
                    jiraUsername = configuration["jira.username"] as String,
                    jiraPassword = configuration["jira.password"] as String,
                    jSessionId = configuration["jsessionid"] as String,
                    token = configuration["atlassian.xsrf.token"] as String
            )
        }
    }
}