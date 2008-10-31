package org.pescuma.cdbgen.outputer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.pescuma.cdbgen.Struct;
import org.pescuma.cdbgen.Utils;

public abstract class VelocityOutputer implements Outputer
{
	public void output(Struct struct, File path)
	{
		validate(struct);
		
		try
		{
			VelocityContext context = new VelocityContext();
			addVariables(context, struct);
			
			Template template = Velocity.getTemplate(getTemplateName());
			
			File file = getFilename(struct, path);
			path.mkdirs();
			
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
	
	protected void validate(Struct struct)
	{
	}
	
	protected abstract String getTemplateName();
	
	protected abstract File getFilename(Struct struct, File path);
	
	protected String getEncoding()
	{
		return "UTF8";
	}
	
	private void addVariables(VelocityContext context, Struct struct)
	{
		context.put("struct", struct.name);
		context.put("fields", struct.fields);
		context.put("utils", new Utils());
	}
}
