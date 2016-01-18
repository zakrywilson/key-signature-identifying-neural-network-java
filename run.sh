#!/bin/sh

clear
rm *.class
javac *.java
java Manager $@
