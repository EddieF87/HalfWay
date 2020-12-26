package xyz.eddief.halfway.ui.landing

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_landing.*
import xyz.eddief.halfway.R
import xyz.eddief.halfway.ui.main.MainActivity
import xyz.eddief.halfway.utils.dLog

@AndroidEntryPoint
class LandingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        landingSignIn.setOnClickListener { launchSignInFlow() }
    }

    public override fun onStart() {
        super.onStart()
        goToMainPage()
        return
        setLoading(true)
        val currentUser = FirebaseAuth.getInstance().currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            goToMainPage()
        } else {
            setLoading(false)
        }
    }

    private fun goToMainPage() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun setLoading(inProgress: Boolean) {
        landingLoader.isVisible = inProgress
        landingSignIn.isEnabled = !inProgress
        landingSignIn.isClickable = !inProgress
    }

    private fun launchSignInFlow() {

        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            SIGN_IN_RESULT_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_RESULT_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                dLog("Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}!")
            } else {
                dLog("Sign in unsuccessful ${response?.error?.errorCode}")
            }
        }
    }

    companion object {
        const val SIGN_IN_RESULT_CODE = 4932
    }
}