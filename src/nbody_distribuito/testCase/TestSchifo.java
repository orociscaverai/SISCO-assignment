package nbody_distribuito.testCase;

import nbody_distribuito.Body;

public class TestSchifo {

    /**
     * @param args
     */
    public static void main(String[] args) {
	// TODO Auto-generated method stub
	Body b = new Body(1, 1.0f);
	float[] pos = new float[2];
	pos[0] = 0;
	pos[1] = 0;
	b.setPosition(pos);
	float[] pos2 = b.getPosition();
	pos2[0] += 1;
	System.out.println(b.toString());
    }

}
