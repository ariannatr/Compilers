package compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class AssemblyCreator{
	
	ArrayList<String> lowering_code;
	ArrayList<String> final_code;
	ArrayList<String> data;
	HashMap<String, Integer> rmap ;
	Integer bcounter;
	public AssemblyCreator(ArrayList<String> lc)
	{
		lowering_code=lc;
		final_code=new ArrayList<String>();
		rmap = new HashMap<String, Integer>();
		bcounter=0;
	}
	
	public void produce()
	{
		Iterator<String> itr=lowering_code.iterator();
		String code_line=".intel_syntax noprefix\n";
		final_code.add(code_line);
		code_line=".text\n";
		final_code.add(code_line);
		code_line="\t.global main\n";
		final_code.add(code_line);
		while(itr.hasNext())
		{
			String command=itr.next();
			String [] token=command.split(",");
			String []temp=token[0].split(" ");
			token[0]=temp[1];
			for(int i=0;i<token.length;i++)
				token[i]=token[i].trim();
			if ("unit".equals(token[0])) {	
				 code_line="push ebp\n";
				 String code_line2="mov ebp, esp\n";
				 final_code.add(code_line);
				 final_code.add(code_line2);
			}
			else if (":=".equals(token[0]))
			{
				Integer reg=0;
				Integer reg2=0;
				token[3]=token[3].replaceAll("\n", "");
				if(rmap.containsKey(token[1]))
				{
					reg=rmap.get(token[1]);
					if(rmap.containsKey(token[3]))
					{
						reg2=rmap.get(token[3]);
						code_line="mov eax,DWORD PTR [ebp -"+reg2+"]\n";
						final_code.add(code_line);
					}
					else if(!token[3].contains("$"))
					{
						code_line="mov eax,"+token[3]+"\n";
						final_code.add(code_line);
					}
				}
				if(!rmap.containsKey(token[1]))
				{
					reg=rmap.get(token[1]);
					if(rmap.containsKey(token[3]))
					{
						reg2=rmap.get(token[3]);
						code_line="mov eax,DWORD PTR [ebp -"+reg2+"]\n";
						final_code.add(code_line);
					}
					else if(!token[3].contains("$"))
					{
						code_line="mov eax,"+token[3]+"\n";
						final_code.add(code_line);
					}
					bcounter+=2;
					rmap.put(token[1],bcounter);
					code_line="move DWORD PTR [ebp -"+bcounter+"],eax\n";
					final_code.add(code_line);
				}
			}
			else if ("array".equals(token[0])) {
			}
			else if ("*".equals(token[0]))
			{
				Integer reg=0;
				Integer reg2=0;
				if(rmap.containsKey(token[1]))
				{
					reg=rmap.get(token[1]);
					code_line="mov eax,"+reg+"\n";
					final_code.add(code_line);
				}
				else
				{
					code_line="mov eax,"+token[1]+"\n";
					final_code.add(code_line);
				}
				if(rmap.containsKey(token[2]))
				{
					reg2=rmap.get(token[2]);
					code_line="imul eax,"+reg2+"\n";
					final_code.add(code_line);
				}
				else
				{
					code_line="mov ebx,"+token[2]+"\n";
					final_code.add(code_line);
					code_line="imul eax,ebx\n";
					final_code.add(code_line);
				}
			}
			else if ("+".equals(token[0])) 
			{
				Integer reg=0;
				Integer reg2=0;
				System.err.println(rmap);
				if(rmap.containsKey(token[1]))
				{
					reg=rmap.get(token[1]);
					code_line="mov eax, DWORD PTR [ebp -"+reg+"]\n";
					final_code.add(code_line);
				}
				else
				{
					code_line="mov eax,"+token[1]+"\n";
					final_code.add(code_line);
				}
				if(rmap.containsKey(token[2]))
				{
					reg2=rmap.get(token[2]);
					code_line="add eax,"+reg2+"\n";
					final_code.add(code_line);
				}
				else
				{
					code_line="mov ebx,"+token[2]+"\n";
					final_code.add(code_line);
					code_line="add eax,ebx\n";
					final_code.add(code_line);
				}
			}
			else if ("-".equals(token[0])) {
				Integer reg=0;
				Integer reg2=0;
				if(rmap.containsKey(token[1]))
				{
					reg=rmap.get(token[1]);
					code_line="mov eax,"+reg+"\n";
					final_code.add(code_line);
				}
				else
				{
					code_line="mov eax,"+token[1]+"\n";
					final_code.add(code_line);
				}
				if(rmap.containsKey(token[2]))
				{
					reg2=rmap.get(token[2]);
					code_line="sub eax,"+reg2+"\n";
					final_code.add(code_line);
				}
				else
				{
					code_line="mov ebx,"+token[2]+"\n";
					final_code.add(code_line);
					code_line="sub eax,ebx\n";
					final_code.add(code_line);
				}
			}
			else if ("/".equals(token[0])) {
			}
			else if ("endu".equals(token[0])) {
			}
			else if ("jump".equals(token[0])) {
			}
			else {
				System.err.println("dn to vrika akoma ");
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