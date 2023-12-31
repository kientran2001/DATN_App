package com.example.datnapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.datnapp.databinding.ActivityLoginBinding;
import com.example.datnapp.model.LoginRequest;
import com.example.datnapp.model.User;

import java.math.BigInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding activityLoginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(activityLoginBinding.getRoot());

        activityLoginBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLogin();
            }
        });
    }

    private void clickLogin() {
        String phoneNumber = activityLoginBinding.edtPhoneNumber.getText().toString().trim();
        if (phoneNumber.length() == 0) {
            Toast.makeText(LoginActivity.this, "Nhập SĐT để đăng nhập!", Toast.LENGTH_SHORT).show();
            return;
        }
        String password = activityLoginBinding.edtPassword.getText().toString().trim();
        if (password.length() == 0) {
            Toast.makeText(LoginActivity.this, "Nhập mật khẩu để đăng nhập!", Toast.LENGTH_SHORT).show();
            return;
        }
        LoginRequest loginRequest = new LoginRequest(phoneNumber, password);
        Log.e("loginRequest", loginRequest.toString());
        ApiService.apiService.loginApp(loginRequest).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User staff = response.body();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("obj_staff", staff);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Số điện thoại hoặc mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("throwable", t.toString());
                Toast.makeText(LoginActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}