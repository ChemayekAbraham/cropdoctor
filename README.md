# SmartCrop Doctor — AI-Based Crop Disease Detection Android Application

**SmartCrop Doctor** is a native Android application built with **Java in Android Studio**, designed to help farmers identify potential crop diseases from leaf photographs, compare similar-looking conditions, follow crop-specific symptom inspection checklists, and access verified agronomic treatment and prevention guidance.

The application operates **100% offline**, performing on-device machine learning inference via **TensorFlow Lite (LiteRT)** and persisting crop and disease catalogues locally via **Room / SQLite**.

---

## 1. Key Architectural Features

- **Native Android Architecture**: Developed in Java with XML layouts, Material Design Components, ViewModels, and Room persistence.
- **Genuine On-Device AI Classification**: Powered by `DiseaseClassifier.java` using a MobileNetV2 architecture with TensorFlow Lite runtime. All images are processed locally on the farmer's device.
- **Transparent Diagnostic Reasoning Engine**: `DiagnosticReasoningEngine.java` evaluates model candidates alongside crop context, farmer observations, distinguishing geometric signs, and image quality checks.
- **Score Integrity**: Raw AI confidence percentages are strictly preserved and never silently overwritten by symptom responses.
- **Crop-Specific Follow-Up Checklists**: Reusable interactive symptom checklists tailored to each crop (Yes / No / Not Sure) with documented diagnostic impact weights.
- **Differential Diagnosis Screen**: Side-by-side comparison of candidate diseases, highlighting plant parts affected, distinguishing geometric features, and next recommended actions.
- **Image Quality & Uncertainty Checks**: Laplacian variance focus checking, luminance/exposure detection, and Excess Green Index (ExG) plant coverage analysis.
- **Non-Foliar Health Disclaimers**: For diseases affecting roots, vascular systems, or berries (such as *Cassava Brown Streak*, *Banana Bacterial Wilt*, or *Coffee Berry Disease*), the application explicitly warns that a leaf photograph alone cannot confirm diagnosis.

---

## 2. Crop Library (Minimum 12 Crops)

All 12 crops include representative leaf illustrations, botanical details, healthy-leaf guidance, supported AI classes, and associated disease records:

| # | Crop Name | Botanical Name | AI Detection Status | Key Diseases Covered |
|---|-----------|----------------|---------------------|----------------------|
| 1 | **Maize (Corn)** | *Zea mays* | Supported | Northern Leaf Blight, Common Rust, Gray Leaf Spot, Maize Streak Virus, Healthy |
| 2 | **Tomato** | *Solanum lycopersicum* | Supported | Early Blight, Late Blight, Septoria Leaf Spot, Bacterial Spot, ToMV, Healthy |
| 3 | **Cassava** | *Manihot esculenta* | Supported | Cassava Mosaic (CMD), Brown Streak (CBSD), Bacterial Blight, Anthracnose, Healthy |
| 4 | **Beans** | *Phaseolus vulgaris* | Supported | Angular Leaf Spot, Bean Rust, Anthracnose, Common Bacterial Blight, Healthy |
| 5 | **Potato** | *Solanum tuberosum* | Supported | Early Blight, Late Blight, Potato Virus Y, Healthy |
| 6 | **Groundnuts** | *Arachis hypogaea* | Supported | Early Leaf Spot, Late Leaf Spot, Rust, Healthy |
| 7 | **Banana** | *Musa acuminata* | Reference Catalogue | Black Sigatoka, Banana Bacterial Wilt (BXW), Banana Bunchy Top Virus (BBTV), Healthy |
| 8 | **Coffee** | *Coffea arabica* | Supported | Coffee Leaf Rust, Cercospora (Brown Eye Spot), Coffee Berry Disease (CBD), Healthy |
| 9 | **Rice** | *Oryza sativa* | Supported | Rice Blast, Bacterial Leaf Blight, Brown Spot, Rice Tungro Disease, Healthy |
| 10 | **Pepper** | *Capsicum annuum* | Supported | Bacterial Leaf Spot, Cercospora (Frogeye), Anthracnose, Healthy |
| 11 | **Sorghum** | *Sorghum bicolor* | Reference Catalogue | Anthracnose, Rust, Leaf Blight, Healthy |
| 12 | **Sweet Potato** | *Ipomoea batatas* | Reference Catalogue | Sweet Potato Virus Disease (SPVD), Alternaria Leaf Spot, Bacterial Spot, Healthy |

