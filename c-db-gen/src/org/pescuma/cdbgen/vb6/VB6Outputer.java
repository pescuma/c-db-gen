package org.pescuma.cdbgen.vb6;

import java.io.File;

import org.pescuma.cdbgen.Struct;
import org.pescuma.cdbgen.Utils;
import org.pescuma.cdbgen.outputer.VelocityOutputer;

public class VB6Outputer extends VelocityOutputer
{
	public String getName()
	{
		return "VB6";
	}
	
	@Override
	protected String[] getInvalidFieldNames()
	{
		return new String[] { "connection" };
	}
	
	@Override
	protected File[] getFilenames(Struct struct, File path)
	{
		return new File[] { new File(path, struct.name + ".cls"), new File(path, struct.name + "DB.cls"), };
	}
	
	@Override
	protected String[] getTemplateNames()
	{
		return new String[] { "vb6.vm", "vb6_db.vm" };
	}
	
	@Override
	protected Utils getUtils()
	{
		return new VB6Utils();
	}
}
