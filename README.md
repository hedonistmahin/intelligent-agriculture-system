# 🌾 Sonali Fasal — Agricultural Assistance App (Android + ML)

> An Android app for farmers in Bangladesh that delivers local weather, on-device crop **price prediction** (TensorFlow Lite), agricultural resources, and a roadmap toward image-based **disease detection** — with a clean Bengali-first UI.&#x20;

---

## ✨ Key Features

* **Weather updates**: air quality, UV index, sunrise/sunset, wind, rainfall, temperature, humidity.&#x20;
* **Crop Price Prediction (on-device)**: TFLite neural network predicts prices from crop type, district, month, year; runs offline after initial setup.&#x20;
* **Agricultural information hub**: videos, crop guidelines, seasonal timing, and helplines.&#x20;
* **Future goal**: image-based crop disease detection with a MobileNetV2-style CNN (on-device).&#x20;
* **Bengali-first UX** with accessibility (large text, high contrast) and Material Design consistency.&#x20;

---

## 🧭 Project Overview

Sonali Fasal is a **standalone** Android application targeting **Android 8.0 (API 26)+**, optimized for low-end devices and offline usability. Weather uses a secure HTTPS API; ML inference (price prediction now, disease detection later) runs fully on-device with **TensorFlow Lite**.&#x20;

---

## 🏗️ Architecture

* **Client-only app** with minimal server dependence; caches content for offline use.&#x20;
* **Modules/UI**: Home, Weather, Prediction, Information (bottom navigation).&#x20;
* **ML Inference**:

  * `crop_price_model.tflite` + `crop_mapping.json` + `district_mapping.json` + `scalers.json` bundled as assets.
  * Inputs: crop (label-encoded), district (label-encoded), **month**, **year** → scaled with MinMax; output (scaled price) inverse-transformed to BDT.&#x20;

---

## 🧪 ML Model (Price Prediction)

* **Architecture**: Dense(64, ReLU) → Dense(32, ReLU) → Dense(1, **sigmoid**) for a scaled price.
* **Training**: 50 epochs, batch 32, Adam + MSE (MAE for monitoring).
* **Preprocessing**:

  * Date → month, year
  * LabelEncoder for crop/district
  * MinMaxScaler for features & price
* **Data**: 1,225 rows CSV (train/val 80/20).
* **Deployment**: Converted to **TFLite** with JSON helpers for encoders/scalers.&#x20;

> ⚠️ Keep **training-time encoders & scalers** in sync with the app. If you retrain, regenerate the JSON files and replace the TFLite model together.&#x20;

---

## 📁 Project Structure (suggested)

```
sonali-fasal/
├─ app/
│  ├─ src/main/
│  │  ├─ java/.../ui/            # Activities/Fragments (Home, Weather, Prediction, Info)
│  │  ├─ java/.../ml/            # TFLite inference helpers
│  │  ├─ java/.../data/          # Repositories (weather, cache)
│  │  ├─ res/layout/             # XML screens (ConstraintLayout/Grid)
│  │  ├─ res/values/             # strings.xml (bn default, en optional), colors, styles
│  │  └─ assets/
│  │     ├─ crop_price_model.tflite
│  │     ├─ crop_mapping.json
│  │     ├─ district_mapping.json
│  │     └─ scalers.json
│  └─ build.gradle
├─ docs/                         # SRS, screenshots, design notes
└─ README.md
```

UI follows Material Design; uses ConstraintLayout, CardView, ViewPager2/RecyclerView, and CameraX (for future disease module).&#x20;

---

## 🚀 Getting Started

### Prerequisites

* **Android Studio** (Arctic Fox+ recommended) and Android 8.0+ device/emulator.
* Weather API key (e.g., OpenWeatherMap) for live data via **HTTPS**.&#x20;

### 1) Clone & Open

```bash
git clone https://github.com/<your-org>/sonali-fasal.git
cd sonali-fasal
```

Open in Android Studio and let Gradle sync.

### 2) Configure Secrets

Set your weather API key in a safe place (example using `local.properties`):

```properties
WEATHER_API_KEY=YOUR_KEY
```

Expose it in `build.gradle` (app):

```gradle
buildConfigField "String", "WEATHER_API_KEY", "\"${WEATHER_API_KEY}\""
```

### 3) Add ML Assets

Place files in `app/src/main/assets/`:

