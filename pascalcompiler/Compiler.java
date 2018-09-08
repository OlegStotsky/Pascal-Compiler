package pascalcompiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import parser.Parser;
import tokenizer.Token;
import tokenizer.TokenTypes;
import tokenizer.Tokenizer;

public class Compiler {
	private static final int ERR_WRONG_NUMBER_OF_ARGS = -1;

	private static void parseArgs(String[] args) throws Exception {
		System.out.println(args);
		if (args.length < 3) {
			System.out.println("Number of args should be 3");
			System.exit(-1);
		}
		PrintStream outStream = new PrintStream(new FileOutputStream(args[2]));
		System.setOut(outStream);

		if (args[0].toLowerCase().equals("-l")) {
			Tokenizer tokenizer = new Tokenizer(args[1]);
			while (!tokenizer.eof()) {
				try {
					Token token = tokenizer.nextToken();
					token.print();
				} catch (Exception e) {
					System.out.println(e.getMessage());
					System.exit(-1);
				}
			}
			return;
		}
		else if (args[0].toLowerCase().equals("-s")) {
			Parser parser = new Parser(new Tokenizer(args[1]));
			try {
				parser.parse();
				parser.print();
			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.exit(0);
			}
			return;
		}
		else if (args[0].toLowerCase().equals("-h")) {
			System.out.println("2017 Pascal Compiler by Oleg Stotsky");
			System.out.println("Usage: java -jar PascalCompiler.jar [options] [input file] [output file]");
			System.out.println("Options:");
			System.out.println("-l   lexical analysis");
			System.out.println("-s   syntax analysis");
			return;
		}
	}

	public static void main(String[] args) throws Exception {
		parseArgs(args);
		System.exit(0);
	}
}
