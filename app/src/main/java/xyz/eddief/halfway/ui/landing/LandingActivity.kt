package xyz.eddief.halfway.ui.landing

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
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

    private val landingViewModel: LandingViewModel by viewModels()

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        dLog("wwwwwwwwwwwwwwwwwwww onNewIntent  ${intent?.extras}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        val data = intent?.extras
        val latitude = data?.getString("latitude")?.toDouble()
        val longitude = data?.getString("longitude")?.toDouble()
        val title = data?.getString("title")
        val address = data?.getString("address")


        dLog("wwwwwwwwwwwwww  $latitude-$longitude  $address   $title")

        landingSignIn.setOnClickListener { launchSignInFlow() }
        landingViewModel.done.observe(this) {
            if (it == true) {
                goToMainPage()
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        dLog("wwwwwwwwwwwwwwwwwwww onStart  ${intent?.extras}")
        landingViewModel.setUserId("xe123")
        return
        setLoading(true)
        val currentUser = FirebaseAuth.getInstance().currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            landingViewModel.setUserId(currentUser.uid)
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
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val response = IdpResponse.fromResultIntent(result.data)
            if (result.resultCode == Activity.RESULT_OK) {
                dLog("Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}!")
            } else {
                dLog("Sign in unsuccessful ${response?.error?.errorCode}")
            }
        }.launch(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build()
        )
    }

    companion object {
        const val SIGN_IN_REQUEST_CODE = 4932
    }
}