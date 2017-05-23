package compiler;
import java.util.*;
import compiler.analysis.DepthFirstAdapter;
import compiler.node.*;

public class Collector extends DepthFirstAdapter {

	public FunctionSum f;
	public FunctionSum current;
	public String vartype="";
	public String name="";
	public String type="";
	public String ref="";
	public String error="";
	public boolean flag=false;
	public boolean reflag=false;
	public String exprtype="";
	public String mtype="";
	public ArrayList<VarSum> paramtemp=null;
	public FunctionSum standar_library= new FunctionSum("standard");

	public void create_standar_library()
	{
		FunctionSum fun1=new FunctionSum("puti");
		fun1.type="nothing";
		VarSum var1=new VarSum("n","int");
		fun1.arg.add(var1);
		standar_library.fun.add(fun1);
		FunctionSum fun2 = new FunctionSum("puts");
		fun2.type="nothing";
		VarSum var2=new VarSum("s","char[]");
		var2.ref="char";//
		fun2.arg.add(var2);
		standar_library.fun.add(fun2);
		FunctionSum fun3 = new FunctionSum("putc");
		VarSum var3=new VarSum("c","char");
		fun3.arg.add(var3);
		standar_library.fun.add(fun3);
		FunctionSum fun4 = new FunctionSum("geti");
		fun4.type="int";
		standar_library.fun.add(fun4);
		FunctionSum fun5 = new FunctionSum("gets");
		fun5.type="nothing";
		VarSum var5a=new VarSum("n","int");
		fun5.arg.add(var5a);
		VarSum var5b=new VarSum("s","char[]");
		var5b.ref="char";//
		fun5.arg.add(var5b);
		standar_library.fun.add(fun5);
		FunctionSum fun6 = new FunctionSum("getc");
		fun6.type="char";
		standar_library.fun.add(fun6);
		FunctionSum fun7 = new FunctionSum("abs");
		fun7.type="int";
		VarSum var7=new VarSum("n","int");
		fun7.arg.add(var7);
		standar_library.fun.add(fun7);
		FunctionSum fun8 = new FunctionSum("ord");
		fun8.type="int";
		VarSum var8=new VarSum("n","int");
		fun8.arg.add(var8);
		standar_library.fun.add(fun8);
		FunctionSum fun9 = new FunctionSum("chr");
		fun9.type="char";
		VarSum var9=new VarSum("c","char");
		fun9.arg.add(var9);
		standar_library.fun.add(fun9);
		FunctionSum fun10 = new FunctionSum("strlen");
		fun10.type="int";
		VarSum var10=new VarSum("s","char");
		var10.ref="char";//
		fun10.arg.add(var10);
		standar_library.fun.add(fun10);
		FunctionSum fun11 = new FunctionSum("strcmp");
		fun11.type="int";
		VarSum var11a=new VarSum("s1","char");
		var11a.ref="char";//
		fun11.arg.add(var11a);
		VarSum var11b=new VarSum("s2","char");
		var11b.ref="char";//
		fun11.arg.add(var11b);
		standar_library.fun.add(fun11);
		FunctionSum fun12 = new FunctionSum("strcpy");
		fun12.type="nothing";
		VarSum var12a=new VarSum("s1","char");
		var12a.ref="char";//
		fun12.arg.add(var12a);
		VarSum var12b=new VarSum("s2","char");
		var12b.ref="char";//
		fun12.arg.add(var12b);
		standar_library.fun.add(fun12);
		FunctionSum fun13 = new FunctionSum("strcat");
		fun13.type="nothing";
		VarSum var13a=new VarSum("s1","char");
		var13a.ref="char";//
		fun13.arg.add(var13a);
		VarSum var13b=new VarSum("s2","char");
		var13b.ref="char";//
		fun13.arg.add(var13b);
		standar_library.fun.add(fun13);
	}

	
	public void print_errors()
	{
		System.out.println("The errors found: \n"+error);
	}
	
	int first_time=0;
	    @Override
	    public void caseStart(Start node)
	    {
	        inStart(node);
	        node.getPProgramm().apply(this);
	        node.getEOF().apply(this);
	        outStart(node);
	    }

