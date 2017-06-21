package compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class AssemblyCreator{
	
	ArrayList<String> lowering_code;
	ArrayList<String> final_code;
	ArrayList<String> data;
	HashMap<String, Integer> rmap ;
	FunctionSum symboltable;
	Integer reg=0;
	Integer reg2=0;
	Integer label=0;
	Integer bcounter;
	Boolean parameter_flag=true; //new function params
	ArrayList<String> parameters; // store oarameters to push them reversed
	ArrayList<String> parameters_kind;
	public AssemblyCreator(ArrayList<String> lc,FunctionSum symboltable)
	{
		lowering_code=lc;
		final_code=new ArrayList<String>();
		rmap = new HashMap<String, Integer>();
		data=new ArrayList<String>();
		bcounter=0;
		this.symboltable=symboltable;
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
			FunctionSum current;
			token[0]=temp[1];
			for(int i=0;i<token.length;i++)
				token[i]=token[i].trim();
			if("unit".equals(token[0])) {
				code_line=""+token[1]+":\n";
				final_code.add(code_line);
				code_line="push ebp\n";
				String code_line2="mov ebp, esp\n";
				final_code.add(code_line);
				final_code.add(code_line2);
				current=symboltable.get_function_from_Symboltable(token[1]);
				int size=current.vars.size()*4;//not sure
				System.out.println("eimai stn sunartisi "+current.name+" me megethos "+size+"\n");
			
				code_line="sub esp, "+size+"\n";
				 final_code.add(code_line);
			}
			else if (":=".equals(token[0]))
			{
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
					else
					{
						code_line="mov DWORD PTR [ebp -"+reg+"],eax\n";
						final_code.add(code_line);
					}
				}
				else
				{
					if(rmap.containsKey(token[3]))
					{
						reg2=rmap.get(token[3]);
						code_line="mov ebx,DWORD PTR [ebp -"+reg2+"]\n";
						final_code.add(code_line);
					}
					else if(!token[3].contains("$"))
					{
						code_line="mov eax,"+token[3]+"\n";
						final_code.add(code_line);
					}
					bcounter+=4;
					rmap.put(token[1],bcounter);
					code_line="move DWORD PTR [ebp -"+bcounter+"],eax\n";
					final_code.add(code_line);
				}
			}
			else if ("array".equals(token[0])) {
			}
			else if ("*".equals(token[0]))
			{
				if(rmap.containsKey(token[1]))
				{
					reg=rmap.get(token[1]);
					code_line="mov eax,DWORD PTR [ebp -"+reg+"]\n";
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
					code_line="mov ebx,DWORD PTR [ebp -"+reg+"]\n";
					final_code.add(code_line);
				}
				else
				{
					code_line="mov ebx,"+token[2]+"\n";
					final_code.add(code_line);

				}
				code_line="imul eax,ebx\n";
				final_code.add(code_line);
			}
			else if ("+".equals(token[0])) 
			{		
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
					code_line="mov ebx, DWORD PTR [ebp -"+reg2+"]\n";
					final_code.add(code_line);
				}
				else
				{
					code_line="mov ebx,"+token[2]+"\n";
					final_code.add(code_line);
				}
				code_line="add eax,ebx\n";
				final_code.add(code_line);
			}
			else if ("-".equals(token[0])) {

				if(rmap.containsKey(token[1]))
				{
					reg=rmap.get(token[1]);
					code_line="mov eax, DWORD PTR [ebp -"+reg+"]\n";;
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
					code_line="mov ebx, DWORD PTR [ebp -"+reg+"]\n";;
					final_code.add(code_line);
				}
				else
				{
					code_line="mov ebx,"+token[2]+"\n";
					final_code.add(code_line);	
				}
				code_line="sub eax,ebx\n";
				final_code.add(code_line);
			}
			else if ("/".equals(token[0])) {
			}
			else if ("endu".equals(token[0])) {
				code_line="mov esp, ebp\n";
				final_code.add(code_line);
				code_line="pop ebp\n";
				final_code.add(code_line);
				code_line="ret\n";
				final_code.add(code_line);
			}
			else if ("jump".equals(token[0])) {
			}
			else if (">".equals(token[0])) {
				if(rmap.containsKey(token[1]))
				{
					reg=rmap.get(token[1]);
					code_line="mov eax, DWORD PTR [ebp -"+reg+"]\n";;
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
					code_line="mov ebx, DWORD PTR [ebp -"+reg2+"]\n";;
					final_code.add(code_line);
				}
				else
				{
					code_line="mov ebx,"+token[2]+"\n";
					final_code.add(code_line);	
				}
				code_line="cmp eax, ebx\n";
				final_code.add(code_line);
				code_line="jg L"+label+++"\n";
				final_code.add(code_line);		
			}
			else if ("<".equals(token[0])) {
			}
			else if (">=".equals(token[0])) {
			}
			else if ("<=".equals(token[0])) {
			}
			else if ("=".equals(token[0])) {
			}
			else if ("#".equals(token[0])) {
			}
			else if("call".equals(token[0]))
			{
				for (int i=parameters.size();i>0;i--)
				{
					if(parameters_kind.get(i-1).equals("V"))
					{
						code_line="push "+parameters.get(i-1)+"\n";
						final_code.add(code_line);
					}
					else {
						code_line="mov eax, OFFSET FLAT:"+parameters.get(i-1)+"\n";
						final_code.add(code_line);
						code_line="push eax\n";
						final_code.add(code_line);
						code_line=parameters.get(i-1)+":"+"\t.asciz\t"+parameters.get(i-1)+"\n";
						data.add(code_line);
					}
				}
				code_line="call "+token[3]+"\n";
				final_code.add(code_line);
				code_line="add esp, "+parameters.size()*4+"\n";
				final_code.add(code_line);
				parameter_flag=true;
				parameters=new ArrayList<String>();
				parameters_kind=new ArrayList<String>();
			}
			else if("par".equals(token[0]))
			{
				if(parameter_flag==true)
				{
					parameter_flag=false;
					parameters=new ArrayList<String>();
					parameters_kind=new ArrayList<String>();
				}
				parameters.add(token[1]);
				parameters_kind.add(token[2]);
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
		System.out.println(".data");
		Iterator<String> itr2=data.iterator();
		while(itr2.hasNext())
		{
			String ret=itr2.next();
			System.out.print(ret);
		}
		return;
	}
}
