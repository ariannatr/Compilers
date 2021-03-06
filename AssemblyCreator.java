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
	Boolean refflag;
	Boolean prakseis;
	String [] token;
	String code_line;
	FunctionSum current;
	Boolean parameter_flag=true; //new function params
	ArrayList<String> parameters; // store oarameters to push them reversed
	ArrayList<String> parameters_kind;
	ArrayList<String> library_calls;
	Integer count_temp;
	String main;
	Helper helper;
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
		helper=new Helper();
		line=1;
		funid=-1;
		refflag=false;
		prakseis=false;
		current=null;
		count_temp=0;
		scope=0;
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
		helper.include_library_defs(final_code,thesi,data,library_calls);//add code for library
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
			if("unit".equals(token[0])) 
			{
				thesi++;
				arr=new ArrayList<String>();
				HashMap<String, Integer> tmap=new  HashMap<String,Integer>();
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
				int size=0;
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
				if(token[3].equals("$$"))//store the return value of the function
				{
					getvar(token[1],"eax");
					code_line="\tmov esi,DWORD PTR [ebp+12]\n";
					final_code.get(thesi).add(code_line);
					code_line="\tmov DWORD PTR [esi],eax\n";
					final_code.get(thesi).add(code_line);
				}
				else
				{
					token[3]=token[3].replaceAll("\n", "");
					if(rmap.get(current.name).containsKey(token[1]))//topiki
					{
						reg=rmap.get(current.name).get(token[1]);
						if(rmap.get(current.name).containsKey(token[3]))
						{
							
							code_line="\tmov eax,DWORD PTR [ebp -"+rmap.get(current.name).get(token[3])+"]\n";
							final_code.get(thesi).add(code_line);
							code_line="\tmov DWORD PTR [ebp -"+rmap.get(current.name).get(token[1])+"],eax\n";
							final_code.get(thesi).add(code_line);
							
						}
						else
						{
							getvar(token[3],"eax");
							code_line="\tmov DWORD PTR [ebp -"+reg+"],eax\n";
							final_code.get(thesi).add(code_line);
						}
					}
					else
					{
						if(rmap.get(current.name).containsKey(token[3]))
						{
							code_line="\tmov eax,DWORD PTR [ebp -"+rmap.get(current.name).get(token[3])+"]\n";
							final_code.get(thesi).add(code_line);
						}
						else
							getvar(token[3],"eax");
						
						if((current.findparameter(token[1])) && !current.is_ref(token[1])) //parameter by value
						{	
							
							Integer calc=(current.get_parameter_num(token[1])*4)+16;
							code_line="\tmov DWORD PTR [ebp +"+calc+"],eax\n";
							final_code.get(thesi).add(code_line);
						}
						else if ((current.findparameter(token[1])) && current.is_ref(token[1]))//anathesi se ref
						{
								Integer calc=(current.get_parameter_num(token[1])*4)+16;
								code_line="\tmov esi,DWORD PTR [ebp +"+calc+"]\n";
								final_code.get(thesi).add(code_line);
								code_line="\tmov DWORD PTR [esi],eax\n";
								final_code.get(thesi).add(code_line);
								refflag=true;	
						}
						else//anathesi eite s parametro mothers eite se metavliti
						{
							code_line="\tmov esi ,DWORD PTR[ebp +"+8+"]\n";//paw sti mama
							final_code.get(thesi).add(code_line);
							FunctionSum find;
							find=current.belongs;
							if (find.findparameter(token[1].trim())) //anathesi se mothers param
							{
								Integer calc=(find.get_parameter_num(token[1].trim())*4)+16;
								
								code_line="\tmov DWORD PTR [esi +"+calc+"],eax\n";
								final_code.get(thesi).add(code_line);
							}
							else//push mothers or upper var
							{
								while(!rmap.get(find.name.trim()).containsKey(token[1].trim()))
								{
									code_line="\tmov esi ,DWORD PTR[esi +"+8+"]\n";
									final_code.get(thesi).add(code_line);
									find=find.belongs;
								}
								code_line="\tmov DWORD PTR [esi-"+rmap.get(find.name.trim()).get(token[1].trim())+"],eax\n";
								final_code.get(thesi).add(code_line);
							}
						}
					}	
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
			else if ("div".equals(token[0]))
			{
				prakseis=true;
				getvar(token[1],"eax");//load token[1] to "eax"
				code_line="\tcdq\n";
				final_code.get(thesi).add(code_line);
				getvar(token[2],"ebx");//load token[2] to "ebx"
				prakseis=false;
				code_line="\tidiv ebx\n";
				final_code.get(thesi).add(code_line);
				if(rmap.get(current.name).containsKey(token[3])){		
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
				prakseis=true;
				getvar(token[1],"eax");//load token[1] to "eax"
				code_line="\tcdq\n";
				final_code.get(thesi).add(code_line);
				getvar(token[2],"ebx"); //load token[2] to "ebx"
				prakseis=false;
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
				code_line="\tmov eax, 0\n";
				final_code.get(thesi).add(code_line);
				if(current.deapth>1)
				{
					code_line="\tpop esi\n";
					final_code.get(thesi).add(code_line);
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
					final_code.get(thesi).add(code_line);
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
			
				
				if(parameters!=null)
				{
					
					for (int i=parameters.size();i>0;i--)
					{
						if(parameters_kind.get(i-1).equals("RET"))
						{
							rmap.get(current.name).put(parameters.get(i-1),rmapcounter.get(current.name));
							rmapcounter.put(current.name,rmapcounter.get(current.name)+4);
							code_line="\tlea esi ,DWORD PTR [ebp-"+rmap.get(current.name).get(parameters.get(i-1))+"]\n";
							final_code.get(thesi).add(code_line);
							code_line="\tpush esi \n";
							final_code.get(thesi).add(code_line);
						}
						else if(parameters_kind.get(i-1).equals("V") )//Value
						{
							getvar(parameters.get(i-1),"eax");
							code_line="\tpush eax \n";
							final_code.get(thesi).add(code_line);
						}
						else// Reference 
						{
							if(parameters.get(i-1).startsWith("\""))//string
							{
								temp_fun=symboltable.get_function_from_Symboltable(token[3]);
								if(temp_fun==null)
								{
									temp_fun=library.getFunction(token[3].replaceAll("grace_",""));
									if(temp_fun==null)
									{
										System.err.println("bad entrance\n");
									}
								}

								temp_par="temp"+count_temp++;
								code_line="\tmov esi, OFFSET FLAT:"+temp_par+"\n";
								final_code.get(thesi).add(code_line);
								code_line="\tpush esi\n";
								final_code.get(thesi).add(code_line);
								code_line="\t"+temp_par+":"+"\t.asciz\t"+parameters.get(i-1)+"\n";
								data.add(code_line);
							}
							else
							{ 
								FunctionSum find;
								find=current;
								if(rmap.get(find.name.trim()).containsKey(parameters.get(i-1)))//push a topic variable
								{
									code_line="\tlea esi,DWORD PTR [ebp-"+rmap.get(find.name.trim()).get(parameters.get(i-1).trim())+"]\n";
									final_code.get(thesi).add(code_line);
									code_line="\tpush esi\n";
									final_code.get(thesi).add(code_line);
								}
								else if(current.findparameter(parameters.get(i-1).trim()))//push a parameter
								{
									Integer calc=(find.get_parameter_num(parameters.get(i-1).trim())*4)+16;
									code_line="\tlea esi,DWORD PTR [ebp+"+calc+"]\n";
									final_code.get(thesi).add(code_line);
									code_line="\tpush esi\n";
									final_code.get(thesi).add(code_line);
								}
								else//push mothers var
								{
									
									code_line="\tmov esi ,DWORD PTR[ebp +"+8+"]\n";//paw sti mama
									final_code.get(thesi).add(code_line);
									find=current.belongs;
									if (find.findparameter(parameters.get(i-1).trim())) //push a mother's parameter
									{
										Integer calc=(find.get_parameter_num(parameters.get(i-1).trim())*4)+16;
										code_line="\tlea esi,DWORD PTR [esi +"+calc+"]\n";
										final_code.get(thesi).add(code_line);
									}
									else//push mothers or upper var
									{
										while(!rmap.get(find.name.trim()).containsKey(parameters.get(i-1).trim()))
										{
											
											code_line="\tmov esi ,DWORD PTR[esi +"+8+"]\n";
											final_code.get(thesi).add(code_line);
											find=find.belongs;
										}
										
										code_line="\tlea esi,DWORD PTR [esi-"+rmap.get(find.name.trim()).get(parameters.get(i-1).trim())+"]\n";
										final_code.get(thesi).add(code_line);
									}
									code_line="\tpush esi\n";
									final_code.get(thesi).add(code_line);
								}
							}
						}
					}
				}
				
				temp_fun=library.getFunction(token[3].replaceAll("grace_",""));
				if(temp_fun!=null)
				{
					code_line="\tsub esp,4\n";
					final_code.get(thesi).add(code_line);
				}
				
				else
				{
					if(scopes.get(token[3])>scopes.get(current.name))
					{
						if(symboltable.get_function_from_Symboltable(token[3]).type.trim().equals("nothing"))
						{
								code_line="\tsub esp,4\n";
								final_code.get(thesi).add(code_line);
						}
						code_line="\tpush ebp\n";
						final_code.get(thesi).add(code_line);
					}
					else if(scopes.get(token[3])==scopes.get(current.name))//iso scope
					{
						if(symboltable.get_function_from_Symboltable(token[3]).type.trim().equals("nothing "))
						{
								code_line="\tsub esp,4\n";
								final_code.get(thesi).add(code_line);
						}
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

				if(token[3].equals(main))
					code_line="\tcall main\n";
				else
					code_line="\tcall "+token[3]+"\n";
				final_code.get(thesi).add(code_line);

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
					if(token[2].equals("RET"))
					{
						parameters.add(0,token[1]);
						parameters_kind.add(0,token[2]);
					}
					else
					{
						parameters.add(token[1]);
						parameters_kind.add(token[2]);
					}
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

	public void getvar(String name,String calleeregister)
	{

		if(rmap.get(current.name).containsKey(name))//an to exei i idia tin metavliti
		{
			code_line="\tmov "+calleeregister+" , DWORD PTR [ebp -"+rmap.get(current.name).get(name)+"]\n";
			final_code.get(thesi).add(code_line);
		}
		else if(current.findparameter(name))
		{
			reg2=0;	
			
			boolean reflag=false;
			for(int p=0;p<current.arg.size();p++)
			{
				if(name.trim().equals(current.get_parameter(p).name.trim()))
				{		
					reg2=p;
					if(!current.get_parameter(p).ref.equals(""))
						reflag=true;
				}
			}
			Integer calc=(reg2*4)+16;
			if(reflag==true && prakseis==false)//anathesi/fortwsi
			{
				
				code_line="\tmov esi,DWORD PTR [ebp +"+calc+"]\n";
				final_code.get(thesi).add(code_line);
				code_line="\tmov "+calleeregister+",DWORD PTR [esi]\n";
				final_code.get(thesi).add(code_line);
				refflag=true;
			}
			else if(reflag==true && prakseis==true)//prakseis
			{
				
				code_line="\tmov esi,DWORD PTR [ebp +"+calc+"]\n";
				final_code.get(thesi).add(code_line);
				code_line="\tmov "+calleeregister+" ,DWORD PTR[esi]\n";
				final_code.get(thesi).add(code_line);
				refflag=true;
			}
			else
			{
				code_line="\tmov "+calleeregister+",DWORD PTR [ebp +"+calc+"]\n";
				final_code.get(thesi).add(code_line);
			}
		}
		else if(Character.isDigit(name.charAt(0)))
		{
			code_line="\tmov "+calleeregister+","+name+"\n";
			final_code.get(thesi).add(code_line);
		}
		else if(name.startsWith("$"))//is a new temp
		{
			
			rmap.get(current.name).put(name,rmapcounter.get(current.name));
			rmapcounter.put(current.name,rmapcounter.get(current.name)+4);
		}
		else if(name.startsWith("\'"))//is a char
		{
			code_line="\tmov "+calleeregister+","+name+"\n";
			final_code.get(thesi).add(code_line);
		}
		else//an to exei i mama
		{
			code_line="\tmov esi ,DWORD PTR[ebp +"+8+"]\n";
			final_code.get(thesi).add(code_line);
			FunctionSum find;
			find=current.belongs;
			while(!rmap.get(find.name.trim()).containsKey(name.trim()) && !find.findparameter(name.trim()))
			{
				
				code_line="\tmov esi ,DWORD PTR[esi +"+8+"]\n";
				final_code.get(thesi).add(code_line);
				find=find.belongs;
			}
			if(rmap.get(find.name.trim()).containsKey(name.trim()))
			{
				
				code_line="\tmov "+calleeregister+" ,DWORD PTR[esi -"+rmap.get(find.name.trim()).get(name.trim())+"]\n";
				final_code.get(thesi).add(code_line);
			}
			else if (find.findparameter(name.trim())) 
			{
				Integer calc=(find.get_parameter_num(name.trim())*4)+16;
				code_line="\tmov "+calleeregister+" ,DWORD PTR[esi +"+calc+"]\n";
				final_code.get(thesi).add(code_line);
			}
		}
	}

	public void add(String []token)
	{
		add(token);
		return;
	}

	public void mycalc(String symbol)
	{
		
		prakseis=true;
		getvar(token[1],"eax"); //load token[1] to "eax"	
		getvar(token[2],"ebx"); //load token[2] to "ebx"
		prakseis=false;
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
		getvar(token[1],"eax");// load token[1] to "eax"
		getvar(token[2],"ebx"); //load token[2] to "ebx"
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
}
