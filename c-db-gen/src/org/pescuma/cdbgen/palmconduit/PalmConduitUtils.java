package org.pescuma.cdbgen.palmconduit;

import static java.lang.Math.*;
import static org.pescuma.cdbgen.StructField.Type.*;

import org.pescuma.cdbgen.Struct;
import org.pescuma.cdbgen.StructField;
import org.pescuma.cdbgen.Utils;

public class PalmConduitUtils extends Utils
{
	public static String typeName(StructField f)
	{
		if (f.typeName != null)
			return f.typeName;
		
		switch (f.type)
		{
			case Boolean:
				return "bool";
			case Char:
				if (f.array > 0)
					return "std::tstring";
				else
					return "TCHAR";
			case Int8:
				return "char";
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
			return "DWORD " + f.name;
		if (f.array > 0 && f.type == Char)
			return typeName(f) + " " + f.name;
		if (f.array > 0)
			return typeName(f) + " " + f.name + "[" + f.array + "]";
		return typeName(f) + " " + f.name;
	}
	
	public static String sizeOf(Struct struct)
	{
		int ret = 0;
		for (StructField field : struct.fields)
		{
			if (field.isList())
				continue;
			
			if (field.name != null)
			{
				ret += 4;
				continue;
			}
			
			switch (field.type)
			{
				case Boolean:
					ret += 2 * max(1, field.array);
					break;
				case Char:
					ret += 1 * max(1, field.array);
					break;
				case Int8:
					ret += 1 * max(1, field.array);
					break;
				case UInt8:
					ret += 1 * max(1, field.array);
					break;
				case Int16:
					ret += 2 * max(1, field.array);
					break;
				case UInt16:
					ret += 2 * max(1, field.array);
					break;
				case Int32:
					ret += 3 * max(1, field.array);
					break;
				case UInt32:
					ret += 3 * max(1, field.array);
					break;
			}
		}
		return Integer.toString(ret);
	}
}
