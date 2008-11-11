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
	private static final Pattern listField = Pattern.compile("^\\s*List<\\s*(" + var + ")\\s*>\\s+(" + var + ")\\s*;\\s*$");
	private static final Pattern referenceField = Pattern.compile("^\\s*(" + var + ")\\s+(" + var + ")\\s*;\\s*$");
	private static final Pattern index = Pattern.compile("^\\s*INDEX\\s*\\((\\s*" + var + "\\s*(,\\s*" + var + "\\s*)*)\\)\\s*;\\s*$");
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
				
				if (isStartStruct(line))
					continue;
				
				if (handleField(line, cur))
					continue;
				
				if (handleFlagField(line, cur))
					continue;
				
				if (handleListField(line, cur))
					continue;
				
				if (handleReferenceField(line, cur))
					continue;
				
				if (handleIndex(line, cur))
					continue;
				
				if (isEndStruct(line))
				{
					cur = null;
					continue;
				}
				
				throw new IllegalArgumentException("Unknown line: " + line);
			}
		}
		finally
		{
			in.close();
		}
		
		for (Struct s : structs)
		{
			for (StructField f : s.fields)
			{
				if (f.typeName != null && Utils.findStruct(structs, f.typeName) == null)
					throw new IllegalArgumentException("Unknown type: " + f.typeName);
			}
			for (StructIndex i : s.indexes)
			{
				for (String f : i.fields)
					if (Utils.findField(s, f) == null)
						throw new IllegalArgumentException("Unknown field: " + f);
			}
			
		}
		
		return structs;
	}
	
	private boolean isStartStruct(String line)
	{
		return startStruct.matcher(line).matches();
	}
	
	private boolean isEndStruct(String line)
	{
		return endStruct.matcher(line).matches();
	}
	
	private boolean handleIndex(String line, Struct cur)
	{
		Matcher m = index.matcher(line);
		if (!m.matches())
			return false;
		
		StructIndex i = new StructIndex();
		String[] fs = m.group(1).split(",");
		for (int j = 0; j < fs.length; j++)
		{
			String f = fs[j];
			f = f.trim();
			if (!f.isEmpty())
				i.fields.add(f);
		}
		cur.indexes.add(i);
		return true;
	}
	
	private boolean handleReferenceField(String line, Struct cur)
	{
		Matcher m = referenceField.matcher(line);
		if (!m.matches())
			return false;
		
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
		cur.fields.add(f);
		return true;
	}
	
	private boolean handleListField(String line, Struct cur)
	{
		Matcher m = listField.matcher(line);
		if (!m.matches())
			return false;
		
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
		return true;
	}
	
	private boolean handleFlagField(String line, Struct cur)
	{
		Matcher m = flagField.matcher(line);
		if (!m.matches())
			return false;
		
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
		return true;
	}
	
	private boolean handleField(String line, Struct cur)
	{
		Matcher m = field.matcher(line);
		if (!m.matches())
			return false;
		
		StructField f = new StructField();
		f.name = m.group(2);
		f.type = StructField.Type.valueOf(m.group(1));
		String arr = m.group(4);
		if (arr == null)
			f.array = 0;
		else
			f.array = Integer.parseInt(arr);
		cur.fields.add(f);
		return true;
	}
	
}
