package org.pescuma.cdbgen.v8;

import java.io.File;

import org.pescuma.cdbgen.Struct;
import org.pescuma.cdbgen.Utils;
import org.pescuma.cdbgen.outputer.VelocityOutputer;

public class V8Outputer extends VelocityOutputer
{
	public String getName()
	{
		return "V8";
	}
	
	@Override
	protected File[] getFilenames(Struct struct, File path)
	{
		return new File[] { new File(path, struct.name + "_v8_wrapper.h"), new File(path, struct.name + "_v8_wrapper.cpp") };
	}
	
	@Override
	protected String[] getTemplateNames()
	{
		return new String[] { "v8_h.vm", "v8_cpp.vm" };
	}
	
	@Override
	protected Utils getUtils()
	{
		return new V8Utils();
	}
	
	@Override
	protected File[] getGlobalFilenames(File path)
	{
		return new File[] { new File(path, "V8Templates.h"), new File(path, "V8Templates.cpp") };
	}
	
	@Override
	protected String[] getGlobalTemplateNames()
	{
		return new String[] { "v8_global_h.vm", "v8_global_cpp.vm" };
	}
}
