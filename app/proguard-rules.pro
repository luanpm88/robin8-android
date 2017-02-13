# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Program Files\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
        -keepclassmembers class fqcn.of.javascript.interface.for.webview {
           public *;
        }

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

        # 保持哪些类不被混淆
        -keep public class * extends android.app.Fragment
        -keep public class * extends android.app.Activity
        -keep public class * extends android.app.Application
        -keep public class * extends android.app.Service
        -keep public class * extends android.content.BroadcastReceiver
        -keep public class * extends android.content.ContentProvider
        -keep public class * extends android.app.backup.BackupAgentHelper
        -keep public class * extends android.preference.Preference
        -keep public class com.android.vending.licensing.ILicensingService
         #如果引用了v4或者v7包
         -dontwarn android.support.**

        #忽略警告
        -ignorewarning
        #apk 包内所有 class 的内部结构
        -dump class_files.txt
        #未混淆的类和成员
        -printseeds seeds.txt
        #列出从 apk 中删除的代码
        -printusage unused.txt
        #混淆前后的映射
        -printmapping mapping.txt
        #如果不想混淆 keep 掉
        -keep class com.lippi.recorder.iirfilterdesigner.** {*; }
        #友盟
        -keep class com.umeng.**{*;}
        #项目特殊处理代码
        #忽略警告
        -dontwarn com.lippi.recorder.utils**
        #保留一个完整的包
        -keep class com.lippi.recorder.utils.** { *; }
        -keep class com.lippi.recorder.utils.AudioRecorder{*;}

        -keep public class * extends android.view.View {*;}
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

        -keepclassmembers class * {
            public void *ButtonClicked(android.view.View);
        }

        #不混淆资源类
        -keepclassmembers class **.R$* {
            public static <fields>;
        }

        #如果用用到Gson解析包的，直接添加下面这几行就能成功混淆，不然会报错。
        #gson
        -keepattributes Signature
        -keep class sun.misc.Unsafe { *; }
        -keep class com.google.gson.examples.android.model.** { *; }

        #javabean
        -keep class com.robin8.rb.model.** { *; }
        -keep class com.robin8.rb.model.sortlist.** { *; }
        -keep class com.robin8.rb.indiana.model.** { *; }
        -keep class com.robin8.rb.module.first.model.** { *; }
        -keep class com.robin8.rb.module.mine.model.** { *; }
        -keep class com.robin8.rb.module.reword.bean.** { *; }
         -keep class com.robin8.rb.base.** { *; }
          -keep class com.robin8.rb.module.create.model.** { *; }

         #支付宝
         -keep class com.alipay.android.app.IAlixPay{*;}
         -keep class com.alipay.android.app.IAlixPay$Stub{*;}
         -keep class com.alipay.android.app.IRemoteServiceCallback{*;}
         -keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
         -keep class com.alipay.sdk.app.PayTask{ public *;}
         -keep class com.alipay.sdk.app.AuthTask{ public *;}

        #talkingdata
        -dontwarn com.tendcloud.tenddata.**
        -keep class com.tendcloud.** {*;}
        -keep public class com.tendcloud.tenddata.** { public protected *;}
        -keepclassmembers class com.tendcloud.tenddata.**{  public void *(***); }
        -keep class com.talkingdata.sdk.TalkingDataSDK {public *;}
        -keep class com.apptalkingdata.** {*;}

        #sharesdk
        -keep class cn.sharesdk.onekeyshare.**{*;}
        -keep  class cn.sharesdk.**{*;}
        -keep interface cn.sharesdk.**{*;}
        -keep class com.sina.**{*;}
        -keep class **.R$* {*;}
        -keep class **.R{*;}
        -dontwarn cn.sharesdk.**
        -dontwarn **.R$*
        -keep class m.framework.**{*;}
        -keep interface m.framework.** { *; }
        -keep class android.net.http.SslError
        -keep class android.webkit.**{*;}
        -keep class com.mob.tools.utils
        -keep class com.xxx.share.onekey.theme.classic.EditPage

        #okhttp
        -dontwarn com.squareup.okhttp.**
        -keep class com.squareup.okhttp.** { *;}
        -dontwarn okio.**
        #个推
        -dontwarn com.igexin.**
        -keep class com.igexin.**{*;}

         #glide
         -dontwarn com.bumptech.glide.**
         -keep class com.bumptech.glide.**{*;}
         #jackson
         -dontwarn com.fasterxml.jackson.annotation.**
         -keep class com.fasterxml.jackson.annotation.**{*;}
         -dontwarn com.fasterxml.jackson.core.**
         -keep class com.fasterxml.jackson.core.**{*;}
         -dontwarn com.fasterxml.jackson.databind.**
         -keep class com.fasterxml.jackson.databind.**{*;}
         #jjwt
         -dontwarn io.jsonwebtoken.**
         -keep class io.jsonwebtoken.**{*;}
         #nineoldandroids
         -dontwarn com.nineoldandroids.**
         -keep class com.nineoldandroids.**{*;}
         #bouncycastle
         -dontwarn org.bouncycastle.**
         -keep class org.bouncycastle.**{*;}
          #zxing
         -dontwarn com.google.zxing.**
         -keep class com.google.zxing.**{*;}
          #com.mcxiaoke.bus
         -dontwarn com.mcxiaoke.bus.**
         -keep class com.mcxiaoke.bus.**{*;}
         #amap
          -dontwarn com.amap.api.**
          -keep class com.loc.**{*;}
          -keep class com.amap.api.**{*;}
          -keep class com.autonavi.aps.amapapi.model.**{*;}

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


