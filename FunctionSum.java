package compiler;

import java.util.ArrayList;

public class FunctionSum 
{
	public String name;
	public String type;
	public String belongs;
	public ArrayList<FunctionSum> fun;
	public ArrayList<VarSum> arg;
	public ArrayList<VarSum> vars;
	public FunctionSum(String n)
	{
		name=n;
		vars = new ArrayList<VarSum>();
		arg= new ArrayList<VarSum>();
		fun= new ArrayList<FunctionSum>();
	}
}
