package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nbody.ObservableComponent;
import nbody.PlanetsMap;
import nbody.event.DeltaTimeEvent;
import nbody.event.Event;
import nbody.event.PausedEvent;
import nbody.event.RandomizeEvent;
import nbody.event.SingleStepEvent;
import nbody.event.StartedEvent;
import nbody.event.StoppedEvent;

/**
 * Class representing the view part of the application.
 * 
 * @author aricci
 * 
 */
public class NBodyView extends ObservableComponent implements NBodySetListener {

    private NBodyFrame frame;
    private ArrayBlockingQueue<PlanetsMap> coda;

    /**
     * Costruisce una finestra che ha le dimensioni di disegno pari ai due
     * paramentri più le dimensioni dei menù.
     * */
    public NBodyView(int w, int h, ArrayBlockingQueue<PlanetsMap> coda) {
	frame = new NBodyFrame(this, w, h);
	frame.setVisible(true);
	this.coda = coda;
    }

    // TODO Ma perchè deve essere final????????
    public void setUpdated(final PlanetsMap map) {
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {

		try {
		    frame.setPanel.updateImage(coda.take());
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }
	});
    }

    public void setProcessingState() {
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		frame.state.setText("Processing...");
	    }
	});
    }

    public void setCompletedState(final long dt) {
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		frame.state.setText("Terminated - Time elapsed: " + dt);
		frame.startButton.setEnabled(true);
		frame.stopButton.setEnabled(false);
	    }
	});
    }

    public void setInterruptedState(final long dt) {
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		frame.state.setText("Interrupted - Time elapsed: " + dt);
		frame.startButton.setEnabled(true);
		frame.stopButton.setEnabled(false);
	    }
	});
    }

    @SuppressWarnings("serial")
    class FloatJSlider extends JSlider {

	final int scale;

	public FloatJSlider(int min, int max, int value, int scale) {
	    super(min, max, value);
	    this.scale = scale;
	}

	public FloatJSlider(int min, int max, float value, int scale) {
	    super(min, max, (int) (value * max));
	    this.scale = scale;
	}

	public float getScaledValue() {
	    return ((float) super.getValue()) / this.scale;
	}
    }

    @SuppressWarnings("serial")
    class NBodyFrame extends JFrame implements ActionListener, ChangeListener {

	private JButton randomizeButton;
	private JButton startButton;
	private JButton stopButton;
	private JButton pauseButton;
	private JButton stepButton;
	private JTextField numBodies;
	private JTextField state;
	private FloatJSlider deltaTimeSlider;
	private NBodyPanel setPanel;
	private NBodyView view;

	public NBodyFrame(NBodyView view, int w, int h) {
	    super("NBody Viewer");
	    // setSize(w, h);

	    this.view = view;

	    numBodies = new JTextField(10);
	    numBodies.setText("100");

	    randomizeButton = new JButton("randomize");

	    startButton = new JButton("start");
	    stopButton = new JButton("stop");
	    pauseButton = new JButton("pause");
	    stepButton = new JButton("step");

	    System.getProperty("deltaTime", "0.01");
	    // The interval between 0 and 1 in 100 steps.
	    deltaTimeSlider = new FloatJSlider(0, 100, 0.5f, 100);
	    deltaTimeSlider.setOrientation(SwingConstants.VERTICAL);

	    startButton.setEnabled(true);
	    stopButton.setEnabled(false);
	    pauseButton.setEnabled(false);
	    stepButton.setEnabled(false);

	    JPanel controlPanel = new JPanel();
	    controlPanel.add(new JLabel("Num Bodies"));
	    controlPanel.add(numBodies);

	    // Uso le lable solo per creare un po' di spazio
	    controlPanel.add(new JLabel(" "));
	    controlPanel.add(randomizeButton);
	    controlPanel.add(new JLabel("  "));

	    controlPanel.add(startButton);
	    controlPanel.add(stopButton);
	    controlPanel.add(pauseButton);
	    controlPanel.add(stepButton);

	    setPanel = new NBodyPanel(w, h);
	    setPanel.setPreferredSize(new Dimension(w, h));

	    JPanel infoPanel = new JPanel();
	    state = new JTextField(20);
	    state.setText("Idle");
	    state.setEditable(false);
	    infoPanel.add(new JLabel("State"));
	    infoPanel.add(state);
	    JPanel cp = new JPanel();
	    LayoutManager layout = new BorderLayout();
	    cp.setLayout(layout);
	    cp.add(BorderLayout.NORTH, controlPanel);
	    cp.add(BorderLayout.CENTER, setPanel);
	    cp.add(BorderLayout.SOUTH, infoPanel);
	    cp.add(BorderLayout.WEST, deltaTimeSlider);
	    setContentPane(cp);
	    pack();
	    setResizable(false);

	    randomizeButton.addActionListener(this);
	    startButton.addActionListener(this);
	    stopButton.addActionListener(this);
	    pauseButton.addActionListener(this);
	    stepButton.addActionListener(this);

	    deltaTimeSlider.addChangeListener(this);

	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent ev) {
	    String cmd = ev.getActionCommand();
	    if (cmd.equals("start")) {
		startButton.setEnabled(false);
		stopButton.setEnabled(true);
		pauseButton.setEnabled(true);
		stepButton.setEnabled(false);
		randomizeButton.setEnabled(false);
		notifyStarted();
	    } else if (cmd.equals("stop")) {
		startButton.setEnabled(true);
		stopButton.setEnabled(false);
		pauseButton.setEnabled(false);
		stepButton.setEnabled(false);
		randomizeButton.setEnabled(true);
		notifyStopped();
	    } else if (cmd.equals("pause")) {
		startButton.setEnabled(true);
		stopButton.setEnabled(true);
		pauseButton.setEnabled(false);
		stepButton.setEnabled(true);
		randomizeButton.setEnabled(false);
		notifyPaused();
	    } else if (cmd.equals("step")) {
		startButton.setEnabled(true);
		stopButton.setEnabled(true);
		pauseButton.setEnabled(false);
		stepButton.setEnabled(false);
		randomizeButton.setEnabled(false);
		notifySingleStep();
	    } else if (cmd.equals("randomize")) {
		startButton.setEnabled(true);
		stopButton.setEnabled(false);
		pauseButton.setEnabled(false);
		stepButton.setEnabled(false);
		notifyRandomize();
	    }
	}

	public void stateChanged(ChangeEvent e) {
	    notifyDeltaTimeChanced();
	}

	private void notifyDeltaTimeChanced() {
	    float deltaTime = deltaTimeSlider.getScaledValue();
	    Event ev = new DeltaTimeEvent(view, deltaTime);
	    view.notifyEvent(ev);

	}

	private void notifyStarted() {
	    Event ev = new StartedEvent(view);
	    view.notifyEvent(ev);
	}

	private void notifyStopped() {
	    Event ev = new StoppedEvent(view);
	    view.notifyEvent(ev);
	}

	private void notifyPaused() {
	    Event ev = new PausedEvent(view);
	    view.notifyEvent(ev);
	}

	private void notifySingleStep() {
	    Event ev = new SingleStepEvent(view);
	    view.notifyEvent(ev);
	}

	private void notifyRandomize() {
	    int nb = Integer.parseInt(numBodies.getText());
	    Event ev = new RandomizeEvent(view, nb);
	    view.notifyEvent(ev);
	}
    }
}
