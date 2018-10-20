# Pascal-Compiler
Compiler of Pascal Programming Langauge written in Java. Syntax tree is constructed using the recursive descent. Assembly code generation is still in progress.

## Building:
In order to build the project you need to generate JAR file. In terminal: `bash build.sh`. 

## Running
In order to run jar the jar file you need to run `java -jar compiler.jar`.

## 🔨 Commands

<!-- commands -->
* `java -jar compiler.jar -h`
* `java -jar compiler.jar -l [input file] [output file]`
* `java -jar compiler.jar -s [input file] [output file]`

## `java -jar compiler.jar -h`

Displays credentials and avaliable commands

```
USAGE
  $ java -jar compiler.jar -h

ARGUMENTS
  NAME  name of command
```

## `java -jar compiler.jar -l [input file] [output file]`

Performs lexical analysis of the input file and writes the result to output file.

```
USAGE
  $ java -jar compiler.jar -l [input file] [output file]

ARGUMENTS
  input file  path to input file
  output file path to output file
 ```
 
 
## `java -jar compiler.jar -s [input file] [output file]`

Performs syntaxis analysis of the input file and writes the resulting syntax tree to output file.

```
USAGE
  $ java -jar compiler.jar -s [input file] [output file]

ARGUMENTS
  input file  path to input file
  output file path to output file
 ```
