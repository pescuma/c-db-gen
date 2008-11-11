package org.pescuma.cdbgen;

import static java.lang.Character.*;
import static org.pescuma.cdbgen.StructField.Type.*;

import java.util.List;

public class Utils
{
	public static String firstUpper(String str)
	{
		int len = str.length();
		if (len <= 0)
			return "";
		else if (len == 1)
			return str.toUpperCase();
		else
			return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
	
	public static String firstLower(String str)
	{
		int len = str.length();
		if (len <= 0)
			return "";
		else if (len == 1)
			return str.toLowerCase();
		else
			return str.substring(0, 1).toLowerCase() + str.substring(1);
	}
	
	public static String toDefine(String name)
	{
		name = name.charAt(0) + name.substring(1).replaceAll("([a-z])([A-Z]+)", "$1_$2");
		name = name.replaceAll("_+", "_");
		return name.toUpperCase();
	}
	
	public static String toVariable(String name)
	{
		StringBuilder ret = new StringBuilder();
		char last = '\0';
		for (int i = 0; i < name.length(); i++)
		{
			char c = name.charAt(i);
			
			if (i == 0)
			{
				ret.append(toLowerCase(c));
			}
			else if (c == ' ' || c == '_' || c == '.')
			{
				continue;
			}
			else if (last == ' ' || last == '_' || last == '.')
			{
				ret.append(toUpperCase(c));
			}
			else
			{
				ret.append(toLowerCase(c));
			}
			
			last = c;
		}
		return ret.toString();
	}
	
	public static String getSetterName(StructField f)
	{
		return "set" + firstUpper(f.name);
	}
	
	public static String getGetterName(StructField f)
	{
		return (f.type == StructField.Type.Boolean ? "is" : "get") + firstUpper(f.name);
	}
	
	public static boolean hasList(Struct s)
	{
		for (StructField f : s.fields)
		{
			if (f.isList())
				return true;
		}
		return false;
	}
	
	public static boolean hasNotList(Struct s)
	{
		for (StructField f : s.fields)
		{
			if (!f.isList())
				return true;
		}
		return false;
	}
	
	public static boolean hasReference(Struct s)
	{
		for (StructField f : s.fields)
		{
			if (!f.isList() && f.typeName != null)
				return true;
		}
		return false;
	}
	
	public static boolean hasString(Struct s)
	{
		for (StructField f : s.fields)
		{
			if (f.isString())
				return true;
		}
		return false;
	}
	
	public static boolean hasDateTime(Struct s)
	{
		for (StructField f : s.fields)
		{
			if (f.type == DateTime)
				return true;
		}
		return false;
	}
	
	public static boolean hasFlags(Struct s)
	{
		for (StructField f : s.fields)
		{
			if (f.flags.size() > 0)
				return true;
		}
		return false;
	}
	
	public static Struct findStruct(List<Struct> structs, String name)
	{
		for (Struct s : structs)
			if (name.equals(s.name))
				return s;
		return null;
	}
	
	public static Object findField(Struct struct, String fieldName)
	{
		for (StructField field : struct.fields)
			if (field.name.equals(fieldName))
				return field;
		return null;
	}
}
