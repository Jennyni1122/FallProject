# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/baoyz/Developer/Android/sdk/tools/proguard/proguard-android.txt
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
-ignorewarnings                                     #抑制警告
-keep class com.com.itheima.topline.bean.* *{*;}    #保持实体类不被混淆
-optimizationpasses 5                               #指定代码的压缩级别
-dontusemixedcaseclassnames                         #是否使用大小写混合
-dontpreverify                                      #混淆时是否做预校验
-verbose                                            #混淆时是否记录日志
#指定混淆时采用的算法
-optimizationpasses !code/simplification/arithmetic,!field/*,!class/merging/*

#对于继承Android的四大组件等系统类，保持不被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.* *{*;}
-keepclasseswithmembernames class * {               #保持native不被混淆
       native<methods>;
}
#保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity{
    public void * (android.view.View);
}
#保持枚举类enum不被混淆
-keepclassmembers enum * {
    public static * *[] values();
    public static * *valueOf(java.lang.String);
}
#保持Parcelable的类不被混淆
-keep class * implements android.os.Parcelable{
    public static final android.os.Parcelable&Creator *;
}
#保持继承自View对象中的set/get方法以及初始化方法的方法名不被混淆
-keep public class * extends android.view.View{
    * * * get*();
    void set *(* * *);
    public <init>(android.content.Context);
    public <init>(android.content.Context,android.util.AttributeSet);
    public <init>(android.content.Context,android.util.AttributeSet,int);
}
#对所有类的初始化方法的方法名不进行混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet);
    public <init>(android.content.Context,android.util.AttributeSet,int);
}
#保持Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable{
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
#对于R(资源)下的所有类及其方法，都不能被混淆
-keep class * *.R$*{
*;
}
#对于带有回调函数onXXEvent的，不能被混淆
-keepclassmembers class * {
    void * (* * On * Event);
}
#Webview混淆的处理
-keepclassmembers class fqcn.of.javascript.interface.for.Webview{
    public *;
}

-keepclassmembers class * extends android.webkit.WebViewClient{
    public void * (android.webkit.WebView,java.lang.String,android.graphics.Bitmap);
    public boolean * (android.webkit.WebView,java.lang.String);
}

-keepclassmembers class * extends android.webkit.WebViewClient{
    public void * (android.webkit.WebView,java.lang.String);
}