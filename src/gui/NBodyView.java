package gui;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import nbody.Planets;

/**
 * Class representing the view part of the application.
 * 
 * @author aricci
 * 
 */
public class NBodyView implements NBodySetListener {

    private MandelbrotFrame frame;

    public NBodyView(int w, int h) {
	frame = new MandelbrotFrame(this, w, h);
	frame.setVisible(true);
    }

    // TODO Ma perchè deve essere final????????
    public void setUpdated(final Planets set) {
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		frame.setPanel.updateImage(set.getPlanets());
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
    class MandelbrotFrame extends JFrame implements ActionListener {

	private JButton startButton;
	private JButton stopButton;
	private JButton pauseButton;
	private JButton stepButton;
	private JTextField cx;
	private JTextField cy;
	private JTextField diam;
	private JTextField state;
	private NBodyPanel setPanel;
	private NBodyView view;

	public MandelbrotFrame(NBodyView view, int w, int h) {
	    super("Mandelbrot Viewer");
	    setSize(w, h);

	    this.view = view;
	    cx = new JTextField(10);
	    cy = new JTextField(10);
	    diam = new JTextField(10);

	    cx.setText("0");
	    cy.setText("0");
	    diam.setText("4");

	    startButton = new JButton("start");
	    stopButton = new JButton("stop");
	    pauseButton = new JButton("pause");
	    stepButton = new JButton("step");

	    startButton.setEnabled(true);
	    stopButton.setEnabled(false);
	    pauseButton.setEnabled(false);
	    stepButton.setEnabled(true);

	    JPanel controlPanel = new JPanel();
	    controlPanel.add(new JLabel("center "));
	    controlPanel.add(cx);
	    controlPanel.add(cy);
	    controlPanel.add(new JLabel("diam."));
	    controlPanel.add(diam);
	    controlPanel.add(startButton);
	    controlPanel.add(stopButton);
	    controlPanel.add(pauseButton);
	    controlPanel.add(stepButton);

	    setPanel = new NBodyPanel(w, h);
	    setPanel.setSize(w, h);

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
	    setContentPane(cp);
	    setResizable(false);

	    startButton.addActionListener(this);
	    stopButton.addActionListener(this);
	    pauseButton.addActionListener(this);
	    stepButton.addActionListener(this);

	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent ev) {
	    String cmd = ev.getActionCommand();
	    if (cmd.equals("start")) {
		startButton.setEnabled(false);
		stopButton.setEnabled(true);
		pauseButton.setEnabled(true);
		stepButton.setEnabled(false);
		notifyStarted();
	    } else if (cmd.equals("stop")) {
		startButton.setEnabled(true);
		stopButton.setEnabled(false);
		pauseButton.setEnabled(false);
		stepButton.setEnabled(false);
		notifyStopped();
	    } else if (cmd.equals("pause")) {
		startButton.setEnabled(true);
		stopButton.setEnabled(true);
		pauseButton.setEnabled(false);
		stepButton.setEnabled(true);
		notifyPaused();
	    } else if (cmd.equals("step")) {
		startButton.setEnabled(true);
		stopButton.setEnabled(true);
		pauseButton.setEnabled(false);
		stepButton.setEnabled(false);
		notifySingleStep();
	    }
	}

	private void notifyStarted() {
	    // Complex c0 = new Complex(Double.parseDouble(cx.getText()),
	    // Double.parseDouble(cy.getText()));
	    // double d = Double.parseDouble(diam.getText());
	    // Event ev = new StartedEvent(c0, d, view);
	    // view.notifyEvent(ev);
	}

	private void notifyStopped() {
	    // Event ev = new StoppedEvent(view);
	    // view.notifyEvent(ev);
	}

	private void notifyPaused() {
	}

	private void notifySingleStep() {
	}

    }
}
