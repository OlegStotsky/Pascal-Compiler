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
	
	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.out.println(args.length);
			System.exit(ERR_WRONG_NUMBER_OF_ARGS);
		}
		Parser parser = new Parser(new Tokenizer(args[0]));
		PrintStream outStream = new PrintStream(new FileOutputStream(args[1]));
		System.setOut(outStream);
		try {
			parser.parse();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
