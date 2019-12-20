# FramebufferY8 Benchmarks

This project compares the output and performance of the methods in the newer [FramebufferY8](src/main/java/com/sun/glass/ui/monocle/FramebufferY8.java) class with those in the older [Framebuffer](src/main/java/com/sun/glass/ui/monocle/Framebuffer.java) class.
These classes are part of the Monocle implementation of the Glass windowing component in the [JavaFX Graphics](https://github.com/jgneff/javafx-graphics) module.
The tests invoke the methods that convert and copy a 32-bit source image in ARGB32 pixel format into a target image in one of three pixel formats: 8-bit Y8 grayscale, 16-bit RGB565 color, or 32-bit ARGB32 color.

## Licenses

This project is licensed under the [GNU General Public License v3.0](https://choosealicense.com/licenses/gpl-3.0/) except for the following files, which are licensed by Oracle under the [GNU General Public License v2.0](src/main/java/com/sun/glass/ui/monocle/LICENSE) with the [Classpath Exception](src/main/java/com/sun/glass/ui/monocle/ADDITIONAL_LICENSE_INFO):

* [Framebuffer.java](src/main/java/com/sun/glass/ui/monocle/Framebuffer.java)
* [FramebufferY8.java](src/main/java/com/sun/glass/ui/monocle/FramebufferY8.java)
* [PlatformLogger.java](src/main/java/com/sun/javafx/logging/PlatformLogger.java)
* [Logging.java](src/main/java/com/sun/javafx/util/Logging.java)

## Building

This is a Maven project that depends on the [Java Microbenchmark Harness](https://openjdk.java.net/projects/code-tools/jmh/).
You can build and package the application as the file *target/benchmarks.jar* with the commands:

```console
john@tower:~/src/framebufferY8$ export JAVA_HOME=$HOME/opt/jdk-13.0.1
john@tower:~/src/framebufferY8$ mvn package
```

## Running

When the benchmark mode is *SingleShotTime* (ss), this class saves the content of the source buffer and all target buffers and channels as files in the PNG image format.
Run the tests in this mode with the command:

```console
ubuntu@clarahd:~$ java -jar target/benchmarks.jar -f 1 -bm ss
```

The source image depends on the value of the benchmark *type* parameter as follows:

* **color** - The source image is a fully opaque 24-bit color square of 4,096 × 4,096 pixels with all values of 8-bit red, green, and blue.
Use this value to verify that the methods correctly convert all color values.
* **alpha** - The source image is a 16-bit color square of 256 × 256 pixels with all values of 8-bit alpha and red.
Use this value to verify that the methods correctly convert all alpha values.
* **small** - The source image is a color rectangle of 800 × 600 pixels.
Use this value to measure the frames per second on older ARM devices with 6-inch displays at 167 pixels per inch.
* **large** - The source image is a color rectangle of 1,448 × 1,072 pixels.
Use this value to measure the frames per second on newer ARM devices with 6-inch displays at 300 pixels per inch.

The default value of the *type* parameter is *color*.
Change the *type* to *large*, for example, with the command:

```console
ubuntu@clarahd:~$ java -jar target/benchmarks.jar -p type=large
```

## Results

The output files of the tests are found under the *log* directory grouped by the Ubuntu version used to run them:

* **trusty** - Ubuntu Base 14.04.6 LTS (Trusty Tahr)
* **xenial** - Ubuntu Base 16.04.6 LTS (Xenial Xerus)
* **bionic** - Ubuntu Base 18.04.3 LTS (Bionic Beaver)
* **focal** - Ubuntu Base 20.04 LTS (Focal Fossa) Daily Build

The names of the log files are in the format *host-date-jvm*.log, where *host* is one of the following:

* **touchc** - Kobo Touch Model N905C with an 800 MHz i.MX507 ARM Cortex-A8 processor and 256 MiB of RAM
* **clarahd** - Kobo Clara HD Model N249 with a 1.0 GHz i.MX6SLL ARM Cortex-A9 processor and 512 MiB of RAM
* **armfocal** - QEMU virtual machine with 1 CPU and 1024 MiB of RAM running the *armv7l* architecture emulated by *qemu-system-arm* on a Dell Precision Tower 3420 workstation with a 3.3 GHz Intel Xeon Processor E3-1225 v5
* **tower** - Dell Precision Tower 3420 workstation with a 3.3 GHz Intel Xeon Processor E3-1225 v5 and 16 GiB of RAM

and where *jvm* is one of the following (with *arch* either *armhf* or *amd64*):

* **java-11-openjdk-_arch_** - Ubuntu build of OpenJDK 11
* **java-13-openjdk-_arch_** - Ubuntu build of OpenJDK 13
* **java-14-openjdk-_arch_** - Ubuntu build of OpenJDK 14
* **jdk-11.0.5+10** - AdoptOpenJDK build of OpenJDK 11
* **jdk-12.0.2+10** - AdoptOpenJDK build of OpenJDK 12
* **jdk-13.0.1+9** - AdoptOpenJDK build of OpenJDK 13
* **jdk-13.0.1** - Oracle build of OpenJDK 13
