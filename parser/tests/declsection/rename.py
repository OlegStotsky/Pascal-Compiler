from subprocess import check_output

for i in range(29, 36):
	check_output('mv {0}.in {1}.in'.format(i, i-29), shell=True)
	check_output('mv {0}.out {1}.out'.format(i, i-29), shell=True)
