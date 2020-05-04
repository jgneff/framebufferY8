---
layout: default
title: May 2020
description: JavaFX FramebufferY8 benchmark results from tests run in May 2020.
---

# May 2020
{:.no_toc}

The results of the benchmark tests are grouped by the processor, operating system, and build of the Java Development Kit (JDK) on which they ran.

* Generates table of contents as an unordered list
{:toc}

## VM Comparisons

### ARM Cortex-A9

#### Ubuntu 20.04

##### copyTo16New/Old
{% include results.md name="clarahd-focal-copyTo16-2020-05-03" width=718 height=410 %}

##### writeTo16New/Old
{% include results.md name="clarahd-focal-writeTo16-2020-05-03" width=716 height=410 %}

##### copyTo32New/Old
{% include results.md name="clarahd-focal-copyTo32-2020-05-03" width=734 height=410 %}

##### writeTo32New/Old
{% include results.md name="clarahd-focal-writeTo32-2020-05-03" width=716 height=410 %}

## Benchmarks

I ran the following command for the benchmark tests:

```console
$ java -jar target/benchmarks.jar -p type=large
```

### ARM Cortex-A9

#### Ubuntu 20.04

##### Ubuntu OpenJDK 11
{% include results.md name="clarahd-focal-java-11-openjdk-armhf-2020-05-03" width=685 height=410 %}

##### AdoptOpenJDK 11
{% include results.md name="clarahd-focal-jdk-11.0.7+10-2020-05-03" width=703 height=409 %}

##### Ubuntu OpenJDK 13
{% include results.md name="clarahd-focal-java-13-openjdk-armhf-2020-05-03" width=685 height=410 %}

##### AdoptOpenJDK 13
{% include results.md name="clarahd-focal-jdk-13.0.2+8-2020-05-03" width=685 height=410 %}

##### Ubuntu OpenJDK 14
{% include results.md name="clarahd-focal-java-14-openjdk-armhf-2020-05-03" width=685 height=410 %}

##### AdoptOpenJDK 14
{% include results.md name="clarahd-focal-jdk-14.0.1+7-2020-05-03" width=703 height=410 %}

### QEMU ARM VM

#### Ubuntu 20.04

##### Ubuntu OpenJDK 11
{% include results.md name="armfocal-focal-java-11-openjdk-armhf-2020-05-03" width=685 height=412 %}

##### AdoptOpenJDK 11
{% include results.md name="armfocal-focal-jdk-11.0.7+10-2020-05-03" width=685 height=412 %}

##### Ubuntu OpenJDK 13
{% include results.md name="armfocal-focal-java-13-openjdk-armhf-2020-05-03" width=699 height=413 %}

##### AdoptOpenJDK 13
{% include results.md name="armfocal-focal-jdk-13.0.2+8-2020-05-03" width=687 height=412 %}

##### Ubuntu OpenJDK 14
{% include results.md name="armfocal-focal-java-14-openjdk-armhf-2020-05-03" width=685 height=412 %}

##### AdoptOpenJDK 14
{% include results.md name="armfocal-focal-jdk-14.0.1+7-2020-05-03" width=702 height=412 %}
