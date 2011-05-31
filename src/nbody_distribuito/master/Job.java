package nbody_distribuito.master;

import java.util.ArrayList;
import java.util.List;

public class Job {

    private List<ClientData> set1;
    private List<ClientData> set2;

    public Job() {
	set1 = new ArrayList<ClientData>();
	set2 = new ArrayList<ClientData>();
    }

    public void addDataOnFirstSet(int id, float pos[], float mass) {
	ClientData data = new ClientData(id, pos, mass);
	set1.add(data);
    }

    public void addDataOnSecondSet(int id, float pos[], float mass) {
	ClientData data = new ClientData(id, pos, mass);
	set2.add(data);
    }

//    public void setFistSet(List<ClientData> data) {
//	set1 = data;
//    }
//
//    public void setSecondSet(List<ClientData> data) {
//	set2 = data;
//    }

    public List<ClientData> getFistSet() {
	return set1;
    }

    public List<ClientData> getSecondSet() {
	return set2;
    }
}
