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


# Code confuses compression ratios
-optimizationpasses 5

# The mixed class name is lowercase
-dontusemixedcaseclassnames

# Specifies classes that do not ignore non-public libraries
-dontskipnonpubliclibraryclasses

-verbose

-dontskipnonpubliclibraryclassmembers

-dontpreverify

-keepattributes *Annotation*,InnerClasses

-keepattributes Signature

-keepattributes SourceFile,LineNumberTable

-optimizations !code/simplification/cast,!field/*,!class/merging/*

# No changes are made to the public classes
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService


-keep class android.support.** {*;}

-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**

-keep class **.R$* {*;}

-keepclasseswithmembernames class * {
    native <methods>;
}

# The method parameter left in the Activity is the view's method
# so that onClick that we write in layout won't be affected
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

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
    void *(**On*Event);
    void *(**On*Listener);
}

#-ignorewarnings

#-libraryjars libs/org-netbeans-modules-java-j2seproject-copylibstask.jar

#-libraryjars libs/eclipse.persistence.jpa.jpql_2.5.2.v20140319-9ad6abd.jar

#-libraryjars libs/org.eclipse.persistence.jpa.modelgen_2.5.2.v20140319-9ad6abd.jar

#-libraryjars libs/javaee-doc-api.jar

#-libraryjars libs/javax.annotation-api.jar

#-libraryjars libs/javax.xml.soap-api.jar

#-libraryjars libs/jaxb-api-osgi.jar

#-libraryjars libs/jaxws-api.jar

#-libraryjars libs/jsr181-api.jar

# Put all the entity classes in one package
-keep class com.ghs.ghspm.bean.** { *; }