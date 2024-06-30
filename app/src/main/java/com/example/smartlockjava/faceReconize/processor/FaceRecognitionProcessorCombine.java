package com.example.smartlockjava.faceReconize.processor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.text.Editable;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageProxy;

import com.example.smartlockjava.database.Person;
import com.example.smartlockjava.database.PersonTableManager;
import com.example.smartlockjava.faceReconize.utils.FaceGraphic;
import com.example.smartlockjava.faceReconize.utils.GraphicOverlay;
import com.example.smartlockjava.faceReconize.utils.VisionBaseProcessor;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;

import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FaceRecognitionProcessorCombine extends VisionBaseProcessor<List<Face>> {

    public interface FaceRecognitionCallback {
        void onFaceRecognised(Face face, float probability, Person person);

        void onFaceDetected(Face face, Bitmap faceBitmap, float[] vector);
    }

    private static final long CACHE_DURATION = 60 * 1000; // Cache duration in milliseconds. Here it's set to 1 minute.
    private long lastFetchTime = 0;
    private static final String TAG = "FaceRecognitionProcessor";

    // Input image size for our facenet model
    private static final int FACENET_INPUT_IMAGE_SIZE = 112;

    private final FaceDetector detector;
    private final Interpreter faceNetModelInterpreter;
    private final ImageProcessor faceNetImageProcessor;
    private final GraphicOverlay graphicOverlay;
    private final FaceRecognitionCallback callback;

    private final Activity activity;

    private List<Person> recognisedFaceList = new ArrayList<>();

    public FaceRecognitionProcessorCombine(Interpreter faceNetModelInterpreter,
                                           GraphicOverlay graphicOverlay,
                                           FaceRecognitionCallback callback,
                                           Activity activity) {

        this.callback = callback;
        this.graphicOverlay = graphicOverlay;
        this.activity = activity;

        Log.d("FaceRecognitionProcessorFace", "Recognised face list: " + recognisedFaceList);

        // initialize processors
        this.faceNetModelInterpreter = faceNetModelInterpreter;
        faceNetImageProcessor = new ImageProcessor.Builder()
                .add(new ResizeOp(FACENET_INPUT_IMAGE_SIZE, FACENET_INPUT_IMAGE_SIZE, ResizeOp.ResizeMethod.BILINEAR))
                .add(new NormalizeOp(0f, 255f))
                .build();

        FaceDetectorOptions faceDetectorOptions = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                // to ensure we don't count and analyse same person again
                .enableTracking()
                .build();
        detector = FaceDetection.getClient(faceDetectorOptions);

    }

    @OptIn(markerClass = ExperimentalGetImage.class)
    public Task<List<Face>> detectInImage(ImageProxy imageProxy, Bitmap bitmap, int rotationDegrees) {
        InputImage inputImage = InputImage.fromMediaImage(imageProxy.getImage(), rotationDegrees);
        int rotation = rotationDegrees;

        // In order to correctly display the face bounds, the orientation of the analyzed
        // image and that of the viewfinder have to match. Which is why the dimensions of
        // the analyzed image are reversed if its rotation information is 90 or 270.
        boolean reverseDimens = rotation == 90 || rotation == 270;
        int width;
        int height;
        if (reverseDimens) {
            width = imageProxy.getHeight();
            height = imageProxy.getWidth();
        } else {
            width = imageProxy.getWidth();
            height = imageProxy.getHeight();
        }
        return detector.process(inputImage)
                .addOnSuccessListener(new OnSuccessListener<List<Face>>() {
                    @Override
                    public void onSuccess(List<Face> faces) {
                        PersonTableManager personTableManager = new PersonTableManager((Context) activity);
                        recognisedFaceList = personTableManager.findAllPersons();

                        graphicOverlay.clear();
                        for (Face face : faces) {
                            if (face.getSmilingProbability() != null && face.getSmilingProbability() > 0.5) {
                                Log.d(TAG, "Smiling face found");

                                FaceGraphic faceGraphic = new FaceGraphic(graphicOverlay, face, false, width, height);
                                Log.d(TAG, "face found, id: " + face.getTrackingId());
//                            if (activity != null) {
//                                activity.setTestImage(cropToBBox(bitmap, face.getBoundingBox(), rotation));
//                            }
                                // now we have a face, so we can use that to analyse age and gender
                                Bitmap faceBitmap = cropToBBox(bitmap, face.getBoundingBox(), rotation);

                                if (faceBitmap == null) {
                                    Log.d("GraphicOverlay", "Face bitmap null");
                                    return;
                                }

                                TensorImage tensorImage = TensorImage.fromBitmap(faceBitmap);
                                ByteBuffer faceNetByteBuffer = faceNetImageProcessor.process(tensorImage).getBuffer();
                                float[][] faceOutputArray = new float[1][192];
                                if (faceNetModelInterpreter != null) {
                                    faceNetModelInterpreter.run(faceNetByteBuffer, faceOutputArray);
                                } else {
                                    Log.e(TAG, "faceNetModelInterpreter is null");
                                }

                                Log.d(TAG, "output array: " + Arrays.deepToString(faceOutputArray));

                                if (callback != null) {
                                    callback.onFaceDetected(face, faceBitmap, faceOutputArray[0]);
                                    if (!recognisedFaceList.isEmpty()) {
                                        Pair<Person, Float> result = findNearestFace(faceOutputArray[0]);
                                        // if distance is within confidence
                                        if (result.second < 0.8f) {
                                            Log.d("facepro", "Recognised face: " + result.first.getUsername() + result.first.getPassword() + " with distance: " + result.second);
                                            faceGraphic.name = "Nearly successful";
                                            callback.onFaceRecognised(face, result.second, result.first);
                                        }
                                        if (result.second < 0.7f) {
                                            stop();
                                        }
                                    }
                                }

                                graphicOverlay.add(faceGraphic);
                            }else {
                                Log.d(TAG, "Not smiling face found");
                                Toast.makeText(activity, "Please smile", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // intentionally left empty
                    }
                });
    }

    // looks for the nearest vector in the dataset (using L2 norm)
    // and returns the pair <name, distance>
    private Pair<Person, Float> findNearestFace(float[] vector) {

        Pair<Person, Float> ret = null;
        for (Person person : recognisedFaceList) {
            final float[] knownVector = person.getFaceVector();

            float distance = 0;
            for (int i = 0; i < vector.length; i++) {
                float diff = vector[i] - knownVector[i];
                distance += diff * diff;
            }
            distance = (float) Math.sqrt(distance);
            if (ret == null || distance < ret.second) {
                ret = new Pair<>(person, distance);
            }
        }
        return ret;

    }

    public void stop() {
        detector.close();
    }

    private Bitmap cropToBBox(Bitmap image, Rect boundingBox, int rotation) {
        int shift = 0;
        if (rotation != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            image = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
        }
        if (boundingBox.top >= 0 && boundingBox.bottom <= image.getWidth()
                && boundingBox.top + boundingBox.height() <= image.getHeight()
                && boundingBox.left >= 0
                && boundingBox.left + boundingBox.width() <= image.getWidth()) {
            return Bitmap.createBitmap(
                    image,
                    boundingBox.left,
                    boundingBox.top + shift,
                    boundingBox.width(),
                    boundingBox.height()
            );
        } else return null;
    }

    // Register a name against the vector
    public void registerFace(Editable username, Editable password, float[] tempVector) {
        recognisedFaceList.add(new Person(username.toString(), password.toString(), tempVector));
        PersonTableManager personTableManager = new PersonTableManager((Context) activity);
        personTableManager.savePerson(new Person(username.toString(), password.toString(), tempVector));
    }

    public void registerFace(String username, String password, float[] tempVector) {
        recognisedFaceList.add(new Person(username, password, tempVector));
    }
}
