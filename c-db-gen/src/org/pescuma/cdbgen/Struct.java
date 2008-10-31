package org.pescuma.cdbgen;

import java.util.ArrayList;
import java.util.List;

public class Struct
{
	public String name;
	public final List<StructField> fields = new ArrayList<StructField>();
	
	@Override
	public String toString()
	{
		return "Struct[" + name + "]";
	}
	
}
