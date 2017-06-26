package compiler;
import java.util.*;
import compiler.analysis.DepthFirstAdapter;
import compiler.node.*;

public class Collector extends DepthFirstAdapter {

	public FunctionSum f;
	public FunctionSum current;
	public String vartype="";
	public String vartype2="";
	public String name="";
	public String type="";
	public String ref="";
	public String error="";
	public String gsize="";
	public boolean flag=false;
	public boolean reflag=false;
	public boolean reff=false;
	public String exprtype="";
	public String mtype="";
	public ArrayList<VarSum> paramtemp=null;
	public int bcounter=0;
	public int bcounter2=0;
	public int bcounter3=0;
	public FunctionSum standar_library= new FunctionSum("standard");
	public String ref2="";

	public String create_type(String vartype,int counter)
	{

        if(vartype.contains("int"))
        	vartype="int";
        else if(vartype.contains("char"))
        	vartype="char";
        for(int i=0;i<counter;i++)
        	vartype+="[]";
        return vartype;
	}
	
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
		var2.ref="char";
		fun2.arg.add(var2);
		standar_library.fun.add(fun2);
		FunctionSum fun3 = new FunctionSum("putc");
		fun3.type="nothing";
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
		VarSum var8=new VarSum("n","char");
		fun8.arg.add(var8);
		standar_library.fun.add(fun8);
		FunctionSum fun9 = new FunctionSum("chr");
		fun9.type="char";
		VarSum var9=new VarSum("c","int");
		fun9.arg.add(var9);
		standar_library.fun.add(fun9);
		FunctionSum fun10 = new FunctionSum("strlen");
		fun10.type="int";
		VarSum var10=new VarSum("s","char[]");
		var10.ref="char";//
		fun10.arg.add(var10);
		standar_library.fun.add(fun10);
		FunctionSum fun11 = new FunctionSum("strcmp");
		fun11.type="int";
		VarSum var11a=new VarSum("s1","char[]");
		var11a.ref="char";//
		fun11.arg.add(var11a);
		VarSum var11b=new VarSum("s2","char[]");
		var11b.ref="char";//
		fun11.arg.add(var11b);
		standar_library.fun.add(fun11);
		FunctionSum fun12 = new FunctionSum("strcpy");
		fun12.type="nothing";
		VarSum var12a=new VarSum("s1","char[]");
		var12a.ref="char";//
		fun12.arg.add(var12a);
		VarSum var12b=new VarSum("s2","char[]");
		var12b.ref="char";//
		fun12.arg.add(var12b);
		standar_library.fun.add(fun12);
		FunctionSum fun13 = new FunctionSum("strcat");
		fun13.type="nothing";
		VarSum var13a=new VarSum("s1","char[]");
		var13a.ref="char";//
		fun13.arg.add(var13a);
		VarSum var13b=new VarSum("s2","char[]");
		var13b.ref="char";//
		fun13.arg.add(var13b);
		standar_library.fun.add(fun13);
	}

	
	public void print_errors()
	{
		if(!error.replaceAll(" ","").equals(""))
			System.out.println("The errors found: \n"+error);
		//else
		//	System.out.println("No Semantic Errors found \n");		
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
		        	error+="No return statement for function "+current.name+" found !\n";
		        reflag=false;
	        }
	      	for(FunctionSum ftemp:current.fun)
	      	{
	      		if(ftemp.dec==true)
	      			error+="Function "+ftemp.name+" was declared but not defined !\n";
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
	        	if(!type.replaceAll(" ","").equals("nothing"))
	        	{
	        		error+="Error: Main programm sould have no return value !\n";
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
	        	
	        	if(current.findfunction(fa))
	        	{
	    	        if(flag==false)
	    	        {
	        			ftemp=current.getFunction(name);
	        			if(ftemp.dec==false)
	        			{
	        				error+="Error:The function "+fa.name+" has been already defined !\n";
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
	        		error+="Error:The name "+fa.name+" already exists for parameter name !\n";
	        	}
	        	else if(current.findvariable(fa.name))
	        	{
					error+="Error:The name "+fa.name+" already exists for variable name !\n";
	        	}
	        	else if (current.belongs!=null && current.belongs.findvariable(fa.name)) 
	        	{
	        			error+="Error:The name "+fa.name+" already exists for variable name !\n";
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
	        
	        if(node.getFparDef() != null)
	        {
	           node.getFparDef().apply(this);
	        }
	        if(ftemp!=null)
	        {
	        	
	        	if(ftemp.arg.size()!=current.arg.size())
	        	{
	        		
	        		error+="Error :Amount of args differ between definition and declaration in function " +current.name+" !\n";
	        		return;
	        	}
	        	Iterator<VarSum> itr=current.arg.iterator();
	        	Iterator<VarSum> itr2=ftemp.arg.iterator();
	    		while(itr.hasNext() && itr2.hasNext())
	    		{
	    			VarSum ret=itr.next();
	    			VarSum ret2=itr2.next();
	    			if(!ret.name.equals(ret2.name))
	    				error+="Error :Different name in argument "+ret.name+" in function "+current.name+" between definiton and declaration :"+ret.name+"-"+ret2.name+" !\n";
	    			if(!ret.type.equals(ret2.type))
	    				error+="Error :Different type in argument "+ret.name+" in function "+current.name+" between definiton and declaration :"+ret.type+"-"+ret2.type+" !\n";
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
	    		
	        	error+="Error :Main programm sould have no parameters !\n";
	    	}
	    	String varname="";
	        inAFparDef1FparDef(node);
	        if(node.getRef() != null)
	        {
	           ref2=node.getRef().toString();
	           reff=true;
	        }
	       
	        if(node.getVariable() != null)
	        {
	            varname=node.getVariable().toString();
	        }
	        
	        if(node.getType() != null)
	        {
	        	vartype2=vartype;
	            vartype=node.getType().toString();
	            gsize=vartype;
	            int counter = 0;
	            for( int i=0; i<vartype.length(); i++ ) {
	                if( vartype.charAt(i) == '[' ) {
	                    counter++;
	                } 
	            }
	            if(counter>0)
	            	vartype=create_type(vartype,counter);
	        }
	        VarSum v=new VarSum(varname,vartype);
	        v.sizes=gsize;
	        if(vartype.contains("[]") && reff==false)
	        	error+="Variable "+varname+" should be declared with ref !\n"; 	
	        v.ref=ref2;
	        reff=false;
	        if(!current.findparameter(v.name))
	        {
	        	current.arg.add(v);
	        }
	        else
	        {
	        	error+="Error :The parameter "+v.name+" already exists !\n";	        	
	        }
	        ref2="";
	        outAFparDef1FparDef(node);
	    }

	    @Override
	    public void caseAFparDef2FparDef(AFparDef2FparDef node)
	    {
	    	if(first_time==1)
	    	{
	    		
	        	error+="Error :Main programm sould have no parameters !\n";
	    	}
	    	String varname="";
	        inAFparDef2FparDef(node);
	        boolean tef=false;
	        if(reff==true)
	        	 tef=true;
	        if(node.getRef() != null)
	        {
	           ref2=node.getRef().toString();
	           reff=true;
	           tef=true;
	        }
	       String tref=ref2;
	        if(node.getVariable() != null)
	        {
	            varname=node.getVariable().toString();
	        }
	        Integer pl=current.arg.size();
	        String vtemp="";
	        if(!vartype.equals(""))
	        	vtemp=vartype;
	        if(node.getFparDef() != null)
	        {
	            node.getFparDef().apply(this);
	        }
	        VarSum v;
	        if(vartype2.equals(vtemp))
	        {
	        	if(vartype.contains("[]") && tef==false)
		        	error+="Variable2 "+varname+" should be declared with ref !\n"; 
	        	v=new VarSum(varname,vartype);
	        }	 
	        else
	        {
	        	if(vartype2.contains("[]") && reff==false)
		        	error+="Variable "+varname+" should be declared with ref !\n";
	        	v=new VarSum(varname,vartype2);
	        }
	        v.sizes=gsize;
	        
	        reff=false;
	        v.ref=tref;
	        if(!current.findparameter(v.name))
	        {
	        	current.arg.add(pl,v);
	        }
	        else
	        {
	        	error+="Error :The parameter "+v.name+" already exists !\n";
	        }
	       
	        outAFparDef2FparDef(node);
	    }
	      

	    @Override
	    public void caseAFparDef3FparDef(AFparDef3FparDef node)
	    {
	    	if(first_time==1)
	    	{
	    		
	        	error+="Error :Main programm sould have no parameters !\n";
	    	}
	    	String varname="";
	        inAFparDef3FparDef(node);
	        if(node.getRef() != null)
	        {
	        	ref2=node.getRef().toString();
	        	reff=true;
	        }
	        if(node.getVariable() != null)
	        {
	            varname=node.getVariable().toString();
	        }
	        if(node.getType() != null)
	        {
	           vartype2=vartype;
	           vartype= node.getType().toString();
	           gsize=vartype;
	           int counter = 0;
	            for( int i=0; i<vartype.length(); i++ ) {
	                if( vartype.charAt(i) == '[' ) {
	                    counter++;
	                } 
	            }
	            if(counter>0)
	            	vartype=create_type(vartype,counter);
	        }
	        if(vartype.contains("[]") && reff==false)
	        	error+="Variable "+varname+" should be declared with ref !\n";
	        VarSum v=new VarSum(varname,vartype);
	        v.sizes=gsize;
	        v.ref=ref2;
	       	if(!current.findparameter(v.name))
	        {
	        	current.arg.add(v);	        	
	        }
	        else
	        {
	           	error+="Error :The parameter "+v.name+" already exists !\n";
	        	
	        }
	       	reff=false;
	        if(node.getFparDef() != null)
	        {
	            node.getFparDef().apply(this);
	        }
	        ref2="";
	        outAFparDef3FparDef(node);
	    }

	    @Override
	    public void caseAFparDef4FparDef(AFparDef4FparDef node)
	    {
	    	if(first_time==1)
	    	{
	    		
	        	error+="Error :Main programm sould have no parameters !\n";
	    	}
	    	String varname="";
	        inAFparDef4FparDef(node);
	        if(node.getRef() != null)
	        {
	        	ref2=node.getRef().toString();
	        	reff=true;
	        }
	        if(node.getVariable() != null)
	        {
	            varname=node.getVariable().toString();
	        }
	        if(node.getType() != null)
	        {
	        	vartype2=vartype;
	            vartype=node.getType().toString();
	            gsize=vartype;
	            int counter = 0;
	            for( int i=0; i<vartype.length(); i++ ) {
	                if( vartype.charAt(i) == '[' ) {
	                    counter++;
	                } 
	            }
	            if(counter>0)
	            	vartype=create_type(vartype,counter);
	        }
	        if(vartype.contains("[]") && reff==false)
	        	error+="Variable"+varname+" should be declared with ref !\n";
	        VarSum v=new VarSum(varname,vartype);
	        v.sizes=gsize;
	        reff=false;
	        v.ref=ref2;
	        if(!current.findparameter(v.name))
	        {
	        	current.arg.add(v);
	        }
	        else
	        {
	        	error+="Error :The parameter "+v.name+" already exists !\n";
	        }
	        if(node.getFparDef() != null)
	        {
	            node.getFparDef().apply(this);
	        }
	        ref2="";
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
	    	String sizes="";
	        inAVarDefVarDef(node);
	        if(node.getVariable() != null)
	        {
	            name=node.getVariable().toString();
	        }
	        if(node.getType() != null)
	        {
	        	
	            vartype=node.getType().toString();
	            sizes=vartype;
	            int counter = 0;
	            for( int i=0; i<vartype.length(); i++ ) {
	                if( vartype.charAt(i) == '[' ) {
	                    counter++;
	                } 
	            }
	            if(counter>0)
	            	vartype=create_type(vartype,counter);
	           
	        }
	        VarSum va=new VarSum(name,vartype);
	        va.sizes=sizes;
	       	if(!current.findvariable(va.name) && !current.findparameter(va.name) && !current.exist_name(va.name))
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
	               v1.sizes=sizes;
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
	        bcounter=0;
	        bcounter2=0;
	        if(node.getLValue() != null)
	        {
	        	node.getLValue().apply(this);
	            left=mtype;
	        }
	        bcounter=0;
	        bcounter3=0;
	        if(node.getExpr() != null)
	        {
	            node.getExpr().apply(this);
	            right=mtype;
	        }
	       
	        if(bcounter2<bcounter3)
	        	error+="Error :Bigger Dimension on array than Expected !\n";
	        left=left.replaceAll(" ","");
	        right=right.replaceAll(" ","");
	        if(!left.equals(right))
	        	error+="Error :Wrong on Lvalue "+left+"<-"+right+" !\n";
	        bcounter=0;
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
	           String top2=current.type;
	           top2=top2.replaceAll(" ","");
	           topic=topic.replace(" ","");
	            if(!topic.equals(top2))
	            	error+="Error :Wrong return statement ,expecting "+top2+" but got "+topic+" in function "+current.name+" !\n";
	        }
	        else
	        {
	        	if(!current.type.equals("nothing "))
	        		error+="Error :Wrong return statement, expecting "+current.type+" in function "+current.name+" !\n";
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
	        	if((current.belongs!=null) && !current.belongs.exist_name(name) )
	        	{
        			error+="Error :The function "+name+" doesn't exist to be called !\n";
        			return;
        		}
        		else if(current.belongs==null)
        		{
        			error+="Error :The function "+name+" doesn't exist to be called !\n";
        			return;
        		}
        	}
	        
	        {
	        	FunctionSum ftemp;
	        	if(current.name.equals(name))
	        		ftemp=current;
	        	else
	        	{
	        		ftemp=current.getFunction(name);
	        		if(ftemp==null)
	        		{
	        			ftemp=standar_library.getFunction(name);
	        			if(ftemp==null)
	        			{
	        				ftemp=current.belongs.getFunction(name);
	        			}
	        		}
	        	}

	            List<PExpr> copy = new ArrayList<PExpr>(node.getExpr());

	            int i=0;
	           
	            for(PExpr e : copy)
	            {
	            	bcounter=0;
	            	bcounter2=0;
		            bcounter3=0;
		            reff=false;
	                e.apply(this);

	                String temp=exprtype;

	                
	                if(i>=ftemp.arg.size())
	                {
	                	error+="Error :Wrong functioncall "+ftemp.name+" with more arguments than expected !\n";
	                	return;
	            	}
	                if(bcounter3!=0 &&bcounter2<bcounter3)
	                	error+="Error :Used array with bigger dimension than expected !\n";
	               
	                VarSum vartemp=ftemp.arg.get(i);
	                String element=current.findvarsize(e.toString());
	                String target=e.toString();
	                String []clear=target.split(" ");
	                target=clear[0];
	                FunctionSum ftemp2=current;
	                while(element.equals("NULL") && ftemp2!=null && ftemp2.belongs!=null)
	                {
	                	ftemp2=current.belongs.getFunction(name);
	                	if(ftemp2!=null)
	                		element=ftemp2.findvarsize(target);
	                }
	                
	               
	                if(!element.equals("NULL"))
	                {
	                    if(vartemp.sizes.contains("["))
		               {
		            	   String element2=vartemp.sizes;
		            	   if(!target.contains("["))
		            	   {
		            		
		            		   element=element.replaceAll(" ","");
		            		   element2=element2.replaceAll(" ","");
		            		   String[] token=element.split("]");
		            		   String[] token2=element2.split("]");
		            		   for(int j=0;j<token.length;j++)
		            		   {
		            			   if(token[j].contains("int["))
		            				   token[j]=token[j].replace("int[","");
		            				else if(token[j].contains("char["))
		            				   token[j]=token[j].replace("char[","");
		            				else
		            					token[j]=token[j].replace("[","");
		            		   }
	            			   for(int j=0;j<token2.length;j++)
	            			   {
	            				   if(token2[j].contains("int["))
		            				   token2[j]=token2[j].replace("int[","");
		            			   else if(token2[j].contains("char["))
		            				   token2[j]=token2[j].replace("char[","");
		            			   else
		            				   token2[j]=token2[j].replace("[","");
	            			   }
		            		   for(int j=0;j<token2.length;j++)
		            		   {
		            			   if(j<token.length)
		            			   {
		            				   if(token2[j].equals(""))
		            					   continue;
		            				   Integer c1=Integer.parseInt(token[j]);
		            				   Integer c2=Integer.parseInt(token2[j]);
		            				  if(c2<c1)
		            					  error+="Bigger array than expected in argument size "+c1+" against "+c2+"\n";
		            			   }
		            			   else
		            				   break;
		            		   }
		            	   }
		               }
	                }
	                String currenttype=vartemp.type;
	                
	                currenttype=currenttype.replaceAll(" ","");
	                temp=temp.replaceAll(" ","");
	                
	                if(!temp.equals(currenttype))
	                {
	                	
	                	error+="Error :Different expression type in argument "+i+" in function call "+ftemp.name+" (it was declared "+currenttype+" "+vartemp.name+",but the one used is "+temp+") !\n";      	
	                }
	                if(!vartemp.ref.equals("")&&reff==false)
	                {
	                	error+="Error :Expecting ref expression type in argument "+i+" in function call "+ftemp.name+" (it was declared "+currenttype+" "+vartemp.name+",but the one used is "+temp+") !\n";      	
	                }
	                i++;
	            }
	            
	            if(i!=ftemp.arg.size())
                {
                	error+="Error :Wrong functioncall "+ftemp.name+" with less arguments than expected !\n";
                	return;
            	}
	            mtype=ftemp.type;
	            exprtype=ftemp.type;//now added
	        }
	        bcounter=0;
	     
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
	            		if(ftemp==null)
	            		{
	            			error+="Error :Variable "+var+" isn't declared !\n";
	            			return;
	            		}
	            		mtype=ftemp.findvariabletype(var);            		
	            		if(mtype.equals("NULL"))
	            		{
	            			mtype=ftemp.findparametertype(var);
	            			if(mtype.equals("NULL"))
	            			{//error+="Error :Variable "+var+" isn't declared !\n";
		            			FunctionSum ftemp2=ftemp.belongs;
		            			if(ftemp2==null)
		            			{
		            				error+="Error :Variable "+var+" isn't declared !\n";
		            				return;
		            			}
		            			mtype=ftemp2.findvariabletype(var);
		            			if(mtype==null)
		            			{
		            				mtype=ftemp2.findparametertype(var);
		            				if(mtype.equals("NULL"))
	            					{
	            						error+="Error :Variable "+var+" isn't declared !\n";
		            					return;
	            					}
		            			}
	            			}          			
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
	        String mtemp=mtype;
	        
	     
	        int counter = 0;
            for( int i=0; i<mtemp.length(); i++ ) {
                if( mtemp.charAt(i) == '[' ) {
                		counter++;
                		if(bcounter>0)
                		bcounter3--;
                }
            }
             bcounter=counter; 
             if(bcounter2==0)
            	 bcounter2=counter;
	        if(node.getExpr() != null)
	        {
	            node.getExpr().apply(this);
	            
	            String exprt=exprtype.replace(" ","");
	            if(!exprt.equals("int"))
	            {
	            	error+="Error :Wrong type of value in brackets of array !\n";
	            }
	            bcounter--;
	            bcounter3++;
	            mtemp=create_type(mtemp,bcounter);
	        }
	        
	        mtype=mtemp;
	        exprtype=mtype;
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
	        	error+="Error :Wrong exprtypes in Or expression ("+left+" or "+right+" ) !\n";
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
	        	error+="Error :Wrong exprtypes in And expression ( "+left+" and "+right+" ) !\n";
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
	       
	        if(!left.trim().equals(right.trim()))
	        	error+="Error :Wrong on CompareExpression ( "+left+" op "+right+" ) !\n";
	        else if(!left.equals("int") && !left.equals("int "))
	        	error+="Error :Wrong on CompareExpression, was expecting integer ( "+left+" op "+right+" ) !\n";
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
        	left=left.replaceAll(" ","");
        	right=right.replaceAll(" ","");
	        if(!left.equals(right))
	        	error+="Error :Wrong Plus Expression ( "+left+" + "+right+" ) !\n";
	        else if(!left.equals("int") && !left.equals("int "))
	        	error+="Error :Wrong Plus Expression,integer expected ( "+left+" + "+right+" ) !\n";
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
	        left=left.replaceAll(" ","");
        	right=right.replaceAll(" ","");
	        if(!left.equals(right))
	        	error+="Error :Wrong Minus Expression ( "+left+"  -  "+right+" ) !\n";
	        else if(!left.equals("int") && !left.equals("int "))
	        	error+="Error :Wrong Minus Expression,integer expected ( "+left+" + "+right+" ) !\n";
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
	        left=left.replaceAll(" ","");
        	right=right.replaceAll(" ","");
	        if(!left.equals(right))
	        	error+="Error :Wrong Mult Expression ( "+left+ " * "+right+" ) !\n";
	        else if(!left.equals("int") && !left.equals("int "))
	        	error+="Error :Wrong Mult Expression,integer expected ( "+left+ " * "+right+" ) !\n";
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
	        left=left.replaceAll(" ","");
        	right=right.replaceAll(" ","");
	        if(!left.equals(right))
	        	error+="Error :Wrong Slash Expression ( "+left+" / "+ right+" ) !\n";
	        else if(!left.equals("int") && !left.equals("int "))
	        	error+="Error :Wrong Slash Expression,integer expected ( "+left+" + "+right+" ) !\n";
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
	        left=left.replaceAll(" ","");
        	right=right.replaceAll(" ","");
	        if(!left.equals(right))
	        	error+="Error :Wrong Mod Expression ( "+left+" mod "+right+" ) !\n";
	        else if(!left.equals("int") && !left.equals("int "))
	        	error+="Error :Wrong Mod Expression,integer expected ( "+left+" + "+right+" ) !\n";
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
	        left=left.replaceAll(" ","");
        	right=right.replaceAll(" ","");
	        if(!left.equals(right))
	        	error+="Error :Wrong Div Expression ( "+left+" div "+right+" ) !\n";
	        else if(!left.equals("int") && !left.equals("int "))
	        	error+="Error :Wrong Div Expression,integer expected ( "+left+" + "+right+" ) !\n";
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
	            reff=true;
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
	            if(!mtype.equals("int ") && !mtype.equals("int"))
	            	error+="Cant negate something different than integer!\n";
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
