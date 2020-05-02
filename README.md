This project compares the output and performance of the methods in the new [`FramebufferY8`](src/main/java/com/sun/glass/ui/monocle/FramebufferY8.java) class with those in the old [`Framebuffer`](src/main/java/com/sun/glass/ui/monocle/Framebuffer.java) class.
These classes are part of the Monocle implementation of the Glass windowing component in the [JavaFX Graphics](https://github.com/jgneff/javafx-graphics)  module.
The benchmarks test the methods that copy a 32-bit source image in ARGB32 pixel format into a target image in one of three pixel formats: 8-bit Y8 grayscale, 16-bit RGB565 color, or 32-bit ARGB32 color.

## Results

The results of running the benchmarks on my systems are published on the [website associated with this repository](https://jgneff.github.io/framebufferY8/).

## Licenses

This project is licensed under the [GNU General Public License v3.0](LICENSE) except for the following files, which are licensed by Oracle under the [GNU General Public License v2.0](src/main/java/com/sun/glass/ui/monocle/LICENSE) with the [Classpath Exception](src/main/java/com/sun/glass/ui/monocle/ADDITIONAL_LICENSE_INFO):

* [Framebuffer.java](src/main/java/com/sun/glass/ui/monocle/Framebuffer.java)
* [FramebufferY8.java](src/main/java/com/sun/glass/ui/monocle/FramebufferY8.java)
* [PlatformLogger.java](src/main/java/com/sun/javafx/logging/PlatformLogger.java)
* [Logging.java](src/main/java/com/sun/javafx/util/Logging.java)

The contents of the [website](https://jgneff.github.io/framebufferY8/) associated with this project are licensed under the [Creative Commons Attribution-ShareAlike 4.0 International License](https://creativecommons.org/licenses/by-sa/4.0/).
The website style is based on [Water.css](https://github.com/kognise/water.css).

## Building

This is a Maven project that depends on the [Java Microbenchmark Harness](https://openjdk.java.net/projects/code-tools/jmh/).
You can build and package the application as the file *target/benchmarks.jar* with the commands:

```console
$ export JAVA_HOME=$HOME/opt/jdk-14.0.1
$ mvn package
```

## Running

When the benchmark mode is *SingleShotTime* (ss), the tests save the contents of the source buffer and all target buffers and channels as files in the PNG image format.
Run the tests in this mode with the command:

```console
$ java -jar target/benchmarks.jar -f 1 -bm ss
```

The source image depends on the value of the benchmark *type* parameter as follows:

* **color** - The source image is a fully opaque 24-bit color square of 4,096 × 4,096 pixels with all values of 8-bit red, green, and blue.
Use this value to verify that the methods correctly convert all color values.
* **alpha** - The source image is a 16-bit color square of 256 × 256 pixels with all values of 8-bit alpha and red.
Use this value to verify that the methods correctly convert all alpha values.
* **small** - The source image is a color rectangle of 800 × 600 pixels.
Use this value to measure the frames per second on older ARM devices that have 6-inch displays with 167 pixels per inch.
* **large** - The source image is a color rectangle of 1,448 × 1,072 pixels.
Use this value to measure the frames per second on newer ARM devices that have 6-inch displays with 300 pixels per inch.

The default value of the *type* parameter is *color*.
Change the *type* to *large*, for example, with the command:

```console
$ java -jar target/benchmarks.jar -p type=large
```

Use the Bash script [*bin/compare.sh*](bin/compare.sh) to compare the images created by the new `FramebufferY8` class with those created by the old `Framebuffer` class.
Edit the Bash script [*bin/benchmarks.sh*](bin/benchmarks.sh) to run the tests with different JDK installations on a system.
The script sets the *type* parameter to *large* by default.
