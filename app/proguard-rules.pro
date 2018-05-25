# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

-dontwarn com.google.errorprone.annotations.*

####--------Okio----------####
-dontwarn okio.**
-dontwarn org.jetbrains.annotations.**
-dontwarn java.lang.reflect.**
-dontwarn javax.annotation.**
-dontwarn javax.annotation.concurrent.**
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault


-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses

-keepclassmembers enum * {
    public static **[] values();
}

-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

-dontwarn retrofit2.**
-dontwarn org.apache.**
-dontwarn org.hamcrest.**
-dontwarn android.test.**
-dontwarn android.support.test.**
-dontwarn org.junit.**
-dontwarn junit.**
-dontwarn sun.misc.**
-dontwarn com.squareup.javawriter.JavaWriter

-keepattributes *Annotation*,EnclosingMethod,Signature
-keep public class * extends java.lang.Exception

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


#App Rules
-keep @android.support.annotation.Keep class * {*;}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <methods>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <fields>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <init>(...);
}
-keepclassmembers class * {
    @android.support.annotation.Keep <init>(...);
}

-keep interface com.bariski.cryptoniffler.domain.model.ArbitragePresentable{*;}
-keep interface com.bariski.cryptoniffler.domain.model.FilterItem{*;}
-keep interface com.bariski.cryptoniffler.presentation.calendar.models.CalendarItem{*;}
-keep interface com.bariski.cryptoniffler.presentation.common.models.GridItem{*;}

#Kotlin
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
-keep class kotlin.Metadata { *; }

#Moshi
-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}
-keep interface com.squareup.moshi.Json
-keep @com.squareup.moshi.JsonQualifier interface *
-keepclassmembers class * {
    @com.squareup.moshi.Json *;
    @com.squareup.moshi.FromJson <methods>;
    @com.squareup.moshi.ToJson <methods>;
}


#OTHER EXTERNAL LIBRARIES
-dontwarn com.android.volley.toolbox.**
-dontwarn okhttp3.**

#Square
-dontwarn com.squareup.okhttp.**

####--------Retrofit----------####
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8

