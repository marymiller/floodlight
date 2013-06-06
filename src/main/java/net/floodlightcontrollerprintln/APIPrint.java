package net.floodlightcontrollerprintln;

import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import net.floodlightcontroller.restserver.RestletRoutable;

public class APIPrint implements RestletRoutable{
	
	@Override
	public Restlet getRestlet(Context context) {
		Router router = new Router(context);
        router.attach("/start/", Restapistartup.class); // G
        router.attachDefault(Restapistartup.class);
        return router;
	}

	@Override
	public String basePath() {
		return "/netfloodlightcontrollerprintln";
	}	
}

