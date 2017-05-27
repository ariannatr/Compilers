package compiler;
import java.io.IOException;
import java.util.*;
import compiler.analysis.DepthFirstAdapter;
import compiler.node.*;

public class LoweringCode extends DepthFirstAdapter {
	
	public FunctionSum library;
	public String exprtype="";
	public String mtype="";
	public String code="";
	public String name="";
	public int register=0;
	public HelpfullMethods help;
	public FunctionSum symboltable;

	LoweringCode(FunctionSum symboltable,FunctionSum library)
	{
		this.symboltable=symboltable;
		this.library=library;
		help= new HelpfullMethods();
		System.out.println("Main class name is "+symboltable.name);
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
	    if(node.getCondition() != null)
	    {
	        node.getCondition().apply(this);
	        left=mtype;
	    }
	    String dest=""+help.nextquad();
	    String code_line=help.genquad("jump","-","-","*");
	    help.instruction_list.add(code_line);
        String dest2=""+help.nextquad();
        help.modifiyquad(left,dest2);
	    if(node.getStatement() != null)
	    {
	        node.getStatement().apply(this);
	        right=""+help.nextquad();
	    } 
	    help.modifiyquad(dest, right);
	    outAIfStmt(node);
	}
	
	@Override
	public void caseAIfElseStmt(AIfElseStmt node)
	{
	    inAIfElseStmt(node);
	    String left="";
	    String right="";
	    if(node.getCondition() != null)
	    {
	        node.getCondition().apply(this);
	        left=mtype;
	    }
	    String dest=""+help.nextquad();
	    String code_line=help.genquad("jump","-","-","*");
	    help.instruction_list.add(code_line);
        String dest2=""+help.nextquad();
        help.modifiyquad(left,dest2);
	    if(node.getThen() != null)
	    {
	        node.getThen().apply(this);
	        right=""+help.nextquad();
	    }
	    help.modifiyquad(dest, right);
	    if(node.getElse() != null)
	    {
	        node.getElse().apply(this);
	    }
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
	    
	    {
	        List<PExpr> copy = new ArrayList<PExpr>(node.getExpr());
	        for(PExpr e : copy)
	        {
	            e.apply(this);
	            String temp=mtype;
	    	    String code_line=help.genquad("par",temp,"R","-");
	            help.instruction_list.add(code_line);
	        }
	        String code_line=help.genquad("call","-","-",left);
            help.instruction_list.add(code_line);
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
	    register++;
	    mtype="$"+register;
	    String code_line=help.genquad("array",left,right,"$"+register);
	    mtype="[$"+register+"]";
        help.instruction_list.add(code_line);
	    outALValueArrayLValueArray(node);
	}
	
	@Override
	public void caseACondOrExpr(ACondOrExpr node)
	{
	    inACondOrExpr(node);
	    String left="";
	    String right="";
	    if(node.getLeft() != null)
	    {
	        node.getLeft().apply(this);
	        left=mtype;
	    }
	    System.out.println(left+"<-");
	    String dest=""+help.nextquad();
	    String code_line=help.genquad("jump","-","-","*");
	    help.instruction_list.add(code_line);
	    String dest2=""+help.nextquad();
	    if(node.getRight() != null)
	    {
	        node.getRight().apply(this);
	        right=""+help.nextquad();
	    }
	    help.modifiyquad(dest,dest2);
	    mtype=right;
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
	    if(node.getRight() != null)
	    {
	        node.getRight().apply(this);
	    }
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
	    mtype=""+help.nextquad();//<------------notsure
	    String code_line=help.genquad(coop,left,right,"*");
        help.instruction_list.add(code_line);
        
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
	


