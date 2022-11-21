-keepattributes SourceFile,LineNumberTable
-dontwarn java.lang.invoke.*
-dontwarn **$$Lambda$*
-dontwarn javax.annotation.**

# RetroFit
-dontwarn retrofit.**
-keep class retrofit.** { *; }

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}

# OkHttp
-keepattributes Signature
-keepattributes *Annotation*
-keep interface com.squareup.okhttp3.** { *; }
-dontwarn com.squareup.okhttp3.**

#-dontwarn
#-ignorewarnings

#Jaudiotagger
-dontwarn org.jaudiotagger.**
-dontwarn org.jcodec.**
-keep class org.jaudiotagger.** { *; }
-keep class org.jcodec.** { *; }

-keepclassmembers enum * { *; }
-keepattributes *Annotation*, Signature, Exception
-keepnames class androidx.navigation.fragment.NavHostFragment
-keep class * extends androidx.fragment.app.Fragment{}
-keepnames class * extends android.os.Parcelable
-keepnames class * extends java.io.Serializable
-keep class com.name.monkey.retromusic.network.model.** { *; }
-keep class com.name.monkey.retromusic.model.** { *; }
-keep class com.google.android.material.bottomsheet.** { *; }