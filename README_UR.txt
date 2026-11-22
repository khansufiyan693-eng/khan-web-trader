KHAN AI TRADER Android project V22

یہ Android Studio project ہے، تیار شدہ APK نہیں۔ اس ماحول میں Android SDK/Gradle نصب نہیں، اس لیے APK compile نہیں کیا جا سکا۔

APK بنانے کے لیے:
1) ZIP extract کریں۔
2) Android Studio میں KHAN_AI_TRADER_ANDROID_V22 فولڈر Open کریں۔
3) Gradle Sync مکمل ہونے دیں (Android SDK Platform 35 اور Android Gradle Plugin 8.7.3 درکار ہیں)۔
4) Build > Build Bundle(s) / APK(s) > Build APK(s) منتخب کریں۔
5) APK app/build/outputs/apk/debug/app-debug.apk میں ملے گا۔

Data persistence:
- WebView DOM Storage فعال ہے؛ localStorage ایپ بند/موبائل restart ہونے کے بعد برقرار رہتا ہے۔
- ہر localStorage تبدیلی کا snapshot app کے private files/khan_trader_backup.json میں محفوظ کیا جاتا ہے۔
- Android Auto Backup فعال ہے؛ Android کے backup settings، Google account اور device policy کے مطابق یہ فائل restore ہو سکتی ہے۔
- Logout فیچر موجودہ V21 HTML میں نہیں ملا؛ اس لیے کوئی حقیقی login/logout flow شامل یا ٹیسٹ نہیں کیا گیا۔ مستقبل میں logout صرف session/auth state صاف کرے، trading history/settings/capital نہیں۔
- Uninstall، Clear Storage/Data، فون reset یا backup بند ہونے پر ڈیٹا کے ضائع ہونے کا امکان رہتا ہے۔ کسی بھی ایپ میں 100% ہمیشہ کے لیے data retention کی ضمانت نہیں دی جا سکتی۔ اہم ریکارڈ کا الگ export/backup ضروری ہے۔

Safety:
- یہ صرف موجودہ V21 HTML کا Android WebView wrapper ہے۔ SmartAPI credentials/real trading شامل نہیں۔
- API Secret, PIN, TOTP یا Access Token اس HTML/LocalStorage میں نہ رکھیں۔
