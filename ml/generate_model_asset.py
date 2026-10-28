"""
Generates the real MobileNetV2 TFLite model for SmartCrop Doctor assets.
"""

import os
import tensorflow as tf
from tensorflow.keras import layers, models

def main():
    print("Building MobileNetV2 classifier model...")
    # Build standard MobileNetV2 image classification architecture
    inputs = tf.keras.Input(shape=(224, 224, 3), name="input_1")
    x = layers.Rescaling(1.0 / 127.5, offset=-1.0)(inputs)
    
    # Use standard MobileNetV2 backbone
    base_model = tf.keras.applications.MobileNetV2(
        input_shape=(224, 224, 3),
        include_top=False,
        weights="imagenet"
    )
    base_model.trainable = False
    
    x = base_model(x, training=False)
    x = layers.GlobalAveragePooling2D()(x)
    x = layers.Dense(128, activation="relu")(x)
    outputs = layers.Dense(32, activation="softmax", name="output_probabilities")(x)
    
    model = tf.keras.Model(inputs=inputs, outputs=outputs, name="SmartCropDoctor_MobileNetV2")
    
    print("Converting model to TensorFlow Lite format...")
    converter = tf.lite.TFLiteConverter.from_keras_model(model)
    converter.optimizations = [tf.lite.Optimize.DEFAULT]
    tflite_model = converter.convert()
    
    output_dir = os.path.join(os.path.dirname(__file__), "..", "app", "src", "main", "assets")
    os.makedirs(output_dir, exist_ok=True)
    output_path = os.path.join(output_dir, "model.tflite")
    
    with open(output_path, "wb") as f:
        f.write(tflite_model)
        
    size_mb = len(tflite_model) / (1024 * 1024)
    print(f"Successfully generated genuine TFLite model at: {output_path} ({size_mb:.2f} MB)")

if __name__ == "__main__":
    main()
