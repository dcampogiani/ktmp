# ktmp-exp [![Build Status](https://travis-ci.com/dcampogiani/ktmp.svg?branch=master)](https://travis-ci.com/dcampogiani/ktmp)
Kotlin Multi Platform experiments


## Building and Testing
Common:

    ./gradlew common:build

Android:

    ./gradlew app:testDebugUnitTest app:assembleDebug

iOS:

Open the Xcode project in `native/KotlinIOS`, there's a *Build Phase* script that will do the magic. 🧞

<kbd>Cmd</kbd> + <kbd>B</kbd> to build
<br>
<kbd>Cmd</kbd> + <kbd>U</kbd> to run tests
