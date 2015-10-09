package lang.fn;

import java.io.*;

public class OutputPort extends Port {
	public static OutputPort CURRENT = new OutputPort(System.out);
	private OutputStream value;
	
	public boolean isInputPort() {
		return false;
	}
	
	public boolean isOutputPort() {
		return true;
	}
	
	public OutputPort() {
		value = CURRENT.value;
	}
	
	public OutputPort(OutputStream os)	{
		value = os;
	}
	
	public OutputPort(Str fileName) throws FnException {
		try {
			value = new PrintStream(new FileOutputStream(fileName.write()));
		}catch(FileNotFoundException ex) {
			throw new FnException(Error.FILE_NOT_FOUND, fileName);
		}
	}

	public Expression close() throws FnException {
		try	{
			value.close();
		} catch(IOException ex) {
			throw new FnException(Error.IO, this);
		}
		return Void.VOID;
	}

	public Expression write(Expression z) throws FnException {
		((PrintStream)value).print(z.write());
		return Void.VOID;
	}

	public Expression writeChar(Char z) throws FnException {
		((PrintStream)value).print(z.charValue());
		return Void.VOID;
	}

	public Expression display(Expression z) throws FnException {
		String s = (z.isString()) ? ((Str)z).stringValue() : ((z.isChar()) ? new Character(((Char)z).charValue()).toString() : z.write());
		((PrintStream)value).print(s);
		return Void.VOID;
	}
	
	public Expression newline() throws FnException {
		((PrintStream)value).println();
		return Void.VOID;
	}
	
}