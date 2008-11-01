package org.pescuma.cdbgen.palm;

import java.io.File;

import org.pescuma.cdbgen.Struct;
import org.pescuma.cdbgen.outputer.VelocityOutputer;

public class PalmOutputer extends VelocityOutputer
{
	public String getName()
	{
		return "Palm";
	}
	
	@Override
	protected String[] getInvalidFieldNames()
	{
		return new String[] { "category", "uniqueID", "secret" };
	}
	
	@Override
	protected File[] getFilenames(Struct struct, File path)
	{
		return new File[] { new File(path, struct.name + ".h") };
	}
	
	@Override
	protected String[] getTemplateNames()
	{
		return new String[] { "palm.vm" };
	}
}
