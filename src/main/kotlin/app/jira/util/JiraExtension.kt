package app.jira.util

import app.ApplicationContext
import app.retrofit.interceptor.BasicAuthenticationInterceptor
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

fun cookies(applicationContext: ApplicationContext) =
        applicationContext.let { it -> hashSetOf("JSESSIONID=${it.jSessionId}", "atlassian.xsrf.atlassianXsrfToken=${it.atlassianXsrfToken}") }

fun basicAuthenticationInterceptor(applicationContext: ApplicationContext) =
        BasicAuthenticationInterceptor(applicationContext.jiraUsername, applicationContext.jiraPassword)

fun String.jiraDateTime(): OffsetDateTime = OffsetDateTime.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"))