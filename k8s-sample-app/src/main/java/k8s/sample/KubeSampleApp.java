package k8s.sample;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import k8s.sample.health.AppHealthCheck;
import k8s.sample.resources.LivenessResource;
import k8s.sample.resources.MainResource;
import k8s.sample.resources.ReadinessResource;

public class KubeSampleApp extends Application<HelloWorldConfiguration> {
	private static final String STARTUP_DELAY_ENV = System.getenv("STARTUP_DELAY");
	private static final long STARTUP_DELAY = Long.parseLong(STARTUP_DELAY_ENV != null ? STARTUP_DELAY_ENV : "0");

	public static void main(String[] args) throws Exception {
		new KubeSampleApp().run(args);
	}

	@Override
	public String getName() {
		return "KubeSampleApp";
	}

	@Override
	public void initialize(Bootstrap<HelloWorldConfiguration> bootstrap) {
		// nothing to do yet
	}

	@Override
	public void run(HelloWorldConfiguration configuration, Environment environment) {
		final LivenessResource livenessResource = new LivenessResource();
		final ReadinessResource readinessResource = new ReadinessResource();
		final MainResource mainResource = new MainResource();

		environment.jersey().register(livenessResource);
		environment.jersey().register(readinessResource);
		environment.jersey().register(mainResource);

		final AppHealthCheck healthCheck = new AppHealthCheck();
		environment.healthChecks().register("k8s-sample-app-hc", healthCheck);

		try {
			System.out.println("Applying startup delay: " + STARTUP_DELAY + " ms");
			Thread.sleep(STARTUP_DELAY);
		} catch (InterruptedException e) {
		}
	}

}