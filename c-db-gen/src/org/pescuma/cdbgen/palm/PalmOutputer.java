package org.pescuma.cdbgen.palm;

import java.io.File;

import org.pescuma.cdbgen.Struct;
import org.pescuma.cdbgen.StructField;
import org.pescuma.cdbgen.outputer.OutputerValidationException;
import org.pescuma.cdbgen.outputer.VelocityOutputer;

public class PalmOutputer extends VelocityOutputer
{
	private static final String[] invalidFieldNames = { "category", "uniqueID", "secret" };
	
	public String getName()
	{
		return "Palm";
	}
	
	@Override
	protected void validate(Struct struct)
	{
		for (StructField field : struct.fields)
		{
			for (String invName : invalidFieldNames)
				if (invName.equalsIgnoreCase(field.name))
					throw new OutputerValidationException(struct, field, field.name + " is a reserved field name");
		}
	}
	
	@Override
	protected File getFilename(Struct struct, File path)
	{
		return new File(path, struct.name + ".h");
	}
	
	@Override
	protected String getTemplateName()
	{
		return "palm.vm";
	}
}
