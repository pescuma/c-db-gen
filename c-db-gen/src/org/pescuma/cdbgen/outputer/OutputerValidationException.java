package org.pescuma.cdbgen.outputer;

import org.pescuma.cdbgen.Struct;
import org.pescuma.cdbgen.StructField;

public class OutputerValidationException extends RuntimeException
{
	private static final long serialVersionUID = -242520497934628607L;
	
	private final Struct struct;
	private final StructField field;
	
	public OutputerValidationException(Struct struct, StructField field, String message)
	{
		super(message);
		this.struct = struct;
		this.field = field;
	}
	
	public OutputerValidationException(Struct struct, String message)
	{
		super(message);
		this.struct = struct;
		field = null;
	}
	
	public Struct getStruct()
	{
		return struct;
	}
	
	public StructField getField()
	{
		return field;
	}
	
}
