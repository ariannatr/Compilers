
package compiler;


import java.io.PushbackReader;

import compiler.lexer.Lexer;
import compiler.node.Start;
import compiler.parser.Parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

class Main { 

	public static void main(String args[]) throws FileNotFoundException
	{ 
		FileInputStream myfile = null;
		myfile=new FileInputStream("C:\\Users\\span9\\workspace2\\Compilers\\src\\compiler\\myfile.txt");
		System.out.println("Compiler started");
		Parser p = new Parser(new Lexer(new PushbackReader(new InputStreamReader(myfile), 1024)));
        try {
            Start tree = p.parse();
            tree.apply(new CCT());
        } catch (Exception e) 
        {
        	
            e.printStackTrace();
        }
	    System.exit(0);
		}
	

}