	    @Override
	    public void caseAProgramm(AProgramm node)
	    {
	        inAProgramm(node);
	        if(node.getFuncDef() != null)
	        {
	            node.getFuncDef().apply(this);
	        }
	        
	        for(FunctionSum fa:f.fun)
	        {
		        for(VarSum va : fa.arg)
		       {
		    	   System.out.println(va.ref+" "+va.name+" "+va.type);
		       }
		       for(VarSum va : fa.vars)
		       {
		    	   System.out.println(va.ref+" "+va.name+" "+va.type);
		       }
	        }
	        outAProgramm(node);
	    }

	    @Override
	    public void caseAFuncDefFuncDef(AFuncDefFuncDef node)
	    {
	        inAFuncDefFuncDef(node);
	    	FunctionSum previous=null;
	        if(current!=null)
	        	previous=current;
	        if(node.getHeader() != null)
	        {
	        	
	            node.getHeader().apply(this);
	        }
	        {
	            List<PLocalDef> copy = new ArrayList<PLocalDef>(node.getLocalDef());
	            for(PLocalDef e : copy)
	            {
	                e.apply(this);
	            }
	        }
	        {

	            List<PStmt> copy = new ArrayList<PStmt>(node.getStmt());
	            for(PStmt e : copy)
	            {
	                e.apply(this);
	            }
	            String ctype=current.type;
		        if(reflag==false && !ctype.equals("nothing "))
		        	error+="No return statement for function "+current.name+" found\n";
		        reflag=false;
	        }
	      	for(FunctionSum ftemp:current.fun)
	      	{
	      		if(ftemp.dec==true)
	      			error+="Function "+ftemp.name+" was declared but not defined \n";
	      	}
	        if(previous!=null)
	        	current=previous;
	        outAFuncDefFuncDef(node);
	    }

	    @Override
	    public void caseAHeaderHeader(AHeaderHeader node)
	    {
	        inAHeaderHeader(node);
	        boolean fparam=false;
	        FunctionSum ftemp=null;
	        FunctionSum prev=null;
	        if(node.getVariable() != null)
	        {
	            name=node.getVariable().toString();
	        }
	        if(node.getRetType() != null)
	        {
	            type=node.getRetType().toString();
	        }
	        if(first_time==0)
	        {
	        	f=new FunctionSum(name);
	        	if(type.replaceAll(" ","")!="nothing")
	        	{
	        		System.out.println("Error:Main programm sould have no return value!");
	        		error+="Error:Main programm sould have no return value!\n";
	        	}
	        	if(node.getFparDef() != null)
	        	{
	        		//System.out.println("Error:Main programm sould have no parameters!");
	        		//error+="Error:Main programm sould have no parameters!\n";
	        	}
	        	f.type=type;
	        	current=f;
	        	first_time=1;
	        }    
	        else
	        {
	        	first_time++;
	        	FunctionSum fa=new FunctionSum(name);
	        	fa.type=type;
	        	fa.belongs=current;
	        	fa.dec=flag;
	        	System.out.println("Mother funtion of "+fa.name+" is function "+ current.name+flag);
	        	if(current.findfunction(fa))
	        	{
	    	        if(flag==false)
	    	        {
	        			ftemp=current.getFunction(name);
	        			if(ftemp.dec==false)
	        			{
	        				error+="Error:The function "+fa.name+" has been already defined exists !\n";
	        				return;
	        			}
	        			if(!ftemp.type.equals(fa.type))
	        			{
	        				error+="Error:The function "+fa.name+" has been defined with different return type !\n";
	        				return;
	        			}
	        			ftemp.dec=false;
	        			fparam=true;
	    	        }
	    	        else
	    	        	error+="Error:The function "+fa.name+" has been already declared !\n";
	    	              		
	        	}
	        	else if(current.findparameter(fa.name) )
	        	{
	        		error+="Error:The name "+fa.name+" already exists for parameter name!\n";
	        	}
	        	else if(current.findvariable(fa.name))
	        	{
					error+="Error:The name "+fa.name+" already exists for variable name!\n";
	        	}
	        	else if ((current.belongs!=null)) 
	        	{
	        		System.out.println("Mother of mother is "+current.belongs.name);
	        		if( current.belongs.findvariable(fa.name))
	        			error+="Error:The name "+fa.name+" already exists for variable name!\n";
	        	}
	        	else
	        	{
	        		if(fparam==false)
	        			current.fun.add(fa);
	        	}
	        	if(flag==true)
	        		prev=current;
	        	current=fa;
	        }
	        System.out.println("EIMAI STIN "+current.name);
	        if(node.getFparDef() != null)
	        {
	           node.getFparDef().apply(this);
	        }
	        if(ftemp!=null)
	        {
	        	System.out.println(ftemp.arg.size()+current.arg.size());
	        	if(ftemp.arg.size()!=current.arg.size())
	        		error+="Amount of args differ between definition and declaration in " +current.name+"\n";
	        	Iterator<VarSum> itr=current.arg.iterator();
	        	Iterator<VarSum> itr2=ftemp.arg.iterator();
	    		while(itr.hasNext() && itr2.hasNext())
	    		{
	    			VarSum ret=itr.next();
	    			VarSum ret2=itr2.next();
	    			if(!ret.name.equals(ret2.name))
	    				error+="Different name in argument "+ret.name+"in function "+current.name+" between definiton and declaration "+ret.name+"-"+ret2.name+"\n";
	    			if(!ret.type.equals(ret2.type))
	    				error+="Different type in argument "+ret.name+"in function "+current.name+" between definiton and declaration "+ret.type+"-"+ret2.type+"\n";
	    		}
	        }
	        if(flag==true)
	        	current=prev;
	        outAHeaderHeader(node);
	    }

