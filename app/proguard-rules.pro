# Add project specific ProGuard rules here.
# You can control the level of optimization performed by ProGuard system property:
#    android.enableR8.fullMode=true
# R8 performs some optimizations by default, including removing classes and methods that aren't used.
# By default, R8 obfuscates and optimizes your code. More information: https://r8.googlesource.com/r8
-dontobfuscate
-dontoptimize
-dontwarn kotlin.Unit # Needed due to Compose usages
# Add any project specific keep rules here:
# -keep class my.package.MyClass { *; }
# If you use reflection, you might need to keep the classes you reflect on:
# -keep class my.package.MyClass { *; }
# If you use JNI (Java Native Interface), you might need to keep the native methods:
# -keepclasseswithmembernames,includedescriptorclasses class * {
#   native <methods>;
# }
# If you use libraries that depend on reflection, you might need to keep their classes:
# -keep class com.example.LibraryClass { *; }
# If you use libraries that depend on resources, you might need to keep them:
# -keepresources R.drawable.*
# If you use serialization, you might need to keep the classes you serialize:
# -keep class my.package.SerializableClass { *; }
# Jetpack Compose specific rules
-keepclasseswithmembers public class * {
    @androidx.compose.runtime.Composable <methods>;
}
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}
-keepclassmembers enum * {
    <fields>;
}
-keepclassmembers public class * extends androidx.lifecycle.ViewModel {
    public <init>(...);
}
