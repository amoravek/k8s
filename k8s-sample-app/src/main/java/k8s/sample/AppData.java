package k8s.sample;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public final class AppData {

	public static final AtomicBoolean alive = new AtomicBoolean(true);
	public static final AtomicBoolean ready = new AtomicBoolean(true);
	public static final AtomicLong unreadyAt = new AtomicLong(0);
	
	private static final String SECRET_DATA_FILE_DEFAULT = "/vault/secrets/secret-data.txt";
	private static final String SECRET_DATA_FILE = System.getenv("SECRET_DATA_FILE") != null ? System.getenv("SECRET_DATA_FILE") : SECRET_DATA_FILE_DEFAULT;
	private static final String SECRETS_FROM_ENV = System.getenv("DEMO_USER") + ":" + System.getenv("DEMO_PASSWORD");

	private static final String readFileContents(String fileName) throws IOException {
		File f = new File(fileName);

		if (!f.exists()) {
			return "-no secret-";
		}
		
		Path path = Paths.get(fileName);
		byte[] bytes = Files.readAllBytes(path);
		
		return new String(bytes).trim();
	}

	public static final String print() {
		String fileContents = null;
		
		try {
			fileContents = readFileContents(SECRET_DATA_FILE);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return "From ENV: " + SECRETS_FROM_ENV + ", from file: " + SECRET_DATA_FILE + ": '" + fileContents + "', alive: " + alive.toString() + ", ready: " + ready.toString();
	}

}