```
crop_price_model.tflite
crop_mapping.json
district_mapping.json
scalers.json
```

Names must match; these come from the training pipeline and are required for correct encoding/scaling & inverse transforms.&#x20;

### 4) Run

Build & run on a device. Offline prediction works; weather requires connectivity.&#x20;

---

## 🧩 TFLite Inference (Kotlin snippet)

```kotlin
class PricePredictor(context: Context) {
    private val tflite by lazy {
        val model = FileUtil.loadMappedFile(context, "crop_price_model.tflite")
        Interpreter(model, Interpreter.Options())
    }

    private val cropMap: Map<String, Int> =
        loadJsonMap(context, "crop_mapping.json") // e.g., {"rice":0,"wheat":1,...}
    private val districtMap: Map<String, Int> =
        loadJsonMap(context, "district_mapping.json")

    private val scalers = loadScalers(context, "scalers.json") // {featureMins, featureMaxs, priceMin, priceMax}

    fun predictBDT(crop: String, district: String, date: LocalDate): Double {
        // 1) Encode & build feature vector: [cropId, districtId, month, year]
        val x = floatArrayOf(
            (cropMap[crop] ?: error("Unknown crop")).toFloat(),
            (districtMap[district] ?: error("Unknown district")).toFloat(),
            date.monthValue.toFloat(),
            date.year.toFloat()
        )
        // 2) MinMax scale features
        val xScaled = minMaxScale(x, scalers.featureMins, scalers.featureMaxs)

        // 3) Run TFLite
        val input = arrayOf(xScaled)
        val output = Array(1) { FloatArray(1) }
        tflite.run(input, output)

        // 4) Inverse scale price → BDT
        val yScaled = output[0][0]
        return inverseMinMax(yScaled, scalers.priceMin, scalers.priceMax).toDouble()
    }
}
```

> The app **must** use the **same encoders & MinMax bounds** used during training; otherwise predictions will be incorrect.&#x20;

---

## 🌤️ Weather Module

* City selector + refresh; cards for AQI (PM2.5), UV, sunrise/sunset, wind, rainfall, feels-like, humidity.
* Uses HTTPS; minimal bandwidth to suit rural connectivity.&#x20;

---

## 🌐 Localization & Accessibility

* **Primary language: Bengali** (Noto Sans Bengali recommended); **English** as secondary.
* Larger text & high-contrast options for better readability.&#x20;

---

## ⚙️ Performance Targets

* Screen load ≤ **2s** on a mid-range device (2GB RAM).
* Weather refresh ≤ **5s** (with internet).
* TFLite prediction ≤ **500ms**; future disease detection ≤ **1s** per image.&#x20;

---

## 🔐 Permissions & Data

* Local storage for preferences, cached weather, and (future) captured images.
* No data sharing without consent; API calls over **HTTPS**.&#x20;

---

## 🗺️ Roadmap

* ✅ Price prediction (TFLite)
* 🔜 Disease diagnosis: MobileNetV2-based CNN → TFLite; camera capture; on-device inference with guidance.&#x20;
* 🔜 Real-time market feeds, pest management, IoT soil monitoring integrations.&#x20;

---

## 🧠 For ML Engineers

* Keep a small `ml/` folder (not in app) with: training notebooks, data schema, `export_to_tflite.py`, and a script to dump:

  * `crop_price_model.tflite`
  * `crop_mapping.json`, `district_mapping.json`
  * `scalers.json` with `{featureMins, featureMaxs, priceMin, priceMax}`
* Validate post-training with the same preprocessing pipeline used in app; add a unit test that compares Python vs TFLite outputs on a fixed sample.&#x20;

---

## 🤝 Contributing

1. Fork the repo & create a feature branch.
2. Keep UI consistent (Material, rounded corners, color scheme).
3. For ML changes, update all artifacts together and note versions in `CHANGELOG.md`.
4. Open a PR with screenshots and a brief technical note.&#x20;

---

## 📜 License

Add your license here (e.g., MIT/Apache-2.0).

---

## 🙏 Acknowledgments

* Android Developers & Material Design
* TensorFlow & TensorFlow Lite
* (Planned) Kaggle crop disease datasets for future research&#x20;

---

## 📬 Contact

Maintainer: *Md. Modabbir Hossain Mahin* 
.Email: *modabbirmahin@gmail.com*
If you’re using this in the field or research, we’d love to hear your feedback and results!
