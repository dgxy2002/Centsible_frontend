package com.example.andyapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.andyapp.queries.RetrofitClient;
import com.example.andyapp.queries.mongoModels.LoginModel;
import com.example.andyapp.utils.CheckPasswordValid;
import com.example.andyapp.utils.CheckUsernameValid;

import java.io.IOException;
import java.util.Map;

import io.github.muddz.styleabletoast.StyleableToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class SignUpActivity extends AppCompatActivity {

    EditText usernameEditText;
    EditText passwordEditText;
    TextView invalidUsernameTextView;
    TextView invalidPasswordTextView;
    AppCompatButton btnSignUp;
    Button btnLogIn;
    SignUp signUpService;
    String TAG = "LOGCAT";

    interface SignUp {
        @POST("users/register")
        Call<Map<String, String>> signUp(@Body LoginModel loginModel);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //Initialise Views
        usernameEditText = findViewById(R.id.signupUsernameEditText);
        passwordEditText = findViewById(R.id.signupPWEditText);
        btnSignUp = findViewById(R.id.btnSigningUp);
        invalidUsernameTextView = findViewById(R.id.usernameErrorTextView);
        invalidPasswordTextView = findViewById(R.id.invalidPWTextView);
        btnLogIn = findViewById(R.id.signupBtnLogIn);
        signUpService = RetrofitClient.getRetrofit().create(SignUp.class);
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String passwordValidity = new CheckPasswordValid().validPassword(password);
                String usernameValidity = new CheckUsernameValid().validUsername(username);
                if (passwordValidity.equals("valid") && usernameValidity.equals("valid")) {
                    invalidPasswordTextView.setVisibility(View.INVISIBLE);
                    invalidUsernameTextView.setVisibility(View.INVISIBLE);
                    Log.d(TAG, String.format("username: %s, password: %s", username, password));
                    LoginModel user = new LoginModel(username, password);
                    signUpService.signUp(user).enqueue(new Callback<Map<String, String>>() {
                        @Override
                        public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                            if (response.body() != null) {
                                Map<String, String> messageMap = response.body();
                                String message = messageMap.get("message");
                                if (message != null) {
                                    Log.d(TAG, message);
                                    StyleableToast.makeText(SignUpActivity.this, "message", R.style.custom_toast).show();
                                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                }
                            } else {
                                try {
                                    invalidUsernameTextView.setVisibility(View.VISIBLE);
                                    String error = response.errorBody().string();
                                    invalidUsernameTextView.setText("Username is taken.");
                                    Log.e(TAG, "Response code: " + response.code());
                                    Log.e(TAG, "Error body: " + error);
                                } catch (IOException e) {
                                    Log.e(TAG, "Error reading errorBody", e);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Map<String, String>> call, Throwable t) {
                            if (t.getMessage() != null) {
                                invalidUsernameTextView.setVisibility(View.VISIBLE);
                                Log.d(TAG, t.getMessage());
                            }
                        }
                    });
                }else{
                    if (!passwordValidity.equals("valid")) {
                        invalidPasswordTextView.setVisibility(View.VISIBLE);
                        invalidPasswordTextView.setText(passwordValidity);
                    }
                    else{
                        invalidUsernameTextView.setVisibility(View.VISIBLE);
                        invalidUsernameTextView.setText(usernameValidity);
                    }
                }
            }
        });
    }
}