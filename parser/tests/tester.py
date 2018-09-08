import os
import fnmatch
import re
from subprocess import check_output

cwd = os.getcwd()
compilerPath = os.path.join(cwd, '..', '..', "compiler.jar")

for root, dir, files in os.walk("."):
    for file in fnmatch.filter(files, "*.in"):
        file_name = re.match(r"(\w+)\.in", file).group(1)
        inPath = os.path.join(os.path.join(os.getcwd(), root), file)
        outPath = os.path.join(os.path.join(os.getcwd(), root), file_name + ".out")
        expectedPath = os.path.join(os.path.join(os.getcwd(), root), file_name + ".expected")
        result = check_output(" ".join(["java", "-jar", compilerPath, "-s", inPath, outPath]), shell=True)
        out = open(outPath, 'r')
        expected = open(expectedPath, 'r')
        out_tokens = out.read().split()
        expected_tokens = expected.read().split()
        if (out_tokens == expected_tokens):
            print(outPath, ' ---OK')
        else:
            print(outPath, ' ---FAIL')
            for y in range(0, min(len(out_tokens), len(expected_tokens))):
                if (out_tokens[y] != expected_tokens[y]):
                    print('got:{0} expected:{1}'.format(out_tokens[y], expected_tokens[y]))
            raise Exception('Failed')