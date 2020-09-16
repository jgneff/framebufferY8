---
layout: default
title: December 2019
description: JavaFX FramebufferY8 benchmark results from tests run in December 2019.
---

# December 2019
{:.no_toc}

* Generates table of contents as an unordered list
{:toc}

## Anomalies

One interesting anomaly I found is a large drop in performance of the tests under JDK 13 and 14 compared to the same tests under JDK 11 and 12.

### ARM Cortex-A9

#### Ubuntu 20.04

##### copyTo16New/Old
{% include results.md name="clarahd-focal-copyTo16-2019-12-16" width=727 height=410 %}

### QEMU ARM VM

#### Ubuntu 20.04

##### copyTo16New/Old
{% include results.md name="armfocal-focal-copyTo16-2019-12-17" width=716 height=412 %}

## Benchmarks

The results of the benchmark tests are grouped by the processor, operating system, and build of the Java Development Kit (JDK) on which they ran.

I ran the following command on the ARM Cortex-A8 processor:

```console
$ java -jar target/benchmarks.jar -p type=small
```

I ran the following command on the ARM Cortex-A9 processor and QEMU ARM virtual machine:

```console
$ java -jar target/benchmarks.jar -p type=large
```

### ARM Cortex-A8

#### Ubuntu 14.04

##### AdoptOpenJDK 13
{% include results.md name="touchc-trusty-jdk-13.0.1+9-2019-12-17" width=692 height=410 %}

### ARM Cortex-A9

#### Ubuntu 18.04

##### Ubuntu OpenJDK 11
{% include results.md name="clarahd-bionic-java-11-openjdk-armhf-2019-12-17" width=685 height=410 %}

##### AdoptOpenJDK 13
{% include results.md name="clarahd-bionic-jdk-13.0.1+9-2019-12-17" width=685 height=410 %}

#### Ubuntu 20.04

##### Ubuntu OpenJDK 11
{% include results.md name="clarahd-focal-java-11-openjdk-armhf-2019-12-16" width=685 height=410 %}

##### AdoptOpenJDK 11
{% include results.md name="clarahd-focal-jdk-11.0.5+10-2019-12-17" width=701 height=410 %}

##### AdoptOpenJDK 12
{% include results.md name="clarahd-focal-jdk-12.0.2+10-2019-12-17" width=691 height=410 %}

##### Ubuntu OpenJDK 13
{% include results.md name="clarahd-focal-java-13-openjdk-armhf-2019-12-16" width=688 height=410 %}

##### AdoptOpenJDK 13
{% include results.md name="clarahd-focal-jdk-13.0.1+9-2019-12-17" width=685 height=410 %}

##### Ubuntu OpenJDK 14
{% include results.md name="clarahd-focal-java-14-openjdk-armhf-2019-12-16" width=685 height=410 %}

### QEMU ARM VM

#### Ubuntu 20.04

##### Ubuntu OpenJDK 11
{% include results.md name="armfocal-focal-java-11-openjdk-armhf-2019-12-17" width=685 height=412 %}

##### AdoptOpenJDK 11
{% include results.md name="armfocal-focal-jdk-11.0.5+10-2019-12-17" width=685 height=412 %}

##### AdoptOpenJDK 12
{% include results.md name="armfocal-focal-jdk-12.0.2+10-2019-12-17" width=685 height=412 %}

##### Ubuntu OpenJDK 13
{% include results.md name="armfocal-focal-java-13-openjdk-armhf-2019-12-17" width=699 height=412 %}

##### AdoptOpenJDK 13
{% include results.md name="armfocal-focal-jdk-13.0.1+9-2019-12-17" width=691 height=412 %}

##### Ubuntu OpenJDK 14
{% include results.md name="armfocal-focal-java-14-openjdk-armhf-2019-12-17" width=689 height=412 %}
