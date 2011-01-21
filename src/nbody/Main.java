package nbody;

/**
 * @author Boccacci Andrea, Cicora Saverio
 * 
 */
public class Main
{

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println(new Planets(3).toString());
		System.out.println(new InteractionMatrix(5).toString());
		//System.out.println(bodyBodyInteraction(bi, bj, ai, 0.0f));
	}

}
