package com.smartcrop.doctor.ui.camera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;
import com.smartcrop.doctor.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class CameraActivity extends AppCompatActivity {

    private PreviewView previewView;
    private ImageView imageCapturePreview;
    private View layoutBottomControls;
    private View layoutPreviewActions;
    private View reticleFrame;

    private ImageCapture imageCapture;
    private Camera camera;
    private boolean isFlashOn = false;
    private Uri capturedPhotoUri;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    startCamera();
                } else {
                    Toast.makeText(this, "Camera permission is required to capture leaf photographs.", Toast.LENGTH_LONG).show();
                    finish();
                }
            });

    private final ActivityResultLauncher<String> galleryPickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    Intent intent = new Intent(CameraActivity.this, ImageReviewActivity.class);
                    intent.putExtra(ImageReviewActivity.EXTRA_IMAGE_URI, uri.toString());
                    startActivity(intent);
                    finish();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        previewView = findViewById(R.id.previewView);
        imageCapturePreview = findViewById(R.id.imageCapturePreview);
        layoutBottomControls = findViewById(R.id.layoutBottomControls);
        layoutPreviewActions = findViewById(R.id.layoutPreviewActions);
        reticleFrame = findViewById(R.id.reticleFrame);

        findViewById(R.id.btnCloseCamera).setOnClickListener(v -> finish());

        findViewById(R.id.btnToggleFlash).setOnClickListener(v -> toggleFlash());

        findViewById(R.id.btnGalleryQuick).setOnClickListener(v -> galleryPickerLauncher.launch("image/*"));

        findViewById(R.id.btnShutter).setOnClickListener(v -> takePhoto());

        findViewById(R.id.btnRetake).setOnClickListener(v -> resumeCameraPreview());

        findViewById(R.id.btnAnalyze).setOnClickListener(v -> {
            if (capturedPhotoUri != null) {
                Intent intent = new Intent(CameraActivity.this, ImageReviewActivity.class);
                intent.putExtra(ImageReviewActivity.EXTRA_IMAGE_URI, capturedPhotoUri.toString());
                startActivity(intent);
                finish();
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                imageCapture = new ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build();

                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                cameraProvider.unbindAll();
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);

            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(this, "Failed to initialize camera: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void toggleFlash() {
        if (camera != null && camera.getCameraInfo().hasFlashUnit()) {
            isFlashOn = !isFlashOn;
            camera.getCameraControl().enableTorch(isFlashOn);
        } else {
            Toast.makeText(this, "Flash unavailable on this device.", Toast.LENGTH_SHORT).show();
        }
    }

    private void takePhoto() {
        if (imageCapture == null) return;

        File outputDir = getExternalCacheDir();
        if (outputDir == null) outputDir = getCacheDir();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(System.currentTimeMillis());
        File photoFile = new File(outputDir, "CROP_LEAF_" + timeStamp + ".jpg");

        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(this), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                capturedPhotoUri = Uri.fromFile(photoFile);
                showCapturedPreview(capturedPhotoUri);
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Toast.makeText(CameraActivity.this, "Capture failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCapturedPreview(Uri uri) {
        previewView.setVisibility(View.GONE);
        reticleFrame.setVisibility(View.GONE);
        layoutBottomControls.setVisibility(View.GONE);

        imageCapturePreview.setVisibility(View.VISIBLE);
        imageCapturePreview.setImageURI(uri);
        layoutPreviewActions.setVisibility(View.VISIBLE);
    }

    private void resumeCameraPreview() {
        imageCapturePreview.setVisibility(View.GONE);
        layoutPreviewActions.setVisibility(View.GONE);

        previewView.setVisibility(View.VISIBLE);
        reticleFrame.setVisibility(View.VISIBLE);
        layoutBottomControls.setVisibility(View.VISIBLE);
    }
}