---

## 3. Disease Catalogue & Agronomic Guidance

Every disease entry in the local database includes:
- Scientific pathogen name & category (Fungal, Bacterial, Viral, Oomycete, Healthy).
- Possible causes and environmental conditions.
- Typical symptoms and most distinguishing visual sign.
- Cultural management & sanitation practices (pruning, mulching, crop rotation).
- Verified chemical treatment safety notes (PPE instructions, local registration requirements).
- Recommended follow-up inspection points and interval (e.g., 3 days for Late Blight, 7 days for Rust).
- Expert agricultural extension referral conditions.
- Verified scientific sources (FAO, CABI Plantwise, CIMMYT, IITA, CIAT, IRRI, CIP).

---

## 4. Application Navigation & Screens

1. **Home Screen (`MainActivity`)**:
   - Offline Availability Indicator ("Offline Ready • On-Device AI Active").
   - Primary "Scan a Leaf" hero card.
   - Quick actions: Take Photo, Choose from Gallery, Browse 12 Crops, Disease Library, History, How to Use, About.
2. **Camera (`CameraActivity`)**:
   - CameraX integration with center framing reticle, flash toggle, photo review, retake, and proceed controls.
3. **Image Review (`ImageReviewActivity`)**:
   - Preview photo with automated focus/exposure quality banner, target crop selector, and "Identify Disease" action.
4. **Results Screen (`DiseaseResultsActivity`)**:
   - Thumbnail of analyzed leaf, leading prediction, healthy badge, and model confidence %.
   - Diagnostic certainty status banner.
   - Ranked candidate predictions list with visual score bars.
   - "Check Symptoms More Closely", "Compare Possible Diseases", and "Next Inspection" buttons.
   - Complete agronomic management, prevention, and pesticide safety guidance.
5. **Differential Diagnosis (`DifferentialDiagnosisActivity`)**:
   - Direct comparison cards of top candidates with distinguishing visual traits and plant parts to examine.
6. **Follow-Up Checklist (`SymptomChecklistActivity`)**:
   - Crop-specific questions with Yes / No / Not Sure options that feed into the Diagnostic Reasoning Engine.
7. **Crop Library (`CropLibraryActivity` & `CropDetailActivity`)**:
   - Searchable catalogue of all 12 crops, descriptions, healthy leaf references, and associated disease lists.
8. **Disease Library (`DiseaseLibraryActivity` & `DiseaseDetailActivity`)**:
   - Filterable catalogue with comprehensive agronomic profiles, severity levels, and extension triggers.
9. **Diagnosis History (`DiagnosisHistoryActivity` & `HistoryDetailActivity`)**:
   - Local diagnosis history with thumbnails, timestamps, predictions, checklist responses, delete, and clear all.
10. **Farmer Account & Authentication (`LoginActivity`, `RegisterActivity`, `ProfileActivity`)**:
    - 100% offline account system powered by Room and SHA-256 salted credentials.
    - Supports farmer name, email/phone, farm name, and optional **Guest Mode** for frictionless immediate diagnostic scanning.
    - Profile dashboard displays active account tier, offline status, and historical diagnostic scans count.
11. **How to Use & About (`HowToUseActivity`, `AboutActivity`)**:
    - Farmer photography tips, AI limitations, transparency, and reference citations.

---

## 5. Machine Learning Pipeline (`ml/`)

The Python ML workflow is organized in `ml/`:
- `train_crop_model.py`: MobileNetV2 transfer learning pipeline, data augmentation, class weighting, and TFLite conversion.
- `evaluate_model.py`: Generates precision, recall, F1-scores, confusion matrix, and calibration curves.
- `requirements.txt`: Python package requirements.
- `README.md`: Detailed dataset instructions and attribution (PlantVillage, Kaggle Cassava, CGIAR).

---

## 6. How to Build & Test

### Command Line
```bash
# Run unit tests
./gradlew testDebugUnitTest

# Assemble debug APK
./gradlew assembleDebug
```
The output APK is generated at:
`app/build/outputs/apk/debug/app-debug.apk`

### Android Studio
1. Open Android Studio.
2. Select **Open** and choose the `Crop Doctor` project root.
3. Allow Gradle to sync dependencies.
4. Click **Run** (`Shift + F10`) to deploy to an emulator or physical device.
