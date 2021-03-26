package kz.application.stocks

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kz.application.stocks.data.network.interceptors.DefaultInterceptor
import kz.application.stocks.data.remote.FinnhubApi
import kz.application.stocks.data.repository.FiinhubRepository
import kz.application.stocks.presentation.constants.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        okHttpClient = createRetrofitOkHttpClient()
        finnhubApi = createWebService(okHttpClient)
        finnhubRepository = FiinhubRepository(finnhubApi)
    }

    private fun createRetrofitOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpBuilder = OkHttpClient.Builder()
            .addNetworkInterceptor(DefaultInterceptor())
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(Constants.READ_TIMEOUT, TimeUnit.MILLISECONDS)
        return okHttpBuilder.build()
    }

    private inline fun <reified T> createWebService(
        okHttpClient: OkHttpClient,
        baseUrl: String = Constants.BASE_URL
    ): T = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(createGson()))
        .build()
        .create(T::class.java)

    private fun createGson(): Gson = GsonBuilder().setLenient().create()

    companion object {
        lateinit var okHttpClient: OkHttpClient
        lateinit var finnhubApi: FinnhubApi
        lateinit var finnhubRepository: FiinhubRepository
    }

}