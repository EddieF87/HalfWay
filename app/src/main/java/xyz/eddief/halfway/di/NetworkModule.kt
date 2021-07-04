package xyz.eddief.halfway.di

import android.app.Application
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import xyz.eddief.halfway.BuildConfig
import xyz.eddief.halfway.data.service.MapsService
import xyz.eddief.halfway.utils.SignatureManager
import xyz.eddief.halfway.utils.dLog
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideMapsService(application: Application): MapsService {
        val builder: OkHttpClient.Builder = OkHttpClient().newBuilder()
            .addInterceptor(ChuckerInterceptor(application))
            .also {
            val signature =
                SignatureManager.getSignature(application.packageManager, application.packageName) ?: ""
            it.readTimeout(10, TimeUnit.SECONDS)
            it.connectTimeout(5, TimeUnit.SECONDS)
            it.addInterceptor(Interceptor { chain: Interceptor.Chain ->
                val request: Request = chain.request().newBuilder()
                    .addHeader("X-Android-Package", application.packageName)
                    .addHeader("X-Android-Cert", signature)
                    .build()
                val response = chain.proceed(request)
                if(BuildConfig.DEBUG) {
                    dLog("request = $request")
                    dLog("response = ${response.peekBody(PEEK_BODY_COUNT).string()}")
                }
                response
            })
        }

        val client = builder.build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(MapsService::class.java)
    }

    private const val PEEK_BODY_COUNT: Long = 99999

}