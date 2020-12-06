package xyz.eddief.halfway.data.service

import android.app.Application
import android.content.pm.PackageManager
import android.content.pm.Signature
import com.google.common.io.BaseEncoding
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.concurrent.TimeUnit


class NetworkService(application: Application) {

    private fun getSignature(pm: PackageManager, packageName: String): String? {
        return try {
            val packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            if (packageInfo?.signatures == null || packageInfo.signatures.isEmpty() || packageInfo.signatures[0] == null
            ) {
                null
            } else signatureDigest(packageInfo.signatures[0])
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }

    private fun signatureDigest(sig: Signature): String? {
        val signature: ByteArray = sig.toByteArray()
        return try {
            val md: MessageDigest = MessageDigest.getInstance("SHA1")
            val digest: ByteArray = md.digest(signature)

            BaseEncoding.base16().lowerCase().encode(digest)
        } catch (e: NoSuchAlgorithmException) {
            null
        }
    }

    private val builder: OkHttpClient.Builder = OkHttpClient().newBuilder().also {
        it.readTimeout(10, TimeUnit.SECONDS)
        it.connectTimeout(5, TimeUnit.SECONDS)
        val signature = getSignature(application.packageManager, application.packageName) ?: ""
        it.addInterceptor(Interceptor { chain: Interceptor.Chain ->
            val request: Request = chain.request().newBuilder()
                .addHeader("X-Android-Package", application.packageName)
                .addHeader("X-Android-Cert", signature)
                .build()

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