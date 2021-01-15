package k8s.sample.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.codahale.metrics.annotation.Timed;

import k8s.sample.AppData;
import k8s.sample.api.OutData;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class MainResource {

	public MainResource() {
	}

	@GET
	@Timed
	public Response main() {
		return Response.ok(new OutData("app-output", AppData.print())).build();
	}

}