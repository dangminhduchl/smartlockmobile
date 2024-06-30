package com.example.smartlockjava;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartlockjava.client.ApiClient;
import com.example.smartlockjava.database.Person;
import com.example.smartlockjava.database.PersonTableManager;
import com.example.smartlockjava.dto.LoginResponse;
import com.example.smartlockjava.faceReconize.camera.MLVideoHelperActivity;
import com.example.smartlockjava.faceReconize.processor.FaceRecognitionProcessorCombine;
import com.example.smartlockjava.faceReconize.utils.VisionBaseProcessor;
import com.example.smartlockjava.service.LoginApiService;
import com.example.smartlockjava.service.LoginViewModel;
import com.google.mlkit.vision.face.Face;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;

import java.io.IOException;

import retrofit2.Call;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private Interpreter faceNetInterpreter;
    private FaceRecognitionProcessorCombine faceRecognitionProcessorCombine;
    private PersonTableManager personTableManager;
    private Face face;
    private Bitmap faceBitmap;
    private float[] faceVector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView userNameTextview = findViewById(R.id.usernameEditText);
        TextView passwordTextView = findViewById(R.id.passwordEditText);
        TextView loginButton = findViewById(R.id.loginButton);
        ImageView faceLoginButton = findViewById(R.id.face_login_button);
        TextView registerButton = findViewById(R.id.registerTextView);
        final LoginViewModel loginViewModel = new LoginViewModel(getApplicationContext());

        // Set up the sign-in button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userNameTextview.getText().toString();
                String password = passwordTextView.getText().toString();
                LoginApiService loginApiService = ApiClient.getClient().create(LoginApiService.class);
                loginViewModel.login(userName, password, new LoginViewModel.LoginCallback() {
                    @Override
                    public void onSuccess(LoginResponse response) {
                        // Navigate to the MainActivity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Close the login activity
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(getApplicationContext(), "Login failed: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        faceLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FaceLoginActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
