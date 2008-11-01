package org.pescuma.cdbgen.sqlite;

import org.pescuma.cdbgen.StructField;
import org.pescuma.cdbgen.Utils;

public class SqliteUtils extends Utils
{
	public String typeName(StructField f)
	{
		if (f.typeName != null)
			return f.typeName;
		
		switch (f.type)
		{
			case Boolean:
				return "bool";
			case Char:
				return "TCHAR";
			case Int8:
				return "short";
			case UInt8:
				return "short";
			case Int16:
				return "int";
			case UInt16:
				return "unsigned int";
			case Int32:
				return "int";
			case UInt32:
				return "unsigned int";
		}
		throw new IllegalStateException();
	}
	
	public String fieldDeclaration(StructField f)
	{
		if (f.isList())
			return "std::vector<" + typeName(f) + "> " + f.name;
		if (f.array > 0)
			return typeName(f) + " " + f.name + "[" + f.array + "]";
		return typeName(f) + " " + f.name;
	}
}
