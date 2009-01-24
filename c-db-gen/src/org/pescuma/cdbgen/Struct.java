package org.pescuma.cdbgen;

import java.util.ArrayList;
import java.util.List;

public class Struct
{
	public String name;
	public String parentName;
	public Struct parent;
	public final List<StructField> fields = new ArrayList<StructField>();
	public final List<StructIndex> indexes = new ArrayList<StructIndex>();
	
	public String getName()
	{
		return name;
	}
	
	public String getParentName()
	{
		return parentName;
	}
	
	public Struct getParent()
	{
		return parent;
	}
	
	public List<Struct> getHierarchy()
	{
		List<Struct> ret = new ArrayList<Struct>();
		addToHierarchy(ret, this);
		return ret;
	}
	
	private void addToHierarchy(List<Struct> ret, Struct st)
	{
		if (st == null)
			return;
		
		addToHierarchy(ret, st.parent);
		ret.add(st);
	}
	
	public List<StructField> getFields()
	{
		return fields;
	}
	
	public List<StructIndex> getIndexes()
	{
		return indexes;
	}
	
	@Override
	public String toString()
	{
		return "Struct[" + name + "]";
	}
}
