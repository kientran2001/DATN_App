package com.example.datnapp.fragment;

import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatCallback;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.datnapp.ApiService;
import com.example.datnapp.CaptureAct;
import com.example.datnapp.MainActivity;
import com.example.datnapp.databinding.FragmentHomeBinding;
import com.example.datnapp.model.Record;
import com.example.datnapp.model.ScanData;
import com.example.datnapp.model.User;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.github.dhaval2404.imagepicker.ImagePickerActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import kotlin.Unit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding fragmentHomeBinding;
    ScanData scanData;
    Uri imageUri;
    TextRecognizer textRecognizer;

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
                            fragmentHomeBinding.edtCurrentValue.setText("");
                        } else {
                            Toast.makeText(getActivity(), "Chưa có dữ liệu", Toast.LENGTH_SHORT).show();
                            fragmentHomeBinding.edtLastValue.setText("");
                            fragmentHomeBinding.edtCurrentValue.setText("");
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
                    if (scanData == null) {
                        Toast.makeText(getActivity(), "Chưa có dữ liệu đồng hồ!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String waterMeterId = scanData.getWaterMeterId();
                    String lastValueStr = fragmentHomeBinding.edtLastValue.getText().toString().trim();
                    String valueStr = fragmentHomeBinding.edtCurrentValue.getText().toString().trim();
                    if (lastValueStr.length() == 0 || valueStr.length() == 0) {
                        Toast.makeText(getActivity(), "Chưa nhập số liệu đồng hồ!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Double lastValue = Double.parseDouble(lastValueStr);
                    Double value = Double.parseDouble(valueStr);
                    if (lastValue > value) {
                        fragmentHomeBinding.edtCurrentValue.setTextColor(Color.RED);
                        Toast.makeText(getActivity(), "Dữ liệu không hợp lệ!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Bundle bundle1 = getArguments();
                    User staff = (User) bundle1.get("obj_staff");
                    String recorderName;
                    String recorderPhone;
                    if (staff != null) {
                        recorderName = staff.getName();
                        recorderPhone = staff.getPhoneNumber();
                    } else {
                        Toast.makeText(getActivity(), "Chưa có thông tin người ghi nhận!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    dateFormat.setTimeZone(TimeZone.getDefault());
                    String strDate = fragmentHomeBinding.edtRecordDate.getText().toString();
                    if (strDate.length() == 0) {
                        Toast.makeText(getActivity(), "Vui lòng chọn ngày ghi nhận!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Date date = dateFormat.parse(strDate);
                    String img = "test_img_record";
                    Record record = new Record(waterMeterId, value, date, recorderName, recorderPhone, img);
                    Log.e("Record", record.toString());
                    ApiService.apiService.addRecord(record).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getActivity(), "Ghi dữ liệu thành công!", Toast.LENGTH_LONG).show();
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
            Log.e("Scan", "Scan: " + result.getContents());

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