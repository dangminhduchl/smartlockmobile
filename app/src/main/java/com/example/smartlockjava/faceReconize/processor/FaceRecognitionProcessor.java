package com.example.smartlockjava.faceReconize.processor;

import android.graphics.Bitmap;
import android.text.Editable;
import android.util.Log;
import android.util.Pair;

import com.example.smartlockjava.database.Person;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class FaceRecognitionProcessor {
    private static final String TAG = "FaceRecognitionProcessor";

    // Input image size for our facenet model

    private static final int FACENET_INPUT_IMAGE_SIZE = 112;
    private final Interpreter faceNetModelInterpreter;
    private final ImageProcessor faceNetImageProcessor;
    private List<Person> recognisedFaceList = new ArrayList();

    public FaceRecognitionProcessor(Interpreter faceNetModelInterpreter) {
        this.faceNetModelInterpreter = faceNetModelInterpreter;
        faceNetImageProcessor = new ImageProcessor.Builder()
                .add(new ResizeOp(FACENET_INPUT_IMAGE_SIZE, FACENET_INPUT_IMAGE_SIZE, ResizeOp.ResizeMethod.BILINEAR))
                .add(new NormalizeOp(0f, 255f))
                .build();
    }

    public float[] getFaceVector(Bitmap faceBitmap) {
        TensorImage tensorImage = TensorImage.fromBitmap(faceBitmap);
        ByteBuffer faceNetByteBuffer = faceNetImageProcessor.process(tensorImage).getBuffer();
        float[][] faceOutputArray = new float[1][192];
        if (faceNetModelInterpreter != null) {
            faceNetModelInterpreter.run(faceNetByteBuffer, faceOutputArray);
        } else {
            Log.e(TAG, "faceNetModelInterpreter is null");
        }
        return faceOutputArray[0];
    }

    public void registerFace(Editable username, Editable password, float[] tempVector) {
        recognisedFaceList.add(new Person(username.toString(), password.toString(), tempVector));
    }

    public Pair<String, Float> findNearestFace(float[] vector) {
        Pair<String, Float> ret = null;
        for (Person person : recognisedFaceList) {
            final String name = person.getUsername();
            final float[] knownVector = person.getFaceVector();

            float distance = 0;
            for (int i = 0; i < vector.length; i++) {
                float diff = vector[i] - knownVector[i];
                distance += diff * diff;
            }
            distance = (float) Math.sqrt(distance);
            if (ret == null || distance < ret.second) {
                ret = new Pair<>(name, distance);
            }
        }
        return ret;
    }
}