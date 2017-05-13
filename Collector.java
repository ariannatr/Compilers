package compiler;
import java.util.*;
import compiler.analysis.DepthFirstAdapter;

public class Collector extends DepthFirstAdapter {

	public ArrayList<FunctionSum> f;
	public Collector()
	{
		f = new ArrayList<FunctionSum>();
	}
	
	
}
