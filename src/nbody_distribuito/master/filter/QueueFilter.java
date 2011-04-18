package nbody_distribuito.master.filter;

import pcd.actors.Message;
import pcd.actors.filters.MsgFilter;

public class QueueFilter implements MsgFilter {

	@Override
	public boolean match(Message msg) {
		return msg.getType().equalsIgnoreCase("client_queue_response");
	}

}
