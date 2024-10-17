# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.aml.newsapp.models.* {*;}

# Keep Retrofit interfaces
-keep interface retrofit2.http.* {
    *;
}

# Keep Retrofit method annotations (GET, POST, etc.)
-keepattributes RuntimeVisibleAnnotations

# Keep Retrofit service method parameters
-keepclassmembers,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Keep model classes (example: NewsResponse, News)
# You can adjust this to match your package structure
-keepclassmembers class com.aml.newsapp.** {
    *;
}

-keepclassmembers interface com.aml.newsapp.** {
    *;
}

-keepclassmembers class okhttp3.** {
    *;
}

# Keep Gson classes (if you are using Gson for serialization)
-keep class com.google.gson.** { *; }
-keep class com.google.gson.annotations.SerializedName { *; }
-keepattributes Signature

# Keep generic types for Retrofit
-keepattributes Signature, InnerClasses

# If you are using coroutines with Retrofit
-keepclassmembers class kotlinx.coroutines.** { *; }


-keepattributes Signature, RuntimeVisibleAnnotations

# Keep parameterized types (to avoid casting issues)
-keepclassmembers class * implements java.lang.reflect.ParameterizedType {
    *;
}

# Keep generic types in the codebase
-keepclassmembers class * {
    @retrofit2.http.* <methods>;
}

# Keep generic signature of Call, Response (R8 full mode strips signatures from non-kept items).
 -keep,allowobfuscation,allowshrinking interface retrofit2.Call
 -keep,allowobfuscation,allowshrinking class retrofit2.Response

 # With R8 full mode generic signatures are stripped for classes that are not
 # kept. Suspend functions are wrapped in continuations where the type argument
 # is used.
 -keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE

# Suppress reflection warnings for hidden APIs like makeOptionalFitsSystemWindows
-dontwarn android.view.ViewGroup