package lang.fn;

import java.io.*;

public class InputPort extends Port {
	public static InputPort CURRENT = new InputPort(System.in);
	private BufferedReader value;
	
	public boolean isInputPort() {
		return true;
	}
	
	public boolean isOutputPort() {
		return false;
	}
	
	public InputPort() {
		value = CURRENT.value;
	}
	
	private InputPort(InputStream is) {
		value = new BufferedReader(new InputStreamReader(is));
	}
	
	public InputPort(Str fileName) throws FnException {
		try	{
			value = new BufferedReader(new FileReader(fileName.stringValue()));
		} catch(FileNotFoundException ex) {
			throw new FnException(Error.FILE_NOT_FOUND, fileName);
		}
	}

	public Expression close() throws FnException {
		try {
			value.close();
		}catch(IOException ex) {
			throw new FnException(Error.IO, this);
		}
		return Void.VOID;
	}

	public Expression readChar() throws FnException {
		Expression retVal = Eof.EOF;
		try {
			if(value.ready()) retVal = new Char(value.read());
		}catch(IOException ex) {
			throw new FnException(Error.IO, this);
		}
		return retVal;
	}

	public Expression peekChar() throws FnException	{
		Expression retVal = Eof.EOF;
		try {
			if (value.ready()) {
				value.mark(1);
			 	retVal = new Char(value.read());
				value.reset();
			}
		} catch(IOException ex) {
			throw new FnException(Error.IO, this);
		}
		return retVal;
	}
	
}