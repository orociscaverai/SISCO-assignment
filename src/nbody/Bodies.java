package nbody;

/**
 * @author Boccacci Andrea, Cicora Saverio
 * 
 */
public class Bodies {

	/**
	 * Rappresenta il numero di corpi da valutare
	 * */
	int numBodies = 0;

	public static float[] bodyBodyInteraction(float[] bi, float[] bj,
			float[] ai, float soft2) {
		float[] r = new float[2];

		r[0] = bj[0] - bi[0];
		r[1] = bj[1] - bi[1];

		float distSqr = r[0] * r[0] + r[1] * r[1] + soft2;
		double distSixth = distSqr * distSqr * distSqr;
		// TODO: controllare il cast double float
		float invDistCube = 1.0f / (float) Math.sqrt(distSixth);
		float s = bj[2] * invDistCube;

		ai[0] += r[0] * s;
		ai[1] += r[1] * s;
		return ai;
	}

	public void computeNBodyGravitation()
	{
	    for(int i = 0; i < numBodies; ++i)
	    {
	        m_force[i*4] = m_force[i*4+1] = m_force[i*4+2] = 0;

		    for(int j = 0; j < numBodies; ++j)
		    {
		        float[] acc = {0, 0, 0};
		        bodyBodyInteraction(acc, &m_pos[m_currentRead][j*4], &m_pos[m_currentRead][i*4],
					    m_softeningSquared);
		        for (int k = 0; k < 3; ++k)
		        {
		            m_force[i*4+k] += acc[k];
		        }
		    }
	    }
	}

	public void integrateNBodySystem(float deltaTime) {
		computeNBodyGravitation();

		for (int i = 0; i < numBodies; ++i) {
			
			int index = 4 * i;
			float[] pos = new float[3];
			float[] vel = new float[3];
			float[] force = new float[3];
			
			pos[0] = m_pos[m_currentRead][index + 0];
			pos[1] = m_pos[m_currentRead][index + 1];
			pos[2] = m_pos[m_currentRead][index + 2];
			float mass = m_pos[m_currentRead][index + 3];

			vel[0] = m_vel[m_currentRead][index + 0];
			vel[1] = m_vel[m_currentRead][index + 1];
			vel[2] = m_vel[m_currentRead][index + 2];
			float invMass = m_vel[m_currentRead][index + 3];

			force[0] = m_force[index + 0];
			force[1] = m_force[index + 1];
			force[2] = m_force[index + 2];

			// acceleration = force / mass;
			// new velocity = old velocity + acceleration * deltaTime
			vel[0] += (force[0] * invMass) * deltaTime;
			vel[1] += (force[1] * invMass) * deltaTime;
			vel[2] += (force[2] * invMass) * deltaTime;

			vel[0] *= m_damping;
			vel[1] *= m_damping;
			vel[2] *= m_damping;

			// new position = old position + velocity * deltaTime
			pos[0] += vel[0] * deltaTime;
			pos[1] += vel[1] * deltaTime;
			pos[2] += vel[2] * deltaTime;

			m_pos[m_currentWrite][index + 0] = pos[0];
			m_pos[m_currentWrite][index + 1] = pos[1];
			m_pos[m_currentWrite][index + 2] = pos[2];
			m_pos[m_currentWrite][index + 3] = mass;

			m_vel[m_currentWrite][index + 0] = vel[0];
			m_vel[m_currentWrite][index + 1] = vel[1];
			m_vel[m_currentWrite][index + 2] = vel[2];
			m_vel[m_currentWrite][index + 3] = invMass;
		}

	}
}
