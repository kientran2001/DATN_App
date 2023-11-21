package com.example.datnapp;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import com.example.datnapp.databinding.ActivityMainBinding;
import com.example.datnapp.fragment.HomeFragment;
import com.example.datnapp.fragment.YouFragment;
import com.example.datnapp.model.User;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;
    User staff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        Bundle bundleReceive = getIntent().getExtras();
        if (bundleReceive != null) {
            staff = (User) bundleReceive.get("obj_staff");
//            Log.e("staff", staff.toString());
        }

//        YouFragment youFragment = new YouFragment();
//        youFragment.setArguments(bundleReceive);
//        replaceFragment(youFragment);
//        activityMainBinding.bottomNav.getMenu().findItem(R.id.you).setChecked(true);

        YouFragment youFragment = new YouFragment();
        youFragment.setArguments(bundleReceive);
        replaceFragment(youFragment);
        activityMainBinding.bottomNav.getMenu().findItem(R.id.you).setChecked(true);

        activityMainBinding.bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                HomeFragment homeFragment = new HomeFragment();
                homeFragment.setArguments(bundleReceive);
                replaceFragment(homeFragment);
            } else if (item.getItemId() == R.id.you) {
                YouFragment youFragment1 = new YouFragment();
                youFragment1.setArguments(bundleReceive);
//                Log.e("bundle", "receive bundle");
                replaceFragment(youFragment1);
            } else if (item.getItemId() == R.id.log_out) {
                // Đăng xuất
                AlertDialog.Builder myDialog = new AlertDialog.Builder(MainActivity.this);
                myDialog.setTitle("Đăng xuất");
                myDialog.setMessage("Bạn có muốn đăng xuất?");
                myDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });
                myDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                myDialog.create().show();
            }
            return true;
        });

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            protected long backPressTime;
            @Override
            public void handleOnBackPressed() {
                if (backPressTime + 2000 > System.currentTimeMillis()) {
                    MainActivity.super.getOnBackPressedDispatcher();
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Nhấn một lần nữa để thoát", Toast.LENGTH_SHORT).show();
                }
                backPressTime = System.currentTimeMillis();
            }
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}