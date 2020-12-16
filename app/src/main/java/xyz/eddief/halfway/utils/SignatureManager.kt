package xyz.eddief.halfway.utils

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.pm.Signature
import com.google.common.io.BaseEncoding
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object SignatureManager {

    fun getSignature(pm: PackageManager, packageName: String): String? = try {
        pm.getSignatures(packageName)?.takeIf { it.isNotEmpty() }?.let {
            signatureDigest(it[0])
        }
    } catch (e: PackageManager.NameNotFoundException) {
        null
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

    @SuppressLint("PackageManagerGetSignatures")
    private fun PackageManager.getSignatures(packageName: String): Array<Signature>? =
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            this.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNING_CERTIFICATES
            ).signingInfo.apkContentsSigners
        } else {
            this.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES
            ).signatures
        }
}