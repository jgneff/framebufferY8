---
layout: default
title: December 2019
description: JavaFX FramebufferY8 benchmark results from tests run in December 2019.
---

# December 2019

The results of the benchmark tests I ran in December 2019 are grouped by the hardware, operating system, and JDK used to run them.
The [anomalies](#anomalies) I found are organized as follows:

* ARM Cortex-A9
    * Ubuntu 20.04
        * [copyToBuffer 16-bit](#A9-20-copyTo16)
* QEMU ARM VM
    * Ubuntu 20.04
        * [copyToBuffer 16-bit](#VM-20-copyTo16)

The results of the [benchmarks](#benchmarks) are organized as follows:

* ARM Cortex-A8
    * Ubuntu 14.04
        * [AdoptOpenJDK 13](#A8-14-adopt13)
* ARM Cortex-A9
    * Ubuntu 18.04
        * [OpenJDK 11](#A9-18-open11)
        * [AdoptOpenJDK 13](#A9-18-adopt13)
    * Ubuntu 20.04
        * [OpenJDK 11](#A9-20-open11)
        * [AdoptOpenJDK 11](#A9-20-adopt11)
        * [AdoptOpenJDK 12](#A9-20-adopt12)
        * [OpenJDK 13](#A9-20-open13)
        * [AdoptOpenJDK 13](#A9-20-adopt13)
        * [OpenJDK 14](#A9-20-open14)
* QEMU ARM VM
    * Ubuntu 20.04
        * [OpenJDK 11](#VM-20-open11)
        * [AdoptOpenJDK 11](#VM-20-adopt11)
        * [AdoptOpenJDK 12](#VM-20-adopt12)
        * [OpenJDK 13](#VM-20-open13)
        * [AdoptOpenJDK 13](#VM-20-adopt13)
        * [OpenJDK 14](#VM-20-open14)

## Anomalies

One interesting anomaly I found is that the performance when running under JDK 13 or 14 is much slower than when running under JDK 11 or 12.
You can see the difference in the charts and tables below.
I plan to figure out the cause of the performance difference.

### ARM Cortex-A9 / Ubuntu 20.04 / copyToBuffer 16-bit
{:#A9-20-copyTo16}
{% include results.md name="clarahd-focal-copyTo16-2019-12-16" width=718 height=410 %}

### QEMU ARM VM / Ubuntu 20.04 / copyToBuffer 16-bit
{:#VM-20-copyTo16}
{% include results.md name="armfocal-focal-copyTo16-2019-12-17" width=706 height=412 %}

## Benchmarks

I used the following command when running on an ARM Cortex-A9 processor or the QEMU ARM guest virtual machine:

```console
ubuntu@clarahd:~$ java -jar target/benchmarks.jar -p type=large
```

I used the following command when running on an ARM Cortex-A8 processor:

```console
ubuntu@touchc:~$ java -jar target/benchmarks.jar -p type=small
```

### ARM Cortex-A8 / Ubuntu 14.04 / AdoptOpenJDK 13
{:#A8-14-adopt13}
{% include results.md name="touchc-trusty-jdk-13.0.1+9-2019-12-17" width=692 height=410 %}

### ARM Cortex-A9 / Ubuntu 18.04 / OpenJDK 11
{:#A9-18-open11}
{% include results.md name="clarahd-bionic-java-11-openjdk-armhf-2019-12-17" width=685 height=410 %}

### ARM Cortex-A9 / Ubuntu 18.04 / AdoptOpenJDK 13
{:#A9-18-adopt13}
{% include results.md name="clarahd-bionic-jdk-13.0.1+9-2019-12-17" width=685 height=410 %}

### ARM Cortex-A9 / Ubuntu 20.04 / OpenJDK 11
{:#A9-20-open11}
{% include results.md name="clarahd-focal-java-11-openjdk-armhf-2019-12-16" width=685 height=410 %}

### ARM Cortex-A9 / Ubuntu 20.04 / AdoptOpenJDK 11
{:#A9-20-adopt11}
{% include results.md name="clarahd-focal-jdk-11.0.5+10-2019-12-17" width=701 height=410 %}

### ARM Cortex-A9 / Ubuntu 20.04 / AdoptOpenJDK 12
{:#A9-20-adopt12}
{% include results.md name="clarahd-focal-jdk-12.0.2+10-2019-12-17" width=691 height=410 %}

### ARM Cortex-A9 / Ubuntu 20.04 / OpenJDK 13
{:#A9-20-open13}
{% include results.md name="clarahd-focal-java-13-openjdk-armhf-2019-12-16" width=688 height=410 %}

### ARM Cortex-A9 / Ubuntu 20.04 / AdoptOpenJDK 13
{:#A9-20-adopt13}
{% include results.md name="clarahd-focal-jdk-13.0.1+9-2019-12-17" width=685 height=410 %}

### ARM Cortex-A9 / Ubuntu 20.04 / OpenJDK 14
{:#A9-20-open14}
{% include results.md name="clarahd-focal-java-14-openjdk-armhf-2019-12-16" width=685 height=410 %}

### QEMU ARM VM / Ubuntu 20.04 / OpenJDK 11
{:#VM-20-open11}
{% include results.md name="armfocal-focal-java-11-openjdk-armhf-2019-12-17" width=685 height=412 %}

### QEMU ARM VM / Ubuntu 20.04 / AdoptOpenJDK 11
{:#VM-20-adopt11}
{% include results.md name="armfocal-focal-jdk-11.0.5+10-2019-12-17" width=685 height=412 %}

### QEMU ARM VM / Ubuntu 20.04 / AdoptOpenJDK 12
{:#VM-20-adopt12}
{% include results.md name="armfocal-focal-jdk-12.0.2+10-2019-12-17" width=685 height=412 %}

### QEMU ARM VM / Ubuntu 20.04 / OpenJDK 13
{:#VM-20-open13}
{% include results.md name="armfocal-focal-java-13-openjdk-armhf-2019-12-17" width=699 height=412 %}

### QEMU ARM VM / Ubuntu 20.04 / AdoptOpenJDK 13
{:#VM-20-adopt13}
{% include results.md name="armfocal-focal-jdk-13.0.1+9-2019-12-17" width=691 height=412 %}

### QEMU ARM VM / Ubuntu 20.04 / OpenJDK 14
{:#VM-20-open14}
{% include results.md name="armfocal-focal-java-14-openjdk-armhf-2019-12-17" width=689 height=412 %}