	    @Override
	    public void caseAFuncDefLocalDef(AFuncDefLocalDef node)
	    {
	        inAFuncDefLocalDef(node);
	        if(node.getFuncDef() != null)
	        {
	            node.getFuncDef().apply(this);
	        }
	        outAFuncDefLocalDef(node);
	    }

	    @Override
	    public void caseAFuncDeclLocalDef(AFuncDeclLocalDef node)
	    {
	        inAFuncDeclLocalDef(node);
	        if(node.getFuncDecl() != null)
	        {
	            node.getFuncDecl().apply(this);
	        }
	        outAFuncDeclLocalDef(node);
	    }

	    @Override
	    public void caseAVarDefLocalDef(AVarDefLocalDef node)
	    {
	        inAVarDefLocalDef(node);
	        if(node.getVarDef() != null)
	        {
	            node.getVarDef().apply(this);
	        }
	        outAVarDefLocalDef(node);
	    }

	    @Override
	    public void caseAFuncDeclFuncDecl(AFuncDeclFuncDecl node)
	    {
	        inAFuncDeclFuncDecl(node);
	        flag=true;
	        if(node.getHeader() != null)
	        {
	            node.getHeader().apply(this);
	        }
	        flag=false;
	        outAFuncDeclFuncDecl(node);
	    }

	    @Override
	    public void caseAFparDef1FparDef(AFparDef1FparDef node)
	    {
	    	if(first_time==1)
	    	{
	    		System.out.println("Error:Main programm sould have no parameters!");
	        	error+="Error:Main programm sould have no parameters!\n";
	    	}
	    	String varname="";
	        inAFparDef1FparDef(node);
	        if(node.getRef() != null)
	        {
	           ref=node.getRef().toString();
	        }
	        if(node.getVariable() != null)
	        {
	            varname=node.getVariable().toString();
	        }
	        if(node.getType() != null)
	        {
	            vartype=node.getType().toString();
	        }
	        VarSum v=new VarSum(varname,vartype);
	        v.ref=ref;
	        if(!current.findparameter(v.name))
	        {
	        	current.arg.add(v);
	        }
	        else
	        {
	        	error+="Error:The parameter "+v.name+" already exists !\n";
	        	System.out.println("Error:The parameter "+v.name+" already exists !");
	        }
	        outAFparDef1FparDef(node);
	    }

