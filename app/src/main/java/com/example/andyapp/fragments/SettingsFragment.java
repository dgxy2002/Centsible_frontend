package com.example.andyapp.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.andyapp.DataObserver;
import com.example.andyapp.DataSubject;
import com.example.andyapp.LoginActivity;
import com.example.andyapp.R;
import com.example.andyapp.models.UserSettings;
import com.example.andyapp.queries.SettingsService;
import com.example.andyapp.utils.ConvertBitmapToFile;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    private TextInputEditText firstNameEditText;
    private TextInputEditText lastNameEditText;
    private TextInputEditText birthdayEditText;
    private TextInputEditText bioEditText;

    private Button btnApply;
    private final String TAG = "LOGCAT";
    private ImageView profilePicImageView;
    private ImageView editImageView;
    private DataSubject<UserSettings> subject;
    UserSettings userSettings;
    SharedPreferences sharedPreferences;
    String username;
    String date;
    private SettingsService settingsService;
    boolean isPictureUpdated = false;

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null){
                Intent data = result.getData();
                if (data.getData() != null){
                    Uri imageURI = data.getData(); //If you are using Gallery
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageURI);
                        profilePicImageView.setImageBitmap(bitmap);
                        isPictureUpdated = true;
                    } catch (IOException e) {
                        Log.d(TAG, e.toString());
                    }
                }else if (data.getExtras() != null){
                    //If you are using the camera
                    isPictureUpdated = true;
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    profilePicImageView.setImageBitmap(photo);
                }
            }
        }
    });


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //SharedPreferences permissions
        sharedPreferences = requireContext().getSharedPreferences(LoginActivity.PREFTAG, Context.MODE_PRIVATE);
        username = sharedPreferences.getString(LoginActivity.USERNAMEKEY, LoginActivity.DEFAULT_USERNAME);
        firstNameEditText = view.findViewById(R.id.firstNameEditText);
        lastNameEditText = view.findViewById(R.id.lastNameEditText);
        birthdayEditText = view.findViewById(R.id.birthdayEditText);
        bioEditText = view.findViewById(R.id.bioEditText);
        profilePicImageView = view.findViewById(R.id.settingsImageView);
        editImageView = view.findViewById(R.id.settingsEditImageView);
        btnApply = view.findViewById(R.id.settingsBtnApply);
        settingsService = new SettingsService(requireContext());
        subject = new DataSubject<>();
        subject.registerObserver(new ViewObserver());
        getSettings(username);

        profilePicImageView.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View view) {
                //TODO Make profile picture dynamically change
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Gallery Intent
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                // Combine into a chooser
                Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{ cameraIntent });

                // Launch the chooser
                launcher.launch(chooserIntent);
            }
        });

//        editImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //TODO Same as above profile picture
//            }
//        });

        birthdayEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, String.valueOf(isPictureUpdated));
                if (isPictureUpdated) {
                    Bitmap bitMap = ((BitmapDrawable) profilePicImageView.getDrawable()).getBitmap();
                    try {
                        File file = ConvertBitmapToFile.convertToFile(requireContext(), bitMap, "profile_pic.jpg");
                        RequestBody reqFile = RequestBody.create(file, MediaType.get("image/jpeg"));
                        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), reqFile);
                        settingsService.uploadProfilePicture(username, filePart);
                    } catch (IOException e) {
                        Log.d(TAG, e.toString());
                    }
                }
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                String birthday = birthdayEditText.getText().toString();
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                LocalDate birthdayLocalDate = LocalDate.parse(birthday, formatter);
                String biography = bioEditText.getText().toString();
                Map<String, Object> settings = new HashMap<>();
                if(!firstName.isEmpty()){
                    settings.put("firstname", firstName);
                }
                if(!lastName.isEmpty()){
                    settings.put("lastname", lastName);
                }
                if(!birthday.isEmpty()){
                    settings.put("birthdate", birthday);
                }
                if(!biography.isEmpty()){
                    settings.put("biography", biography);
                }
                Log.d(TAG, firstName + lastName + birthday + biography);
                settingsService.uploadSettings(username, settings);
            }
        });
        //TODO make btnBarRight become the apply changes button
    }

    public void getSettings(String username){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Looper looper = Looper.getMainLooper();
        Handler handler = new Handler(looper);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                settingsService.getSettings(username, handler, subject);
            }
        });
    }
    private void openDialog(){
        DatePickerDialog dialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                date = String.format("%04d-%02d-%02d", year, month + 1, day);
                birthdayEditText.setText(date);
            }
        },2025, 0, 0);
        dialog.show();
    }
    class ViewObserver implements DataObserver<UserSettings> {
        @Override
        public void updateData(UserSettings data) {
            firstNameEditText.setText(data.getFirstname());
            lastNameEditText.setText(data.getLastname());
            if(data.getBirthdate() != null){
                birthdayEditText.setText(data.getBirthdate());
            }
            String imageURL = data.getImageUrl();
            Glide.with(requireContext())
                    .load(imageURL)
                    .into(profilePicImageView);
            bioEditText.setText(data.getBiography());
        }
    }
}