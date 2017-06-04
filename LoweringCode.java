package compiler;
import java.util.*;
import compiler.analysis.DepthFirstAdapter;
import compiler.node.*;

public class LoweringCode extends DepthFirstAdapter {
	
	public FunctionSum library;
	public String exprtype="";
	public String mtype="";
	public String code="";
	public String name="";
	public String currentf="";
	public String kra="";
	public int register=0;
	public HelpfullMethods help;
	public FunctionSum symboltable;
	ArrayList<String> conditions=new ArrayList<String>();
	ArrayList<String> orcond=new ArrayList<String>();
	ArrayList<String> andjumps=new ArrayList<String>();
	ArrayList<String> jumps=new ArrayList<String>();	
	ArrayList<String> operator;

	LoweringCode(FunctionSum symboltable,FunctionSum library)
	{
		this.symboltable=symboltable;
		this.library=library;
		help= new HelpfullMethods();
	}
	
	public void print_code()
	{
		System.out.println("The code Constructed: \n"+code);
	}

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
	    String tname="";
	    if(node.getHeader() != null)
	    {
	        node.getHeader().apply(this);
	        tname=name;
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
	    }
	    String code_line=help.genquad("endu",tname,"-","-");
        help.instruction_list.add(code_line);
	    outAFuncDefFuncDef(node);
	}
	
	@Override
	public void caseAHeaderHeader(AHeaderHeader node)
	{
	    inAHeaderHeader(node);
	    if(node.getVariable() != null)
	    {
	        name=node.getVariable().toString();
	        currentf=name;
	        String code_line=help.genquad("unit",name,"-","-");
	        help.instruction_list.add(code_line);
	    }
	   
	    if(node.getFparDef() != null)
	    {
	        node.getFparDef().apply(this);
	    }
	    if(node.getRetType() != null)
	    {
	        node.getRetType().apply(this);
	    }
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
	    if(node.getHeader() != null)
	    {
	        node.getHeader().apply(this);
	    }
	    outAFuncDeclFuncDecl(node);
	}
	
	@Override
	public void caseAFparDef1FparDef(AFparDef1FparDef node)
	{
	    inAFparDef1FparDef(node);
	    if(node.getRef() != null)
	    {
	        node.getRef().apply(this);
	    }
	    if(node.getVariable() != null)
	    {
	        node.getVariable().apply(this);
	    }
	    if(node.getType() != null)
	    {
	        node.getType().apply(this);
	    }
	    outAFparDef1FparDef(node);
	}
	
	@Override
	public void caseAFparDef2FparDef(AFparDef2FparDef node)
	{
	    inAFparDef2FparDef(node);
	    if(node.getRef() != null)
	    {
	        node.getRef().apply(this);
	    }
	    if(node.getVariable() != null)
	    {
	        node.getVariable().apply(this);
	    }
	    if(node.getFparDef() != null)
	    {
	        node.getFparDef().apply(this);
	    }
	    outAFparDef2FparDef(node);
	}
	
	@Override
	public void caseAFparDef3FparDef(AFparDef3FparDef node)
	{
	    inAFparDef3FparDef(node);
	    if(node.getRef() != null)
	    {
	        node.getRef().apply(this);
	    }
	    if(node.getVariable() != null)
	    {
	        node.getVariable().apply(this);
	    }
	    if(node.getType() != null)
	    {
	        node.getType().apply(this);
	    }
	    if(node.getFparDef() != null)
	    {
	        node.getFparDef().apply(this);
	    }
	    outAFparDef3FparDef(node);
	}
	
	@Override
	public void caseAFparDef4FparDef(AFparDef4FparDef node)
	{
	    inAFparDef4FparDef(node);
	    if(node.getRef() != null)
	    {
	        node.getRef().apply(this);
	    }
	    if(node.getVariable() != null)
	    {
	        node.getVariable().apply(this);
	    }
	    if(node.getType() != null)
	    {
	        node.getType().apply(this);
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
	    inAVarDefVarDef(node);
	    if(node.getVariable() != null)
	    {
	        node.getVariable().apply(this);
	    }
	    {
	        List<TVariable> copy = new ArrayList<TVariable>(node.getVariables());
	        for(TVariable e : copy)
	        {
	            e.apply(this);
	        }
	    }
	    if(node.getType() != null)
	    {
	        node.getType().apply(this);
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
	    kra="";
	    if(node.getLValue() != null)
	    {
	    	 node.getLValue().apply(this);
	    	if(node.getLValue() instanceof ALValueArrayLValue)
	    	{
	    		String cs=node.getLValue().toString();
	    		String []s=cs.split(" ");
	    		FunctionSum f=symboltable.get_function_from_Symboltable(currentf);
	    		String ttype=f.findvarsize(s[0]);
	    		
	    		String []s2=ttype.split("]");
	    		s2[0]=s2[0].replaceAll("int ","");
	    		for(int i=0;i<s2.length-1;i++)
	    		{
	    			s2[i]=s2[i].replace("[","");
	    			if(s2[i].length()>1)
	    				s2[i]=s2[i].replaceAll(" ","");
	    		}
	    		
	    		boolean addflag=false;
	    		String prereg="";
	    		for(int i=1;i<s.length-1;i++)
	    		{
	    			
	    			register++;
	    			if(s2[i].equals(" "))
	    				break;
	    		    String code_line=help.genquad("*",s[i],s2[i],"$"+register);
	    	        help.instruction_list.add(code_line);
	    	       
	    	        if(addflag==false)
	    				addflag=true;
	    	        else
	    	        {
	    	        	register++;
	    	        	code_line=help.genquad("+",prereg,"$"+(register-1),"$"+register);
		    	        help.instruction_list.add(code_line);
		    	        prereg="$"+register;
	    	        	addflag=false;
	    	        }
	    	        prereg="$"+register;
	    		}
	    		
	    		register++;
	    		String code_line="";
	    		if(prereg.equals(""))
	    		{
	    			
	    			if(!kra.equals(""))
	    				code_line=help.genquad("+",kra,s[s.length-1],"$"+register);
	    			else
	    				code_line=help.genquad("+","0",s[s.length-1],"$"+register);
	    		}
	        	else
	        	{
	        		if(!kra.equals(""))
	        			code_line=help.genquad("+",prereg,kra,"$"+register);
	        		else
	        			code_line=help.genquad("+",prereg,s[s.length-1],"$"+register);
	        	}
    	        help.instruction_list.add(code_line);
    	        register++;
    	        code_line=help.genquad("array",s[0],"$"+(register-1),"$"+register);
   	         	help.instruction_list.add(code_line);
		         mtype="[$"+register+"]";
		         kra=mtype;
	    	}
	        else
	        	left=mtype;
	    }
	    
	    if(node.getExpr() != null)
	    {
	        node.getExpr().apply(this);
	        right=mtype;
	    }
	    left=left.replaceAll(" ","");
	    right=right.replaceAll(" ","");
	    String code_line=help.genquad(":=",left,"-",right);
        help.instruction_list.add(code_line);
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
	    String right="";
	    String left="";
	    String start="";
	    start=""+help.nextquad();
	    conditions=new ArrayList<String>();
  		jumps=new ArrayList<String>();	
  		operator=new ArrayList<String>();
  		ArrayList<String> conditions2;//=new ArrayList<String>();
  		ArrayList<String> jumps2;//=new ArrayList<String>();
	    if(node.getExpr() != null)
	    {
	        node.getExpr().apply(this);
	        left=""+help.nextquad();
	    }
	    conditions2=conditions;
	    jumps2=jumps;
	    if(node.getStmt() != null)
	    {
	        node.getStmt().apply(this);
	        right=""+help.nextquad();
	    }
	    for(String s:conditions2)
	    {
	    	help.modifiyquad(s, left);
	    }
	    for(String s:jumps2)
	    {
	    	String where=Integer.toString(Integer.parseInt(right)+1);
	    	help.modifiyquad(s, where);
	    }
	    String code_line=help.genquad("jump","-","-",start);
	    help.instruction_list.add(code_line);
	    outAWhileStatementStmt(node);
	}
	
	@Override
	public void caseAReturnStmt(AReturnStmt node)
	{
	    inAReturnStmt(node);
	    if(node.getExpr() != null)
	    {
	        node.getExpr().apply(this);
	        String k=mtype;
	        String code_line=help.genquad(":=",k,"-","$$");
	        help.instruction_list.add(code_line);
	    }
	    String code_line=help.genquad("ret","-","-","-");
        help.instruction_list.add(code_line);
	    outAReturnStmt(node);
	}
	
	@Override
	public void caseAIfStmt(AIfStmt node)
	{
	    inAIfStmt(node);
	    String left="";
	    String right="";
	    conditions=new ArrayList<String>();
		jumps=new ArrayList<String>();	
		operator=new ArrayList<String>();
		ArrayList<String> conditions2;//=new ArrayList<String>();
		ArrayList<String> jumps2;//=new ArrayList<String>();	
	    if(node.getCondition() != null)
	    {
	        node.getCondition().apply(this);
	        left=""+help.nextquad();
	    }
	    conditions2=conditions;
	    jumps2=jumps;
	    if(node.getStatement() != null)
	    {
	        node.getStatement().apply(this);
	        right=""+help.nextquad();
	    }
	    for(String s:conditions2)
	    {
	    	help.modifiyquad(s, left);
	    }
	  
	    for(String s:jumps2)
	    {
	    	help.modifiyquad(s, right);
	    }
	    mtype=right;
	    outAIfStmt(node);
	}
	
	@Override
	public void caseAIfElseStmt(AIfElseStmt node)
	{
	    inAIfElseStmt(node);
	    String left="";
	    String right="";
	    conditions=new ArrayList<String>();
  		jumps=new ArrayList<String>();	
  		operator=new ArrayList<String>();
  		ArrayList<String> conditions2;//=new ArrayList<String>();
  		ArrayList<String> jumps2;//=new ArrayList<String>();	  
	    if(node.getCondition() != null)
	    {
	        node.getCondition().apply(this);
	        left=""+help.nextquad();
	    }
	    conditions2=conditions;
	    jumps2=jumps;
	    if(node.getThen() != null)
	    {
	        node.getThen().apply(this);
	        right=""+(help.nextquad()+1);
	    }
	    for(String s:conditions2)
	    {
	    	help.modifiyquad(s, left);
	    }
	  
	    for(String s:jumps2)
	    {
	    	help.modifiyquad(s, right);
	    }
	    int place=help.nextquad();
	    String code_line=help.genquad("jump","-","-","*");
	    help.instruction_list.add(code_line);
	    if(node.getElse() != null)
	    {
	        node.getElse().apply(this);
	    }
	    String where=""+help.nextquad();
	    help.modifiyquad(""+place, where);
	    outAIfElseStmt(node);
	}
	
	@Override
	public void caseAFuncCallFuncCall(AFuncCallFuncCall node)
	{
	    inAFuncCallFuncCall(node);
	    String left="";
	    if(node.getVariable() != null)
	    {
	        left=node.getVariable().toString();
	    }
	
	    FunctionSum fun=null;
	    fun=symboltable.get_function_from_Symboltable(left);
	    if(fun==null)
	    {
	    	fun=library.getFunction(left);
	    }
	    {
	        List<PExpr> copy = new ArrayList<PExpr>(node.getExpr());
	        int num_of_param=0;
	        for(PExpr e : copy)
	        {
	            e.apply(this);
	            String temp=mtype;
	            String code_line;
	            if(fun!=null)
	            {
		            if(fun.get_variable_ref(num_of_param))
		    	    	code_line=help.genquad("par",temp,"R","-");
		            else
						code_line=help.genquad("par",temp,"V","-");	            	
		            help.instruction_list.add(code_line);
	            }
	            num_of_param++;
	            
	        }
	        String code_line=help.genquad("call","-","-",left);
            help.instruction_list.add(code_line);
	    }
	    mtype="$$";
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
	    if(node.getArrayNum() != null)
	    {
	        node.getArrayNum().apply(this);
	    }
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
	public void caseAArrayNumDefinedArrayNumDefined(AArrayNumDefinedArrayNumDefined node)
	{
	    inAArrayNumDefinedArrayNumDefined(node);
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
	    outAArrayNumDefinedArrayNumDefined(node);
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
	        node.getVariable().apply(this);
	        mtype=node.getVariable().toString();
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
	        mtype=node.getStringLit().toString();
	        
	    }
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
	    /*register++;
	    mtype="$"+register;
	    String code_line=help.genquad("array",left,right,"$"+register);
	    mtype="[$"+register+"]";
        help.instruction_list.add(code_line);*/
	    outALValueArrayLValueArray(node);
	}
	
	@Override
	public void caseACondOrExpr(ACondOrExpr node)
	{
	    inACondOrExpr(node);
	    String right="";
	    String left="";
	    operator.add("or");
	    int prin=jumps.size();
	    if(node.getLeft() != null)
	    {
	        node.getLeft().apply(this);
	        left=""+help.nextquad();
	    }
	    int meta=jumps.size();
    	for(int i=0;i<meta-prin;i++)
    	{
    		Integer t=jumps.size()-i-1;
	    	help.modifiyquad(jumps.get(t),left);
	    	jumps.remove(t.toString());
    	}
	    if(node.getRight() != null)
	    {
	        node.getRight().apply(this);
	        right=""+help.nextquad();
	    }
	    	mtype=right;
	    outACondOrExpr(node);
	}
	
	@Override
	public void caseACompAndExpr(ACompAndExpr node)
	{
	    inACompAndExpr(node);
	    operator.add("and");
	    String right="";
	    String left="";
	    int prin=conditions.size();
	    if(node.getLeft() != null)
	    {
	        node.getLeft().apply(this);
	        left=""+help.nextquad();
	    }
	    int meta=conditions.size();
    	for(int i=0;i<meta-prin;i++)
    	{
    		
    		Integer t=conditions.size()-i-1;
	    	help.modifiyquad(conditions.get(t),left);
	    	conditions.remove(t.toString());
    	}

	    if(node.getRight() != null)
	    {
	        node.getRight().apply(this);
	        right=""+help.nextquad();
	    }
	    
	    mtype=right;
	    outACompAndExpr(node);
	}
	
	@Override
	public void caseACompEqExpr(ACompEqExpr node)
	{
	    inACompEqExpr(node);
	    String left="";
	    String right="";
	    String coop="";
	    if(node.getLeft() != null)
	    {
	        node.getLeft().apply(this);
	        left=mtype;
	    }
	    if(node.getCompareRelOperators() != null)
	    {
	        coop=node.getCompareRelOperators().toString();
	    }
	    if(node.getRight() != null)
	    {
	        node.getRight().apply(this);
	        right=mtype;
	    }
	    mtype=""+help.nextquad();
	    String code_line=help.genquad(coop,left,right,"*");
	   	String mtype2=""+help.nextquad();
	    conditions.add(mtype);
        help.instruction_list.add(code_line); 
        String code_line2=help.genquad("jump","-","-","*");
        help.instruction_list.add(code_line2);
        jumps.add(mtype2);      
	    outACompEqExpr(node);
	}
	
	@Override
	public void caseACompNotEqExpr(ACompNotEqExpr node)
	{
	    inACompNotEqExpr(node);
	    String current=""+help.nextquad();
	    Integer arxi=Integer.parseInt(current);
	    Integer jtemp=jumps.size();
	    Integer ctemp=conditions.size();
	    if(node.getExpr() != null)
	    {
	        node.getExpr().apply(this);
	    }
	    String end=""+help.nextquad();
	    Integer telos=Integer.parseInt(end);
	    int js=jumps.size()-jtemp;
	    int cs=conditions.size()-ctemp;
	    for(int i=0;i<js;i++)
	    {
	    	jumps.remove(jumps.size()-1);
	    }
	    for(int i=0;i<cs;i++)
	    {
	    	conditions.remove(conditions.size()-1);
	    }
	    for(int i=arxi;i<telos;i+=2)
	    {
	    	
	    	String inst=help.instruction_list.get(i-1);
	    	String inst2=help.instruction_list.get(i);
	    	String[] tel1=inst.split(",");
	    	String[] tel2=inst2.split(",");
	    	String newc=tel1[0]+","+tel1[1]+","+tel1[2]+","+tel2[3];
	    	String newc2=tel2[0]+","+tel2[1]+","+tel2[2]+","+tel1[3];
	    	if(tel2[3].contains("*"))
	    	{
	    		if(tel1[0].contains("jump"))
	    			jumps.add(tel1[0].substring(0,1));
	    		else
	    			conditions.add(tel1[0].substring(0,1));
	    	}
	    	if(tel1[3].contains("*"))
	    	{
	    		if(tel2[0].contains("jump"))
	    			jumps.add(tel2[0].substring(0,1));
	    		else
	    			conditions.add(tel2[0].substring(0,1));
	    	}
	    	help.instruction_list.remove(i-1);
	    	help.instruction_list.remove(i-1);
	    	help.instruction_list.add(i-1,newc);
	    	help.instruction_list.add(i,newc2);
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
	    String left="";
	    String right="";
	    if(node.getLeft() != null)
	    {
	        node.getLeft().apply(this);
	        left=mtype;
	    }
	    if(node.getRight() != null)
	    {
	        node.getRight().apply(this);
	        right=mtype;
	    }
	    register++;
	    String code_line=help.genquad("+",left,right,"$"+register);
	    mtype="$"+register;
        help.instruction_list.add(code_line);
	    outAPlusExpr(node);
	}
	
	@Override
	public void caseAMinusExpr(AMinusExpr node)
	{
	    inAMinusExpr(node);
	    String left="";
	    String right="";
	    if(node.getLeft() != null)
	    {
	        node.getLeft().apply(this);
	        left=mtype;
	    }
	    if(node.getRight() != null)
	    {
	        node.getRight().apply(this);
	        right=mtype;
	    }
	    register++;
	    String code_line=help.genquad("-",left,right,"$"+register);
        help.instruction_list.add(code_line);
        mtype="$"+register;
	    outAMinusExpr(node);
	}
	
	@Override
	public void caseAMultExpr(AMultExpr node)
	{
	    inAMultExpr(node);
	    String left="";
	    String right="";
	    if(node.getLeft() != null)
	    {
	        node.getLeft().apply(this);
	        left=mtype;
	    }
	    if(node.getRight() != null)
	    {
	        node.getRight().apply(this);
	        right=mtype;
	    }
	    register++;
	    String code_line=help.genquad("*",left,right,"$"+register);
        help.instruction_list.add(code_line);
        mtype="$"+register;
	    outAMultExpr(node);
	}
	
	@Override
	public void caseASlashExpr(ASlashExpr node)
	{
	    inASlashExpr(node);
	    String left="";
	    String right="";
	    if(node.getLeft() != null)
	    {
	        node.getLeft().apply(this);
	        left=mtype;
	    }
	    if(node.getRight() != null)
	    {
	        node.getRight().apply(this);
	        right=mtype;
	    }
	    register++;
	    String code_line=help.genquad("/",left,right,"$"+register);
        help.instruction_list.add(code_line);
        mtype="$"+register;
	    outASlashExpr(node);
	}
	
	@Override
	public void caseAModExpr(AModExpr node)
	{
	    inAModExpr(node);
	    String left="";
	    String right="";
	    if(node.getLeft() != null)
	    {
	        node.getLeft().apply(this);
	        left=mtype;
	    }
	    if(node.getRight() != null)
	    {
	        node.getRight().apply(this);
	        right=mtype;
	    }
	    register++;
	    String code_line=help.genquad("mod",left,right,"$"+register);
        help.instruction_list.add(code_line);
        mtype="$"+register;
	    outAModExpr(node);
	}
	
	@Override
	public void caseADivExpr(ADivExpr node)
	{
	    inADivExpr(node);
	    String left="";
	    String right="";
	    if(node.getLeft() != null)
	    {
	        node.getLeft().apply(this);
	        left=mtype;
	    }
	    if(node.getRight() != null)
	    {
	        node.getRight().apply(this);
	        right=mtype;
	    }
	    register++;
	    String code_line=help.genquad("div",left,right,"$"+register);
        help.instruction_list.add(code_line);
        mtype="$"+register;
	    outADivExpr(node);
	}
	
	@Override
	public void caseATermIntExpr(ATermIntExpr node)
	{
	    inATermIntExpr(node);
	    if(node.getInteger() != null)
	    {
	        node.getInteger().apply(this);
	        mtype=node.getInteger().toString();
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
	        mtype=node.getConstChar().toString();
	    }
	    outATermCharExpr(node);
	}
	
	@Override
	public void caseATermValExpr(ATermValExpr node)
	{
	    inATermValExpr(node);
	    kra="";
	    if(node.getLValue() != null)
	    {
	    	 node.getLValue().apply(this);
	    	if(node.getLValue() instanceof ALValueArrayLValue)
	    	{
	    		String cs=node.getLValue().toString();
	    		String []s=cs.split(" ");
	    		FunctionSum f=symboltable.get_function_from_Symboltable(currentf);
	    		String ttype=f.findvarsize(s[0]);
	    		
	    		String []s2=ttype.split("]");
	    		s2[0]=s2[0].replaceAll("int ","");
	    		for(int i=0;i<s2.length-1;i++)
	    		{
	    			s2[i]=s2[i].replace("[","");
	    			if(s2[i].length()>1)
	    				s2[i]=s2[i].replaceAll(" ","");
	    		}
	    		
	    		boolean addflag=false;
	    		String prereg="";
	    		for(int i=1;i<s.length-1;i++)
	    		{
	    			
	    			register++;
	    			if(s2[i].equals(" "))
	    				break;
	    		    String code_line=help.genquad("*",s[i],s2[i],"$"+register);
	    	        help.instruction_list.add(code_line);
	    	       
	    	        if(addflag==false)
	    				addflag=true;
	    	        else
	    	        {
	    	        	register++;
	    	        	code_line=help.genquad("+",prereg,"$"+(register-1),"$"+register);
		    	        help.instruction_list.add(code_line);
		    	        prereg="$"+register;
	    	        	addflag=false;
	    	        }
	    	        prereg="$"+register;
	    		}
	    		
	    		register++;
	    		String code_line="";
	    		if(prereg.equals(""))
	    		{
	    			
	    			if(!kra.equals(""))
	    				code_line=help.genquad("+",kra,s[s.length-1],"$"+register);
	    			else
	    				code_line=help.genquad("+","0",s[s.length-1],"$"+register);
	    		}
	        	else
	        	{
	        		if(!kra.equals(""))
	        			code_line=help.genquad("+",prereg,kra,"$"+register);
	        		else
	        			code_line=help.genquad("+",prereg,s[s.length-1],"$"+register);
	        	}
    	        help.instruction_list.add(code_line);
    	        register++;
    	        code_line=help.genquad("array",s[0],"$"+(register-1),"$"+register);
   	         	help.instruction_list.add(code_line);
		         mtype="[$"+register+"]";
		         kra=mtype;
	    	}
	    }
	    
	    outATermValExpr(node);
	}
	
	@Override
    public void caseAPlusMinusExpExpr(APlusMinusExpExpr node)
    {
        inAPlusMinusExpExpr(node);
        String  mid="";
        String  prosimo="";
        if(node.getPlusMinus() != null)
        {
            node.getPlusMinus().apply(this);
            prosimo=node.getPlusMinus().toString();
        }
        if(node.getExpr() != null)
        {
            node.getExpr().apply(this);
            mid=mtype;
        }
        prosimo=prosimo.replaceAll(" ","");
        if(prosimo.equals("-"))
        {
        	register++;
        	String code_line=help.genquad("-","0",mid,"$"+register);
            help.instruction_list.add(code_line);
            mtype="$"+register;
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

	    @Override
	    public void caseAEqCompareRelOperators(AEqCompareRelOperators node)
	    {
	        inAEqCompareRelOperators(node);
	        if(node.getEqual() != null)
	        {
	            node.getEqual().apply(this);
	        }
	        outAEqCompareRelOperators(node);
	    }


	    @Override
	    public void caseANeqCompareRelOperators(ANeqCompareRelOperators node)
	    {
	        inANeqCompareRelOperators(node);
	        if(node.getNeq() != null)
	        {
	            node.getNeq().apply(this);
	        }
	        outANeqCompareRelOperators(node);
	    }

	    @Override
	    public void caseAHashtagCompareRelOperators(AHashtagCompareRelOperators node)
	    {
	        inAHashtagCompareRelOperators(node);
	        if(node.getHashtag() != null)
	        {
	            node.getHashtag().apply(this);
	        }
	        outAHashtagCompareRelOperators(node);
	    }

	    @Override
	    public void caseALtCompareRelOperators(ALtCompareRelOperators node)
	    {
	        inALtCompareRelOperators(node);
	        if(node.getLt() != null)
	        {
	            node.getLt().apply(this);
	        }
	        outALtCompareRelOperators(node);
	    }

	    @Override
	    public void caseAGtCompareRelOperators(AGtCompareRelOperators node)
	    {
	        inAGtCompareRelOperators(node);
	        if(node.getGt() != null)
	        {
	            node.getGt().apply(this);
	        }
	        outAGtCompareRelOperators(node);
	    }


	    @Override
	    public void caseALteqCompareRelOperators(ALteqCompareRelOperators node)
	    {
	        inALteqCompareRelOperators(node);
	        if(node.getLteq() != null)
	        {
	            node.getLteq().apply(this);
	        }
	        outALteqCompareRelOperators(node);
	    }


	    @Override
	    public void caseAGteqCompareRelOperators(AGteqCompareRelOperators node)
	    {
	        inAGteqCompareRelOperators(node);
	        if(node.getGteq() != null)
	        {
	            node.getGteq().apply(this);
	        }
	        outAGteqCompareRelOperators(node);
	    }
}