	    @Override
	    public void caseAFparDef2FparDef(AFparDef2FparDef node)
	    {
	    	if(first_time==1)
	    	{
	    		System.out.println("Error:Main programm sould have no parameters!");
	        	error+="Error:Main programm sould have no parameters!\n";
	    	}
	    	String varname="";
	        inAFparDef2FparDef(node);
	        if(node.getRef() != null)
	        {
	           ref=node.getRef().toString();
	        }
	        if(node.getVariable() != null)
	        {
	            varname=node.getVariable().toString();
	        }
	        if(node.getFparDef() != null)
	        {
	            node.getFparDef().apply(this);
	        }
	        VarSum v=new VarSum(varname,vartype);
	        v.ref=ref;
	        if(!current.findparameter(v.name))
	        {
	        	current.arg.add(v);
	        }
	        else
	        {
	        	error+="Error:The parameter "+v.name+" already exists !\n";

	        	System.out.println("Error:The parameter "+v.name+" already exists !");
	        }
	        outAFparDef2FparDef(node);
	    }

	    @Override
	    public void caseAFparDef3FparDef(AFparDef3FparDef node)
	    {
	    	if(first_time==1)
	    	{
	    		System.out.println("Error:Main programm sould have no parameters!");
	        	error+="Error:Main programm sould have no parameters!\n";
	    	}
	    	String varname="";
	        inAFparDef3FparDef(node);
	        if(node.getRef() != null)
	        {
	        	 ref=node.getRef().toString();
	        }
	        if(node.getVariable() != null)
	        {
	            varname=node.getVariable().toString();
	        }
	        if(node.getType() != null)
	        {
	           vartype= node.getType().toString();
	        }
	        if(node.getFparDef() != null)
	        {
	            node.getFparDef().apply(this);
	        }
	        VarSum v=new VarSum(varname,vartype);
	        v.ref=ref;
	       	if(!current.findparameter(v.name))
	        {
	        	current.arg.add(v);
	        }
	        else
	        {
	           	error+="Error:The parameter "+v.name+" already exists !\n";
	        	System.out.println("Error:The parameter "+v.name+" already exists !");
	        }
	        outAFparDef3FparDef(node);
	    }

	    @Override
	    public void caseAFparDef4FparDef(AFparDef4FparDef node)
	    {
	    	if(first_time==1)
	    	{
	    		System.out.println("Error:Main programm sould have no parameters!");
	        	error+="Error:Main programm sould have no parameters!\n";
	    	}
	    	String varname="";
	        inAFparDef4FparDef(node);
	        if(node.getRef() != null)
	        {
	        	 ref=node.getRef().toString();
	        }
	        if(node.getVariable() != null)
	        {
	            varname=node.getVariable().toString();
	        }
	        if(node.getType() != null)
	        {
	            vartype=node.getType().toString();
	        }
	        VarSum v=new VarSum(varname,vartype);
	        v.ref=ref;
	        if(!current.findparameter(v.name))
	        {
	        	current.arg.add(v);
	        }
	        else
	        {
	        	error+="Error:The parameter "+v.name+" already exists !\n";
	        	System.out.println("Error :The parameter "+v.name+" already exists !");
	        }
	        if(node.getFparDef() != null)
	        {
	            node.getFparDef().apply(this);
	        }
	        outAFparDef4FparDef(node);
	    }

	    @Override
	    public void caseAFparDef5FparDef(AFparDef5FparDef node)
	    {
	        inAFparDef5FparDef(node);
	        outAFparDef5FparDef(node);
	    }

	    @Override
	    public void caseAVarDefVarDef(AVarDefVarDef node)
	    {
	    	String name="";
	        inAVarDefVarDef(node);
	        if(node.getVariable() != null)
	        {
	            name=node.getVariable().toString();
	        }
	        if(node.getType() != null)
	        {
	            vartype=node.getType().toString();
	        }
	        VarSum va=new VarSum(name,vartype);
	       	if(!current.findvariable(va.name) && !current.findparameter(va.name))
	       	{
	       		current.vars.add(va);
	       	}
	       	else
	       	{
	       		error+="Error :This variable name "+va.name+" is already used !\n";
	       	}
	        {
	            List<TVariable> copy = new ArrayList<TVariable>(node.getVariables());
	            for(TVariable e : copy)
	            {
	               name=e.toString();
	               VarSum v1=new VarSum(name,vartype);
	               if(!current.findvariable(v1.name) && !current.findparameter(v1.name))
	        		{
		   	           	current.vars.add(v1);
	        		}
	        		else
	        		{
	        			error+="Error :This variable name "+v1.name+" is already used !\n";
	        		}
	        	}
	        }
	        outAVarDefVarDef(node);
	    }

