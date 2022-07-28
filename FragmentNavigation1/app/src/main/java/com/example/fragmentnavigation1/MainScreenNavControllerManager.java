package com.example.fragmentnavigation1;

import androidx.navigation.NavController;

public class MainScreenNavControllerManager {

    public static NavController navController;

    public enum Screen {
        MAIN,
        PROFILE,
        CONFIG
    }

    public static Screen currentScreen;

}
