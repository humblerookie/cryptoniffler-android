# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/anvithbhat/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

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

####--------Retrofit----------####
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

-dontwarn com.google.errorprone.annotations.*

####--------Okio----------####
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn javax.annotation.concurrent.**
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault
-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}
-keep @com.squareup.moshi.JsonQualifier interface *
-keep class com.bariski.cryptoniffler.presentation.common.models.GridItem
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
-keepclassmembers class ** {
    @com.squareup.moshi.Json *;
    @com.squareup.moshi.FromJson *;
    @com.squareup.moshi.ToJson *;
}

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}



-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses

-dontpreverify
-repackageclasses
-allowaccessmodification
-optimizations !code/simplification/arithmetic
#-keepattributes *Annotation*






#for application class
-keep public class * extends android.app.Application
-keep class javax.** { *; }
-keep class org.** { *; }
-keep class android.support.v7.widget.** { *; }
#FOR ACTIVITY , BASEACTIVITY , BASEFRAGMENT , APPCOMPATACTIVITY
-keep public class * extends android.app.Activity
-keep public class * extends android.support.v7.app.AppCompatActivity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver

#FOR FRAGMENT AND DIALOG FRAGMENT
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Fragment
-keep public class * extends android.support.v4.app.DialogFragment

#FOR LISTVIEW ADAPTER
-keep public class * extends android.widget.BaseAdapter
-keep public class * extends android.support.v7.widget.RecyclerView.Adapter

-keep public class * extends android.app.Application
# -keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
# -keep public class * extends android.content.ContentProvider
-keep public class * extends android.preference.Preference

-keep public interface com.android.vending.licensing.ILicensingService

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}


#OTHER EXTERNAL LIBRARIES
-dontwarn com.android.volley.toolbox.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-dontwarn okio.**

#Square
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.content.Context {
    public void *(android.view.View);
    public void *(android.view.MenuItem);
}


-dontwarn okio.**
-dontwarn retrofit2.**
-dontwarn org.apache.**
-dontwarn org.hamcrest.**
-dontwarn android.test.**
-dontwarn android.support.test.**
-keep class org.hamcrest.** {*;}
-keep class org.junit.** { *; }
-dontwarn org.junit.**
-keep class junit.** { *; }
-dontwarn junit.**
-keep class sun.misc.** { *; }
-dontwarn sun.misc.**
-dontwarn com.squareup.javawriter.JavaWriter

-keepattributes *Annotation*,EnclosingMethod,Signature

 -renamesourcefileattribute com.craftsvilla.app.**


 -keep public class * extends java.lang.Exception

-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**
-keepattributes *Annotation*
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
-printmapping mapping.txt


-keepclassmembers class * implements java.io.Serializable {
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}
-keep @com.squareup.moshi.JsonQualifier interface *
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
-dontwarn org.jetbrains.annotations.**
-keep class kotlin.Metadata { *; }

-dontwarn java.lang.reflect.**
-keepnames @kotlin.Metadata class com.bariski.cryptoniffler.domain.model.**
-keepnames @kotlin.Metadata class com.bariski.cryptoniffler.data.api.models.**
-keepnames @kotlin.Metadata class com.bariski.cryptoniffler.presentation.model.**
-keep class com.bariski.cryptoniffler.domain.model.** { *; }
-keep class com.bariski.cryptoniffler.data.api.models.** { *; }
-keep class com.bariski.cryptoniffler.presentation.model.** { *; }
-keep interface com.squareup.moshi.Json
-keepclassmembers class com.bariski.cryptoniffler.domain.model** {
  <init>(...);
  <fields>;
}
-keepclassmembers class com.bariski.cryptoniffler.data.api.models.** {
  <init>(...);
  <fields>;
}
-keepclassmembers class com.bariski.cryptoniffler.presentation.model.** {
  <init>(...);
  <fields>;
}
-keepclassmembers class * {
    @com.squareup.moshi.FromJson <methods>;
    @com.squareup.moshi.ToJson <methods>;
}
-keepclasseswithmembers class * {
     @com.squareup.moshi.* <methods>;
 }

-dontwarn okio.**
-dontwarn javax.annotation.**
-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}
-keep @com.squareup.moshi.JsonQualifier interface *
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
