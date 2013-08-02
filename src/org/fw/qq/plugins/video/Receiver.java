package org.fw.qq.plugins.video;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.RealizeCompleteEvent;
import javax.media.control.BufferControl;
import javax.media.protocol.DataSource;
import javax.media.rtp.InvalidSessionAddressException;
import javax.media.rtp.RTPManager;
import javax.media.rtp.ReceiveStream;
import javax.media.rtp.ReceiveStreamListener;
import javax.media.rtp.SessionAddress;
import javax.media.rtp.event.ByeEvent;
import javax.media.rtp.event.NewReceiveStreamEvent;
import javax.media.rtp.event.ReceiveStreamEvent;
import javax.swing.JFrame;

public class Receiver extends JFrame implements ReceiveStreamListener,ControllerListener{
	
	public static void main(String[] args) {
		try {
			SessionAddress local = new SessionAddress(InetAddress.getLocalHost(),60000);
			SessionAddress target = new SessionAddress(InetAddress.getLocalHost(),50000);		
			new Receiver(local,target);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}		
	}
	
	public Receiver(SessionAddress local, SessionAddress target) {
		mgr = RTPManager.newInstance();
		mgr.addReceiveStreamListener(this);
		try {
			mgr.initialize(local);
			mgr.addTarget(target);
		} catch ( IOException e) {
			e.printStackTrace();
		} catch (InvalidSessionAddressException e) {
			e.printStackTrace();
		}
		BufferControl bc = (BufferControl)mgr.getControl("javax.media.control.BufferControl");
        if (bc != null)
            bc.setBufferLength(350);
        addWindowListener(new WindowAdapter() {
        	@Override
        	public void windowClosing(WindowEvent e) {
        		disconnect();
        	}
		});
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
	}
	@Override
	public void update(ReceiveStreamEvent e) {
		if(e instanceof NewReceiveStreamEvent) {
			ReceiveStream rs = ((NewReceiveStreamEvent)e).getReceiveStream();
			DataSource ds = rs.getDataSource();
			try {
				player = Manager.createPlayer(ds);
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (NoPlayerException e2) {
				e2.printStackTrace();
			}
			player.addControllerListener(this);
			player.start();			
		} else if(e instanceof ByeEvent) {
			disconnect();
		}
	}
	public void disconnect() {
		if(player!=null) {
			player.stop();
			player.close();
		}
		if(mgr!=null) {
			mgr.removeTargets("closing session");
			mgr.dispose();
			mgr = null;
		}		
	}
	@Override
	public void controllerUpdate(ControllerEvent e) {
		if(e instanceof RealizeCompleteEvent) {
			if(player.getVisualComponent()!=null)
				add(player.getVisualComponent());
			if(player.getControlPanelComponent()!=null)
				add(player.getControlPanelComponent(),BorderLayout.SOUTH);
			pack();
		}
	}
	Player player;
	RTPManager mgr;
}
