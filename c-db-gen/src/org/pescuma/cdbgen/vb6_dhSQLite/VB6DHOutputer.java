package org.pescuma.cdbgen.vb6_dhSQLite;

import java.io.File;

import org.pescuma.cdbgen.Struct;
import org.pescuma.cdbgen.Utils;
import org.pescuma.cdbgen.outputer.VelocityOutputer;

public class VB6DHOutputer extends VelocityOutputer
{
	public String getName()
	{
		return "VB6 dhSQLite";
	}
	
	@Override
	protected String[] getInvalidFieldNames()
	{
		return new String[] { "connection" };
	}
	
	@Override
	protected File[] getFilenames(Struct struct, File path)
	{
		return new File[] { new File(path, struct.name + ".cls"), new File(path, struct.name + "Factory.cls"), };
	}
	
	@Override
	protected String[] getTemplateNames()
	{
		return new String[] { "vb6_dhSQLite.vm", "vb6_dhSQLite_db.vm" };
	}
	
	@Override
	protected Utils getUtils()
	{
		return new VB6DHUtils();
	}
}
