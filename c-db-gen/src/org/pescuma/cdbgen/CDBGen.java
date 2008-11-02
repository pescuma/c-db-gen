package org.pescuma.cdbgen;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.velocity.app.Velocity;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.pescuma.cdbgen.outputer.Outputer;
import org.pescuma.cdbgen.outputer.OutputerException;
import org.pescuma.cdbgen.outputer.OutputerValidationException;
import org.pescuma.cdbgen.palm.PalmOutputer;
import org.pescuma.cdbgen.sqlite.SqliteOutputer;
import org.pescuma.cdbgen.velocity.VelocityLogger;
import org.pescuma.cdbgen.velocity.VelocityResourceLoader;
import org.pescuma.jfg.gui.swt.JfgFormComposite;
import org.pescuma.jfg.gui.swt.JfgFormData;
import org.pescuma.jfg.model.ann.NotNull;
import org.pescuma.jfg.reflect.ReflectionGroup;

public class CDBGen
{
	private static class ConfigItem
	{
		public boolean enabled;
		
		@NotNull
		public File outputDir;
	}
	
	private static class Config
	{
		public File recFile;
		public final ConfigItem palm = new ConfigItem();
		public final ConfigItem sqlite = new ConfigItem();
	}
	
	public static void main(String[] args)
	{
		Config cfg = new Config();
		if (!askForConfigData(cfg))
			return;
		
		initVelocity();
		
		try
		{
			process(cfg);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private static boolean askForConfigData(Config cfg)
	{
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, true));
		
		final JfgFormComposite form = new JfgFormComposite(shell, SWT.NONE, new JfgFormData(JfgFormData.DIALOG));
		form.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		form.addContentsFrom(new ReflectionGroup(cfg));
		
		final boolean[] ret = new boolean[1];
		Button ok = new Button(shell, SWT.PUSH);
		ok.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		ok.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event)
			{
				ret[0] = true;
				form.copyToModel();
				shell.dispose();
			}
		});
		ok.setText("Generate");
		
		shell.setText("c-db-gen config");
		shell.pack();
		shell.setSize(300, shell.getSize().y);
		shell.open();
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
		
		return ret[0];
	}
	
	private static void initVelocity()
	{
		try
		{
			Properties props = new Properties();
			props.put("runtime.log.logsystem.class", VelocityLogger.class.getName());
			props.put("file.resource.loader.class", VelocityResourceLoader.class.getName());
			props.put("velocimacro.permissions.allow.inline.local.scope", "true");
			Velocity.init(props);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private static void process(Config cfg) throws IOException
	{
		List<Struct> structs = new RecParser().parse(cfg.recFile);
		
		for (Struct struct : structs)
		{
			if (cfg.palm.enabled && cfg.palm.outputDir != null)
				processOutputer(cfg.recFile, new PalmOutputer(), struct, cfg.palm.outputDir);
			if (cfg.sqlite.enabled && cfg.sqlite.outputDir != null)
				processOutputer(cfg.recFile, new SqliteOutputer(), struct, cfg.sqlite.outputDir);
		}
	}
	
	private static void processOutputer(File rec, Outputer outputer, Struct struct, File dir)
	{
		try
		{
			outputer.output(struct, dir);
		}
		catch (OutputerException e)
		{
			System.err.println(rec.getName() + " : [" + outputer.getName() + "] Error creating files:");
			e.printStackTrace();
		}
		catch (OutputerValidationException e)
		{
			System.out.println(rec.getName() + " : [" + outputer.getName() + "] " + e.getMessage());
		}
	}
}
