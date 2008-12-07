package org.pescuma.cdbgen.vb6_ADO;

import org.pescuma.cdbgen.StructField;
import org.pescuma.cdbgen.Utils;

public class VB6ADOUtils extends Utils
{
	public static String typeName(StructField f)
	{
		if (f.typeName != null)
			return f.typeName;
		
		if (f.isString())
			return "String";
		
		switch (f.type)
		{
			case Boolean:
				return "Boolean";
			case Char:
				return "String";
			case Int8:
				return "Byte";
			case UInt8:
				return "Byte";
			case Int16:
				return "Integer";
			case UInt16:
				return "Integer";
			case Int32:
				return "Long";
			case UInt32:
				return "Long";
			case DateTime:
				return "Date";
			case Currency:
				return "Currency";
		}
		throw new IllegalStateException();
	}
	
	public static String fieldDeclaration(StructField f)
	{
		if (f.isString())
			return f.name + " As " + typeName(f);
		if (f.array > 0)
			return f.name + "(" + f.array + ") As " + typeName(f);
		return f.name + " As " + typeName(f);
	}
	
	public static String convertionFunction(StructField f)
	{
		if (f.isString())
			return "CStr";
		
		switch (f.type)
		{
			case Boolean:
				return "CBool";
			case Char:
				return "Str";
			case Int8:
				return "CByte";
			case UInt8:
				return "CByte";
			case Int16:
				return "CInt";
			case UInt16:
				return "CInt";
			case Int32:
				return "CLng";
			case UInt32:
				return "CLng";
			case DateTime:
				return "CDate";
			case Currency:
				return "CCur";
		}
		throw new IllegalStateException();
	}
}
