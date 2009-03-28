package org.pescuma.cdbgen;

import static org.pescuma.cdbgen.StructField.Type.*;

import java.util.ArrayList;
import java.util.List;

public class StructField
{
	public static class Flag
	{
		public final String flag;
		public final String name;
		
		public Flag(String flag, String name)
		{
			this.flag = flag;
			this.name = name;
		}
		
		public String getFlag()
		{
			return flag;
		}
		
		public String getName()
		{
			return name;
		}
	}
	
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
		DateTime,
		Currency
	};
	
	public boolean readOnly;
	public Type type;
	public String typeName;
	public String setterCode;
	public int array;
	public boolean list;
	public String name;
	public final List<Flag> flags = new ArrayList<Flag>();
	public String defVal;
	
	public Type getType()
	{
		return type;
	}
	
	public String getTypeName()
	{
		return typeName;
	}
	
	public String getSetterCode()
	{
		return setterCode;
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
	
	public List<Flag> getFlags()
	{
		return flags;
	}
	
	public boolean hasFlags()
	{
		return flags.size() > 0;
	}
	
	public boolean isString()
	{
		return type == Char && array > 0;
	}
	
	public boolean isReadOnly()
	{
		return readOnly;
	}
	
	public String getDefVal()
	{
		if (defVal != null)
			return defVal;
		
		if (isString())
			return "\"\"";
		
		switch (type)
		{
			case Boolean:
				return "false";
			case Char:
				return "'\\\\0'";
			case Currency:
			case DateTime:
			case Int8:
			case Int16:
			case Int32:
			case UInt8:
			case UInt16:
			case UInt32:
				return "0";
		}
		
		throw new IllegalStateException();
	}
	
	@Override
	public String toString()
	{
		return (type == null ? typeName : type.name()) + " " + name + (array > 0 ? "[" + array + "]" : "");
	}
}
