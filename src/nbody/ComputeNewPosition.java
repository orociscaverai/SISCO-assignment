package nbody;

public class ComputeNewPosition implements Runnable {

    public ComputeNewPosition(int body, int deltaTime,
	    InteractionMatrix interactionMatrix) {
    }

    @Override
    public void run() {
	computePosition();
	computeVelocity();
    }

    private void computeVelocity() {
    }

    private void computePosition() {
    }
}
