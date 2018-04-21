package app.retrofit

import app.ApplicationContext
import app.jira.util.basicAuthenticationInterceptor
import app.retrofit.interceptor.AddCookiesInterceptor
import app.retrofit.interceptor.ReceivedCookiesInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import okhttp3.ConnectionPool



inline fun <reified T> ApplicationContext.service(): T {
    val httpLoggingInterceptor = HttpLoggingInterceptor({ message ->
        println(message)
    }).apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    // credit - https://allegro.tech/2016/04/meet-retrofit2.html
    val connectionPool = ConnectionPool(10, 60, TimeUnit.SECONDS)

    val client = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(AddCookiesInterceptor())
            .addInterceptor(ReceivedCookiesInterceptor())
            .addInterceptor(basicAuthenticationInterceptor(this))
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .connectionPool(connectionPool)
            .retryOnConnectionFailure(true)
            .build()

    val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    return retrofit.create(T::class.java)
}