	    @Override
	    public void caseAEmptyStmt(AEmptyStmt node)
	    {
	        inAEmptyStmt(node);
	        outAEmptyStmt(node);
	    }

	    @Override
	    public void caseASemiStmt(ASemiStmt node)
	    {
	        inASemiStmt(node);
	        outASemiStmt(node);
	    }

	    @Override
	    public void caseAExprStmt(AExprStmt node)
	    {
	        inAExprStmt(node);
	        String left="";
	        String right="";
	        if(node.getLValue() != null)
	        {
	        	node.getLValue().apply(this);
	            left=mtype;
	        }
	        if(node.getExpr() != null)
	        {
	            node.getExpr().apply(this);
	            right=mtype;
	        }
	        if(!left.equals(right))
	        	error+="Wrong on Lvalue "+left+"<-"+right+"\n";
	        outAExprStmt(node);
	    }

	    @Override
	    public void caseABlockStmt(ABlockStmt node)
	    {
	        inABlockStmt(node);
	        {
	            List<PStmt> copy = new ArrayList<PStmt>(node.getStatements());
	            for(PStmt e : copy)
	            {
	                e.apply(this);
	            }
	        }
	        
	        outABlockStmt(node);
	    }

	    @Override
	    public void caseAFuncCallStmt(AFuncCallStmt node)
	    {
	        inAFuncCallStmt(node);
	        if(node.getFuncCall() != null)
	        {
	            node.getFuncCall().apply(this);
	        }
	        outAFuncCallStmt(node);
	    }

	    @Override
	    public void caseAWhileStatementStmt(AWhileStatementStmt node)
	    {
	        inAWhileStatementStmt(node);
	        if(node.getExpr() != null)
	        {
	            node.getExpr().apply(this);
	        }
	        if(node.getStmt() != null)
	        {
	            node.getStmt().apply(this);
	        }
	        outAWhileStatementStmt(node);
	    }

	    @Override
	    public void caseAReturnStmt(AReturnStmt node)
	    {
	        inAReturnStmt(node);
	        reflag=true;
	        if(node.getExpr() != null)
	        {
	            node.getExpr().apply(this);
	            String topic=exprtype;
	           
	            if(!topic.equals(current.type))
	            	error+="Wrong return statement in function "+current.name+"\n";
	        }
	        else
	        {
	        	if(!current.type.equals("nothing"))
	        		error+="Wrong return statement in function "+current.name+"\n";
	        }
	        outAReturnStmt(node);
	    }

	    @Override
	    public void caseAIfStmt(AIfStmt node)
	    {
	        inAIfStmt(node);
	        if(node.getCondition() != null)
	        {
	            node.getCondition().apply(this);
	        }
	        if(node.getStatement() != null)
	        {
	            node.getStatement().apply(this);
	        }
	        outAIfStmt(node);
	    }

	    @Override
	    public void caseAIfElseStmt(AIfElseStmt node)
	    {
	        inAIfElseStmt(node);
	        if(node.getCondition() != null)
	        {
	            node.getCondition().apply(this);
	        }
	        if(node.getThen() != null)
	        {
	            node.getThen().apply(this);
	        }
	        if(node.getElse() != null)
	        {
	            node.getElse().apply(this);
	        }
	        outAIfElseStmt(node);
	    }

