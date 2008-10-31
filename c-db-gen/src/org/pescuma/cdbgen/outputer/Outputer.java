package org.pescuma.cdbgen.outputer;

import java.io.File;

import org.pescuma.cdbgen.Struct;

public interface Outputer
{
	String getName();
	
	void output(Struct struct, File path);
}
