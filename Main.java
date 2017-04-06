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
import java.io.FileDescriptor;

class Main { 

	public static void main(String args[]) throws FileNotFoundException
	{ 
		if(args.length <1)
        {	
      			System.err.println("Not enough arguments");
      			System.exit(1);
      	}
      	System.out.println("Have to compile "+args.length+" files ");
		System.out.println("Compiler started");
		PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
		int i;
		for(i=0;i<args.length;i++)		
		{		
			System.out.println("Compiling file "+args[i]);
			FileInputStream myfile = null;
			myfile=new FileInputStream(args[i]);
			Parser p = new Parser(new Lexer(new PushbackReader(new InputStreamReader(myfile), 1024)));
        	try {
	  			System.setOut(out);
    			Start tree = p.parse();
    			tree.apply(new CCT());
        	} catch (Exception e) 
    		{	
    		    e.printStackTrace();
    		}
    		System.out.println("------------------------------------------------------------------------------------------");
    		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
		}
		System.out.println("Finished all the files ");
		out.close();
		System.exit(0);
	}
}
