package org.pescuma.cdbgen.outputer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.pescuma.cdbgen.Struct;
import org.pescuma.cdbgen.StructField;
import org.pescuma.cdbgen.Utils;

public abstract class VelocityOutputer implements Outputer
{
	public void output(Struct struct, File path)
	{
		validate(struct);
		
		String[] templateNames = getTemplateNames();
		File[] files = getFilenames(struct, path);
		
		if (templateNames.length != files.length)
			throw new IllegalStateException();
		
		for (int i = 0; i < files.length; i++)
		{
			try
			{
				VelocityContext context = new VelocityContext();
				addVariables(context, struct);
				
				Template template = Velocity.getTemplate(templateNames[i]);
				
				File file = files[i];
				file.getParentFile().mkdirs();
				
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), getEncoding()));
				try
				{
					template.merge(context, out);
				}
				finally
				{
					out.close();
				}
			}
			catch (Exception e)
			{
				throw new OutputerException(e);
			}
		}
	}
	
	protected void validate(Struct struct)
	{
		for (String invName : getInvalidFieldNames())
		{
			for (StructField field : struct.fields)
			{
				if (invName.equalsIgnoreCase(field.name))
					throw new OutputerValidationException(struct, field, field.name + " is a reserved field name");
			}
		}
	}
	
	protected String[] getInvalidFieldNames()
	{
		return new String[0];
	}
	
	protected abstract String[] getTemplateNames();
	
	protected abstract File[] getFilenames(Struct struct, File path);
	
	protected String getEncoding()
	{
		return "UTF8";
	}
	
	protected void addVariables(VelocityContext context, Struct struct)
	{
		context.put("struct", struct);
		context.put("utils", new Utils());
	}
}
