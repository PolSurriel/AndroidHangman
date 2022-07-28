package com.example.fragmentnavigation1;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fragmentnavigation1.databinding.FragmentBottomMenuBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BottomMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BottomMenuFragment extends Fragment {

    FragmentBottomMenuBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BottomMenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BottomMenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BottomMenuFragment newInstance(String param1, String param2) {
        BottomMenuFragment fragment = new BottomMenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentBottomMenuBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.configbm.setOnClickListener(toConfigButtonClickEvent);
        binding.mainbm.setOnClickListener(toMainButtonClickEvent);
        binding.profilebm.setOnClickListener(toProfileButtonClickEvent);
    }

    View.OnClickListener toMainButtonClickEvent = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View v) {

            //MainActivity.instance.startAnimation();
            if(MainScreenNavControllerManager.currentScreen == MainScreenNavControllerManager.Screen.PROFILE){
                MainScreenNavControllerManager.navController.navigate(R.id.action_profileFragment_to_mainFragment2);

            }else if(MainScreenNavControllerManager.currentScreen == MainScreenNavControllerManager.Screen.CONFIG){
                MainScreenNavControllerManager.navController.navigate(R.id.action_configFragment_to_mainFragment);
            }
        }
    };

    View.OnClickListener toConfigButtonClickEvent = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View v) {

            //MainActivity.instance.startAnimation();
            if(MainScreenNavControllerManager.currentScreen == MainScreenNavControllerManager.Screen.PROFILE){
                MainScreenNavControllerManager.navController.navigate(R.id.action_profileFragment_to_configFragment);

            }else if(MainScreenNavControllerManager.currentScreen == MainScreenNavControllerManager.Screen.MAIN){
                MainScreenNavControllerManager.navController.navigate(R.id.action_mainFragment_to_configFragment);
            }
        }
    };

    View.OnClickListener toProfileButtonClickEvent = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View v) {

            if(MainScreenNavControllerManager.currentScreen == MainScreenNavControllerManager.Screen.MAIN){
                MainScreenNavControllerManager.navController.navigate(R.id.action_mainFragment_to_profileFragment);

            }else if(MainScreenNavControllerManager.currentScreen == MainScreenNavControllerManager.Screen.CONFIG){
                MainScreenNavControllerManager.navController.navigate(R.id.action_configFragment_to_profileFragment);
            }
        }
    };

}