package org.pescuma.cdbgen;

import java.util.ArrayList;
import java.util.List;

public class Struct
{
	public String name;
	public final List<StructField> fields = new ArrayList<StructField>();
	
	public String getName()
	{
		return name;
	}
	
	public List<StructField> getFields()
	{
		return fields;
	}
	
	@Override
	public String toString()
	{
		return "Struct[" + name + "]";
	}
	
}
