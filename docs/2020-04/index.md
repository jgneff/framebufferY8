---
layout: default
title: April 2020
description: JavaFX FramebufferY8 benchmark results from tests run in April 2020.
---

# April 2020
{:.no_toc}

The results of the benchmark tests are grouped by the processor, operating system, and build of the Java Development Kit (JDK) on which they ran.

* Generates table of contents as an unordered list
{:toc}

## Anomalies

### QEMU ARM VM

The [anomalies in performance](../2019-12/#anomalies) that I found in December 2019 on ARM processors seems to be fixed in the latest version 14.0.1 release of JDK 14.

#### Ubuntu 20.04

##### copyTo16New/Old
{% include results.md name="armfocal-focal-copyTo16-2020-04-27" width=707 height=412 %}

##### writeTo16New/Old
{% include results.md name="armfocal-focal-writeTo16-2020-04-27" width=713 height=412 %}

##### copyTo32New/Old
{% include results.md name="armfocal-focal-copyTo32-2020-04-27" width=722 height=413 %}

##### writeto32New/Old
{% include results.md name="armfocal-focal-writeTo32-2020-04-27" width=709 height=412 %}

### Intel Xeon Processor

Notice that the performance on an Intel Xeon processor is very different from the performance of the same methods on an ARM processor.

#### Ubuntu 20.04

##### writeTo16New/Old
{% include results.md name="focal-focal-writeTo16-2020-04-28" width=717 height=410 %}

##### writeTo32New/Old
{% include results.md name="focal-focal-writeTo32-2020-04-28" width=727 height=412 %}

## Benchmarks

I ran the following command for the benchmark tests:

```console
$ java -jar target/benchmarks.jar -p type=large
```

### QEMU ARM VM

#### Ubuntu 20.04

##### AdoptOpenJDK 11
{% include results.md name="armfocal-focal-jdk-11.0.7+10-2020-04-27" width=685 height=412 %}

##### AdoptOpenJDK 12
{% include results.md name="armfocal-focal-jdk-12.0.2+10-2020-04-27" width=685 height=412 %}

##### AdoptOpenJDK 13
{% include results.md name="armfocal-focal-jdk-13.0.2+8-2020-04-27" width=695 height=412 %}

##### AdoptOpenJDK 14
{% include results.md name="armfocal-focal-jdk-14.0.1+7-2020-04-27" width=700 height=412 %}

### Intel Xeon Processor

#### Ubuntu 20.04

##### Ubuntu OpenJDK 11
{% include results.md name="focal-focal-java-11-openjdk-amd64-2020-04-28" width=685 height=412 %}

##### AdoptOpenJDK 11
{% include results.md name="focal-focal-jdk-11.0.7+10-2020-04-28" width=685 height=412 %}

##### AdoptOpenJDK 12
{% include results.md name="focal-focal-jdk-12.0.2+10-2020-04-28" width=685 height=412 %}

##### AdoptOpenJDK 13
{% include results.md name="focal-focal-jdk-13.0.2+8-2020-04-28" width=685 height=412 %}

##### AdoptOpenJDK 14
{% include results.md name="focal-focal-jdk-14.0.1+7-2020-04-28" width=685 height=412 %}

##### Oracle OpenJDK 14
{% include results.md name="focal-focal-jdk-14.0.1-2020-04-28" width=685 height=412 %}
