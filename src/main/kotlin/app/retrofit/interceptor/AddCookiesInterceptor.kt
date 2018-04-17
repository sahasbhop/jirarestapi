package app.retrofit.interceptor

import app.retrofit.util.Cookies
import okhttp3.Interceptor
import okhttp3.Response

/* credit: https://gist.github.com/tsuharesu/cbfd8f02d46498b01f1b */
class AddCookiesInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        val cookies = Cookies.values

        for (cookie in cookies) {
            builder.addHeader("Cookie", cookie)
            println("Adding cookie: $cookie")
        }

        return chain.proceed(builder.build())
    }
}