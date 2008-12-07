package org.pescuma.cdbgen.vb6_ADO;

import java.io.File;

import org.pescuma.cdbgen.Struct;
import org.pescuma.cdbgen.Utils;
import org.pescuma.cdbgen.outputer.VelocityOutputer;

public class VB6ADOOutputer extends VelocityOutputer
{
	public String getName()
	{
		return "VB6 ADO";
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
		return new String[] { "vb6_ADO.vm", "vb6_ADO_db.vm" };
	}
	
	@Override
	protected Utils getUtils()
	{
		return new VB6ADOUtils();
	}
}
