# DocExpiry - Add Button & Build Instructions

## What I Fixed

✅ **Add Button (FAB)** - Fully wired and working
- Location: Bottom-right corner of the "Manage govt cards" screen
- Action: Opens the "Add Document" form
- Icon: Floating + button with primary color

✅ **Logo Display** - All document types show proper logos
- Each card displays the correct logo badge in the top-right corner
- Fallback to default icon if type-specific logo missing
- Logos from your drawable files: `logo_aadhaar.xml`, `logo_pan.xml`, etc.

## How to Build & Run

### Option 1: Simple Batch Script (Recommended for Windows)

1. Open Command Prompt (cmd.exe)
2. Navigate to project folder:
   ```cmd
   cd C:\Users\Shara\AndroidStudioProjects\DocExpiry
   ```
3. Run the build script:
   ```cmd
   build_and_run_simple.bat
   ```

That's it! The script will:
- Clean the project
- Build the debug APK
- Install on your connected device/emulator
- Launch the app automatically

### Option 2: Manual Commands (Step by Step)

If the script doesn't work, run these commands one by one:

```cmd
REM Navigate to project
cd C:\Users\Shara\AndroidStudioProjects\DocExpiry

REM Clean
.\gradlew.bat clean

REM Build APK
.\gradlew.bat assembleDebug

REM Install (ensure device is connected)
.\gradlew.bat installDebug

REM Launch the app
adb shell am start -n com.example.docexpiry/.MainActivity
```

### Option 3: Use Android Studio (Easiest)

1. Open Android Studio
2. Open the DocExpiry project
3. Click the green **Run** button (or press Shift+F10)
4. Select your device/emulator
5. App will build and launch automatically

## Testing the Add Button

1. **Open the app** - You'll see the "Manage govt cards" screen with a list
2. **Tap the + button** (floating action button in bottom-right)
3. The "Add Document" form opens where you can:
   - Select document type (Aadhaar, PAN, Passport, etc.)
   - Take a photo or pick from gallery
   - Enter document details (number, name, dates, etc.)
   - Save the document

## Troubleshooting

### Error: "adb is not recognized"
Android SDK platform-tools not in PATH. Solution:
```cmd
setx ANDROID_SDK_ROOT "C:\Users\Shara\AppData\Local\Android\Sdk"
```
Then restart your terminal/IDE.

### Error: "JAVA_HOME is set to invalid directory"
The batch script automatically detects Java. If it still fails:
```cmd
set JAVA_HOME=
.\gradlew.bat assembleDebug
```
(Gradle will auto-detect Java)

### Error: "Device not found"
```cmd
adb devices
```
Make sure:
- Device is plugged in via USB
- USB debugging is enabled on the device
- Correct drivers are installed

### Error: "Build failed"
Check the build log:
```cmd
type app\build\outputs\errors\error*.txt
```
Or look at build output in Android Studio

## Project Structure

- `app/src/main/java/com/example/docexpiry/MainActivity.kt` - Main screen with FAB
- `app/src/main/java/com/example/docexpiry/AddEditCardActivity.kt` - Add/Edit form
- `app/src/main/res/layout/activity_main.xml` - Main screen layout (has FAB)
- `app/src/main/res/layout/activity_add_edit_card.xml` - Add/Edit form layout
- `app/src/main/res/drawable/` - Logo images and drawables

## Features Added/Working

✅ Floating Action Button (FAB) with + icon
✅ Opens Add Document form when tapped
✅ Document logos display on each card
✅ Logo fallback for missing drawables
✅ Form validation (required fields, date checks)
✅ Photo capture from camera or gallery
✅ Save documents to database

## Files Modified

1. `app/src/main/java/com/example/docexpiry/MainActivity.kt` - Wired FAB click
2. `app/src/main/java/com/example/docexpiry/CardAdapter.kt` - Logo fallback
3. `app/src/main/res/layout/item_card.xml` - Logo default drawable

## Questions?

- **FAB not showing?** - Check `activity_main.xml` has the FloatingActionButton element
- **Add button doesn't open form?** - Verify `AddEditCardActivity` is in AndroidManifest.xml
- **Logos not showing?** - Check `CardTypeVisuals.kt` maps your types to drawable files
- **Can't build?** - Ensure Java and Android SDK are installed

---

**Next Steps:**
1. Run `build_and_run_simple.bat`
2. Test the + button to add a document
3. Verify logos appear on document cards
4. Enjoy managing your documents! 🎉

