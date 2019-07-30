# ktmp
Kotlin Multi Platform experiments


## Building and Testing
Common:

    ./gradlew common:build

Android:

    ./gradlew app:testDebugUnitTest app:assembleDebug

iOS:

Open the XCode project in `native/KotlinIOS` and build it, there's a *Build Phase* script that will do the magic 🧞

<kbd>Cmd</kbd> + <kbd>U</kbd> will run the tests
