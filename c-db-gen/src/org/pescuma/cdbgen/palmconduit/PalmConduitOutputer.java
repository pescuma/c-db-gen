package org.pescuma.cdbgen.palmconduit;

import java.io.File;

import org.pescuma.cdbgen.Struct;
import org.pescuma.cdbgen.StructField;
import org.pescuma.cdbgen.Utils;
import org.pescuma.cdbgen.outputer.OutputerValidationException;
import org.pescuma.cdbgen.outputer.VelocityOutputer;

public class PalmConduitOutputer extends VelocityOutputer
{
	public String getName()
	{
		return "PalmConduit";
	}
	
	@Override
	protected String[] getInvalidFieldNames()
	{
		return new String[] { "uniqueID", "category", "secret" };
	}
	
	@Override
	protected void validate(Struct struct)
	{
		super.validate(struct);
		
		for (int i = 0; i < struct.fields.size() - 1; i++)
		{
			StructField field = struct.fields.get(i);
			if (field.list)
				throw new OutputerValidationException(struct, field, "Only one list is possible, and it has to be the last attribute");
		}
	}
	
	@Override
	protected File[] getFilenames(Struct struct, File path)
	{
		return new File[] { new File(path, struct.name + "Record.h"), new File(path, struct.name + "Record.cpp") };
	}
	
	@Override
	protected String[] getTemplateNames()
	{
		return new String[] { "palmConduit_h.vm", "palmConduit_cpp.vm" };
	}
	
	@Override
	protected Utils getUtils()
	{
		return new PalmConduitUtils();
	}
}
