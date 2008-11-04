package org.pescuma.cdbgen.sqlite;

import org.pescuma.cdbgen.StructField;
import org.pescuma.cdbgen.Utils;

public class SqliteUtils extends Utils
{
	public static String typeName(StructField f)
	{
		if (f.typeName != null)
			return f.typeName;
		
		if (f.isString())
			return "std::tstring";
		
		switch (f.type)
		{
			case Boolean:
				return "bool";
			case Char:
				return "TCHAR";
			case Int8:
				return "BYTE";
			case UInt8:
				return "BYTE";
			case Int16:
				return "short";
			case UInt16:
				return "unsigned short";
			case Int32:
				return "int";
			case UInt32:
				return "unsigned int";
		}
		throw new IllegalStateException();
	}
	
	public static String fieldDeclaration(StructField f)
	{
		if (f.isList())
			return "std::vector<" + typeName(f) + "> " + f.name;
		if (f.isString())
			return typeName(f) + " " + f.name;
		if (f.array > 0)
			return typeName(f) + " " + f.name + "[" + f.array + "]";
		return typeName(f) + " " + f.name;
	}
}
