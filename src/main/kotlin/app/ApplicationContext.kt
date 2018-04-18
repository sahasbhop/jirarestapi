package app

import app.jira.util.basicAuthenticationInterceptor
import app.jira.util.cookies
import app.retrofit.interceptor.AddCookiesInterceptor
import app.retrofit.util.Cookies
import com.esotericsoftware.yamlbeans.YamlReader
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader

data class ApplicationContext(
        var jiraUrl: String,
        var jiraUsername: String,
        var jiraPassword: String,
        var jSessionId: String,
        var atlassianXsrfToken: String) {

    @Suppress("unused") // requested by YamlReader lib
    constructor() : this("", "", "", "", "")

    inline fun <reified T> service(): T {
        Cookies.values = cookies(this)

        val httpLoggingInterceptor = HttpLoggingInterceptor({ message ->
            println(message)
        }).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(AddCookiesInterceptor())
                .addInterceptor(basicAuthenticationInterceptor(this))
                .build()

        val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(jiraUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        return retrofit.create(T::class.java)
    }

    companion object {
        fun read(resourceFileName: String): ApplicationContext {
            val file = File("resources/$resourceFileName")
            if (!file.exists()) throw FileNotFoundException("$file does not exists!")
            return YamlReader(FileReader(file)).read(ApplicationContext::class.java)
        }
    }
}