package nbody_distribuito.master;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class Job implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ClientData> data;
	private List<Interaction> interactions;
	private Map<Integer, Integer> relativeIndexMap;
	
	public Job(){
		data = new Vector<ClientData>();
		interactions = new Vector<Interaction>();
		relativeIndexMap = new HashMap<Integer, Integer>();
	}
	public void addData(int id, float pos[], float mass){
		relativeIndexMap.put(id, data.size());
		ClientData newData = new ClientData(data.size(), id, pos, mass);
		data.add(newData);
	}
	public void addInteraction(int indexA ,int indexB) throws Exception{
		int relativeA = relativeIndexMap.get(indexA);
		if((Integer)relativeA == null)
			throw new Exception("l'indice A("+indexA+") non è presente nel job");
		int relativeB = relativeIndexMap.get(indexB);
		if((Integer)relativeB == null)
			throw new Exception("l'indice B("+indexB+") non è presente nel job");
		interactions.add(new Interaction(indexA,indexB));
	}
	public int getNumTask(){
		return interactions.size();
	}
	public ClientData[] getDataOfNextInteraction(){
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
	public ClientData getData(int index){
		return data.get(index);
	}
	public int getNumBodies(){
		return data.size();
	}
	public String toString(){
		String out = "";
		for(ClientData c: data)
			out += c.toString()+"\n";
		out+= "Interaction size: "+interactions.size()+"\n";
		for(Interaction i : interactions){
			out+= i.toString()+"\n";
		}
		return out;
	}
}
