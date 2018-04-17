package app.retrofit.util

import app.ApplicationContext
import app.retrofit.interceptor.AddCookiesInterceptor
import app.retrofit.interceptor.BasicAuthenticationInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

inline fun <reified T> ApplicationContext.service(): T {
    Cookies.values = cookies(this)

    val client = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor())
            .addInterceptor(AddCookiesInterceptor())
            .addInterceptor(basicAuthenticationInterceptor(this))
            .build()

    val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(jiraUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    return retrofit.create(T::class.java)
}

fun cookies(applicationContext: ApplicationContext) =
        applicationContext.let { it -> hashSetOf("JSESSIONID=${it.jSessionId}", "atlassian.xsrf.token=${it.token}") }

fun basicAuthenticationInterceptor(applicationContext: ApplicationContext) =
        BasicAuthenticationInterceptor(applicationContext.jiraUsername, applicationContext.jiraPassword)

fun httpLoggingInterceptor() = HttpLoggingInterceptor({ message ->
    println(message)
}).apply {
    level = HttpLoggingInterceptor.Level.BODY
}
