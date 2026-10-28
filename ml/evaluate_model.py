"""
SmartCrop Doctor — Model Evaluation and Calibration Script
Evaluates TFLite model precision, recall, confusion matrix, and prediction reliability.
"""

import os
import json
import numpy as np
import tensorflow as tf
try:
    from sklearn.metrics import classification_report, confusion_matrix
except ImportError:
    classification_report = None
    confusion_matrix = None

def evaluate_tflite_model(tflite_path, test_images, test_labels, class_names):
    """Runs inference on test set and computes precision, recall, F1-score."""
    interpreter = tf.lite.Interpreter(model_path=tflite_path)
    interpreter.allocate_tensors()

    input_details = interpreter.get_input_details()
    output_details = interpreter.get_output_details()

    predictions = []
    probabilities = []

    print(f"Evaluating {len(test_images)} test samples...")
    for img in test_images:
        # Preprocess to shape [1, 224, 224, 3]
        input_data = np.expand_dims(img, axis=0).astype(np.float32)
        interpreter.set_tensor(input_details[0]['index'], input_data)
        interpreter.invoke()
        output_data = interpreter.get_tensor(output_details[0]['index'])[0]
        
        pred_class = np.argmax(output_data)
        predictions.append(pred_class)
        probabilities.append(output_data)

    predictions = np.array(predictions)
    probabilities = np.array(probabilities)

    print("\n=== Classification Report ===")
    print(classification_report(test_labels, predictions, target_names=class_names, digits=3))

    cm = confusion_matrix(test_labels, predictions)
    print("\n=== Confusion Matrix ===")
    print(cm)

    # Uncertainty / Calibration summary
    max_probs = np.max(probabilities, axis=1)
    print(f"\nAverage Top-1 Confidence: {np.mean(max_probs) * 100:.1f}%")
    print(f"Predictions with Confidence < 40%: {np.sum(max_probs < 0.40)} / {len(max_probs)}")

if __name__ == "__main__":
    print("Run with a dataset to produce detailed per-class metrics.")
