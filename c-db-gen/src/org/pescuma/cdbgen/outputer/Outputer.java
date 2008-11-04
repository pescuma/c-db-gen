package org.pescuma.cdbgen.outputer;

import java.io.File;
import java.util.List;

import org.pescuma.cdbgen.Struct;

public interface Outputer
{
	String getName();
	
	void output(Struct struct, String namespace, File path, List<Struct> structs);
}
