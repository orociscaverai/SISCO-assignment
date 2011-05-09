package nbody_distribuito.master.filter;

import nbody_distribuito.Constants;
import pcd.actors.Message;
import pcd.actors.filters.MsgFilter;

public class QueueFilter implements MsgFilter {

	@Override
	public boolean match(Message msg) {
		return msg.getType().equals(Constants.CLIENT_QUEUE_RESP);
	}

}
