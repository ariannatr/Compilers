package compiler;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Helper{

	public static void print_final(PrintStream output,ArrayList<ArrayList<String>> final_code,ArrayList<String> data)
	{
		Iterator<String> itr;
		for(int i=0;i<final_code.size();i++)
		{
			itr=final_code.get(i).iterator();
			while(itr.hasNext())
			{
				String ret=itr.next();
				output.print(ret);
			}
		}
		Iterator<String> itr2=data.iterator();
		if(data.size()>0)
				output.println(".data");
		while(itr2.hasNext())
		{
			String ret=itr2.next();
			output.print(ret);
		}
		return;
	}


	public void include_library_defs(ArrayList<ArrayList<String>> final_code,Integer thesi,ArrayList<String> data,ArrayList<String> library_calls)
	{
		String code_line="";
		if(library_calls.contains("grace_gets"))
			grace_gets(final_code,thesi);
		if(library_calls.contains("grace_puti"))
		{
			grace_puti(final_code,thesi);
			code_line="\t"+"int_par:"+"\t.asciz\t"+"\"%d\""+"\n";
			data.add(code_line);
		}
		if(library_calls.contains("grace_geti"))
		{
			grace_geti(final_code,thesi);
			code_line="\t"+"int_par:"+"\t.asciz\t"+"\"%d\""+"\n";
			data.add(code_line);
			code_line="\t"+"int_par2:"+"\t.asciz\t"+"\"%d\""+"\n";
			data.add(code_line);
		}
		if(library_calls.contains("grace_puts"))
			grace_puts(final_code,thesi);
		if(library_calls.contains("grace_putc"))
		{
			grace_putc(final_code,thesi);
			code_line="\t"+"char:"+"\t.asciz\t"+"\"%c\""+"\n";
			data.add(code_line);
		}
	}

	public void grace_puts(ArrayList<ArrayList<String>> final_code,Integer thesi)
	{
		String code_line="";
		code_line="grace_puts:\n";
		final_code.get(thesi).add(code_line);
    	code_line="\tpush ebp\n";
    	final_code.get(thesi).add(code_line);
		code_line="\tmov ebp, esp\n";
		final_code.get(thesi).add(code_line);
		code_line="\tmov eax, DWORD PTR [ebp + 12]\n";
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

	public void grace_puti(ArrayList<ArrayList<String>> final_code,Integer thesi)
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

	public void grace_putc(ArrayList<ArrayList<String>> final_code,Integer thesi)
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

	public void grace_gets(ArrayList<ArrayList<String>> final_code,Integer thesi)
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
	private void grace_geti(ArrayList<ArrayList<String>> final_code,Integer thesi) {
		String code_line="";
		code_line="\tpush ebp\n";
		final_code.get(thesi).add(code_line);
		code_line="\tmov ebp, esp\n";
		final_code.get(thesi).add(code_line);
		code_line="\tsub esp, 4\n";
		final_code.get(thesi).add(code_line);
		code_line="\tlea esi, DWORD PTR [ebp-4]\n";
		final_code.get(thesi).add(code_line);
		code_line="\tpush esi\n\tmov eax, OFFSET FLAT:int_par\n";
		final_code.get(thesi).add(code_line);
		code_line="\tpush eax\n";
		final_code.get(thesi).add(code_line);
		code_line="\tcall scanf\n";
		final_code.get(thesi).add(code_line);
		code_line="\tadd esp, 8\n";
		final_code.get(thesi).add(code_line);
		code_line="\tmov esi, DWORD PTR [ebp+8]\n";
		final_code.get(thesi).add(code_line);
		code_line="\tmov eax, DWORD PTR [ebp-4]\n";
		final_code.get(thesi).add(code_line);
		code_line="\tmov DWORD PTR [esi], eax\n";
		final_code.get(thesi).add(code_line);
		code_line="\tpush eax\n";
		final_code.get(thesi).add(code_line);
		code_line="\tmov eax, OFFSET FLAT:int_par2\n";
		final_code.get(thesi).add(code_line);
		code_line="\tpush eax\n";
		final_code.get(thesi).add(code_line);
		code_line="\tcall printf\n";
		final_code.get(thesi).add(code_line);
		code_line="\tadd esp, 8\n";
		final_code.get(thesi).add(code_line);
		code_line="\tmov esp, ebp\n";
		final_code.get(thesi).add(code_line);
		code_line="\tpop ebp\n\tret\n";
		final_code.get(thesi).add(code_line);
	 }
}