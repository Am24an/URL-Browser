# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# WebView with JavaScript
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebChromeClient {
    public void *(android.webkit.WebView, int);
}

# Room Database - Only your app's data classes
-keep class com.aman.urlbrowser.data.local.UrlHistoryEntity { *; }
-keep class com.aman.urlbrowser.data.local.UrlHistoryDao { *; }
-keep class com.aman.urlbrowser.data.local.UrlHistoryDatabase { *; }

# Retrofit - Only your API classes
-keepattributes Signature
-keepattributes RuntimeVisibleAnnotations
-keep class com.aman.urlbrowser.data.remote.ApiService { *; }
-keep class com.aman.urlbrowser.data.remote.HistoryUploadRequest { *; }
-keep class com.aman.urlbrowser.data.remote.UrlItem { *; }
-keep class com.aman.urlbrowser.data.remote.HistoryUploadResponse { *; }

# ViewBinding - Only your app's binding classes
-keep class com.aman.urlbrowser.databinding.** { *; }

# ViewModel - Only your app's ViewModels
-keep class com.aman.urlbrowser.ui.home.HomeViewModel { *; }
-keep class com.aman.urlbrowser.ui.webview.WebViewViewModel { *; }
-keep class com.aman.urlbrowser.ui.history.HistoryViewModel { *; }

# Sealed classes for state management
-keep class com.aman.urlbrowser.utils.ValidationResult { *; }
-keep class com.aman.urlbrowser.utils.ValidationResult$* { *; }
-keep class com.aman.urlbrowser.ui.webview.NavigationType { *; }
-keep class com.aman.urlbrowser.ui.webview.NavigationType$* { *; }
-keep class com.aman.urlbrowser.ui.history.UploadStatus { *; }
-keep class com.aman.urlbrowser.ui.history.UploadStatus$* { *; }

# Dots Indicator library
-keep class com.tbuonomo.viewpagerdotsindicator.** { *; }

# Debugging
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
