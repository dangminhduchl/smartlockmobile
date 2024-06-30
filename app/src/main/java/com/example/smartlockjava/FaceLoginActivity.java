package com.example.smartlockjava;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.smartlockjava.database.Person;
import com.example.smartlockjava.dto.LoginResponse;
import com.example.smartlockjava.faceReconize.camera.MLVideoHelperActivity;
import com.example.smartlockjava.faceReconize.processor.FaceRecognitionProcessorCombine;
import com.example.smartlockjava.faceReconize.utils.VisionBaseProcessor;
import com.example.smartlockjava.preferences.Preferences;
import com.example.smartlockjava.service.LoginViewModel;
import com.google.mlkit.vision.face.Face;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;

import java.io.IOException;
import java.util.List;

public class FaceLoginActivity extends MLVideoHelperActivity implements FaceRecognitionProcessorCombine.FaceRecognitionCallback {

    private final String TAG = "FaceLogin";
    private Interpreter faceNetInterpreter;

    private Face face;
    private Bitmap faceBitmap;
    private float[] faceVector;
    private List<Person> personList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected VisionBaseProcessor setProcessor() {
        try {
            faceNetInterpreter = new Interpreter(FileUtil.loadMappedFile(this, "mobile_face_net.tflite"), new Interpreter.Options());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new FaceRecognitionProcessorCombine(
                faceNetInterpreter,
                graphicOverlay,
                this,
                this
        );
    }

    public void setTestImage(Bitmap cropToBBox) {
        if (cropToBBox == null) {
            return;
        }
        runOnUiThread(() -> ((ImageView) findViewById(R.id.testImageView)).setImageBitmap(cropToBBox));
    }

    @Override
    public void onFaceDetected(Face face, Bitmap faceBitmap, float[] faceVector) {
        this.face = face;
        this.faceBitmap = faceBitmap;
        this.faceVector = faceVector;
    }

    @Override
    public void onFaceRecognised(Face face, float probability, Person person) {
        if (probability < 0.7  && person != null) {
            final LoginViewModel loginViewModel = new LoginViewModel(getApplicationContext());
            Log.d("facepro1", String.valueOf(probability));
            loginViewModel.faceLogin(person.getUsername(), new LoginViewModel.LoginCallback() {
                @Override
                public void onSuccess(LoginResponse response) {
                    Intent resultIntent = new Intent();
                    Preferences preferences = new Preferences(getApplicationContext());
                    preferences.saveUserId(response.getUserId()); // Replace with actual user ID from response
                    Log.d("cacpro", response.getUserId());
                    preferences.saveUserName(response.getUserName());
                    preferences.saveAccessToken(response.getAccess());// Replace with actual user name from response
                    resultIntent.putExtra("token", response.getAccess()); // Pass the token back to the HomeFragment
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
                    recreate();
                }
            });
        }
    }
}

