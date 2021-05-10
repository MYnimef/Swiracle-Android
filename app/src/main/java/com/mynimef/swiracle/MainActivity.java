package com.mynimef.swiracle;

import android.content.pm.PackageManager;
import android.os.Bundle;

import com.mynimef.swiracle.fragments.NavigationFragment;
import com.mynimef.swiracle.fragments.login.LoginFragment;
import com.mynimef.swiracle.logic.FragmentChanger;
import com.mynimef.swiracle.logic.Repository;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        if (Repository.getInstance().isLoggedIn()) {
            FragmentChanger.replaceFragment(fm, R.id.mainFragment, new NavigationFragment());
        } else {
            FragmentChanger.replaceFragment(fm, R.id.mainFragment, new LoginFragment());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Repository.getInstance().initGallery();
            } else {
                // Explain to the user that the feature is unavailable because
                // the features requires a permission that the user has denied.
                // At the same time, respect the user's decision. Don't link to
                // system settings in an effort to convince the user to change
                // their decision.
            }
        }
    }
}