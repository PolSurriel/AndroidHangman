package com.example.enti_di_unica_pol_surriel.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import com.example.enti_di_unica_pol_surriel.App
import com.example.enti_di_unica_pol_surriel.R
import com.example.enti_di_unica_pol_surriel.databinding.ActivityMainBinding
import com.example.enti_di_unica_pol_surriel.viewmodel.AuthViewModel

/**
 * Handles the main fragments navigation.
 * Loads the user data.
 * */
class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    private val authViewModel: AuthViewModel by viewModels()

    /**
     * The navigation tree expressed with a map
     *
     * Structure:
     *
     * Map<[clicked button ID], Map<[Current fragment ID], [Transition ID]>  >
     *
     *                  For each destination association:
     *
     *                              Destination
     *                   (represented by clicked button ID)
     *                __________________|__________________
     *               |                  |                 |
     *        InFragmentA         InFragmentB          InFragmentC
     *      (represented by     (represented by     (represented by
     *     CurrentFragmentID)   CurrentFragmentID)  CurrentFragmentID)
     *            =                    =                     =
     *     InFragmentA to        InFragmentB to       InFragmentB to
     *       Destination          Destination          Destination
     *       transitionID        transitionID          transitionID
     *
     *
     * */
    private val navigationTransitionGraph: Map<Int, Map<Int, Int>> = mapOf(
        R.id.HomeBottomButton to mapOf(
            R.id.scoreFragment to R.id.action_scoreFragment_to_homeFragment,
            R.id.settingsFragment to R.id.action_settingsFragment_to_homeFragment
        ),
        R.id.SettingsBottomButton to mapOf(
            R.id.homeFragment to R.id.action_homeFragment_to_settingsFragment,
            R.id.scoreFragment to R.id.action_scoreFragment_to_settingsFragment
        ),
        R.id.ScoreBottomButton to mapOf(
            R.id.settingsFragment to R.id.action_settingsFragment_to_scoreFragment,
            R.id.homeFragment to R.id.action_homeFragment_to_scoreFragment
        )
    )

    /**
     * Association between the navigation item clicked and the corresponding fragment.
     * */
    private val buttonToFragmentAssociation = mapOf(
        R.id.HomeBottomButton to R.id.homeFragment,
        R.id.SettingsBottomButton to R.id.settingsFragment,
        R.id.ScoreBottomButton to R.id.scoreFragment
    )

    // -------------- ACTIVITY LIFECYCLE METHODS --------------------

    override fun onCreate(savedInstanceState: Bundle?) {

        setupAuth()

        setTheme(R.style.Theme_ENTI_DI_UNICA_POL_SURRIEL)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
        updateTabName(R.id.homeFragment)

        App.notifiactionsActive.observe(this) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
            sharedPreferences.edit().putBoolean(
                App.NOTIFICATIONS_ALLOWED_PREF_ID,
                App.notifiactionsActive.value!!
            ).apply()

        }

    }

    // -------------- METHODS --------------------

    /**
     * Navigates to GameActivity
     * */
    fun startPlay(){
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }

    /**
     * Checks for previous logins and loads the user information
     * */
    private fun setupAuth() {
        if(!authViewModel.logged()){
            authViewModel.anonLogin()
        }else {
            authViewModel.loadUserSettings()
        }
    }

    /**
     * Setup of the bottom nav view click events.
     * */
    private fun setupNavigation(){
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView2) as NavHostFragment

        val navController = navHostFragment.navController

        binding.bottomNavigationView.setOnItemSelectedListener {
            val currentFragmentID = navController.currentDestination!!.id
            val transitionIDs = navigationTransitionGraph[it.itemId]!!

            if(transitionIDs.containsKey(currentFragmentID)){
                navController.navigate(transitionIDs[currentFragmentID]!!)
            }
            updateTabName(buttonToFragmentAssociation[it.itemId]!!)
            true
        }
    }

    /**
     * On each navigation event changes the activity's title
     * */
    private fun updateTabName(currentScreenID:Int){
        when(currentScreenID){
            R.id.homeFragment-> title = resources.getText(R.string.home_tab_name)
            R.id.scoreFragment-> title = resources.getText(R.string.score_tab_name)
            R.id.settingsFragment-> title = resources.getText(R.string.settings_tab_name)
        }

    }

}