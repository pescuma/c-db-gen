package org.pescuma.cdbgen.outputer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.pescuma.cdbgen.Struct;
import org.pescuma.cdbgen.StructField;
import org.pescuma.cdbgen.Utils;

public abstract class VelocityOutputer implements Outputer
{
	public void output(Struct struct, List<Struct> structs, String namespace, File path)
	{
		validate(struct);
		
		String[] templateNames = getTemplateNames();
		if (templateNames == null)
			return;
		
		File[] files = getFilenames(struct, path);
		
		if (templateNames.length != files.length)
			throw new IllegalStateException();
		
		for (int i = 0; i < files.length; i++)
		{
			try
			{
				VelocityContext context = new VelocityContext();
				addVariables(context, struct, namespace, structs);
				
				Template template = Velocity.getTemplate(templateNames[i]);
				
				File file = files[i];
				File parentFile = file.getParentFile();
				if (!parentFile.exists() && !parentFile.mkdirs())
					throw new OutputerException("Could not create the output path");
				
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
	
	protected void addVariables(VelocityContext context, Struct struct, String namespace, List<Struct> structs)
	{
		context.put("struct", struct);
		context.put("structs", structs);
		context.put("namespace", namespace.trim());
		context.put("utils", getUtils());
	}
	
	protected Utils getUtils()
	{
		return new Utils();
	}
	
	public void globalOutput(List<Struct> structs, String namespace, File path)
	{
		String[] templateNames = getGlobalTemplateNames();
		if (templateNames == null)
			return;
		
		File[] files = getGlobalFilenames(path);
		
		if (templateNames.length != files.length)
			throw new IllegalStateException();
		
		for (int i = 0; i < files.length; i++)
		{
			try
			{
				VelocityContext context = new VelocityContext();
				addVariables(context, null, namespace, structs);
				
				Template template = Velocity.getTemplate(templateNames[i]);
				
				File file = files[i];
				File parentFile = file.getParentFile();
				if (!parentFile.exists() && !parentFile.mkdirs())
					throw new OutputerException("Could not create the output path");
				
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
	
	protected String[] getGlobalTemplateNames()
	{
		return null;
	}
	
	protected File[] getGlobalFilenames(File path)
	{
		return null;
	}
}
