package org.fw.test;

import java.awt.*;
import java.applet.Applet;

import javax.swing.JPanel;

public class TestGridBagLayout extends Applet {

	private static final long serialVersionUID = 6293348226244349825L;

	protected void makebutton(String name, GridBagLayout gridbag,
			GridBagConstraints c) {
		Button button = new Button(name);
		gridbag.setConstraints(button, c);
		add(button);
	}

	public void init() {
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();

		setFont(new Font("SansSerif", Font.PLAIN, 14));
		setLayout(gridbag);

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		makebutton("Button1", gridbag, c);
		makebutton("Button2", gridbag, c);
		makebutton("Button3", gridbag, c);

		c.gridwidth = GridBagConstraints.REMAINDER; // end row
		makebutton("Button4", gridbag, c);

		c.weightx = 0.0; // reset to the default
		makebutton("Button5", gridbag, c); // another row

		c.gridwidth = GridBagConstraints.RELATIVE; // next-to-last in row
		makebutton("Button6", gridbag, c);

		c.gridwidth = GridBagConstraints.REMAINDER; // end row
		makebutton("Button7", gridbag, c);

		c.gridwidth = 1; // reset to the default
		c.gridheight = 2;
		c.weighty = 1.0;
		makebutton("Button8", gridbag, c);

		c.weighty = 0.0; // reset to the default
		c.gridwidth = GridBagConstraints.REMAINDER; // end row
		c.gridheight = 1; // reset to the default
		makebutton("Button9", gridbag, c);
		makebutton("Button10", gridbag, c);

		setSize(300, 100);
	}

	public static void main(String args[]) {
		Frame f = new Frame("GridBag Layout Example");
		
		TestGridBagLayout ex1 = new TestGridBagLayout();
		
		ex1.init();
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(ex1, BorderLayout.CENTER);
		f.add(p);
		f.pack();
		f.setSize(f.getPreferredSize());
		f.setVisible(true);
	}

}
