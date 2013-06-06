package net.floodlightcontrollerprintln;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class Restapistartup extends ServerResource {	
@Get("json")
public void start(String postData) {

	netfloodlightcontrollerprintln.pushFLows();
	netfloodlightcontrollerprintln.printlnout();
}

}
