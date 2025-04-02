package com.example.andyapp;

import android.content.Intent;
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
import com.example.andyapp.queries.mongoModels.UserModel;

import java.io.IOException;

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
    public static final String VIEWERKEY = "USERKEY";
    public static final String TOKENKEY = "TOKENKEY";
    private String TAG = "LOGCAT";
    RequestUser requestUser;

    interface RequestUser{
        @POST("users/login")
        Call<UserModel> getUser(@Body LoginModel login);
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
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if ( response.body() != null ) {
                            UserModel user = response.body();
                            String id = user.getId();
                            String message = user.getMessage();
                            String token = user.getToken();
                            int streak = user.getStreak();
                            Log.d(TAG, String.format("userID: %s, message: %s, token: %s, streak: %d", id, message, token, streak));
                            Intent intent = new Intent(LoginActivity.this, NavigationDrawerActivity.class);
                            intent.putExtra(USERKEY, id);
                            intent.putExtra(VIEWERKEY, id);
                            intent.putExtra(TOKENKEY, "Bearer:` " + token);
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
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        if (t.getMessage() != null) {
                            Log.d(TAG, t.getMessage());
                        }
                    }
                });
            }
        });
    }
}