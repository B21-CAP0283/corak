# **Mobile Development**

**Desain UI**
1. Used Splash screen and slider welcome
2. There Home screen and all batik saved
3. Gamescreen(guess the name and picture)
4. All favorite batik saved
5. Page about description this application
6. Screen setting for change language
7. Detailing result scann batik picture

**Features**
1. You can scanning image with camera or gallery
2. You can ear audio Text to speech from detail result scan
3. Identification batik and history 
4. Save and deleted scanned batik
5. Classification picture with tflite
6. Add scanned batik to favorite after set image batik
7. Play game(guess the name and guess the picture)

**Technology**
1. Make Machine Learning tensorflow/tflite
2. Use firebase from cloud computing
3. Use text to speech
4. Navigation component for android
5. Localization
6. Jetpack compose
7. Database room

## Connect Camera
### Add this to Main Activity
```javascript
private fun checkCameraPermission(): Boolean {
        var b = false
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_PERMISSION
            )
        }else{
            b = true
        }
        return b
    }
   ```
  ### Add this to Manifest
      <uses-permission android:name="android.permission.CAMERA" />
