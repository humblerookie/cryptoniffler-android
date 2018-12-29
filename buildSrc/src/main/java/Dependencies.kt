object Version {
    val min_sdk = 21
    val target_sdk = 27
    val compile_sdk = 27

    val version_code = 37
    val version_name = "2.3.1"

    val kotlin = "1.2.61"
    val support = "27.1.1"
    val rxandroid = "2.1.0"
    val rxkotlin = "2.2.0"
    val constraint = "1.1.0"
    val multidex = "1.0.2"
    val android_testrunner = "1.0.2"
    val espresso = "3.0.2"
    val junit = "4.12"
    val android_gradle = "3.3.0-alpha05"
    val rxbinding = "2.1.1"
    val firebase = "11.4.0"
    val crashlytics = "2.9.5"
    val retrofit = "2.3.0"
    val moshi = "1.5.0"
    val picasso = "2.5.2"
    val picasso_downloader = "1.1.0"
    val traceur = "1.0.1"
    val dagger = "2.11"
    val shimmer = "0.1.0@aar"
    val showcase = "1.0.0"
    val autofit = "0.2.1"
    val dialogs = "0.9.6.0"
    val timber = "4.5.1"
    val room = "1.1.0"
    val leakcanary = "1.6.1"
    val bugsnag = "4.6.1"
    val charting_lib = "v3.0.3"


}


object Deps {

    val android_gradle = "com.android.tools.build:gradle:${Version.android_gradle}"
    val kotlin_gradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlin}"
    val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Version.kotlin}"

    val constraint = "com.android.support.constraint:constraint-layout:${Version.constraint}"
    val cardview = "com.android.support:cardview-v7:${Version.support}"
    val appcompat = "com.android.support:appcompat-v7:${Version.support}"
    val design = "com.android.support:design:${Version.support}"
    val recyclerview = "com.android.support:recyclerview-v7:${Version.support}"
    val support_annotation = "com.android.support:support-annotations:${Version.support}"
    val multidex = "com.android.support:multidex:${Version.multidex}"


    val firebase_core = "com.google.firebase:firebase-core:${Version.firebase}"
    val firebase_config = "com.google.firebase:firebase-config:${Version.firebase}"
    val firebase_messaging = "com.google.firebase:firebase-messaging:${Version.firebase}"
    val firebase_performance = "com.google.firebase:firebase-perf:${Version.firebase}"
    val bugsnag = "com.bugsnag:bugsnag-android:${Version.bugsnag}"

    val retrofit = "com.squareup.retrofit2:retrofit:${Version.retrofit}"
    val moshi = "com.squareup.moshi:moshi-kotlin:${Version.moshi}"
    val picasso = "com.squareup.picasso:picasso:${Version.picasso}"
    val picasso_downloader = "com.jakewharton.picasso:picasso2-okhttp3-downloader:${Version.picasso_downloader}"
    val converter = "com.squareup.retrofit2:converter-moshi:${Version.retrofit}"

    val rx_call_adapter = "com.squareup.retrofit2:adapter-rxjava2:${Version.retrofit}"
    val rxandroid = "io.reactivex.rxjava2:rxandroid:${Version.rxandroid}"
    val rxkotlin = "io.reactivex.rxjava2:rxkotlin:${Version.rxkotlin}"
    val rxbindings = "com.jakewharton.rxbinding2:rxbinding:${Version.rxbinding}"


    val dagger_java = "com.google.dagger:dagger:${Version.dagger}"
    val dagger_android = "com.google.dagger:dagger-android:${Version.dagger}"
    val dagger_support = "com.google.dagger:dagger-android-support:${Version.dagger}"

    val shimmer = "com.facebook.shimmer:shimmer:${Version.shimmer}"
    val showcase = "com.github.faruktoptas:FancyShowCaseView:${Version.showcase}"
    val autofit = "me.grantland:autofittextview:${Version.autofit}"
    val dialogs = "com.afollestad.material-dialogs:commons:${Version.dialogs}"

    val timber = "com.jakewharton.timber:timber:${Version.timber}"
    val traceur = "com.tspoon.traceur:traceur:${Version.traceur}"

    val room = "android.arch.persistence.room:runtime:${Version.room}"
    val room_compiler = "android.arch.persistence.room:compiler:${Version.room}"


    val dagger_java_compiler = "com.google.dagger:dagger-compiler:${Version.dagger}"
    val dagger_android_compiler = "com.google.dagger:dagger-android-processor:${Version.dagger}"
    val leakcanary_debug = "com.squareup.leakcanary:leakcanary-android:${Version.leakcanary}"
    val leakcanary_release = "com.squareup.leakcanary:leakcanary-android-no-op:${Version.leakcanary}"

    val junit = "junit:junit:${Version.junit}"
    val espresso = "com.android.support.test.espresso:espresso-core:${Version.espresso}"
    val android_test_runner = "com.android.support.test:runner:${Version.android_testrunner}"

    val charting_lib = "com.github.PhilJay:MPAndroidChart:${Version.charting_lib}"

    val rx = listOf(rxandroid, rxkotlin, rxbindings, rx_call_adapter)
    val support = listOf(appcompat, recyclerview, constraint, support_annotation, cardview, design, multidex)
    val firebase = listOf(firebase_core, firebase_messaging, firebase_config, firebase_performance, bugsnag)
    val network = listOf(retrofit, moshi, picasso, picasso_downloader, converter)
    val dagger = listOf(dagger_java, dagger_android, dagger_support)
    val storage = listOf(room)
    val custom_ui = listOf(shimmer, showcase, autofit, dialogs)
    val logging = listOf(timber, traceur)
    val annotation_procs = listOf(dagger_android_compiler, dagger_java_compiler, room_compiler)


}