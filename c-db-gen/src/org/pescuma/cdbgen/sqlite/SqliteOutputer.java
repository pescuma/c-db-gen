package org.pescuma.cdbgen.sqlite;

import java.io.File;

import org.pescuma.cdbgen.Struct;
import org.pescuma.cdbgen.Utils;
import org.pescuma.cdbgen.outputer.VelocityOutputer;

public class SqliteOutputer extends VelocityOutputer
{
	public String getName()
	{
		return "SQLite";
	}
	
	@Override
	protected String[] getInvalidFieldNames()
	{
		return new String[] { "id" };
	}
	
	@Override
	protected File[] getFilenames(Struct struct, File path)
	{
		return new File[] { new File(path, struct.name + ".h"), new File(path, struct.name + ".cpp") };
	}
	
	@Override
	protected String[] getTemplateNames()
	{
		return new String[] { "sqlite_h.vm", "sqlite_cpp.vm" };
	}
	
	@Override
	protected Utils getUtils()
	{
		return new SqliteUtils();
	}
}
