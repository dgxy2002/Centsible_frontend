package com.example.andyapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.andyapp.queries.RetrofitClient;
import com.example.andyapp.queries.mongoModels.LoginModel;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private AppCompatButton btnLogin;
    private TextView errorTextView;
    private Button btnSignUp;
    public static final String USERKEY = "USERKEY";
    public static final String VIEWERKEY = "VIEWERKEY";
    public static final String VIEWERNAMEKEY = "VIEWERNAMEKEY";
    public static final String VIEWERIMAGEKEY = "VIEWERIMAGEKEY";
    public static final String TOKENKEY = "TOKENKEY";
    public static final String PREFTAG = "MYPREF";
    public static final String USERNAMEKEY = "USERNAMEKEY";
    public static final String DEFAULT_USERID = "67ecf4e07cb6ed67c0e7e67a";
    public static final String DEFAULT_USERNAME = "hugo";
    public static final String NOT_API_KEY = "227166733379629";
    public static final String NOT_API_SECRET = "gnZXu0awAPwBRPfrjNXCmyS5uo4";
    public static final String NOT_CLOUD_NAME = "dipmlrzfc";


    private String TAG = "LOGCAT";
    RequestUser requestUser;
    SharedPreferences sharedPreferences;

    class LoginResponse {
        private String id;
        private String username;
        private String token;
        private String message;

        public String getId() {
            return id;
        }
        public String getUsername() {
            return username;
        }
        public String getMessage() {
            return message;
        }
        public String getToken() {
            return token;
        }
    }

    interface RequestUser{
        @POST("users/login")
        Call<LoginResponse> getUser(@Body LoginModel login);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        usernameEditText = findViewById(R.id.loginUsernameTextView);
        passwordEditText = findViewById(R.id.loginPWTextView);
        btnLogin = findViewById(R.id.btnLogIn);
        btnSignUp = findViewById(R.id.loginBtnSignUp);
        errorTextView = findViewById(R.id.loginErrorTextView);
        //Create api object using the interface above
        requestUser = RetrofitClient.getRetrofit().create(RequestUser.class);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                LoginModel loginModel = new LoginModel(username, password);
                Log.d(TAG, String.format("username: %s, password: %s", loginModel.getUsername(), loginModel.getPassword()));
                requestUser.getUser(loginModel).enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if ( response.body() != null ) {
                            LoginResponse resp = response.body();
                            String userId = resp.getId();
                            String username = resp.getUsername();
                            String message = resp.getMessage();
                            String token = resp.getToken();
//                            String imageUrl = resp.getImageUrl();
                            sharedPreferences = getSharedPreferences(PREFTAG, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(TOKENKEY, "Bearer " +token);
                            editor.putString(USERKEY, userId);
                            editor.putString(VIEWERKEY, userId);
                            editor.putString(USERNAMEKEY, username);
//                            editor.putString(VIEWERIMAGEKEY, imageUrl);
                            editor.apply();
                            Log.d(TAG, String.format("userId: %s, username, %s, message: %s, token: %s", userId, username, message, token));
                            Intent intent = new Intent(LoginActivity.this, NavigationDrawerActivity.class);
                            startActivity(intent);
                        }else{
                            if (response.errorBody() != null){
                                try {
                                    errorTextView.setVisibility(View.VISIBLE);
                                    String error = response.errorBody().string();
                                    Log.e(TAG, "Response code: " + response.code());
                                    Log.e(TAG, "Error body: " + error);
                                } catch (IOException e) {
                                    Log.e(TAG, "Error reading errorBody", e);
                                }
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        if (t.getMessage() != null) {
                            Log.d(TAG, t.getMessage());
                        }
                    }
                });
            }
        });
    }
}