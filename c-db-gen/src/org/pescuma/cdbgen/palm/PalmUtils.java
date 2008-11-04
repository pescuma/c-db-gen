package org.pescuma.cdbgen.palm;

import org.pescuma.cdbgen.StructField;
import org.pescuma.cdbgen.Utils;

public class PalmUtils extends Utils
{
	public static String typeName(StructField f)
	{
		if (f.typeName != null)
			return f.typeName;
		return f.type.name();
	}
}
