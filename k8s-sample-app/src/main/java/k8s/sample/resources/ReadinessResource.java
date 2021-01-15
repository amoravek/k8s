package k8s.sample.resources;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.codahale.metrics.annotation.Timed;

import k8s.sample.AppData;
import k8s.sample.api.OutData;

@Path("/ready")
@Produces(MediaType.APPLICATION_JSON)
public class ReadinessResource {

	public ReadinessResource() {
	}

	@GET
	@Timed
	public Response isReady() {
		if (AppData.ready.get()) {
			return Response.ok(new OutData("readiness-check", AppData.ready.toString())).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@GET
	@Timed
	@Path("/false")
	public Response setFalse() {
		AppData.ready.set(false);

		try {
			return Response.temporaryRedirect(new URI("/ready")).build();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	@GET
	@Timed
	@Path("/true")
	public Response setTrue() {
		AppData.ready.set(true);

		try {
			return Response.temporaryRedirect(new URI("/ready")).build();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

}