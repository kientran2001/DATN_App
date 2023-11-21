package com.example.datnapp.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.datnapp.R;
import com.example.datnapp.databinding.FragmentYouBinding;
import com.example.datnapp.model.User;

public class YouFragment extends Fragment {
    private FragmentYouBinding fragmentYouBinding;
    User staff;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentYouBinding = FragmentYouBinding.inflate(inflater, container, false);

        initUI();

        return fragmentYouBinding.getRoot();
    }

    private void initUI() {
        Bundle youBundle = getArguments();
        staff = (User) youBundle.get("obj_staff");
        if (staff != null) {
            fragmentYouBinding.tvName.setText(staff.getName());
            fragmentYouBinding.tvPhoneNumber.setText(staff.getPhoneNumber().toString());
            fragmentYouBinding.tvEmail.setText(staff.getEmail());
            fragmentYouBinding.tvRole.setText(String.valueOf(staff.getRole()));
        }
    }
}