package nbody_distribuito.event;

import java.io.File;

import nbody_distribuito.view.ObservableComponent;


public class OpenFileEvent extends Event {

    private File file;

	public OpenFileEvent(ObservableComponent source, File f) {
    	super("Open File", source);
    	this.file = f;
        }

        public File getFile() {
    	return file;
        }

}