	    @Override
	    public void caseAFuncCallFuncCall(AFuncCallFuncCall node)
	    {
	    	String name="";
	        inAFuncCallFuncCall(node);
	        if(node.getVariable() != null)
	        {
	            name=node.getVariable().toString();
	        }
	        if((!current.exist_name(name)) && (!standar_library.exist_name(name)))
        	{
        		error+="Error:The function "+name+" doesn't exist to be called!\n";
        		return;
        	}
	        
	        {
	        	FunctionSum ftemp;
	        	if(current.name.equals(name))
	        		ftemp=current;
	        	else
	        	{
	        		ftemp=current.getFunction(name);
	        		if(ftemp==null)
	        			ftemp=standar_library.getFunction(name);
	        	}

	            List<PExpr> copy = new ArrayList<PExpr>(node.getExpr());

	            int i=0;
	            for(PExpr e : copy)
	            {
	                e.apply(this);
	                String temp=exprtype;
	                VarSum vartemp=ftemp.arg.get(i);
	                String currenttype=vartemp.type;
	              	System.out.println("Function "+current.name+"type "+currenttype);
	                currenttype=currenttype.replaceAll(" ","");
	                temp=temp.replaceAll(" ","");
	                if(!temp.equals(currenttype))
	                {
	                	int num=i+1;
	                	error+="Different expression type in argument "+num+" in function call "+ftemp.name+"\n";
	                System.out.println("->>> "+currenttype+temp); 
	                }
	                i++;
	            }
	            mtype=ftemp.type;
	        }
	        outAFuncCallFuncCall(node);
	    }

	    @Override
	    public void caseATypeType(ATypeType node)
	    {
	        inATypeType(node);
	        if(node.getDataType() != null)
	        {
	            node.getDataType().apply(this);
	        }
	        if(node.getArrayNum() != null)
	        {
	            node.getArrayNum().apply(this);
	        }
	     /*  {
	            List<PArrayNum> copy = new ArrayList<PArrayNum>(node.getArrayNum());
	            for(PArrayNum e : copy)
	            {
	                e.apply(this);
	            }
	        }*/
	        outATypeType(node);
	    }


	    @Override
	   	public void caseAType2Type(AType2Type node)
	    {
	        inAType2Type(node);
	        if(node.getDataType() != null)
	        {
	            node.getDataType().apply(this);
	        }
	        node.getArrayNum().apply(this);
	        {
	            List<PArrayNumDefined> copy = new ArrayList<PArrayNumDefined>(node.getArrayNumDefined());
	            for(PArrayNumDefined e : copy)
	            {
	                e.apply(this);
	            }
	        }
	        outAType2Type(node);
	    }

	    @Override
	    public void caseADataTypeRetType(ADataTypeRetType node)
	    {
	        inADataTypeRetType(node);
	        if(node.getDataType() != null)
	        {
	            node.getDataType().apply(this);
	        }
	        outADataTypeRetType(node);
	    }

	    @Override
	    public void caseANothingRetType(ANothingRetType node)
	    {
	        inANothingRetType(node);
	        if(node.getNothing() != null)
	        {
	            node.getNothing().apply(this);
	        }
	        outANothingRetType(node);
	    }

	    @Override
	    public void caseAArrayNumArrayNum(AArrayNumArrayNum node)
	    {
	        inAArrayNumArrayNum(node);
	        if(node.getLBkt() != null)
	        {
	            node.getLBkt().apply(this);
	        }
	        if(node.getInteger() != null)
	        {
	            node.getInteger().apply(this);
	        }
	        if(node.getRBkt() != null)
	        {
	            node.getRBkt().apply(this);
	        }
	        outAArrayNumArrayNum(node);
	    }

	    @Override
	    public void caseAIntDataType(AIntDataType node)
	    {
	        inAIntDataType(node);
	        if(node.getInt() != null)
	        {
	            node.getInt().apply(this);
	        }
	        outAIntDataType(node);
	    }

	    @Override
	    public void caseACharDataType(ACharDataType node)
	    {
	        inACharDataType(node);
	        if(node.getChar() != null)
	        {
	            node.getChar().apply(this);
	        }
	        outACharDataType(node);
	    }

	    @Override
	    public void caseAIdLValue(AIdLValue node)
	    {
	        inAIdLValue(node);
	        if(node.getVariable() != null)
	        {
	        	String var= node.getVariable().toString();
	            mtype=current.findparametertype(var);
	            if(mtype.equals("NULL"))
	            {
	            	mtype=current.findvariabletype(var);
	            	if(mtype.equals("NULL"))
	            	{
	            		FunctionSum ftemp=current.belongs;
	            		mtype=ftemp.findvariabletype(var);
	            		if(mtype.equals("NULL"))
	            		{
	            			mtype=ftemp.findparametertype(var);
	            			if(mtype.equals("NULL"))
	            			error+="Variable "+var+" isnt declared!\n";
	            		}
	            	}
	            }
	            exprtype=mtype;
	           
	        }
	        outAIdLValue(node);
	    }

