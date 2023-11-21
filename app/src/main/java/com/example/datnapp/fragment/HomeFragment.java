package com.example.datnapp.fragment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.datnapp.ApiService;
import com.example.datnapp.CaptureAct;
import com.example.datnapp.LoginActivity;
import com.example.datnapp.MainActivity;
import com.example.datnapp.R;
import com.example.datnapp.databinding.FragmentHomeBinding;
import com.example.datnapp.model.Record;
import com.example.datnapp.model.ScanData;
import com.example.datnapp.model.User;
import com.google.gson.Gson;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding fragmentHomeBinding;
    ScanData scanData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);

        fragmentHomeBinding.btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
            }
        });

        fragmentHomeBinding.edtRecordDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                fragmentHomeBinding.edtRecordDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });

        fragmentHomeBinding.btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String waterMeterId = fragmentHomeBinding.edtWaterMeterId.getText().toString().trim();
                ApiService.apiService.recentRecord(waterMeterId).enqueue(new Callback<Record>() {
                    @Override
                    public void onResponse(Call<Record> call, Response<Record> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getActivity(), "Lấy dữ liệu thành công!", Toast.LENGTH_SHORT).show();
                            Record record = response.body();
                            fragmentHomeBinding.edtLastValue.setText(record.getValue().toString());
                        } else {
                            Toast.makeText(getActivity(), "Chưa có dữ liệu", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Record> call, Throwable t) {
                        Log.e("throwable", t.toString());
                        Toast.makeText(getActivity(), "Lấy dữ liệu thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        fragmentHomeBinding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String waterMeterId = scanData.getWaterMeterId();
                    Double value = Double.parseDouble(fragmentHomeBinding.edtCurrentValue.getText().toString().trim());
                    Bundle bundle1 = getArguments();
                    User staff = (User) bundle1.get("obj_staff");
                    BigInteger recorder;
                    if (staff != null) {
                        recorder = staff.getPhoneNumber();
                    } else {
                        Toast.makeText(getActivity(), "Chưa có thông tin người ghi nhận!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    dateFormat.setTimeZone(TimeZone.getDefault());
                    Date date = dateFormat.parse(fragmentHomeBinding.edtRecordDate.getText().toString());
                    Record record = new Record(waterMeterId, value, date, recorder);
                    Log.e("Record", record.toString());
                    ApiService.apiService.addRecord(record).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getActivity(), "Ghi dữ liệu thành công!", Toast.LENGTH_LONG).show();
                                fragmentHomeBinding.edtLastValue.setText("");
                                fragmentHomeBinding.edtCurrentValue.setText("");
                            } else {
                                Toast.makeText(getActivity(), String.valueOf(response.body()), Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e("throwable", t.toString());
                            Toast.makeText(getActivity(), "Ghi dữ liệu thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return fragmentHomeBinding.getRoot();
    }

    public ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (isAdded() && result.getContents() != null) {
            Log.d("Scan", "Scan: " + result.getContents());

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Thông tin đồng hồ");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();

            try {
                JSONObject obj = new JSONObject(result.getContents());
                Gson gson = new Gson();
                scanData = gson.fromJson(String.valueOf(obj), ScanData.class);

                fragmentHomeBinding.edtWaterMeterId.setText(scanData.getWaterMeterId());
                fragmentHomeBinding.edtUsername.setText(scanData.getName());
                fragmentHomeBinding.edtPhoneNumber.setText(scanData.getPhoneNumber().toString());
                fragmentHomeBinding.edtBuilding.setText(scanData.getBuilding());
                fragmentHomeBinding.edtHomeCode.setText(scanData.getHomeCode());
                fragmentHomeBinding.edtAddress.setText(scanData.getAddress());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    });

    private void scanCode() {
        if (getActivity() != null) {
            ScanOptions options = new ScanOptions();
            options.setPrompt("Volume up to flash on");
            options.setBeepEnabled(true);
            options.setOrientationLocked(true);
            options.setCaptureActivity(CaptureAct.class);
            barLauncher.launch(options);
        }
    }
}