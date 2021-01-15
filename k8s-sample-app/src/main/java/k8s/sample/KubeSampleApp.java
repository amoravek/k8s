package k8s.sample;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import k8s.sample.health.AppHealthCheck;
import k8s.sample.resources.LivenessResource;
import k8s.sample.resources.MainResource;
import k8s.sample.resources.ReadinessResource;

public class KubeSampleApp extends Application<HelloWorldConfiguration> {
	public static void main(String[] args) throws Exception {
		new KubeSampleApp().run(args);
	}

	@Override
	public String getName() {
		return "hello-world";
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
	}

}