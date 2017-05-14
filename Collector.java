package compiler;
import java.util.*;
import compiler.analysis.DepthFirstAdapter;
import compiler.node.*;

public class Collector extends DepthFirstAdapter {

	public FunctionSum f;
	public FunctionSum current;
	public FunctionSum previous;
	public String vartype="";
	public String name="";
	public String type="";
	public String ref="";
	
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
	        for(VarSum va : current.arg)
	       {
	    	   System.out.println(va.ref+" "+va.name+" "+va.type);
	       }
	       for(VarSum va : current.vars)
	       {
	    	   System.out.println(va.ref+" "+va.name+" "+va.type);
	       }
	        outAProgramm(node);
	    }

	    @Override
	    public void caseAFuncDefFuncDef(AFuncDefFuncDef node)
	    {
	        inAFuncDefFuncDef(node);
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
	        }
	        outAFuncDefFuncDef(node);
	    }

	    @Override
	    public void caseAHeaderHeader(AHeaderHeader node)
	    {
	        inAHeaderHeader(node);
	        if(node.getVariable() != null)
	        {
	            name=node.getVariable().toString();
	        }
	        if(first_time==0)
	        {
	        	f=new FunctionSum(name);
	        	f.belongs="";
	        	current=f;
	        	first_time=1;
	        }    
	        else
	        {
	        	FunctionSum fa=new FunctionSum(name);
	        	fa.belongs=current.name;
	        	current=fa;
	        }
	        if(node.getFparDef() != null)
	        {
	           node.getFparDef().apply(this);
	        }
	        if(node.getRetType() != null)
	        {
	            type=node.getRetType().toString();
	            current.type=type;
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
	        current.arg.add(v);
	        outAFparDef1FparDef(node);
	    }

	    @Override
	    public void caseAFparDef2FparDef(AFparDef2FparDef node)
	    {
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
	        current.arg.add(v);
	        outAFparDef2FparDef(node);
	    }

	    @Override
	    public void caseAFparDef3FparDef(AFparDef3FparDef node)
	    {
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
	        current.arg.add(v);
	        outAFparDef3FparDef(node);
	    }

	    @Override
	    public void caseAFparDef4FparDef(AFparDef4FparDef node)
	    {
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
	        if(node.getFparDef() != null)
	        {
	            node.getFparDef().apply(this);
	        }
	        VarSum v=new VarSum(varname,vartype);
	        v.ref=ref;
	        current.arg.add(v);
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
	        current.vars.add(va);
	        {
	            List<TVariable> copy = new ArrayList<TVariable>(node.getVariables());
	            for(TVariable e : copy)
	            {
	               name=e.toString();
	               VarSum v1=new VarSum(name,vartype);
	   	           current.vars.add(v1);
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
	        if(node.getLValue() != null)
	        {
	            node.getLValue().apply(this);
	        }
	        if(node.getExpr() != null)
	        {
	            node.getExpr().apply(this);
	        }
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
	        inAFuncCallFuncCall(node);
	        if(node.getVariable() != null)
	        {
	            node.getVariable().apply(this);
	        }
	        {
	            List<PExpr> copy = new ArrayList<PExpr>(node.getExpr());
	            for(PExpr e : copy)
	            {
	                e.apply(this);
	            }
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
	        {
	            List<PArrayNum> copy = new ArrayList<PArrayNum>(node.getArrayNum());
	            for(PArrayNum e : copy)
	            {
	                e.apply(this);
	            }
	        }
	        outATypeType(node);
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
	            node.getVariable().apply(this);
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
	        if(node.getRight() != null)
	        {
	            node.getRight().apply(this);
	        }
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
	        if(node.getLeft() != null)
	        {
	            node.getLeft().apply(this);
	        }
	        if(node.getRight() != null)
	        {
	            node.getRight().apply(this);
	        }
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
	        if(node.getRight() != null)
	        {
	            node.getRight().apply(this);
	        }
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
	        if(node.getRight() != null)
	        {
	            node.getRight().apply(this);
	        }
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
	        if(node.getRight() != null)
	        {
	            node.getRight().apply(this);
	        }
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
	        if(node.getRight() != null)
	        {
	            node.getRight().apply(this);
	        }
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
	        if(node.getRight() != null)
	        {
	            node.getRight().apply(this);
	        }
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
	        if(node.getRight() != null)
	        {
	            node.getRight().apply(this);
	        }
	        outADivExpr(node);
	    }

	    @Override
	    public void caseATermIntExpr(ATermIntExpr node)
	    {
	        inATermIntExpr(node);
	        if(node.getInteger() != null)
	        {
	            node.getInteger().apply(this);
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
