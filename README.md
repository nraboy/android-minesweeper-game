# Minesweeper for Android

This is a classic Minesweeper game for Android.  It allows for a game board of any size, with a mine count of whatever you define.  This version includes a way to cheat and reveal mine locations without ending the game.

## Requirements

* [Gradle](https://gradle.org/)
* Android Development Kit (ADK) 19

## Installation

After downloading or cloning this project, navigate to the `local.properties` file and replace the path with your own local Android SDK path.

From the command prompt or terminal, with the project as your current working directory, run:

```
./gradlew assemble
```

You'll end up with a binary APK file in your **build/output/apk** directory that can be installed to a device or simulator by using the following command:

```
adb install -r build/output/apk/Minesweeper-debug.apk
```

Of course, the device or simulator must be on / plugged in.

## Credits

Nic Raboy

* [https://www.twitter.com/nraboy](https://www.twitter.com/nraboy)
* [http://www.nraboy.com](http://www.nraboy.com)

## Resources

Nic Raboy's Code Blog - [https://blog.nraboy.com](https://blog.nraboy.com)
