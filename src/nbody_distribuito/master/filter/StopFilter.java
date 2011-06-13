package nbody_distribuito.master.filter;

import nbody_distribuito.message.JobResultMessage;
import pcd.actors.Message;
import pcd.actors.filters.MsgFilter;

public class StopFilter implements MsgFilter {

	@Override
	public boolean match(Message msg) {
		if (msg instanceof JobResultMessage)
			return true;
		return false;
	}

}
