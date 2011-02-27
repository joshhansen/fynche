#!/bin/bash

./commandExample.sh > experiments/labelSwitching/runWithNoBurnSamples.txt

ruby experiments/labelSwitching/makeMovie.rb experiments/labelSwitching/runWithNoBurnSamples.txt experiments/labelSwitching/noBurn.avi experiments/labelSwitching/noBurnTemp
