import sys

redundant, start, end = sys.argv

start, end = int(start), int(end)
for i in range(start, end+1):
	in_file = open('{}.out'.format(i), 'r')
	out_file = open('{}.expected'.format(i), 'w')
	for line in in_file:
		out_file.write(line)