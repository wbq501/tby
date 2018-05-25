# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\sdk/tools/proguard/proguard-android.txt
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

#指定代码的压缩级别
-optimizationpasses 5
#包明不混合大小写
-dontusemixedcaseclassnames
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
#优化  不优化输入的类文件
-dontoptimize
#预校验
-dontpreverify
#混淆时是否记录日志
-verbose
# 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#保护注解
-keepattributes *Annotation*

-keepattributes InnerClasses
-keepattributes EnclosingMethod
-dontoptimize
-dontwarn InnerClasses

#保持哪些类不被混淆

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.view.ActionProvider
-keep class android.support.design.widget.** { *; }
-keep interface android.support.design.widget.** { *; }

-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.**

-keep class **.R$* { *; }

-dontwarn javax.**
-dontwarn lombok.**
-dontwarn org.apache.**
-dontwarn com.squareup.**
-dontwarn com.sun.**
-dontwarn **retrofit**
-dontwarn okio.**

-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

#忽略警告
-ignorewarning
##记录生成的日志数据,gradle build时在本项目根目录输出##
#apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt
########记录生成的日志数据，gradle build时 在本项目根目录输出-end######

#如果引用了v4或者v7包
-dontwarn android.support.**

-keep public class * extends android.view.View {
      public <init>(android.content.Context);
      public <init>(android.content.Context, android.util.AttributeSet);
      public <init>(android.content.Context, android.util.AttributeSet, int);
      public void set*(...);
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
-keepnames class * implements android.os.Parcelable
#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
  !static !transient <fields>;
  !private <fields>;
  !private <methods>;
   private void writeObject(java.io.ObjectOutputStream);
   private void readObject(java.io.ObjectInputStream);
   java.lang.Object writeReplace();
   java.lang.Object readResolve();
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
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}

#去掉Log打印
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

#-libraryjars libs/sqlcipher.jar
-dontwarn net.sqlcipher.**
-keep class net.sqlcipher.** { *; }
#-keep class example.** { *; }
#-dontwarn example.**

#######==============qiniu=============================
-keep class com.qiniu.**{*;}
-keep class com.qiniu.**{public <init>();}
-ignorewarnings

#######==============Zxing=============================
-keep class com.google.zxing.** { *; }

#######=========Gson==============
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.google.gson.** { *;}
-keep class com.google.gson.examples.android.model.** { *; }

########==============HTTP ==============
-dontnote android.net.http.*
-dontnote org.apache.commons.codec.**
-dontnote org.apache.http.**
-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**
-keep class android.net.http.** { *; }
-dontwarn android.net.http.**

########=============GreenDao===================================
-keepclassmembers class ** {
    public void onEvent*(**);
}
## Only required if you use AsyncExecutor
#-keepclassmembers class * extends de.greenrobot.event.util.ThrowableFailureEvent {
#    public <init>(java.lang.Throwable);
#}
-dontwarn de.greenrobot.event.util.*$Support
-dontwarn de.greenrobot.event.util.*$SupportManagerFragment
-keep class de.greenrobot.dao.** {*;}
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
    public static java.lang.String TABLENAME;
}
-keep class **$Properties

########--------Retrofit + RxJava -- OKHttp3==============
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-dontwarn sun.misc.Unsafe
-dontwarn com.octo.android.robospice.retrofit.RetrofitJackson**
-dontwarn retrofit.appengine.UrlFetchClient

-keepattributes Signature
-keepattributes Exceptions
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}
-keep class com.google.gson.** { *; }
-keep class com.google.inject.** { *; }
-keep class org.apache.http.** { *; }
-keep class org.apache.james.mime4j.** { *; }
-keep class javax.inject.** { *; }
-keep class retrofit.** { *; }
-dontwarn org.apache.http.**
-dontwarn android.net.http.AndroidHttpClient
-dontwarn retrofit.**
-dontwarn sun.misc.**
-dontwarn com.squareup.okhttp.**

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
   long producerNode;
   long consumerNode;
}

#########Web相关##################
-keepattributes *Annotation*
-keepattributes *JavascriptInterface*

########环信################
-keep class com.hyphenate.** {*;}
-dontwarn  com.hyphenate.**

#########Glid##################
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

###################################Alipay Begin###################################
-libraryjars libs/alipaySdk-20170725.jar

-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
###################################Alipay End###################################

###################################Model Start###################################
-keep class com.hy.imp.main.domain.model.db.** { *; }
-keep class com.baigu.dms.domain.netservice.common.model.** { *;}
-keep class com.baigu.dms.domain.netservice.response.** { *;}
-keep class com.baigu.dms.domain.file.** { *;}
###################################Model End###################################