	    @Override
	    public void caseAStringLitLValue(AStringLitLValue node)
	    {
	        inAStringLitLValue(node);
	        if(node.getStringLit() != null)
	        {
	            node.getStringLit().apply(this);
	            mtype="char[]";
	        }
	        exprtype=mtype;
	        outAStringLitLValue(node);
	    }

	    @Override
	    public void caseALValueArrayLValue(ALValueArrayLValue node)
	    {
	        inALValueArrayLValue(node);
	        if(node.getLValueArray() != null)
	        {
	            node.getLValueArray().apply(this);
	        }
	        outALValueArrayLValue(node);
	    }

	    @Override
	    public void caseALValueArrayLValueArray(ALValueArrayLValueArray node)
	    {
	        inALValueArrayLValueArray(node);
	        if(node.getLValue() != null)
	        {
	            node.getLValue().apply(this);
	        }
	        if(node.getExpr() != null)
	        {
	            node.getExpr().apply(this);
	        }
	        outALValueArrayLValueArray(node);
	    }

	    @Override
	    public void caseACondOrExpr(ACondOrExpr node)
	    {
	    	
	        inACondOrExpr(node);
	        if(node.getLeft() != null)
	        {
	            node.getLeft().apply(this);
	        }
	        String left=exprtype;
	        if(node.getRight() != null)
	        {
	            node.getRight().apply(this);
	        }
	        String right=exprtype;
	        if(!left.equals(right))
	        	error+="Wrong exprtypes in Or expression\n";
	        outACondOrExpr(node);
	    }

	    @Override
	    public void caseACompAndExpr(ACompAndExpr node)
	    {
	        inACompAndExpr(node);
	        if(node.getLeft() != null)
	        {
	            node.getLeft().apply(this);
	        }
	        String left=exprtype;
	        if(node.getRight() != null)
	        {
	            node.getRight().apply(this);
	        }
	        String right=exprtype;
	        
	        if(!left.equals(right))
	        	error+="Wrong exprtypes in And expression\n";
	        outACompAndExpr(node);
	    }

	    @Override
	    public void caseACompEqExpr(ACompEqExpr node)
	    {
	        inACompEqExpr(node);
	        if(node.getLeft() != null)
	        {
	            node.getLeft().apply(this);
	        }
	        String left=mtype;
	        if(node.getRight() != null)
	        {
	            node.getRight().apply(this);
	        }
	        String right=mtype;
	        if(!left.equals(right))
	        	error+="Wrong on CompareExpression\n";
	        exprtype="logic";
	        outACompEqExpr(node);
	    }

	    @Override
	    public void caseACompNotEqExpr(ACompNotEqExpr node)
	    {
	        inACompNotEqExpr(node);
	        if(node.getExpr() != null)
	        {
	            node.getExpr().apply(this);
	        }
	        outACompNotEqExpr(node);
	    }

	    @Override
	    public void caseACondBlockExpr(ACondBlockExpr node)
	    {
	        inACondBlockExpr(node);
	        if(node.getExpr() != null)
	        {
	            node.getExpr().apply(this);
	        }
	        outACondBlockExpr(node);
	    }

	    @Override
	    public void caseAPlusExpr(APlusExpr node)
	    {
	        inAPlusExpr(node);
	        if(node.getLeft() != null)
	        {
	        	
	            node.getLeft().apply(this);
	        }
	        String left=mtype;
	        if(node.getRight() != null)
	        {
	            node.getRight().apply(this);
	        }
	        String right=mtype;
	        if(!left.equals(right))
	        	error+="Wrong Plus Expression!\n";
	        exprtype="int";
	        outAPlusExpr(node);
	    }

