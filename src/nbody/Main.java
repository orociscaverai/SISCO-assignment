package nbody;

import gui.NBodyView;

/**
 * @author Boccacci Andrea, Cicora Saverio
 * 
 */
public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {

	NBodyView view = new NBodyView(800, 600);
	Master master = new Master(view);
	master.start();
	// System.out.println(new Planets(3).toString());
	// System.out.println(new InteractionMatrix(5).toString());
	// //System.out.println(bodyBodyInteraction(bi, bj, ai, 0.0f));
    }

}
