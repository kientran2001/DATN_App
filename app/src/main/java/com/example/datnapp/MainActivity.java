package com.example.datnapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.datnapp.databinding.ActivityMainBinding;
import com.example.datnapp.fragment.HomeFragment;
import com.example.datnapp.fragment.YouFragment;
import com.example.datnapp.model.User;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        Bundle bundleReceive = getIntent().getExtras();
        if (bundleReceive != null) {
            User staff = (User) bundleReceive.get("obj_staff");
            if (staff != null) {
//                activityMainBinding.textView.setText(staff.toString());
            }
        }

        replaceFragment(new HomeFragment());
        activityMainBinding.bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.you) {
                replaceFragment(new YouFragment());
            } else if (item.getItemId() == R.id.log_out) {
                // Đăng xuất
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}