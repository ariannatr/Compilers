package compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class AssemblyCreator{
	
	ArrayList<String> lowering_code;
	ArrayList<ArrayList<String>> final_code;
	Integer thesi;
	ArrayList<String> data;
	HashMap<String,HashMap<String, Integer>> rmap ;
	HashMap<String, Integer> labels ;
	HashMap<String, Integer> rmapcounter ;
	HashMap<Integer, String> funlabel ;
	HashMap<Integer, Integer> whilelabel ;
	HashMap<String, Integer> scopes ;
	FunctionSum symboltable;
	FunctionSum library;
	Integer reg=0;
	Integer reg2=0;
	Integer label=0;
	Integer line;
	Integer funid;
	Integer scope;
	Boolean esi_used;
	String [] token;
	String code_line;
	FunctionSum current;
	Boolean parameter_flag=true; //new function params
	ArrayList<String> parameters; // store oarameters to push them reversed
	ArrayList<String> parameters_kind;
	ArrayList<String> library_calls;
	Integer count_temp;
	String main;
	public AssemblyCreator(ArrayList<String> lc,FunctionSum symboltable,FunctionSum library,ArrayList<String> library_calls)
	{
		lowering_code=lc;
		thesi=0;
		whilelabel=new HashMap<Integer, Integer>();
		final_code=new ArrayList<ArrayList<String>>();
		rmap = new HashMap<String,HashMap<String, Integer>>();
		rmapcounter = new HashMap<String, Integer>();
		labels = new HashMap<String, Integer>();
		funlabel = new HashMap<Integer, String>();
		scopes =new HashMap<String, Integer>();
		data=new ArrayList<String>();
		line=1;
		funid=-1;
		current=null;
		count_temp=0;
		scope=0;
		esi_used=false;
		this.symboltable=symboltable;
		this.library=library;
		this.library_calls=library_calls;
		main=symboltable.name.trim();
		parameters=new ArrayList<String>();
	}
	
	public void produce()
	{
		Iterator<String> itr=lowering_code.iterator();
		code_line=".intel_syntax noprefix\n";
		ArrayList<String> arr=new ArrayList<String>();
		final_code.add(arr);
		final_code.get(thesi).add(code_line);
		code_line=".text\n";
		final_code.get(thesi).add(code_line);
		code_line="\t.global main\n";
		final_code.get(thesi).add(code_line);
		include_library_defs();//add code for library
		while(itr.hasNext())
		{
			if(labels.containsKey(line.toString()))
			{
				code_line="L"+labels.get(line.toString())+":\n";
				final_code.get(thesi).add(code_line);
			}
			String command=itr.next();
			token=command.split(",");
			String []temp=token[0].split(" ");
			
			token[0]=temp[1];
			for(int i=0;i<token.length;i++)
				token[i]=token[i].trim();
			if("unit".equals(token[0])) {
				thesi++;
				arr=new ArrayList<String>();
				HashMap tmap=new  HashMap<String,Integer>();
				rmapcounter.put(token[1],4);
				final_code.add(arr);
				rmap.put(token[1],tmap);
				if(token[1].equals(main))
					code_line="main:\n";
				else
					code_line=""+token[1]+":\n";
				scope++;
				scopes.put(token[1],scope);
				final_code.get(thesi).add(code_line);
				code_line="\tpush ebp\n";
				String code_line2="\tmov ebp, esp\n";
				final_code.get(thesi).add(code_line);
				final_code.get(thesi).add(code_line2);
				current=symboltable.get_function_from_Symboltable(token[1]);
				current.name=current.name.trim();
				int size=0;//not sure
				for(int i=0;i<current.vars.size();i++)
				{
					
					if(current.vars.get(i).sizes.contains("["))
					{
						
						String s=current.vars.get(i).sizes;
						s=s.replaceAll(" ", "");
						s=s.replaceAll("\\[", "");
						s=s.replaceAll("int", "");
						s=s.replaceAll("char", "");
						String sa[] = s.split("\\]");
						Integer x=0;
						Integer y;
						for(int z=0;z<sa.length;z++)
						{
							y=Integer.parseInt(sa[z]);
							x*=y;
						}
						rmap.get(current.name).put(current.vars.get(i).name.trim(),rmapcounter.get(current.name));
						rmapcounter.put(current.name,rmapcounter.get(current.name)+x);
						size+=x;
					}
					else
					{
						
						rmap.get(current.name).put(current.vars.get(i).name.trim(), rmapcounter.get(current.name));
						rmapcounter.put(current.name,rmapcounter.get(current.name)+4);
						size+=4;
					}
				}
				funlabel.put(funid,token[1]);
				funid--;
				code_line="\tsub esp, "+size+"\n";
				final_code.get(thesi).add(code_line);

			}
			else if (":=".equals(token[0]))
			{
				token[3]=token[3].replaceAll("\n", "");
				if(rmap.get(current.name).containsKey(token[1]))
				{
					reg=rmap.get(current.name).get(token[1]);
					if(rmap.get(current.name).containsKey(token[3]))
					{

						reg2=rmap.get(current.name).get(token[3]);
						code_line="\tmov eax,DWORD PTR [ebp -"+reg2+"]\n";
						final_code.get(thesi).add(code_line);
					}
					else if(current.findparameter(token[3]))
					{
						reg2=0;
						for(int p=0;p<current.arg.size();p++)
						{
							if(token[3].trim().equals(current.get_parameter(p).name.trim()))
							{		
								reg2=p;
							}
						}
						Integer calc=(reg2*4)+16;
						code_line="\tmov eax,DWORD PTR [ebp +"+calc+"]\n";
						final_code.get(thesi).add(code_line);
					}
					else if(!token[3].startsWith("$"))
					{
						code_line="\tmov eax,"+token[3]+"\n";
						final_code.get(thesi).add(code_line);
					}
					code_line="\tmov DWORD PTR [ebp -"+reg+"],eax\n";
					final_code.get(thesi).add(code_line);
				}
				else
				{
					if(rmap.get(current.name).containsKey(token[3]))
					{
						reg2=rmap.get(current.name).get(token[3]);
						code_line="\tmov ebx,DWORD PTR [ebp -"+reg2+"]\n";
						final_code.get(thesi).add(code_line);
					}
					else if(!token[3].contains("$"))
					{
						code_line="\tmov eax,"+token[3]+"\n";
						final_code.get(thesi).add(code_line);
					}
					rmap.get(current.name).put(token[1],rmapcounter.get(current.name));
					rmapcounter.put(current.name,rmapcounter.get(current.name)+4);
					code_line="\tmov DWORD PTR [ebp -"+rmap.get(current.name).get(token[1])+"],eax\n";
					final_code.get(thesi).add(code_line);
				}
			}
			else if ("array".equals(token[0])) {
				if(rmap.get(current.name).containsKey(token[2]))
				{
					code_line="\tmov eax ,DWORD PTR[ebp -"+rmap.get(token[2])+"\n";
					final_code.get(thesi).add(code_line);
				}
				else
				{
					code_line="\tmov eax ,"+token[2]+"\n";
					final_code.get(thesi).add(code_line);
				}	
				String size=current.findvarsize(token[1]);//to-do gia diastasi >1
				size=size.replaceAll(" ","");
				size=size.replaceAll("\\[", "");
				size=size.replaceAll("\\]", "");
				size=size.replaceAll("int", "");
				size=size.replaceAll("char", "");
				code_line="\tmov ecx ,"+size+"\n";
				final_code.get(thesi).add(code_line);
				code_line="\timul ecx\n";
				final_code.get(thesi).add(code_line);
				code_line="\tlea ecx,DWORD PTR[ebp-"+rmap.get(token[1])+"]\n";
				final_code.get(thesi).add(code_line);
				code_line="\tadd eax, ecx\n";
				final_code.get(thesi).add(code_line);
			
				code_line="\tmov DWORD PTR[ebp-"+rmap.get(token[1])+"],eax\n";
				final_code.get(thesi).add(code_line);
				
			}
			else if ("*".equals(token[0])){
				mycalc("imul");
			}
			else if ("+".equals(token[0])) {		
				mycalc("add");
			}
			else if ("-".equals(token[0])) {
				mycalc("sub");
			}
			else if ("/".equals(token[0]))
			{
				if(rmap.get(current.name).containsKey(token[1]))
				{
					reg=rmap.get(current.name).get(token[1]);
					code_line="\tmov eax, DWORD PTR [ebp -"+reg+"]\n";;
					final_code.get(thesi).add(code_line);
				}
				else
				{
					code_line="\tmov eax,"+token[1]+"\n";
					final_code.get(thesi).add(code_line);
				}
				code_line="\tcdq\n";
				final_code.get(thesi).add(code_line);
				if(rmap.get(current.name).containsKey(token[2]))
				{
					reg2=rmap.get(current.name).get(token[2]);
					code_line="\tmov ebx, DWORD PTR [ebp -"+reg2+"]\n";;
					final_code.get(thesi).add(code_line);
				}
				else
				{
					code_line="\tmov ebx,"+token[2]+"\n";
					final_code.get(thesi).add(code_line);	
				}
				code_line="\tidiv ebx\n";
				final_code.get(thesi).add(code_line);
				if(rmap.get(current.name).containsKey(token[3]))
				{
					code_line="\tmov DWORD PTR [ebp -"+rmap.get(current.name).get(token[3])+"],eax\n";
					final_code.get(thesi).add(code_line);
				}
				else
				{
					rmap.get(current.name).put(token[3],rmapcounter.get(current.name));
					rmapcounter.put(current.name,rmapcounter.get(current.name)+4);
					code_line="\tmov DWORD PTR [ebp -"+rmap.get(current.name).get(token[3])+"],eax\n";
					final_code.get(thesi).add(code_line);
				}

			}
			else if ("mod".equals(token[0]))
			{
				if(rmap.get(current.name).containsKey(token[1]))
				{
					reg=rmap.get(current.name).get(token[1]);
					code_line="\tmov eax, DWORD PTR [ebp -"+reg+"]\n";;
					final_code.get(thesi).add(code_line);
				}
				else
				{
					code_line="\tmov eax,"+token[1]+"\n";
					final_code.get(thesi).add(code_line);
				}
				code_line="\tcdq\n";
				final_code.get(thesi).add(code_line);
				if(rmap.get(current.name).containsKey(token[2]))
				{
					reg2=rmap.get(current.name).get(token[2]);
					code_line="\tmov ebx, DWORD PTR [ebp -"+reg2+"]\n";;
					final_code.get(thesi).add(code_line);
				}
				else
				{
					code_line="\tmov ebx,"+token[2]+"\n";
					final_code.get(thesi).add(code_line);	
				}
				code_line="\tidiv ebx\n";
				final_code.get(thesi).add(code_line);
				if(rmap.get(current.name).containsKey(token[3]))
				{
					code_line="\tmov DWORD PTR [ebp -"+rmap.get(current.name).get(token[3])+"],edx\n";
					final_code.get(thesi).add(code_line);
				}
				else
				{
					rmap.get(current.name).put(token[3],rmapcounter.get(current.name));
					rmapcounter.put(current.name,rmapcounter.get(current.name)+4);
					code_line="\tmov DWORD PTR [ebp -"+rmap.get(current.name).get(token[3])+"],edx\n";
					final_code.get(thesi).add(code_line);
				}

			}
			else if ("endu".equals(token[0])) {
			
				code_line="L"+token[1]+":\n";
				final_code.get(thesi).add(code_line);
				if(esi_used==true)
				{
					code_line="\tpop esi\n";
					final_code.get(thesi).add(code_line);
					esi_used=false;
				}
				code_line="\tmov esp, ebp\n";
				final_code.get(thesi).add(code_line);
				code_line="\tpop ebp\n";
				final_code.get(thesi).add(code_line);
				code_line="\tret\n";
				final_code.get(thesi).add(code_line);
				labels.remove(funid);
				funid++;
				thesi--;
				scope--;
				current=current.belongs;
			}
			else if ("jump".equals(token[0])) {
			
				
				if(labels.containsKey(token[3]))
				{
					code_line="\tjmp L"+labels.get(token[3])+"\n";
					final_code.get(thesi).add(code_line);//arianna added
				}
				else
				{
					code_line="\tjmp L"+label+"\n";
					labels.put(token[3],label);
					final_code.get(thesi).add(code_line);
					Integer k=Integer.parseInt(token[3]);
					if(k<final_code.get(thesi).size())
					{
						for(int j=0;j<whilelabel.size();j++)
						{
							if(whilelabel.containsKey(k))
							{
								final_code.get(thesi).add(whilelabel.get(k),"L"+label+":\n");
								whilelabel.remove(k);
							}
						}
					}
					label++;
				}
			}
			else if (">".equals(token[0])) {
				mycompare("jg");	
			}
			else if ("<".equals(token[0])) {
				mycompare("jl");
			}
			else if (">=".equals(token[0])) {
				mycompare("jge");	
			}
			else if ("<=".equals(token[0])) {
				mycompare("jle");
			}
			else if ("=".equals(token[0])) {
				mycompare("jz");
			}
			else if ("#".equals(token[0])) {
				mycompare("jnz");
			}
			else if("call".equals(token[0]))
			{
				String temp_par="";
				FunctionSum temp_fun=null;
				
				temp_fun=library.getFunction(token[3].replaceAll("grace_",""));
				if(temp_fun!=null)
				{
					code_line="\tsub esp,4\n";
					final_code.get(thesi).add(code_line);
				}
				else
				{
					if(scopes.get(token[3])>scopes.get(current.name))//mikrotero scope
					{
						code_line="\tpush ebp\n";
						final_code.get(thesi).add(code_line);
					}
					else if(scopes.get(token[3])==scopes.get(current.name))//iso scope
					{
						code_line="\tpush DWORD PTR [ebp +8]\n";
						final_code.get(thesi).add(code_line);
					}
					else if(scopes.get(token[3])<scopes.get(current.name))
					{
						code_line="\tmov esi, DWORD PTR[ebp +8]\n";//while thelei mexri na ftasei -TODO
						final_code.get(thesi).add(code_line);
						code_line="\tpush esi\n";
						final_code.get(thesi).add(code_line);
					}
				}

				if(parameters!=null)
				{
					
					for (int i=parameters.size();i>0;i--)
					{
						if(parameters_kind.get(i-1).equals("V") )//Value
						{
							getvar(parameters.get(i-1));
						}
						else// Reference 
						{
							if(parameters.get(i-1).startsWith("\""))
							{
								temp_fun=symboltable.get_function_from_Symboltable(token[3]);
								if(temp_fun==null)
								{
									temp_fun=library.getFunction(token[3].replaceAll("grace_",""));
									if(temp_fun==null)
									{
										break;
									}
								}

								temp_par="temp"+count_temp++;
								code_line="\tmov eax, OFFSET FLAT:"+temp_par+"\n";
								final_code.get(thesi).add(code_line);
								code_line="\tpush eax\n";
								final_code.get(thesi).add(code_line);
								code_line="\t"+temp_par+":"+"\t.asciz\t"+parameters.get(i-1)+"\n";
								data.add(code_line);
							}
							else
							{
								code_line="\tmov eax, OFFSET FLAT:"+parameters.get(i-1)+"\n";
								final_code.get(thesi).add(code_line);
								code_line="\tpush eax\n";
								final_code.get(thesi).add(code_line);
								code_line="\t"+parameters.get(i-1)+":"+"\t.asciz\t"+parameters.get(i-1)+"\n";
								data.add(code_line);
							}
						}
					}
				}
				if(token[3].equals(main))
					code_line="\tcall main\n";
				else
					code_line="\tcall "+token[3]+"\n";
				final_code.get(thesi).add(code_line);
				/*if(parameters!=null)
				{
					FunctionSum f32=current.get_function_from_Symboltable(token[3]);
					if(f32!=null)
					{
						code_line="\tadd esp, "+12+"\n";
						final_code.get(thesi).add(code_line);
					}
					code_line="\tadd esp, "+(parameters.size())*4+"\n";
					final_code.get(thesi).add(code_line);
				}*/
				code_line="\tadd esp, "+(parameters.size()+1)*4+"\n";
				final_code.get(thesi).add(code_line);
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
			else if("ret".equals(token[0]))
			{
				code_line="\tjmp L"+funlabel.get(funid+1)+"\n";
				final_code.get(thesi).add(code_line);
			}
			else {
		//		System.err.println("dn to vrika akoma ");
			}
			line++;
		}	
	}

	public void getvar(String name)
	{

		if(rmap.get(current.name).containsKey(name))//an to exei i idia tin metavliti
		{
			code_line="\tmov eax ,DWORD PTR[ebp -"+rmap.get(current.name).get(name)+"]\n";
			final_code.get(thesi).add(code_line);
			code_line="\tpush eax \n";
			final_code.get(thesi).add(code_line);
		}
		else if(current.findparameter(name))
		{
			reg2=0;
			for(int p=0;p<current.arg.size();p++)
			{
				if(name.trim().equals(current.get_parameter(p).name.trim()))
				{		
					reg2=p;
				}
			}
			Integer calc=(reg2*4)+16;
			code_line="\tmov eax,DWORD PTR [ebp +"+calc+"]\n";
			final_code.get(thesi).add(code_line);
			code_line="\tpush eax \n";
			final_code.get(thesi).add(code_line);
		}
		else//an to exei i mama
		{
			
			code_line="\tpush esi\n";
			final_code.get(thesi).add(code_line);
			code_line="\tmov esi ,DWORD PTR[ebp +"+8+"]\n";
			final_code.get(thesi).add(code_line);
			code_line="\tmov eax ,DWORD PTR[esi -"+rmap.get(current.belongs.name.trim()).get(name.trim())+"]\n";
			final_code.get(thesi).add(code_line);
			code_line="\tpush eax\n";
			final_code.get(thesi).add(code_line);
		}
	}
	public void add(String []token)
	{
		add(token);
		return;
	}
	public void mycalc(String symbol)
	{
		
		if(rmap.get(current.name).containsKey(token[1]))
		{
			reg=rmap.get(current.name).get(token[1]);
			code_line="\tmov eax, DWORD PTR [ebp -"+reg+"]\n";;
			final_code.get(thesi).add(code_line);
		}
		else if(current.findparameter(token[1]))
		{
			reg2=0;
			for(int p=0;p<current.arg.size();p++)
			{
				if(token[1].trim().equals(current.get_parameter(p).name.trim()))
				{		
					reg2=p;
				}
			}
			Integer calc=(reg2*4)+16;
			code_line="\tmov eax,DWORD PTR [ebp +"+calc+"]\n";
			final_code.get(thesi).add(code_line);
		}
		else if(Character.isDigit(token[1].charAt(0)))
		{
			code_line="\tmov eax,"+token[1]+"\n";
			final_code.get(thesi).add(code_line);	
		}
		else if(current.belongs!=null)
		{
			code_line="\tpush esi\n";
			final_code.get(thesi).add(code_line);
			code_line="\tmov esi ,DWORD PTR[ebp +"+8+"]\n";
			final_code.get(thesi).add(code_line);
			code_line="\tmov eax ,DWORD PTR[esi -"+rmap.get(current.belongs.name).get(token[1])+"]\n";
			final_code.get(thesi).add(code_line);
		}
		

		if(rmap.get(current.name).containsKey(token[2]))
		{
			reg2=rmap.get(current.name).get(token[2]);
			code_line="\tmov ebx, DWORD PTR [ebp -"+reg2+"]\n";;
			final_code.get(thesi).add(code_line);
		}
		else if(current.findparameter(token[2]))
		{
			reg2=0;
			for(int p=0;p<current.arg.size();p++)
			{
				if(token[2].trim().equals(current.get_parameter(p).name.trim()))
				{		
					reg2=p;
				}
			}
			Integer calc=(reg2*4)+16;
			code_line="\tmov ebx,DWORD PTR [ebp +"+calc+"]\n";
			final_code.get(thesi).add(code_line);
		}
		else if(Character.isDigit(token[2].charAt(0)))
		{
			code_line="\tmov ebx,"+token[2]+"\n";
			final_code.get(thesi).add(code_line);	
		}
		else if(current.belongs!=null)
		{
			code_line="\tpush esi\n";
			final_code.get(thesi).add(code_line);
			code_line="\tmov esi ,DWORD PTR[ebp +"+8+"]\n";
			final_code.get(thesi).add(code_line);
			code_line="\tmov ebx ,DWORD PTR[esi -"+rmap.get(current.belongs.name).get(token[2])+"]\n";
			final_code.get(thesi).add(code_line);
		}

		code_line="\t"+symbol+" eax,ebx\n";
		final_code.get(thesi).add(code_line);
		if(rmap.get(current.name).containsKey(token[3]))
		{
			code_line="\tmov DWORD PTR [ebp -"+rmap.get(current.name).get(token[3])+"],eax\n";
			final_code.get(thesi).add(code_line);
		}
		else
		{
			rmap.get(current.name).put(token[3],rmapcounter.get(current.name));
			rmapcounter.put(current.name,rmapcounter.get(current.name)+4);
			code_line="\tmov DWORD PTR [ebp -"+rmap.get(current.name).get(token[3])+"],eax\n";
			final_code.get(thesi).add(code_line);
		}
	}
	public void mycompare(String symbol)
	{
	
		final_code.get(thesi).add("");
		whilelabel.put(line,final_code.get(thesi).size());
		if(rmap.get(current.name).containsKey(token[1]))
		{
			reg=rmap.get(current.name).get(token[1]);
			code_line="\tmov eax, DWORD PTR [ebp -"+reg+"]\n";;
			final_code.get(thesi).add(code_line);
		}
		else if(current.findparameter(token[1]))
		{
			reg2=0;
			for(int p=0;p<current.arg.size();p++)
			{
				if(token[1].trim().equals(current.get_parameter(p).name.trim()))
				{		
					reg2=p;
				}
			}
			Integer calc=(reg2*4)+16;
			code_line="\tmov eax,DWORD PTR [ebp +"+calc+"]\n";
			final_code.get(thesi).add(code_line);
		}
		else if(Character.isDigit(token[1].charAt(0)))
		{
			code_line="\tmov eax,"+token[1]+"\n";
			final_code.get(thesi).add(code_line);
		}
		else if(current.belongs!=null)
		{
			code_line="\tpush esi\n";
			final_code.get(thesi).add(code_line);
			code_line="\tmov esi ,DWORD PTR[ebp +"+8+"]\n";
			final_code.get(thesi).add(code_line);
			code_line="\tmov eax ,DWORD PTR[esi -"+rmap.get(current.belongs.name).get(token[1])+"]\n";
			final_code.get(thesi).add(code_line);
		}
		if(rmap.get(current.name).containsKey(token[2]))
		{
			reg2=rmap.get(current.name).get(token[2]);
			code_line="\tmov ebx, DWORD PTR [ebp -"+reg2+"]\n";;
			final_code.get(thesi).add(code_line);
		}
		else if(current.findparameter(token[2]))
		{
			reg2=0;
			for(int p=0;p<current.arg.size();p++)
			{
				if(token[2].trim().equals(current.get_parameter(p).name.trim()))
				{		
					reg2=p;
				}
			}
			Integer calc=(reg2*4)+16;
			code_line="\tmov ebx,DWORD PTR [ebp +"+calc+"]\n";
			final_code.get(thesi).add(code_line);
		}
		else if(Character.isDigit(token[2].charAt(0)))
		{
			code_line="\tmov ebx,"+token[2]+"\n";
			final_code.get(thesi).add(code_line);	
		}
		else if(current.belongs!=null)
		{
			code_line="\tpush esi\n";
			final_code.get(thesi).add(code_line);
			code_line="\tmov esi ,DWORD PTR[ebp +"+8+"]\n";
			final_code.get(thesi).add(code_line);
			code_line="\tmov ebx ,DWORD PTR[esi -"+rmap.get(current.belongs.name).get(token[2])+"]\n";
			final_code.get(thesi).add(code_line);
		}
		code_line="\tcmp eax, ebx\n";
		final_code.get(thesi).add(code_line);
		if(labels.containsKey(token[3]))
		{
			code_line="\t"+symbol+" L"+labels.get(token[3])+"\n";
		}
		else
		{
			code_line="\t"+symbol+" L"+label+"\n";
			labels.put(token[3],label);
			label++;
		}
		final_code.get(thesi).add(code_line);	
	}
	public void print_final()
	{
		Iterator<String> itr;
		for(int i=0;i<final_code.size();i++)
		{
			itr=final_code.get(i).iterator();
			while(itr.hasNext())
			{
				String ret=itr.next();
			//	if(ret.contains(":")|| ret.contains("."))
					System.out.print(ret);
			//	else
			//		System.out.print("\t"+ret);
			}
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

	public void include_library_defs()
	{
		String code_line="";
		if(library_calls.contains("grace_gets"))
		{
			grace_gets();
		//	System.out.println("adding grace_gets");
		}
		if(library_calls.contains("grace_puti"))
		{
			grace_puti();
			code_line="\t"+"int_par:"+"\t.asciz\t"+"\"%d\""+"\n";
			data.add(code_line);
		}
		if(library_calls.contains("grace_puts"))
			grace_puts();
		if(library_calls.contains("grace_putc"))
		{
			grace_putc();
			code_line="\t"+"char:"+"\t.asciz\t"+"\"%c\""+"\n";
			data.add(code_line);
		}
	}

	public void grace_puti()
	{
		String code_line="";
		String int_par="int_par";
		code_line="grace_puti:\n";
		final_code.get(thesi).add(code_line);
    	code_line="\tpush ebp\n";
    	final_code.get(thesi).add(code_line);
		code_line="\tmov ebp, esp\n";
		final_code.get(thesi).add(code_line);
		code_line="\tpush eax\n";
		final_code.get(thesi).add(code_line);
		code_line="\tmov eax, OFFSET FLAT:"+int_par+"\n";
		final_code.get(thesi).add(code_line);
		code_line="\tpush eax\n";
		final_code.get(thesi).add(code_line);
		code_line="\tcall printf\n";
		final_code.get(thesi).add(code_line);		
		code_line="\tadd esp, 8\n";
		final_code.get(thesi).add(code_line);		
		code_line="\tmov esp, ebp\n";
		final_code.get(thesi).add(code_line);
  		code_line="\tpop ebp\n";
		final_code.get(thesi).add(code_line);
   		code_line="\tret\n";
   		final_code.get(thesi).add(code_line);
	}

	public void grace_putc()
	{
		String code_line="";
		code_line="grace_putc:\n";
		final_code.get(thesi).add(code_line);
    	code_line="\tpush ebp\n";
    	final_code.get(thesi).add(code_line);
		code_line="\tmov ebp, esp\n";
		final_code.get(thesi).add(code_line);
		code_line="\tpush eax\n";
		final_code.get(thesi).add(code_line);
		code_line="\tmov eax, OFFSET FLAT:char\n";
		final_code.get(thesi).add(code_line);
		code_line="\tpush eax\n";
		final_code.get(thesi).add(code_line);
		code_line="\tcall printf\n";
		final_code.get(thesi).add(code_line);		
		code_line="\tadd esp, 8\n";
		final_code.get(thesi).add(code_line);		
		code_line="\tmov esp, ebp\n";
		final_code.get(thesi).add(code_line);
  		code_line="\tpop ebp\n";
		final_code.get(thesi).add(code_line);
   		code_line="\tret\n";
   		final_code.get(thesi).add(code_line);
	}
	public void grace_puts()
	{
		String code_line="";
		code_line="grace_puts:\n";
		final_code.get(thesi).add(code_line);
    	code_line="\tpush ebp\n";
    	final_code.get(thesi).add(code_line);
		code_line="\tmov ebp, esp\n";
		final_code.get(thesi).add(code_line);
		code_line="\tmov eax, DWORD PTR [ebp + 8]\n";
		final_code.get(thesi).add(code_line);
		code_line="\tpush eax\n";
		final_code.get(thesi).add(code_line);
		code_line="\tcall printf\n";
		final_code.get(thesi).add(code_line);		
		code_line="\tadd esp, 4\n";
		final_code.get(thesi).add(code_line);		
		code_line="\tmov esp, ebp\n";
		final_code.get(thesi).add(code_line);
  		code_line="\tpop ebp\n";
		final_code.get(thesi).add(code_line);
   		code_line="\tret\n";
   		final_code.get(thesi).add(code_line);
	}
	public void grace_gets()
	{
		String code_line="";
		code_line="grace_gets:\n";
		final_code.get(thesi).add(code_line);
    	code_line="\tpush ebp\n";
    	final_code.get(thesi).add(code_line);
		code_line="\tmov ebp, esp\n";
		final_code.get(thesi).add(code_line);
		code_line="\tmov eax, DWORD PTR stdin\n";
		final_code.get(thesi).add(code_line);
		code_line="\tpush eax\n";
		final_code.get(thesi).add(code_line);
		code_line="\tmov eax, DWORD PTR [ebp + 8]\n";
		final_code.get(thesi).add(code_line);
		code_line="\tpush eax\n";
		final_code.get(thesi).add(code_line);
		code_line="\tmov eax, DWORD PTR [ebp + 12]\n";
		final_code.get(thesi).add(code_line);
		code_line="\tpush eax\n";
		final_code.get(thesi).add(code_line);
		code_line="\tcall fgets\n";
		final_code.get(thesi).add(code_line);
		code_line="\tadd esp, 12\n";
		final_code.get(thesi).add(code_line);
		code_line="\tmov eax, 10\n";
		final_code.get(thesi).add(code_line);
		code_line="\tpush eax\n";
		final_code.get(thesi).add(code_line);
		code_line="\tmov eax, DWORD PTR [ebp + 12]\n";
		final_code.get(thesi).add(code_line);
		code_line="\tpush eax\n";
		final_code.get(thesi).add(code_line);
		code_line="\tcall strchr\n";
		final_code.get(thesi).add(code_line);
		code_line="\tadd esp, 8\n";
		final_code.get(thesi).add(code_line);
		code_line="\tcmp eax, 0\n";
		final_code.get(thesi).add(code_line);
    	code_line="\tje grace_gets_no_newline\n";
    	final_code.get(thesi).add(code_line);
    	code_line="\tmov BYTE PTR [eax], 0\n";
		final_code.get(thesi).add(code_line);
		code_line="grace_gets_no_newline:\n";
		final_code.get(thesi).add(code_line);
    	code_line="\tmov esp, ebp\n";
		final_code.get(thesi).add(code_line);
  		code_line="\tpop ebp\n";
		final_code.get(thesi).add(code_line);
   		code_line="\tret\n";
   		final_code.get(thesi).add(code_line);
	}
}
