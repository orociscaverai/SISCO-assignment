package nbody_distribuito.shared_object;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;


public class Job implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private List<ClientData> data;
    private List<Interaction> interactions;

    public Job() {
	data = new Vector<ClientData>();
	interactions = new Vector<Interaction>();
    }

    public void addData(int id, float pos[], float mass) {
	ClientData newData = new ClientData(data.size(), id, pos, mass);
	data.add(newData);
    }

    public void addInteraction(int indexA, int indexB){
	interactions.add(new Interaction(indexA, indexB));
    }

    public int getNumTask() {
	return interactions.size();
    }

    public ClientData[] getDataOfNextInteraction() {
	if (interactions.isEmpty())
	    return null;
	ClientData[] out = new ClientData[2];
	Interaction interaction = interactions.remove(0);
	
	int indexA = interaction.getFirstIndex();
	out[0] = data.get(indexA);
	
	int indexB = interaction.getSecondIndex();
	out[1] = data.get(indexB);
	return out;
    }

    public ClientData getData(int index) {
	return data.get(index);
    }

    public int getNumBodies() {
	return data.size();
    }

    public String toString() {

	String out = "";

	for (ClientData c : data) {
	    out += c.toString() + "\n";
	}
	out += "Interaction size: " + interactions.size() + "\n";
	for (Interaction i : interactions) {
	    out += i.toString() + "\n";
	}
	return out;
    }
}