	    @Override
	    public void caseAMinusExpr(AMinusExpr node)
	    {
	        inAMinusExpr(node);
	        if(node.getLeft() != null)
	        {
	            node.getLeft().apply(this);
	        }
	        String left=mtype;
	        if(node.getRight() != null)
	        {
	            node.getRight().apply(this);
	        }
	        String right=mtype;
	        if(!left.equals(right))
	        	error+="Wrong Minus Expression!"+left+" "+right+"\n";
	        exprtype="int";
	        outAMinusExpr(node);
	    }

	    @Override
	    public void caseAMultExpr(AMultExpr node)
	    {
	        inAMultExpr(node);
	        if(node.getLeft() != null)
	        {
	            node.getLeft().apply(this);
	        }
	        String left=mtype;
	        if(node.getRight() != null)
	        {
	            node.getRight().apply(this);
	        }
	        String right=mtype;
	        if(!left.equals(right))
	        	error+="Wrong Mult Expression!\n";
	        exprtype="int";
	        outAMultExpr(node);
	    }

	    @Override
	    public void caseASlashExpr(ASlashExpr node)
	    {
	        inASlashExpr(node);
	        if(node.getLeft() != null)
	        {
	            node.getLeft().apply(this);
	        }
	        String left=mtype;
	        if(node.getRight() != null)
	        {
	            node.getRight().apply(this);
	        }
	        String right=mtype;
	        if(!left.equals(right))
	        	error+="Wrong Slash Expression!\n";
	        exprtype="int";
	        outASlashExpr(node);
	    }

	    @Override
	    public void caseAModExpr(AModExpr node)
	    {
	        inAModExpr(node);
	        if(node.getLeft() != null)
	        {
	            node.getLeft().apply(this);
	        }
	        String left=mtype;
	        if(node.getRight() != null)
	        {
	            node.getRight().apply(this);
	        }
	        String right=mtype;
	        if(!left.equals(right))
	        	error+="Wrong Mod Expression!\n";
	        exprtype="int";
	        outAModExpr(node);
	    }

	    @Override
	    public void caseADivExpr(ADivExpr node)
	    {
	        inADivExpr(node);
	        if(node.getLeft() != null)
	        {
	            node.getLeft().apply(this);
	        }
	        String left=mtype;
	        if(node.getRight() != null)
	        {
	            node.getRight().apply(this);
	        }
	        String right=mtype;
	        if(!left.equals(right))
	        	error+="Wrong Div Expression!\n";
	        exprtype="int";
	        outADivExpr(node);
	    }

	    @Override
	    public void caseATermIntExpr(ATermIntExpr node)
	    {
	        inATermIntExpr(node);
	        if(node.getInteger() != null)
	        {
	            node.getInteger().apply(this);
	            mtype="int ";
	            exprtype="int ";
	        }
	        
	        outATermIntExpr(node);
	    }

	    @Override
	    public void caseATermCharExpr(ATermCharExpr node)
	    {
	        inATermCharExpr(node);
	        if(node.getConstChar() != null)
	        {
	            node.getConstChar().apply(this);
	            mtype="char";
	            exprtype="char";
	        }
	        outATermCharExpr(node);
	    }

	    @Override
	    public void caseATermValExpr(ATermValExpr node)
	    {
	        inATermValExpr(node);
	        if(node.getLValue() != null)
	        {
	            node.getLValue().apply(this);
	        }
	        outATermValExpr(node);
	    }

	    @Override
	    public void caseAPlusMinusExpExpr(APlusMinusExpExpr node)
	    {
	        inAPlusMinusExpExpr(node);
	        if(node.getExpr() != null)
	        {
	            node.getExpr().apply(this);
	        }
	        outAPlusMinusExpExpr(node);
	    }

	    @Override
	    public void caseAFuncCallExpr(AFuncCallExpr node)
	    {
	        inAFuncCallExpr(node);
	        if(node.getFuncCall() != null)
	        {
	            node.getFuncCall().apply(this);
	        }
	        outAFuncCallExpr(node);
	    }

	    @Override
	    public void caseATermExprExpr(ATermExprExpr node)
	    {
	        inATermExprExpr(node);
	        if(node.getExpr() != null)
	        {
	            node.getExpr().apply(this);
	        }
	        outATermExprExpr(node);
	    }

}
