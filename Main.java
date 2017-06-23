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
     // 	System.out.println("Have to compile "+args.length+" files ");
	//	System.out.println("Compiler started");
		PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
		int i;
		for(i=0;i<args.length;i++)		
		{		
			//System.out.println("Compiling file "+args[i]);
			FileInputStream myfile = null;
			myfile=new FileInputStream(args[i]);
			Parser p = new Parser(new Lexer(new PushbackReader(new InputStreamReader(myfile), 1024)));
        	try {
	  			//System.setOut(out);
	  		//	System.out.println("Semantic Check of file "+args[i]+" !");
    			Start tree = p.parse();
    			Collector symboltable= new Collector();
    			symboltable.create_standar_library();
    			tree.apply(symboltable);
    			symboltable.print_errors();
    			if(symboltable.error.equals(""))
    			{
    		//		System.out.println("Converting Code of file "+args[i]+" !");
    				LoweringCode converter= new LoweringCode(symboltable.current,symboltable.standar_library);
    				tree.apply(converter);
    			//	converter.help.print_instructions();
    				AssemblyCreator ascreator= new AssemblyCreator(converter.help.instruction_list,symboltable.current,symboltable.standar_library,converter.library_calls);
    				ascreator.produce();
    				ascreator.print_final();
    			}
    			else
    				System.out.println("Couldn't convert code,because there were semantic errors !\n");
        	} catch (Exception e) 
    		{	
    		    e.printStackTrace();
    		}
    		//System.out.println("------------------------------------------------------------------------------------------");
    		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
		}
		System.out.println("Finished all the files ");
		out.close();
		System.exit(0);
	}
}

