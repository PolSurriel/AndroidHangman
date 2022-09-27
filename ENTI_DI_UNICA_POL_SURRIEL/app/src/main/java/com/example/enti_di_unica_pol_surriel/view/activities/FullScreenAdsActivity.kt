package com.example.enti_di_unica_pol_surriel.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.enti_di_unica_pol_surriel.databinding.ActivityFullScreenAdsBinding
import com.example.enti_di_unica_pol_surriel.view.dialogs.LoadingDialog
import com.example.enti_di_unica_pol_surriel.viewmodel.registerAnalyticsLevelShowAd
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

/**
 * Loads and shows a full screen ad.
 *
 * Finishes the activity once the Ad is closed.
 *
 * WARNING: The activity do not preload any ad so it will take some time to download it.
 * --> This is a UX designed intentional decision. <--
 * We want the user to wait a bit for watching the add to increase their focus.
 * */
class FullScreenAdsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullScreenAdsBinding

    companion object{
        private const val AD_ID = "ca-app-pub-3940256099942544/1033173712"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFullScreenAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        loadAndShowAd()
    }

    /**
     * Loads and shows the Ad in full screen.
     *
     * Finishes the activity once the Ad is closed.
     * */
    private fun loadAndShowAd() {

        val loadingDialog = LoadingDialog(this)
        loadingDialog.show()

        val activity = this

        val callback : InterstitialAdLoadCallback = object : InterstitialAdLoadCallback(){
            override fun onAdFailedToLoad(p0: LoadAdError) {
                // Errors do not have translation
                Toast.makeText(activity, "Fail loading ad", Toast.LENGTH_LONG).show()
                activity.finish()
            }

            override fun onAdLoaded(ad: InterstitialAd) {
                loadingDialog.dismiss()
                ad.show(activity)
                registerAnalyticsLevelShowAd(applicationContext)
                finish()
            }
        }

        InterstitialAd.load(
            this,
            AD_ID,
            AdRequest.Builder().build(),
            callback
        )

    }

}