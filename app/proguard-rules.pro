# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/lincoln/Softwares/adt-bundle-mac-x86_64-20140702/sdk/tools/proguard/proguard-android.txt
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

-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

-keep class cn.** { *; }
-keep class com.** { *; }
-keep class belka.** { *; }
-keep class com.** { *; }
-keep class ir.** { *; }
-keep class mehdi.** { *; }
-keep class net.** { *; }
-keep class link.** { *; }
-keep class nl.** { *; }

-dontwarn com.squareup.picasso.**
-dontwarn org.objenesis.instantiator.**

-optimizations !code/simplification/arithmetic, !code/allocation/variable,!code/simplification/cast,!field/*,!class/merging/*