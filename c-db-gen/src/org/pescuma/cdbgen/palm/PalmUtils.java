package org.pescuma.cdbgen.palm;

import org.pescuma.cdbgen.StructField;
import org.pescuma.cdbgen.Utils;

public class PalmUtils extends Utils
{
	public static String typeName(StructField f)
	{
		if (f.typeName != null)
			return f.typeName;
		
		switch (f.type)
		{
			case Boolean:
				return "Boolean";
			case Char:
				return "Char";
			case Int8:
				return "Int8";
			case UInt8:
				return "UInt8";
			case Int16:
				return "Int16";
			case UInt16:
				return "UInt16";
			case Int32:
				return "Int32";
			case UInt32:
				return "UInt32";
			case DateTime:
				return "UInt32";
			case Currency:
				return "Int32";
		}
		throw new IllegalStateException();
	}
}
