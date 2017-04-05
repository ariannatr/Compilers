
package compiler;


import java.io.PushbackReader;

import compiler.lexer.Lexer;
import compiler.node.Start;
import compiler.parser.Parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

class Main { 

	public static void main(String args[]) throws FileNotFoundException
	{ 
		FileInputStream myfile = null;
		myfile=new FileInputStream("C:\\Users\\span9\\workspace2\\Compilers\\src\\compiler\\myfile.txt");
		System.out.println("Compiler started");
		Parser p = new Parser(new Lexer(new PushbackReader(new InputStreamReader(myfile), 1024)));
        try {
        	PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
            System.setOut(out);
            Start tree = p.parse();
            tree.apply(new CCT());
            out.close();
        } catch (Exception e) 
        {
        	
            e.printStackTrace();
        }
	    System.exit(0);
		}
	

}
