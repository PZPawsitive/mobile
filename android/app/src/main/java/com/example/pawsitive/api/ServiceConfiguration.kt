package com.example.pawsitive.api

import android.content.Context
import android.content.SharedPreferences
import com.example.pawsitive.util.LocalDateJsonAdapter
import com.example.pawsitive.util.PreferencesManager
import com.example.pawsitive.util.UUIDJsonAdapter
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RetrofitClient(context: Context) {
    private val preferencesManager = PreferencesManager(context)

    private val authInterceptor = AuthInterceptor(preferencesManager)

    private val moshi = Moshi.Builder()
        .add(UUIDJsonAdapter())
        .add(LocalDateJsonAdapter())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://localhost:8080/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
}

class AuthInterceptor(private val preferencesManager: PreferencesManager): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        preferencesManager.getToken()?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }
        return chain.proceed(requestBuilder.build())
    }

//    okHttpBuilder.addInterceptor { chain ->
//        val requestWithUserAgent = chain.request().newBuilder()
//            .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJleGFtcGxlQGV4YW1wbGUuY29tIiwiaWF0IjoxNzE2MDI1OTEzLCJleHAiOjE3MTYwMjk1MTN9.nmsB1r5gmH1FJVQEmL2PZTmmXbUI4hlYf6qG3sG0D1Q")
//            .build()
//        chain.proceed(requestWithUserAgent)
//    }
//    return okHttpBuilder.build()
}