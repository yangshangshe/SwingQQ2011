package org.fw.qq.plugins.video;

import javax.media.ConfigureCompleteEvent;
import javax.media.ControllerClosedEvent;
import javax.media.ControllerErrorEvent;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.EndOfMediaEvent;
import javax.media.Player;
import javax.media.PrefetchCompleteEvent;
import javax.media.Processor;
import javax.media.RealizeCompleteEvent;

public class StateHelper implements ControllerListener {

	public StateHelper(Player p) {
		player = p;
		p.addControllerListener(this);
	}

	public boolean configure(int timeOutMillis) {
		long startTime = System.currentTimeMillis();
		synchronized (this) {
			if (player instanceof Processor)
				((Processor) player).configure();
			else
				return false;
			while (!configured && !failed) {
				try {
					wait(timeOutMillis);
				} catch (InterruptedException e) {
					// TODO: handle exception
				}
				if (System.currentTimeMillis() - startTime > timeOutMillis)
					break;
			}
		}
		return configured;
	}

	public boolean prefetch(int timeOutMillis) {
		long startTime = System.currentTimeMillis();
		synchronized (this) {
			player.prefetch();
			while (!prefetched && !failed) {
				try {
					wait(timeOutMillis);
				} catch (InterruptedException e) {
					// TODO: handle exception
				}
				if (System.currentTimeMillis() - startTime > timeOutMillis)
					break;
			}
		}
		return prefetched && !failed;
	}

	public boolean realize(int timeOutMillis) {
		long startTime = System.currentTimeMillis();
		synchronized (this) {
			player.realize();
			while (!realized && !failed) {
				try {
					wait(timeOutMillis);
				} catch (InterruptedException e) {
					// TODO: handle exception
				}
				if (System.currentTimeMillis() - startTime > timeOutMillis)
					break;
			}
		}
		return realized;
	}

	public boolean playToEndOfMedia(int timeOutMillis) {
		long startTime = System.currentTimeMillis();
		eom = false;
		synchronized (this) {
			player.start();
			while (!eom && !failed) {
				try {
					wait(timeOutMillis);
				} catch (InterruptedException e) {
					// TODO: handle exception
				}
				if (System.currentTimeMillis() - startTime > timeOutMillis)
					break;
			}
		}
		return eom && !failed;
	}

	public void close() {
		synchronized (this) {
			player.close();
			while (!closed) {
				try {
					wait(100);
				} catch (InterruptedException e) {
					// TODO: handle exception
				}
			}
		}
		player.removeControllerListener(this);
	}

	@Override
	public synchronized void controllerUpdate(ControllerEvent e) {
		if (e instanceof RealizeCompleteEvent) {
			realized = true;
		} else if (e instanceof ConfigureCompleteEvent) {
			configured = true;
		} else if (e instanceof PrefetchCompleteEvent) {
			prefetched = true;
		} else if (e instanceof EndOfMediaEvent) {
			eom = true;
		} else if (e instanceof ControllerErrorEvent) {
			failed = true;
		} else if (e instanceof ControllerClosedEvent) {
			closed = true;
		} else {
			return;
		}
		notifyAll();
	}

	boolean configured = false;
	boolean realized = false;
	boolean prefetched = false;
	boolean closed = false;
	boolean failed = false;
	boolean eom = false;

	Player player = null;
}
