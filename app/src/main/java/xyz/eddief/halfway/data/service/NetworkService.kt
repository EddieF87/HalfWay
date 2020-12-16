package xyz.eddief.halfway.data.service

import android.app.Application
import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import xyz.eddief.halfway.utils.DEBUG_TAG
import xyz.eddief.halfway.utils.SignatureManager
import java.util.concurrent.TimeUnit


class NetworkService(application: Application) {

    private val builder: OkHttpClient.Builder = OkHttpClient().newBuilder().also {
        val signature =
            SignatureManager.getSignature(application.packageManager, application.packageName) ?: ""
        it.readTimeout(10, TimeUnit.SECONDS)
        it.connectTimeout(5, TimeUnit.SECONDS)
        it.addInterceptor(Interceptor { chain: Interceptor.Chain ->
            val request: Request = chain.request().newBuilder()
                .addHeader("X-Android-Package", application.packageName)
                .addHeader("X-Android-Cert", signature)
                .build()

            Log.d(DEBUG_TAG, "request = $request")
            chain.proceed(request)
        })
    }

    private val client = builder.build()

    private var retrofit = Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com/maps/api/distancematrix/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    var mapsService: MapsService = retrofit.create(MapsService::class.java)

}