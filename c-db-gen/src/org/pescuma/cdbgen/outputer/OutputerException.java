package org.pescuma.cdbgen.outputer;

public class OutputerException extends RuntimeException
{
	private static final long serialVersionUID = -5107503144917983847L;
	
	public OutputerException()
	{
		super();
	}
	
	public OutputerException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public OutputerException(String message)
	{
		super(message);
	}
	
	public OutputerException(Throwable cause)
	{
		super(cause);
	}
	
}
