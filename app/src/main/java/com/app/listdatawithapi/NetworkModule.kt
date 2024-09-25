package com.app.listdatawithapi


import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val TAG = "ApiClient"
    private val NO_OF_LOG_CHAR = 1000

    @Provides
    @Singleton
    @Named("base")
    fun provideRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .client(getOkHttpClientBuilder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    
    @Provides
    @Singleton
    fun provideApiService(@Named("base")retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
    

    //Http builder
    private fun getOkHttpClientBuilder(): OkHttpClient.Builder {
        val oktHttpClientBuilder = OkHttpClient.Builder()
            .readTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES)
        oktHttpClientBuilder.addInterceptor(getHttpLoggingInterceptor())
        oktHttpClientBuilder.addInterceptor { chain ->
            val request = chain.request()
            printPostmanFormattedLog(request)
            val response = chain.proceed(request)
            response
        }

        return oktHttpClientBuilder
    }


    //this function is used to print the request and response body in logcat
    private fun printPostmanFormattedLog(request: Request) {
        try {
            val allParams: String = if (request.method == "GET" || request.method == "DELETE") {
                request.url.toString().substring(
                    request.url.toString().indexOf("?") + 1,
                    request.url.toString().length
                )
            } else {
                val buffer = Buffer()
                request.body!!.writeTo(buffer)
                buffer.readString(Charset.forName("UTF-8"))
            }
            val params =
                allParams.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val paramsString = StringBuilder("\n")
            for (param in params) {
                //paramsString.append(decode(param.replace("=", ":")))
                paramsString.append("\n")
            }
            Log.d(TAG, paramsString.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    

    private fun getHttpLoggingInterceptor(): Interceptor {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            if (message.length > NO_OF_LOG_CHAR) {
                for (noOfLogs in 0..message.length / NO_OF_LOG_CHAR) {
                    if (noOfLogs * NO_OF_LOG_CHAR + NO_OF_LOG_CHAR < message.length) {
                        Log.d(
                            TAG, message.substring(
                                noOfLogs * NO_OF_LOG_CHAR,
                                noOfLogs * NO_OF_LOG_CHAR + NO_OF_LOG_CHAR
                            )
                        )
                    } else {
                        Log.d(
                            TAG,
                            message.substring(noOfLogs * NO_OF_LOG_CHAR, message.length)
                        )
                    }
                }
            } else {
                Log.d(TAG, message)
            }
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }


}