package com.example.datnapp;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import com.example.datnapp.databinding.ActivityLoginBinding;
import com.example.datnapp.databinding.ActivityMainBinding;
import com.example.datnapp.databinding.FragmentHomeBinding;
import com.example.datnapp.fragment.HomeFragment;
import com.example.datnapp.fragment.YouFragment;
import com.example.datnapp.model.User;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;
    User staff;
    Uri imageUri;
    TextRecognizer textRecognizer;

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
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        YouFragment youFragment = new YouFragment();
        youFragment.setArguments(bundleReceive);
        replaceFragment(youFragment);
        activityMainBinding.bottomNav.getMenu().findItem(R.id.you).setChecked(true);

        activityMainBinding.bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                HomeFragment homeFragment = new HomeFragment();
                homeFragment.setArguments(bundleReceive);
                replaceFragment(homeFragment);
            } else if (item.getItemId() == R.id.capture) {
                ImagePicker.with(MainActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(20);
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
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    imageUri = data.getData();
                    Toast.makeText(MainActivity.this, "lấy ảnh thành công", Toast.LENGTH_SHORT).show();
                    activityMainBinding.imgCapture.setImageURI(imageUri);
                    recognizeText();
                }
            } else {
                Toast.makeText(MainActivity.this, "chưa lấy ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void recognizeText() {
        if (imageUri != null) {
            try {
                InputImage inputImage = InputImage.fromFilePath(MainActivity.this, imageUri);
                Task<Text> result = textRecognizer.process(inputImage)
                        .addOnSuccessListener(new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text text) {
                                String recognizeText = text.getText();
                                Toast.makeText(MainActivity.this, recognizeText, Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}