package k8s.sample;

import java.util.concurrent.atomic.AtomicBoolean;

public final class AppData {

	public static final AtomicBoolean alive = new AtomicBoolean(true);
	public static final AtomicBoolean ready = new AtomicBoolean(true);

	public static final String print() {
		return "alive: " + alive.toString() + ", ready: " + ready.toString();
	}

}
