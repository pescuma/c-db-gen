package org.pescuma.cdbgen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecParser
{
	private static final Pattern newStruct = Pattern.compile("^\\s*struct\\s+([a-zA-Z][_0-9a-zA-Z]*)\\s*\\{?\\s*$");
	private static final Pattern startStruct = Pattern.compile("^\\s*\\{\\s*$");
	private static final String var = "[a-zA-Z][_0-9a-zA-Z]*";
	private static final String type = "Char|Boolean|Int8|UInt8|Int16|UInt16|Int32|UInt32";
	private static final Pattern field = Pattern.compile("^\\s*(" + type + ")\\s+(" + var + ")\\s*(\\[([0-9]+)\\])?\\s*;\\s*$");
	private static final Pattern flagField = Pattern.compile("^\\s*(" + type + ")\\s+(" + var + ")\\s*((\\|\\s*" + var + "\\s*)+);\\s*$");
	private static final Pattern listField = Pattern.compile("^\\s*List<(" + var + ")>\\s+(" + var + ")\\s*;\\s*$");
	private static final Pattern endStruct = Pattern.compile("^\\s*\\}\\s*;?\\s*$");
	
	public List<Struct> parse(File file) throws IOException
	{
		List<Struct> structs = new ArrayList<Struct>();
		Struct cur = null;
		
		BufferedReader in = new BufferedReader(new FileReader(file));
		try
		{
			String line;
			while ((line = in.readLine()) != null)
			{
				line = line.trim();
				if (line.isEmpty())
					continue;
				if (line.charAt(0) == '#')
					continue;
				
				Matcher m = newStruct.matcher(line);
				if (m.matches())
				{
					cur = new Struct();
					cur.name = m.group(1);
					structs.add(cur);
					continue;
				}
				
				m = startStruct.matcher(line);
				if (m.matches())
					continue;
				
				m = field.matcher(line);
				if (m.matches())
				{
					StructField f = new StructField();
					f.name = m.group(2);
					f.type = StructField.Type.valueOf(m.group(1));
					String arr = m.group(4);
					if (arr == null)
						f.array = 0;
					else
						f.array = Integer.parseInt(arr);
					cur.fields.add(f);
					continue;
				}
				
				m = flagField.matcher(line);
				if (m.matches())
				{
					StructField f = new StructField();
					f.name = m.group(2);
					f.type = StructField.Type.valueOf(m.group(1));
					String[] flags = m.group(3).split("\\|");
					for (int i = 0; i < flags.length; i++)
					{
						String fn = flags[i].trim();
						if (!fn.isEmpty())
							f.flags.add(fn);
					}
					cur.fields.add(f);
					continue;
				}
				
				m = listField.matcher(line);
				if (m.matches())
				{
					StructField f = new StructField();
					f.name = m.group(2);
					try
					{
						f.type = StructField.Type.valueOf(m.group(1));
					}
					catch (IllegalArgumentException e)
					{
						f.type = null;
						f.typeName = m.group(1);
					}
					f.list = true;
					cur.fields.add(f);
					continue;
				}
				
				m = endStruct.matcher(line);
				if (m.matches())
				{
					cur = null;
					continue;
				}
				
				throw new IllegalArgumentException("Unknown line: " + line);
			}
			return structs;
		}
		finally
		{
			in.close();
		}
	}
	
}
