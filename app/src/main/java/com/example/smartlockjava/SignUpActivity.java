package com.example.smartlockjava;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.smartlockjava.database.Person;
import com.example.smartlockjava.faceReconize.camera.MLVideoHelperActivity;
import com.example.smartlockjava.faceReconize.processor.FaceRecognitionProcessorCombine;
import com.example.smartlockjava.faceReconize.utils.VisionBaseProcessor;
import com.example.smartlockjava.service.SignUpViewModel;
import com.google.gson.Gson;
import com.google.mlkit.vision.face.Face;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SignUpActivity extends MLVideoHelperActivity implements FaceRecognitionProcessorCombine.FaceRecognitionCallback {

    private Interpreter faceNetInterpreter;
    private FaceRecognitionProcessorCombine faceRecognitionProcessorCombine;

    private Face face;
    private Bitmap faceBitmap;
    private float[] faceVector;

    private List<Person> personList;

    public SignUpActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeAddFaceVisible();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected VisionBaseProcessor setProcessor() {
        try {
            faceNetInterpreter = new Interpreter(FileUtil.loadMappedFile(this, "mobile_face_net.tflite"), new Interpreter.Options());
        } catch (IOException e) {
            e.printStackTrace();
        }

        faceRecognitionProcessorCombine = new FaceRecognitionProcessorCombine(
                faceNetInterpreter,
                graphicOverlay,
                this,
                this
        );
        return faceRecognitionProcessorCombine;
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
        if (probability < 0.7 && person != null) {
            makeAddFaceDisable();
            Toast.makeText(this, "Recognise " + person.getUsername(), Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Face not recognised", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAddFaceClicked(View view) {
        super.onAddFaceClicked(view);

        if (face == null || faceBitmap == null) {
            return;
        }

        Face tempFace = face;
        Bitmap tempBitmap = faceBitmap;
        float[] tempVector = faceVector;

        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.add_face_dialog, null);
        ((ImageView) dialogView.findViewById(R.id.dlg_image)).setImageBitmap(tempBitmap);

        EditText passwordEditText = dialogView.findViewById(R.id.dlg_password);
        passwordEditText.setOnTouchListener(getPasswordTouchListener(passwordEditText));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setPositiveButton("Save", getDialogOnClickListener(dialogView, tempVector));
        builder.show();
    }

    private DialogInterface.OnClickListener getDialogOnClickListener(View dialogView, float[] tempVector) {
        return (dialog, which) -> {

            Editable usernameInput = ((EditText) dialogView.findViewById(R.id.dlg_username)).getEditableText();
            Editable passwordInput = ((EditText) dialogView.findViewById(R.id.dlg_password)).getEditableText();
            Editable emailInput = ((EditText) dialogView.findViewById(R.id.dlg_email)).getEditableText();
            if (usernameInput.length() > 0 && passwordInput.length() > 0) {
                Gson gson = new Gson();
                String faceVectorJson = gson.toJson(tempVector);
                final SignUpViewModel signupViewModel = new SignUpViewModel(getApplicationContext());
                signupViewModel.signup(usernameInput.toString(), passwordInput.toString(), emailInput.toString(), faceVectorJson, new SignUpViewModel.SignupCallback() {
                    @Override
                    public void onSuccess(String response) {
                        Toast.makeText(SignUpActivity.this, response, Toast.LENGTH_SHORT).show();
                        faceRecognitionProcessorCombine.registerFace(usernameInput, passwordInput, faceVector);

                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(SignUpActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });


            }
        };
    }

    @SuppressLint("ClickableViewAccessibility")
    private View.OnTouchListener getPasswordTouchListener(EditText passwordEditText) {
        return (v, event) -> {
            final int DRAWABLE_RIGHT = 2;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (passwordEditText.getRight() - passwordEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    int currentInputType = passwordEditText.getInputType();
                    if (currentInputType == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    } else {
                        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    }
                    return true;
                }
            }
            return false;
        };
    }
}