package nbody.event;

import java.io.File;

import nbody.view.AbstractView;


public class OpenFileEvent extends Event {

    private File file;

	public OpenFileEvent(AbstractView source, File f) {
    	super("Open File", source);
    	this.file = f;
        }

        public File getFile() {
    	return file;
        }

}
