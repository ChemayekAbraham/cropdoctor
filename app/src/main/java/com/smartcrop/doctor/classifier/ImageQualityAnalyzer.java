package com.smartcrop.doctor.classifier;

import android.graphics.Bitmap;

/**
 * Analyzes leaf photograph quality prior to running inference:
 * - Checks for motion blur and poor focus via edge variance
 * - Checks for underexposure (too dark) or overexposure (blown-out highlights)
 * - Checks for plant canopy presence via Excess Green Index (ExG)
 */
public class ImageQualityAnalyzer {

    public static class QualityReport {
        public final boolean isAcceptable;
        public final boolean isBlurry;
        public final boolean isTooDark;
        public final boolean isOverexposed;
        public final boolean hasLowPlantContent;
        public final float blurScore;
        public final float brightness;
        public final float plantCoverageFraction;
        public final String userGuidance;

        public QualityReport(boolean isAcceptable, boolean isBlurry, boolean isTooDark,
                             boolean isOverexposed, boolean hasLowPlantContent,
                             float blurScore, float brightness, float plantCoverageFraction,
                             String userGuidance) {
            this.isAcceptable = isAcceptable;
            this.isBlurry = isBlurry;
            this.isTooDark = isTooDark;
            this.isOverexposed = isOverexposed;
            this.hasLowPlantContent = hasLowPlantContent;
            this.blurScore = blurScore;
            this.brightness = brightness;
            this.plantCoverageFraction = plantCoverageFraction;
            this.userGuidance = userGuidance;
        }
    }

    private static final float MIN_BRIGHTNESS = 35.0f; // Below this is too dark
    private static final float MAX_BRIGHTNESS = 230.0f; // Above this is overexposed
    private static final float BLUR_VARIANCE_THRESHOLD = 90.0f; // Edge energy threshold
    private static final float MIN_PLANT_COVERAGE = 0.20f; // At least 20% plant canopy pixels

    public static QualityReport analyze(Bitmap bitmap) {
        if (bitmap == null) {
            return new QualityReport(false, true, true, false, true, 0, 0, 0,
                    "Invalid or missing image file. Please capture or select a clear photograph.");
        }

        // Downsample bitmap for fast statistical processing
        int sampleWidth = 160;
        int sampleHeight = 160;
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, sampleWidth, sampleHeight, true);

        int totalPixels = sampleWidth * sampleHeight;
        int[] pixels = new int[totalPixels];
        scaled.getPixels(pixels, 0, sampleWidth, 0, 0, sampleWidth, sampleHeight);

        long totalLuma = 0;
        int plantPixelCount = 0;
        float[] grayscale = new float[totalPixels];

        for (int i = 0; i < totalPixels; i++) {
            int color = pixels[i];
            int r = (color >> 16) & 0xFF;
            int g = (color >> 8) & 0xFF;
            int b = color & 0xFF;

            // Perceptual luminance
            float luma = 0.299f * r + 0.587f * g + 0.114f * b;
            grayscale[i] = luma;
            totalLuma += (long) luma;

            // Excess Green Index (ExG = 2*G - R - B)
            int exg = (2 * g) - r - b;
            if (exg > 15 || (g > r && g > b)) {
                plantPixelCount++;
            }
        }

        float avgBrightness = (float) totalLuma / totalPixels;
        float plantCoverage = (float) plantPixelCount / totalPixels;

        // Compute discrete Laplacian variance for focus / blur detection
        float laplacianVariance = computeLaplacianVariance(grayscale, sampleWidth, sampleHeight);

        boolean isBlurry = laplacianVariance < BLUR_VARIANCE_THRESHOLD;
        boolean isTooDark = avgBrightness < MIN_BRIGHTNESS;
        boolean isOverexposed = avgBrightness > MAX_BRIGHTNESS;
        boolean hasLowPlantContent = plantCoverage < MIN_PLANT_COVERAGE;

        boolean isAcceptable = !isBlurry && !isTooDark && !isOverexposed && !hasLowPlantContent;

        StringBuilder guidance = new StringBuilder();
        if (!isAcceptable) {
            guidance.append("Your photo may not contain enough detail for a reliable assessment. ");
            if (isBlurry) {
                guidance.append("The image appears blurry; hold the camera steady and tap to focus. ");
            }
            if (isTooDark) {
                guidance.append("The lighting is too dim; move into natural indirect sunlight. ");
            } else if (isOverexposed) {
                guidance.append("The image has intense glare or overexposure; shield from direct harsh sun. ");
            }
            if (hasLowPlantContent) {
                guidance.append("Move closer so the leaf fills most of the frame. ");
            }
            guidance.append("Focus on one leaf, and include both affected and healthy areas.");
        } else {
            guidance.append("Image quality is clear with good contrast and focus.");
        }

        return new QualityReport(
                isAcceptable,
                isBlurry,
                isTooDark,
                isOverexposed,
                hasLowPlantContent,
                laplacianVariance,
                avgBrightness,
                plantCoverage,
                guidance.toString()
        );
    }

    private static float computeLaplacianVariance(float[] gray, int w, int h) {
        float sum = 0;
        float sumSq = 0;
        int count = 0;

        // Apply 3x3 Laplacian kernel:
        // [ 0  1  0]
        // [ 1 -4  1]
        // [ 0  1  0]
        for (int y = 1; y < h - 1; y++) {
            for (int x = 1; x < w - 1; x++) {
                int idx = y * w + x;
                float center = gray[idx];
                float top = gray[(y - 1) * w + x];
                float bottom = gray[(y + 1) * w + x];
                float left = gray[y * w + (x - 1)];
                float right = gray[y * w + (x + 1)];

                float lap = top + bottom + left + right - (4.0f * center);
                sum += lap;
                sumSq += (lap * lap);
                count++;
            }
        }

        if (count == 0) return 0;
        float mean = sum / count;
        return (sumSq / count) - (mean * mean);
    }
}
