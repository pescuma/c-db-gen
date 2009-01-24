package org.pescuma.cdbgen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
import org.pescuma.cdbgen.palmconduit.PalmConduitOutputer;
import org.pescuma.cdbgen.sqlite.SqliteOutputer;
import org.pescuma.cdbgen.v8.V8Outputer;
import org.pescuma.cdbgen.vb6_ADO.VB6ADOOutputer;
import org.pescuma.cdbgen.vb6_dhSQLite.VB6DHOutputer;
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
		@NotNull
		public String namespace;
		
		final Outputer outputer;
		
		public ConfigItem(Outputer outputer)
		{
			this.outputer = outputer;
		}
	}
	
	private static class Config
	{
		public File recFile;
		public final ConfigItem sqlite = new ConfigItem(new SqliteOutputer());
		public final ConfigItem palm = new ConfigItem(new PalmOutputer());
		public final ConfigItem palmConduit = new ConfigItem(new PalmConduitOutputer());
		public final ConfigItem vb6_ADO = new ConfigItem(new VB6ADOOutputer());
		public final ConfigItem vb6_dhSQLite = new ConfigItem(new VB6DHOutputer());
		public final ConfigItem v8 = new ConfigItem(new V8Outputer());
		
		public final List<ConfigItem> items = new ArrayList<ConfigItem>();
		
		public Config()
		{
			items.add(sqlite);
			items.add(palm);
			items.add(palmConduit);
			items.add(vb6_ADO);
			items.add(vb6_dhSQLite);
			items.add(v8);
		}
	}
	
	public static void main(String[] args)
	{
		Config cfg = new Config();
		loadFromProperties(cfg);
		if (!askForConfigData(cfg))
			return;
		saveToProperties(cfg);
		
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
	
	private static void loadFromProperties(Config cfg)
	{
		File file = new File("c-db-gen.properties");
		if (!file.exists())
			return;
		
		Properties props = new Properties();
		FileReader reader = null;
		try
		{
			reader = new FileReader(file);
			try
			{
				props.load(reader);
			}
			finally
			{
				reader.close();
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
		
		cfg.recFile = toFile(props.getProperty("recFile", ""));
		for (ConfigItem item : cfg.items)
			get(item, props);
	}
	
	private static void saveToProperties(Config cfg)
	{
		Properties props = new Properties();
		props.setProperty("recFile", toString(cfg.recFile));
		for (ConfigItem item : cfg.items)
			set(props, item);
		
		try
		{
			FileOutputStream out = new FileOutputStream(new File("c-db-gen.properties"));
			try
			{
				props.store(out, null);
			}
			finally
			{
				out.close();
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private static void get(ConfigItem item, Properties props)
	{
		String name = item.outputer.getName();
		item.enabled = Boolean.parseBoolean(props.getProperty(name + ".enabled", "false"));
		item.outputDir = toFile(props.getProperty(name + ".outputDir", ""));
		item.namespace = props.getProperty(name + ".namespace", "");
	}
	
	private static void set(Properties props, ConfigItem item)
	{
		String name = item.outputer.getName();
		props.setProperty(name + ".enabled", Boolean.toString(item.enabled));
		props.setProperty(name + ".outputDir", toString(item.outputDir));
		props.setProperty(name + ".namespace", item.namespace);
	}
	
	private static String toString(File file)
	{
		if (file == null)
			return "";
		try
		{
			return file.getCanonicalPath();
		}
		catch (IOException e)
		{
			return file.getAbsolutePath();
		}
	}
	
	private static File toFile(String prop)
	{
		prop = prop.trim();
		if (prop.isEmpty())
			return null;
		
		return new File(prop);
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
		shell.setDefaultButton(ok);
		shell.pack();
		shell.setSize(500, shell.getSize().y);
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
			for (ConfigItem item : cfg.items)
				processOutputer(cfg.recFile, item, struct, structs);
		}
		for (ConfigItem item : cfg.items)
			processGlobalOutputer(cfg.recFile, item, structs);
	}
	
	private static void processGlobalOutputer(File rec, ConfigItem item, List<Struct> structs)
	{
		if (!item.enabled)
			return;
		try
		{
			item.outputer.globalOutput(structs, item.namespace, item.outputDir);
		}
		catch (OutputerException e)
		{
			System.err.println(rec.getName() + " : [" + item.outputer.getName() + "] Error creating files:");
			e.printStackTrace();
		}
		catch (OutputerValidationException e)
		{
			System.out.println(rec.getName() + " : [" + item.outputer.getName() + "] " + e.getMessage());
		}
	}
	
	private static void processOutputer(File rec, ConfigItem item, Struct struct, List<Struct> structs)
	{
		if (!item.enabled)
			return;
		try
		{
			item.outputer.output(struct, structs, item.namespace, item.outputDir);
		}
		catch (OutputerException e)
		{
			System.err.println(rec.getName() + " : [" + item.outputer.getName() + "] Error creating files:");
			e.printStackTrace();
		}
		catch (OutputerValidationException e)
		{
			System.out.println(rec.getName() + " : [" + item.outputer.getName() + "] " + e.getMessage());
		}
	}
}
