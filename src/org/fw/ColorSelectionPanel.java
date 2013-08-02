package org.fw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ColorSelectionPanel extends JPanel{
	private static final long serialVersionUID = 5248208700393318841L;
	
	@SuppressWarnings("static-access")
	public ColorSelectionPanel() {
	        GridBagLayout gbl = new GridBagLayout();
	        GridBagConstraints c = new GridBagConstraints();
	        setLayout(gbl);
	        
	        ActionListener color_listener = new ActionListener() {
	            public void actionPerformed(ActionEvent evt) {
	            
	                selectColor(((JButton)evt.getSource()).getBackground());
	            }
	        };
	        
	        Color[] colors = new Color[12];
	        colors[0] = Color.white;
	        colors[1] = Color.black;
	        
	        colors[2] = Color.blue;
	        colors[3] = Color.cyan;
	        colors[4] = Color.gray;
	        colors[5] = Color.green;
	        colors[6] = Color.lightGray;
	        colors[7] = Color.magenta;
	        colors[8] = Color.orange;
	        colors[9] = Color.pink;
	        colors[10] = Color.red;
	        colors[11] = Color.yellow;
	        
	        c.gridheight = 1;
	        c.gridwidth = 1;
	        c.fill = c.NONE;
	        c.weightx = 1.0;
	        c.weighty = 1.0;
	        
	        for(int i=0; i<3; i++) {
	            for(int j=0; j<4; j++) {
	                c.gridx=j;
	                c.gridy=i;
	                
	                JButton button = new JButton("");
	                Dimension dim = new Dimension(15,15);
	                button.setSize(dim);
	                button.setPreferredSize(dim);
	                button.setMinimumSize(dim);
	                button.setBackground(colors[j+i*4]);
	                gbl.setConstraints(button,c);
	                add(button);
	                button.addActionListener(color_listener);
	            }
	        }
	        
	    }
	    
	    protected Color selectedColor = Color.black;
	    public void selectColor(Color newColor) {
	        Color oldColor = selectedColor;
	        selectedColor = newColor;
	        firePropertyChange("selectedColor",oldColor, newColor);
	    }
}
