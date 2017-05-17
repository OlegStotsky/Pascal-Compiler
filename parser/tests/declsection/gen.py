from subprocess import check_output
import sys

*redundant, name, start, end = sys.argv
for i in range(int(start), int(end)+1):
	check_output("java -jar {0} {1}.in {1}.expected".format(name, i), shell=True)
