package compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class AssemblyCreator{
	
	ArrayList<String> lowering_code;
	ArrayList<String> final_code;
	HashMap<String, Integer> rmap ;
	public AssemblyCreator(ArrayList<String> lc)
	{
		lowering_code=lc;
		final_code=new ArrayList<String>();
		rmap = new HashMap<String, Integer>();	
	}
	
	public void produce()
	{
		Iterator<String> itr=lowering_code.iterator();
		while(itr.hasNext())
		{
			String command=itr.next();
			String [] token=command.split(",");
			String []temp=token[0].split(" ");
			token[0]=temp[1];
		
			if ("unit".equals(token[0])) {	
				 String code_line="push ebp\n";
				 String code_line2="mov ebp, esp\n";
				 final_code.add(code_line);
				 final_code.add(code_line2);
			}
			else if (":=".equals(token[0])) {
				
			}
			else if ("array".equals(token[0])) {
			}
			else if ("*".equals(token[0])) {
			}
			else if ("+".equals(token[0])) {
				String reg="";
				
				String code_line="add eax,"+reg+"\n";
				final_code.add(code_line);
			}
			else if ("-".equals(token[0])) {
				String reg="";
				
				String code_line="sub eax,"+reg+"\n";
				final_code.add(code_line);
			}
			else if ("/".equals(token[0])) {
			}
			else if ("endu".equals(token[0])) {
			}
			else if ("jump".equals(token[0])) {
			}
			else {
				System.err.println("dn to vrika ");
			}
			
		}
	}
	public void add(String []token)
	{
		add(token);
		return;
	}
	public void print_final()
	{
		Iterator<String> itr=final_code.iterator();
		while(itr.hasNext())
		{
			String ret=itr.next();
			System.out.print(ret);
		}
		return;
	}
}