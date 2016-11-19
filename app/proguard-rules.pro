# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/jiang/devtools/android-sdk-macosx/tools/proguard/proguard-android.txt
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

# webview
#
#-keepclassmembers class com.rabbit.android.js.JavaScriptInterface {
#  public *;
#}

-keepattributes *Annotation*
-keepattributes *JavascriptInterface*

#####################记录生成的日志数据,gradle build时在本项目根目录输出################
#apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt


#指定代码的压缩级别
-optimizationpasses 5
#包明不混合大小写
-dontusemixedcaseclassnames
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
#指定不去忽略包可见的库类的成员。
-dontskipnonpubliclibraryclassmembers
#不进行预校验
-dontpreverify
#忽略警告
-ignorewarnings
 # 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#优化时允许访问并修改有修饰符的类和类的成员
-allowaccessmodification

#保护注解
-keepattributes *Annotation*

#避免混淆泛型 如果混淆报错建议关掉
-keepattributes Signature

#混淆时是否记录日志
-verbose


-libraryjars libs/armeabi/libbtjni.so
-libraryjars libs/armeabi/libnfcjni.so
-libraryjars libs/armeabi/libotgjni.so
-libraryjars libs/armeabi/libwltdecode.so
-libraryjars libs/armeabi/libyoutulivecheck.so

#Vx兼容包
-dontwarn android.support.**
-keep class android.support.** { *; }
-keep public class * extends android.support.**


#不混淆资源类
-keep class **.R$* {   *;  }


#所有实体类所在包
-keep class com.tenglv.gate.ui.facedetector.bean.**{ *; }

-keep class com.tencent.youtulivecheck.** {*;}


-keep public class * extends android.app.Activity
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.support.v7.app.AppCompatActivity
#-keep public class * extends android.support.v4.app.FragmentActivity
#-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
     *** get*();
}

#保持 native 方法不被混淆
 -keepclasseswithmembernames class * {
     native <methods>;
 }

#保持自定义控件类不被混淆
 -keepclasseswithmembers class * {
     public <init>(android.content.Context, android.util.AttributeSet);
 }

#保持自定义控件类不被混淆
 -keepclasseswithmembers class * {
     public <init>(android.content.Context, android.util.AttributeSet, int);
 }

#保持自定义控件类不被混淆
 -keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
 }

#保持 Parcelable 不被混淆
 -keep class * implements android.os.Parcelable {
   public static final android.os.Parcelable$Creator *;
 }

#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable
#保持 Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#保持枚举 enum 类不被混淆 如果混淆报错，建议直接使用上面的 -keepclassmembers class * implements java.io.Serializable即可
#-keepclassmembers enum * {
#  public static **[] values();
#  public static ** valueOf(java.lang.String);
#}
-keepclassmembers class * {
    public void *ButtonClicked(android.view.View);
}



# Gson
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.examples.android.model.** { *; }


#okhttp
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *;}
-dontwarn okio.**


#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
 @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
@butterknife.* <methods>;
}


-keep class org.apache.http.** {*;}
-dontwarn org.apache.http.**

-keep class uk.co.senab.photoview.** { *; }
-dontwarn uk.co.senab.photoview.**


#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.integration.okhttp.OkHttpGlideModule
-keep public class * implements com.bumptech.glide.module.GlideModule

#-keepnames class com.mypackage.MyGlideModule
# or more generally:
-keep public class * implements com.bumptech.glide.module.GlideModule

#retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

#RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

#OkHttp3
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**



# observablescrollview：tab fragment
-keep class com.github.ksoichiro.** { *; }
-keep interface com.github.ksoichiro.** { *; }
-dontwarn com.github.ksoichiro.**


#==================gson==========================
-dontwarn com.google.**
-keep class com.google.gson.** {*;}

#==================protobuf======================
-dontwarn com.google.**
-keep class com.google.protobuf.** {*;}

#otto
-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}
#
##=============ucrop============
#-dontwarn com.yalantis.ucrop**
#-keep class com.yalantis.ucrop** { *; }
#-keep interface com.yalantis.ucrop** { *; }
