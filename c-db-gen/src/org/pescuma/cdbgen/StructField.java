package org.pescuma.cdbgen;

import static org.pescuma.cdbgen.StructField.Type.*;

import java.util.ArrayList;
import java.util.List;

public class StructField
{
	public static enum Type
	{
		Char,
		Boolean,
		Int8,
		UInt8,
		Int16,
		UInt16,
		Int32,
		UInt32,
		DateTime
	};
	
	public Type type;
	public String typeName;
	public int array;
	public boolean list;
	public String name;
	public final List<String> flags = new ArrayList<String>();
	
	public Type getType()
	{
		return type;
	}
	
	public int getArray()
	{
		return array;
	}
	
	public boolean isList()
	{
		return list;
	}
	
	public boolean isPrimitive()
	{
		return typeName == null;
	}
	
	public String getName()
	{
		return name;
	}
	
	public List<String> getFlags()
	{
		return flags;
	}
	
	public boolean isString()
	{
		return type == Char && array > 0;
	}
	
	@Override
	public String toString()
	{
		return (type == null ? typeName : type.name()) + " " + name + (array > 0 ? "[" + array + "]" : "");
	}
}
