CALL RECORDER PRO - Android project

ഇത് പരസ്യങ്ങളില്ലാത്ത അടിസ്ഥാന microphone audio recorder ആണ്.

പ്രധാന പരിമിതി:
- ഈ പതിപ്പ് Android-ന്റെ microphone ഉപയോഗിച്ചാണ് ശബ്ദം റെക്കോർഡ് ചെയ്യുന്നത്.
- WhatsApp/സാധാരണ ഫോൺ കോളുകളുടെ internal two-way audio നേരിട്ട് പിടിച്ചെടുക്കുന്നില്ല.
- മറുവശത്തുള്ള ശബ്ദം ലഭിക്കാൻ speakerphone ഉപയോഗിക്കേണ്ടി വരാം; ഫോണിന്റെയും Android പതിപ്പിന്റെയും നിയന്ത്രണങ്ങൾ കാരണം അതും ഉറപ്പില്ല.
- Android/WhatsApp നിയന്ത്രണങ്ങൾ മറികടക്കാൻ ഈ ആപ്പ് ശ്രമിക്കുന്നില്ല.
- റെക്കോർഡിംഗ് തുടങ്ങുന്നതിന് മുമ്പ് ബന്ധപ്പെട്ടവരുടെ സമ്മതം വാങ്ങുക; പ്രാദേശിക നിയമങ്ങൾ പാലിക്കുക.

Android Studio-യിൽ തുറക്കൽ:
1. Android Studio ഇൻസ്റ്റാൾ ചെയ്യുക.
2. ഈ ZIP extract ചെയ്യുക.
3. Android Studio > Open > CallRecorderPro folder തിരഞ്ഞെടുക്കുക.
4. Gradle sync പൂർത്തിയാക്കുക.
5. Android ഫോൺ USB debugging വഴി ബന്ധിപ്പിച്ച് Run അമർത്തുക.
6. അല്ലെങ്കിൽ Build > Build APK(s) തിരഞ്ഞെടുക്കുക.

ആവശ്യമായത്:
- Android Studio + Android SDK (API 35)
- Java 17 / അനുയോജ്യമായ Gradle

ഈ പ്രവർത്തനപരിസരത്തിൽ Android SDK/Gradle build tools ഇല്ലാത്തതിനാൽ APK ഇവിടെ compile ചെയ്തിട്ടില്ല.

GitHub വഴി APK build ചെയ്യാനുള്ള എളുപ്പവഴി:
1. GitHub-ൽ പുതിയ repository സൃഷ്ടിക്കുക.
2. ZIP extract ചെയ്ത് ഉള്ളിലെ എല്ലാ ഫയലുകളും repository-യിൽ upload ചെയ്യുക (hidden .github folder ഉൾപ്പെടെ).
3. GitHub repository > Actions തുറക്കുക.
4. "Build Call Recorder Pro APK" workflow തിരഞ്ഞെടുക്കുക > Run workflow.
5. Build പൂർത്തിയായാൽ workflow run തുറന്ന് Artifacts-ൽ നിന്ന് CallRecorderPro-debug ഡൗൺലോഡ് ചെയ്യുക.
6. APK Android ഫോണിലേക്ക് മാറ്റി ഇൻസ്റ്റാൾ ചെയ്യുക. ഇൻസ്റ്റാളിന് മുമ്പ് Android ചോദിക്കുന്ന അനുമതികൾ പരിശോധിക്കുക.

കുറിപ്പ്: Debug APK ആണ് ലഭിക്കുക; Play Store release-നുള്ള signed release APK അല്ല.
