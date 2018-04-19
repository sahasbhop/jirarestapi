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

inline fun <reified T> ApplicationContext.service(): T {
    val httpLoggingInterceptor = HttpLoggingInterceptor({ message ->
        println(message)
    }).apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(AddCookiesInterceptor())
            .addInterceptor(ReceivedCookiesInterceptor())
            .addInterceptor(basicAuthenticationInterceptor(this))
            .build()

    val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    return retrofit.create(T::class.java)
}
