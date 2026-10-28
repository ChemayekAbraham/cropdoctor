# SmartCrop Doctor — Machine Learning Training & Integration Guide

This directory contains the machine learning training pipeline, dataset preparation scripts, and mobile conversion utilities for **SmartCrop Doctor**.

## 1. Overview & Model Architecture

- **Backbone**: MobileNetV2 (pre-trained on ImageNet).
- **Input Specifications**: RGB images resized to `224 x 224 x 3`, normalized to range `[-1.0, 1.0]`.
- **Classification Head**: GlobalAveragePooling2D -> Dropout(0.3) -> Dense(128, ReLU) -> Dropout(0.2) -> Dense(32, Softmax).
- **Output Format**: TensorFlow Lite (`model.tflite`) with accompanying class mapping (`labels.txt`).
- **On-Device Inference**: Offline execution via Android TensorFlow Lite runtime with 2 worker threads.

## 2. Dataset Sourcing & Attribution

The model is trained on verified agricultural crop leaf datasets:
- **PlantVillage Dataset**: (Hughes and Salathé, 2015) open access crop disease dataset under Creative Commons Attribution-ShareAlike 4.0 International license.
- **Cassava Disease Dataset**: Makerere AI Lab / Kaggle Cassava Leaf Disease Classification dataset.
- **CGIAR & ICRISAT Open Crop Imagery**: Groundnut and sorghum reference images.

### Data Leakage Prevention
To prevent overly optimistic accuracy reports:
1. Images from the same plant or burst photograph sequence are grouped together and never split across training and validation/test sets.
2. An MD5 hash deduplication step is run to strip duplicate and near-duplicate images.
3. Augmentation (rotations, perspective zooms, lighting shifts) is applied **only** to the training fold.

## 3. Supported Classes vs. Full Catalogue

The trained AI model directly supports **32 distinct classes** covering major fungal, bacterial, and healthy leaves across tomato, maize, potato, cassava, coffee, rice, pepper, groundnuts, and beans.

Diseases that primarily affect roots, stems, or fruits (e.g. *Banana Bacterial Wilt*, *Cassava Brown Streak root rot*, *Coffee Berry Disease*) are intentionally flagged in the application: **a leaf photograph alone cannot reliably establish the diagnosis, and non-foliar inspection is strictly required**.

## 4. Retraining the Model

### Prerequisites
```bash
python3 -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
```

### Running Training
```bash
python3 train_crop_model.py --data_dir data/crops --output_dir ../app/src/main/assets --epochs 20
```

### Evaluating Performance
```bash
python3 evaluate_model.py
```
This produces:
- Per-class precision, recall, and F1-score.
- Multi-class confusion matrix.
- Confidence calibration curve to evaluate probability reliability.
