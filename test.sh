#!/bin/sh
# Run benchmark tests for SocialPanther

# Please be sure to set up all the databases first by going into
# sqlplus and launching the ttest.sql file

javac SocialPanther.java
java SocialPanther<benchmarks/bench1.txt
java SocialPanther<benchmarks/bench2.txt
java SocialPanther<benchmarks/bench3.txt
java SocialPanther<benchmarks/bench4.txt
java SocialPanther<benchmarks/bench5.txt
