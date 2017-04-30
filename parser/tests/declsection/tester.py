from subprocess import check_output

for x in range(1, 28):
	print(x)
	path = './'
	result = check_output('java -jar ./PascalCompiler.jar' +
		' ' + path + str(x) + '.in' + ' ' + path + str(x) + '.out', shell=True)
	
	out = open(path+str(x)+'.out', 'r')
	expected = open(path+str(x)+'.expected', 'r')
	out_tokens = out.read().split()
	expected_tokens = expected.read().split()
	if (out_tokens == expected_tokens):
		print('OK')
	else:
		print(x)
		for y in range(0, min(len(out_tokens), len(expected_tokens))):
			if (out_tokens[y] != expected_tokens[y]):
				print('got:{0} expected:{1}'.format(out_tokens[y], expected_tokens[y]))