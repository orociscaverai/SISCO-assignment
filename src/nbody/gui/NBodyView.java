package nbody.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

import nbody.BodiesMap;
import nbody.event.Event;
import nbody.event.ChangeParamEvent;
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

    /**
     * Costruisce una finestra che ha le dimensioni di disegno pari ai due
     * paramentri più le dimensioni dei menù.
     * */
    public NBodyView(int w, int h) {
	frame = new NBodyFrame(this, w, h);
	frame.setVisible(true);
    }

    // TODO Perchè deve essere final????????
    public void setUpdated(final BodiesMap map) {
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		frame.setPanel.updateImage(map);
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

    public void setParameter(final float deltaTime, final float softFactor) {
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		frame.deltaTimeSlider.setScaledValue(deltaTime);
		frame.softFactorSlider.setScaledValue(softFactor);
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

	public FloatJSlider(float min, float max, float value, int scale) {
	    super((int) (min * scale), (int) (max * scale),
		    (int) (value * scale));
	    this.scale = scale;
	}

	public float getScaledValue() {
	    return ((float) super.getValue()) / this.scale;
	}

	public void setScaledValue(float value) {
	    super.setValue((int) (value * scale));
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
	private FloatJSlider softFactorSlider;
	private JSlider zoomSlider;
	private NBodyPanel setPanel;
	private NBodyView view;

	public NBodyFrame(NBodyView view, int w, int h) {
	    super("NBody Viewer");

	    this.view = view;

	    numBodies = new JTextField(10);
	    numBodies.setText("100");

	    randomizeButton = new JButton("randomize");

	    startButton = new JButton("start");
	    stopButton = new JButton("stop");
	    pauseButton = new JButton("pause");
	    stepButton = new JButton("step");

	    randomizeButton.setEnabled(true);
	    startButton.setEnabled(false);
	    stopButton.setEnabled(false);
	    pauseButton.setEnabled(false);
	    stepButton.setEnabled(false);

	    deltaTimeSlider = new FloatJSlider(0.01f, 1f, 0.5f, 100);
	    deltaTimeSlider.setOrientation(SwingConstants.HORIZONTAL);

	    softFactorSlider = new FloatJSlider(0.01f, 1f, 0.5f, 100);
	    softFactorSlider.setOrientation(SwingConstants.HORIZONTAL);

	    zoomSlider = new JSlider(1, 4, 2);
	    zoomSlider.setOrientation(SwingConstants.HORIZONTAL);

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
	    setPanel.setZoom(zoomSlider.getValue());

	    state = new JTextField(20);
	    state.setText("Idle");
	    state.setEditable(false);

	    JPanel cp = new JPanel(new BorderLayout());
	    cp.add(BorderLayout.NORTH, controlPanel);
	    cp.add(BorderLayout.CENTER, setPanel);

	    // EastPanel ------------------------------------------
	    JPanel eastPanel = new JPanel(new GridBagLayout());
	    GridBagConstraints c = new GridBagConstraints();

	    c.gridx = 0;
	    c.gridy = 0;
	    eastPanel.add(new JLabel("Delta Time:"), c);
	    c.gridy = 1;
	    eastPanel.add(deltaTimeSlider, c);
	    c.gridy = 2;
	    eastPanel.add(new JLabel("Soft Factor:"), c);
	    c.gridy = 3;
	    eastPanel.add(softFactorSlider, c);
	    c.gridy = 4;
	    eastPanel.add(new JLabel("Zoom:"), c);
	    c.gridy = 5;
	    eastPanel.add(zoomSlider, c);
	    c.gridy = 6;
	    eastPanel.add(new JLabel(" "), c);
	    c.gridy = 7;
	    eastPanel.add(new JLabel("State "), c);
	    c.gridy = 8;
	    eastPanel.add(state, c);

	    cp.add(BorderLayout.EAST, eastPanel);

	    // ------------------------------------------------------
	    setContentPane(cp);
	    pack();
	    setResizable(false);

	    randomizeButton.addActionListener(this);
	    startButton.addActionListener(this);
	    stopButton.addActionListener(this);
	    pauseButton.addActionListener(this);
	    stepButton.addActionListener(this);

	    deltaTimeSlider.addChangeListener(this);
	    softFactorSlider.addChangeListener(this);
	    zoomSlider.addChangeListener(this);

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
		startButton.setEnabled(false);
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
		stepButton.setEnabled(true);
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
	    Object source = e.getSource();
	    if (source == deltaTimeSlider || source == softFactorSlider) {
		notifyParameterChanged();
	    } else if (source == zoomSlider) {
		setPanel.setZoom(zoomSlider.getValue());
	    }
	}

	private void notifyParameterChanged() {
	    float deltaTime = deltaTimeSlider.getScaledValue();
	    float softFactor = softFactorSlider.getScaledValue();
	    Event ev = new ChangeParamEvent(view, deltaTime, softFactor);
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
