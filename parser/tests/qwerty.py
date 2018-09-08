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
        in_file = open(outPath, 'r')
        out_file = open(expectedPath, 'w')
        for line in in_file:
            out_file.write(line)