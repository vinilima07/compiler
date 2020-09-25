ALL:
	find ../ -name "*.class" -type f -delete
	javac Main.java
	java Main ../test/test$(TEST).s

# Run "$ make TEST=?" to execute the parser.
# ? is the test file number. 