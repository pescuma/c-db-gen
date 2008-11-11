package org.pescuma.cdbgen;

import java.util.ArrayList;
import java.util.List;

public class Struct
{
	public String name;
	public final List<StructField> fields = new ArrayList<StructField>();
	public final List<StructIndex> indexes = new ArrayList<StructIndex>();
	
	public String getName()
	{
		return name;
	}
	
	public List<StructField> getFields()
	{
		return fields;
	}
	
	public List<StructIndex> getIndexes()
	{
		return indexes;
	}
	
	@Override
	public String toString()
	{
		return "Struct[" + name + "]";
	}
	
}
