# ======================================================================
# Makefile - builds bar charts of the benchmark results
# Copyright (C) 2019-2020 John Neffenger
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
# ======================================================================

SHELL = /bin/bash

# Directories of log files and public website
logdir = ../logs
webdir = ../docs

# Commands
LATEXMK  = latexmk
EXIFTOOL = exiftool
MUDRAW   = mutool draw
OPTIPNG  = optipng
INKSCAPE = inkscape
CLEANER  = $(HOME)/opt/svgcleaner-0.9.5/svgcleaner
RSYNC    = rsync

# Command options
LATEXMK_OPTS = -lualatex
MUDRAW_OPTS  = -r 96 -c rgb
OPTIPNG_OPTS = -quiet
CLEANER_OPTS = --indent=0 --remove-declarations=no
RSYNC_OPTS   = --archive --verbose --checksum --ignore-missing-args

# ExifTool options to list the Creative Commons license metadata
exif_xmp := -XMP-cc:all -XMP-dc:all -XMP-xmpRights:all \
    -groupNames1 -veryShort -duplicates

# Grep patterns for the benchmark results from the log files
results     := -e "^Benchmark"
results_new := $(results) -e "To..New"
results_old := $(results) -e "To..Old"

# Sed scripts to create PGFPLOTS data from the log files
sed_log := -e "s/^Benchmark[ ]\{11\}/Benchmark/"
sed_log += -e "s/^Benchmarks\.//"
sed_log += -e "s/Score[ ]*Error/Score x Error/"
sed_new := -e "s/New /    /"
sed_old := -e "s/Old /    /"

# Sed script to add font stack to the SVG files
sed_svg := -e "s/Arimo/Arimo,Liberation Sans,Arial,sans-serif/g"

# Sed scripts to create Markdown pipe tables from the log files
header    := :---------|-------:|-----:|----:|------:|---|------:|-----:
sed_table := -e "/^Benchmark /a $(header)"
sed_table += -e "s/Score[ ]\+Error/Score ± Error/"
sed_table += -e "s/[ ]\+/ | /g"
sed_table += -e "s/^Benchmarks\.//"

# Sed scripts to create Markdown pipe tables from the PGFPLOTS data files
sed_pgfnew := -e '/^Benchmark/d' -e 's/JDK\(1.\) /JDK\1New /'
sed_pgfold := -e '/^Benchmark/d' -e 's/JDK\(1.\) /JDK\1Old /'

# Sed scripts to edit the XMP metadata for the SVG files
sed_xmp := -e "s/x:xmpmeta.*>/metadata>/"

# Lists of sources
tex_list := $(notdir $(basename $(wildcard src/*.tex)))
log_list := $(notdir $(basename $(wildcard $(logdir)/*.log)))
dat_list := $(subst -new,,$(notdir $(basename $(wildcard src/*-new.dat))))

# Lists of image targets
pdf_list := $(addsuffix .pdf,$(tex_list))
png_list := $(addsuffix .png,$(tex_list))
svg_list := $(addsuffix .svg,$(tex_list))
img_list := $(addprefix out/,$(pdf_list) $(png_list) $(svg_list))

# ======================================================================
# Works around the following error in Jekyll 3.0.1 that occurs when
# including a file with the "+" character in its name:
#   Error: Invalid syntax for include tag.
#   File contains invalid characters or sequences:
#       touchc-trusty-jdk-13.0.1+9-2019-12-17.md
# ======================================================================

# Lists of Markdown targets
md_all   := $(addprefix out/,$(addsuffix .md,$(log_list) $(dat_list)))
md_bad   := $(foreach f,$(md_all),$(if $(findstring +,$f),$f,))
md_good  := $(filter-out $(md_bad),$(md_all))
md_fixed := $(foreach f,$(md_bad),$(subst +,_,$f))
md_list  := $(md_good) $(md_fixed)

# $(call rename,filename)
# Creates a rule to rename the file by replacing "+" characters with "_"
define rename
$(subst +,_,$1): $1
	mv $$< $$@
endef

# $(call publish,files,subdir)
# Publish files from the output directory to the website subdirectory
define publish
    $(RSYNC) $(RSYNC_OPTS) $(foreach f,$1,out/$f) $(webdir)/$2/
endef

# ======================================================================
# Pattern Rules
# ======================================================================

tmp/%.dat: $(logdir)/%.log | tmp
	grep $(results) $< | sed $(sed_log) > $@

tmp/%.dat: src/%.dat | tmp
	cp $< $@

tmp/%-new.dat: tmp/%.dat
	grep $(results_new) $< | sed $(sed_new) > $@

tmp/%-old.dat: tmp/%.dat
	grep $(results_old) $< | sed $(sed_old) > $@

tmp/%.pdf: src/%.tex tmp/%-new.dat tmp/%-old.dat chart.tex
	$(LATEXMK) $(LATEXMK_OPTS) -output-directory=$(@D) $<

out/%.pdf: tmp/%.pdf metadata.xmp | out
	$(EXIFTOOL) -tagsFromFile $(word 2,$^) -out - $< > $@

tmp/%.png: out/%.pdf
	$(MUDRAW) $(MUDRAW_OPTS) -o $@ $<

out/%.png: tmp/%.png metadata.xmp
	$(EXIFTOOL) -tagsFromFile $(word 2,$^) -out - $< > $@
	$(OPTIPNG) $(OPTIPNG_OPTS) $@

tmp/%.svg: out/%.pdf
	$(INKSCAPE) --export-plain-svg=$@ $<

tmp/%-clean.svg: tmp/%.svg
	$(CLEANER) $(CLEANER_OPTS) $< $@

out/%.svg: tmp/%-clean.svg tmp/metadata.xml
	sed -e "/<svg/r $(word 2,$^)" $(sed_svg) $< > $@

out/%.md: $(logdir)/%.log | out
	grep $(results) $< | sed $(sed_table) > $@

tmp/%.md: src/%-new.dat src/%-old.dat
	paste -d '\n' \
        <(cat $(word 1,$^) | sed $(sed_pgfnew)) \
        <(cat $(word 2,$^) | sed $(sed_pgfold)) \
        | tr -s ' ' | sed 's/ / | /g' > $@

out/%.md: tmp/%.md header.md
	cat header.md $< > $@

# ======================================================================
# Explicit rules
# ======================================================================

.PHONY: all list pub clean

all: $(img_list) $(md_list)

.SECONDARY: $(md_bad)
$(foreach f,$(md_bad),$(eval $(call rename,$f)))

tmp out:
	mkdir -p $@

tmp/metadata.xml: metadata.xmp
	sed $(sed_xmp) $< > $@

list: $(img_list)
	$(EXIFTOOL) $(exif_xmp) $^

# Add PDF files after enabling reproducible builds (pdfTeX 1.40.17 or later).
pub:
	$(call publish,*.md,_includes)
	$(call publish,*2019-12-??.png *2019-12-??.svg,2019-12)
	$(call publish,*2020-04-??.png *2020-04-??.svg,2020-04)
	$(call publish,*2020-05-??.png *2020-05-??.svg,2020-05)

clean:
	rm -f tmp/* out/*
