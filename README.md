# compose-graalvm

A sample Compose multiplatform project using Swing compiled using native-image.
Currently being worked on, tested only on Linux.

**NOTE:** _I'm currently working on a writeup explaining issues I ran into along the way and how to work with native image in this context for those new to it, stay tuned._

This project was inspired by [esp-er/compose-graal-hello](https://github.com/esp-er/compose-graal-hello), which uses JWM for its windows. I ran into issues when updating it to the latest compose version and so wanted to test compose running on Swing, before trying out JWM or other tools.

## Building

### Native image version
Use [Liberica NIK](https://bell-sw.com/pages/downloads/native-image-kit) on JVM 21 to be able to run the built native image.
JVM 23 (and 24 early access) currently stalls while building, and regular GraalVM will build successfully but needs
extra setup to actually run the image that Liberica does for you.

