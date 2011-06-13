package nbody_distribuito.message;

import java.io.File;

import nbody_distribuito.Constants;
import pcd.actors.Message;

public class OpenFileMessage extends Message {

    private static final long serialVersionUID = 1L;

    private static final String messageType = Constants.OPEN_FILE_EVENT;

	private File file;

    public OpenFileMessage(File f) {
	super(messageType);
	this.file = f;
    }

    /**
     * @return the numBodies
     */
    public File getFile() {
	return file;
    }

    @Override
    public String toString() {

	String args = getType() + " ";

	args += "File: ";
	args += file.getName();

	return args;

    }

}
