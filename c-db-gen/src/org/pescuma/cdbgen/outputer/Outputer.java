package org.pescuma.cdbgen.outputer;

import java.io.File;
import java.util.List;

import org.pescuma.cdbgen.Struct;

public interface Outputer
{
	String getName();
	
	void output(Struct struct, List<Struct> structs, String namespace, File path);
	
	void globalOutput(List<Struct> structs, String namespace, File outputDir);
}
