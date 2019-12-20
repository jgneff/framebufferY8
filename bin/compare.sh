#!/bin/bash
# Compares the images saved by the benchmarks
diff="diff --report-identical-files"

# Compares the new and old output
$diff buffer16new.png buffer16old.png
$diff buffer32new.png buffer32old.png
$diff output16new.png output16old.png
$diff output32new.png output32old.png

# Compares the buffer and channel output
$diff buffer08new.png output08new.png
$diff buffer16new.png output16new.png
$diff buffer32new.png output32new.png

# Compares the source and 32-bit output
$diff argb32.png buffer32new.png
$diff argb32.png buffer32old.png
$diff argb32.png output32new.png
$diff argb32.png output32old.png
