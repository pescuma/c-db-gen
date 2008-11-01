package org.pescuma.cdbgen;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.velocity.app.Velocity;
import org.pescuma.cdbgen.outputer.Outputer;
import org.pescuma.cdbgen.outputer.OutputerException;
import org.pescuma.cdbgen.outputer.OutputerValidationException;
import org.pescuma.cdbgen.palm.PalmOutputer;
import org.pescuma.cdbgen.sqlite.SqliteOutputer;
import org.pescuma.cdbgen.velocity.VelocityLogger;
import org.pescuma.cdbgen.velocity.VelocityResourceLoader;

public class CDBGen
{
	private static class Config
	{
		File recFile;
		boolean generateForPalm;
		File palmOutputDir;
		boolean generateForSqlite;
		File sqliteOutputDir;
	}
	
	public static void main(String[] args)
	{
		initVelocity();
		
		// TODO: Ask for config data
		Config cfg = new Config();
		cfg.recFile = new File("test.rec");
//		cfg.generateForPalm = true;
//		cfg.palmOutputDir = new File(".");
		cfg.generateForSqlite = true;
		cfg.sqliteOutputDir = new File(".");
		
		try
		{
			process(cfg);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
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
			if (cfg.generateForPalm && cfg.palmOutputDir != null)
				processOutputer(cfg.recFile, new PalmOutputer(), struct, cfg.palmOutputDir);
			if (cfg.generateForSqlite && cfg.sqliteOutputDir != null)
				processOutputer(cfg.recFile, new SqliteOutputer(), struct, cfg.sqliteOutputDir);
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
