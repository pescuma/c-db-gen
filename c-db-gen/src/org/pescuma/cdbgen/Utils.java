package org.pescuma.cdbgen;

import static java.lang.Character.*;

public class Utils
{
	public static String firstUp(String name)
	{
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}
	
	public static String toDefine(String name)
	{
		name = name.charAt(0) + name.substring(1).replaceAll("([a-z])([A-Z]+)", "$1_$2");
		name = name.replaceAll("_+", "_");
		return name.toUpperCase();
	}
	
	public String toVariable(String name)
	{
		String ret = "";
		char last = '\0';
		for (int i = 0; i < name.length(); i++)
		{
			char c = name.charAt(i);
			
			if (i == 0)
			{
				ret += toLowerCase(c);
			}
			else if (c == ' ' || c == '_' || c == '.')
			{
				continue;
			}
			else if (last == ' ' || last == '_' || last == '.')
			{
				ret += toUpperCase(c);
			}
			else
			{
				ret += toLowerCase(c);
			}
			
			last = c;
		}
		return ret;
	}
	
	public static String getSetterName(StructField f)
	{
		return "set" + firstUp(f.name);
	}
	
	public static String getGetterName(StructField f)
	{
		return (f.type == StructField.Type.Boolean ? "is" : "get") + firstUp(f.name);
	}
}
