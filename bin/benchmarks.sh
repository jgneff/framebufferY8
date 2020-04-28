#!/bin/bash
# Runs the benchmarks with selected OpenJDK builds
trap exit INT TERM
set -o errexit

host=$(hostname --short)
code=$(lsb_release --short --codename)
arch=$(dpkg --print-architecture)
date=$(date --iso-8601)
jarfile=target/benchmarks.jar

# Ubuntu builds
ubuntu11=/usr/lib/jvm/java-11-openjdk-$arch

# Oracle builds
oracle14=$HOME/opt/jdk-14.0.1

# AdoptOpenJDK builds
adopt11=$HOME/opt/jdk-11.0.7+10
adopt12=$HOME/opt/jdk-12.0.2+10
adopt13=$HOME/opt/jdk-13.0.2+8
adopt14=$HOME/opt/jdk-14.0.1+7

# Examples: -verbose:gc -Xlog:gc* -XX:+PrintCompilation
jvmflags=''

# Example: writeTo..New
filter=''

# Examples: -f 1 -i 1 -wi 1 -p type=large
jmhflags='-p type=large'

jdklist="$adopt11 $adopt12 $adopt13 $adopt14"
for jdk in $jdklist; do
    printf "\n[$(date)] Testing $jdk ...\n"
    $jdk/bin/java -version
    logfile=${host}-${code}-$(basename $jdk)-${date}.log
    time $jdk/bin/java $jvmflags -jar $jarfile $filter $jmhflags -o $logfile
    grep "^Benchmarks" $logfile
done
