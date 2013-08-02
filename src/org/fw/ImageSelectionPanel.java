package org.fw;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.fw.image.ReSizeImageIcon;
import org.fw.utils.Config;
import org.fw.utils.ImageHelper;
import org.fw.utils.ProjectPath;

public class ImageSelectionPanel extends JPanel {

	private static final long serialVersionUID = -1096462653865584898L;

	protected Config skin = new Config(ProjectPath.getProjectPath()
			+ "cfg/QQMainFrameUI.properties");
	protected Config basic = new Config(ProjectPath.getProjectPath()
			+ "cfg/basic.properties");

	protected Image selectedSkin;// 原始皮肤
	protected FramePanel panel;// 要应用皮肤的组件
	protected boolean isSelected;// 是否应用了皮肤
	protected ImageHelper imageHelper;
	
	public ImageSelectionPanel() {
		imageHelper =new ImageHelper();
		selectedSkin = imageHelper.getFWImageIcon(basic.getProperty(
				"BackgroundImage", "skin/default/bg.jpg")).getImage();// 原始皮肤
	}

	@SuppressWarnings("static-access")
	public ImageSelectionPanel(FramePanel panel) {
		isSelected = false;
		this.panel = panel;
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		setLayout(gbl);

		int count = Integer.parseInt(skin.getProperty(
				"ImageSelectionPanel.Count", "14"));
		ImageIcon[] icons = new ImageIcon[count];
		String[] tips = new String[count];
		int row, col = 7;
		if (count > 7) {
			row = count % col == 0 ? count / col : count / col + 1;
		} else {
			row = 1;
		}
		String path = "";
		for (int i = 0; i < count; i++) {
			path = skin.getProperty("ImageSelectionPanel.Img_" + (i + 1),
					"skin/default/bg.jpg");
			icons[i] = new ReSizeImageIcon(path).getReSizeImageIcon(35, 55);
			tips[i] = skin.getProperty("ImageSelectionPanel.Tip_" + (i + 1),
					"默认皮肤");
		}

		c.gridheight = 1;
		c.gridwidth = 1;
		c.fill = c.NONE;
		c.weightx = 1.0;
		c.weighty = 1.0;

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				c.gridx = j;
				c.gridy = i;

				JButton button = new JButton("");
				Dimension dim = new Dimension(35, 55);
				button.setSize(dim);
				button.setPreferredSize(dim);
				button.setMinimumSize(dim);
				if (j + i * 7 < count) {
					button.setIcon(icons[j + i * 7]);
					button.setToolTipText(tips[j + i * 7]);

					gbl.setConstraints(button, c);
					add(button);
					button.addMouseListener(new MouseListener() {

						@Override
						public void mouseClicked(MouseEvent arg0) {
							isSelected = true;
							selectSkinImage((((JButton) arg0.getSource())
									.getIcon()));
							basic.savePropertie("BackgroundImage",
									(((JButton) arg0.getSource()).getIcon())
											.toString());
						}

						@Override
						public void mouseEntered(MouseEvent arg0) {
							selectSkinImage((((JButton) arg0.getSource())
									.getIcon()));
						}

						@Override
						public void mouseExited(MouseEvent arg0) {
//							if(!isSelected)
//							selectSkinImage(new ImageIcon(selectedSkin));
						}

						@Override
						public void mousePressed(MouseEvent arg0) {

						}

						@Override
						public void mouseReleased(MouseEvent arg0) {

						}

					});
				}
			}
		}

	}

	public void selectSkinImage(Icon icon) {
		// Image oldSkin = selectedSkin;
		// System.out.println("oldSkin"+oldSkin);
		// selectedSkin = ((ImageIcon)icon).getImage();
		Image bgImage = new ImageIcon(icon.toString()).getImage();
		// firePropertyChange("selectedSkin",oldSkin, ((ImageIcon)icon));
		panel.setBackground(bgImage);
	}

}
