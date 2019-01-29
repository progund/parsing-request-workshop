#!/bin/bash

find . -name '*~' | xargs -r rm
find . -name '*.class' | xargs -r rm
