#!/bin/bash
# Runs the benchmarks with the selected builds of OpenJDK
trap exit INT TERM
set -o errexit

arch=$(dpkg --print-architecture)
date=$(date --iso-8601)
host=$(hostname --short)
jarfile=lib/benchmarks.jar

# Oracle builds
oracle13=$HOME/opt/jdk-13.0.1

# Ubuntu builds
ubuntu11=/usr/lib/jvm/java-11-openjdk-$arch
ubuntu13=/usr/lib/jvm/java-13-openjdk-$arch
ubuntu14=/usr/lib/jvm/java-14-openjdk-$arch

# AdoptOpenJDK builds
adopt11=$HOME/opt/jdk-11.0.5+10
adopt12=$HOME/opt/jdk-12.0.2+10
adopt13=$HOME/opt/jdk-13.0.1+9

# Examples: -verbose:gc -Xlog:gc* -XX:+PrintCompilation
jvmflags=''

# Example: writeTo..New
filter=''

# Examples: -f 1 -i 1 -wi 1 -p type=large
jmhflags='-p type=large'

# jdklist="$adopt11 $adopt12 $adopt13"
jdklist="$ubuntu11 $ubuntu13 $ubuntu14"
time for jdk in $jdklist; do
    printf "\n[$(date)] Testing $jdk ...\n"
    $jdk/bin/java -version
    logfile=${host}-${date}-$(basename $jdk).log
    $jdk/bin/java $jvmflags -jar $jarfile $filter $jmhflags -o $logfile
done
