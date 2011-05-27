package nbody_distribuito.testCase;

import nbody_distribuito.BodiesMap;

public class TestBodiesMap {

    public static void main(String[] args) {
	BodiesMap bm = new BodiesMap();

	bm.generateRandomMap(100);

	System.out.println(bm.toString());
    }

}
