package com.example.githubusersearch.data.apis

import com.example.githubusersearch.data.apiEndPoints.ApiEndPoints
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class RetrofitInstance {
    companion object {
        private var CONNECTION_TIMEOUT = 60L
        private var READ_TIMEOUT = 60L
        private var WRITE_TIMEOUT = 60L
        private var CALL_TIMEOUT = 60L

        private val retrofitInstance by lazy {
            val login = HttpLoggingInterceptor()
            login.setLevel(HttpLoggingInterceptor.Level.BODY)

            val gsonNullable = GsonBuilder().setLenient().serializeNulls().create()

            val client = OkHttpClient.Builder()
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .callTimeout(CALL_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(login)
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("Accept", "application/vnd.github+json")
                        .addHeader("Authorization", "Bearer ${ApiEndPoints.TOKEN}")
                        .addHeader("X-GitHub-Api-Version", "2022-11-28")
                        .build()
                    chain.proceed(request)
                }
                .build()

            Retrofit.Builder()
                .baseUrl(ApiEndPoints.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gsonNullable))
                .client(client)
                .build()
        }

        val commonApiServices by lazy {
            retrofitInstance.create(Apis::class.java)
        }
    }
}