package com.example.smartlockjava.faceReconize.processor;

import androidx.camera.core.ImageProxy;

import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.util.List;

public class FaceDetectorProcessor {

    private final FaceDetector detector;

    public FaceDetectorProcessor() {
        FaceDetectorOptions faceDetectorOptions = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
                .enableTracking()
                .build();
        detector = FaceDetection.getClient(faceDetectorOptions);
    }

    public Task<List<Face>> detectInImage(ImageProxy imageProxy, int rotationDegrees) {
        InputImage inputImage = InputImage.fromMediaImage(imageProxy.getImage(), rotationDegrees);
        return detector.process(inputImage);
    }

    public void stop() {
        detector.close();
    }
}