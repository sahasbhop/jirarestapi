package app.jira.util

import app.ApplicationContext
import app.retrofit.interceptor.BasicAuthenticationInterceptor
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

fun basicAuthenticationInterceptor(context: ApplicationContext) = BasicAuthenticationInterceptor(context.username, context.password)

fun String.jiraDateTime(): OffsetDateTime = OffsetDateTime.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"))