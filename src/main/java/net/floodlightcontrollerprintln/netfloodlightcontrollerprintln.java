package net.floodlightcontrollerprintln;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.openflow.protocol.OFMessage;
import org.openflow.protocol.OFType;


import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IFloodlightProviderService;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.restserver.IRestApiService;


public class netfloodlightcontrollerprintln implements IFloodlightModule,
		IOFMessageListener {
	
	protected static IFloodlightProviderService floodlightProvider;
	protected IRestApiService restApi;

	
	public static void pushFLows() {
	     Map<Long, IOFSwitch> activeswitches = floodlightProvider.getSwitches();
	     String currentswitch = "";
	    
	    for(int x = 1; x <= activeswitches.size(); x++)
	     {
	    	try{
	    		currentswitch = (activeswitches.get(new Long(x)).toString());
			     	
	    	} catch(NullPointerException e){
	    		continue; 
	    	}
	    	int index = currentswitch.indexOf("DPID");
		     System.out.println(index);
		     String dpid = currentswitch.substring(index + 5, index + 28);
		     System.out.println(dpid);
	    	 
	     }
	    System.out.println(activeswitches);
	    System.out.println("HEllo");
	    
	    }
	@Override
	public String getName() {
		return "API println";
	}

	@Override
	public boolean isCallbackOrderingPrereq(OFType type, String name) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public static void printlnout(){
		System.out.println("");
	}

	@Override
	public boolean isCallbackOrderingPostreq(OFType type, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public net.floodlightcontroller.core.IListener.Command receive(
			IOFSwitch sw, OFMessage msg, FloodlightContext cntx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleServices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Class<? extends IFloodlightService>, IFloodlightService> getServiceImpls() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleDependencies() {
		Collection<Class<? extends IFloodlightService>> l =
		        new ArrayList<Class<? extends IFloodlightService>>();
		    l.add(IFloodlightProviderService.class);
		    return l;
	}

	@Override
	public void init(FloodlightModuleContext context)
			throws FloodlightModuleException {
		restApi = context.getServiceImpl(IRestApiService.class);
		floodlightProvider = context.getServiceImpl(IFloodlightProviderService.class);

	}

	@Override
	public void startUp(FloodlightModuleContext context)
			throws FloodlightModuleException {
		floodlightProvider.addOFMessageListener(OFType.PACKET_IN, this);
		restApi.addRestletRoutable(new APIPrint());
	}

}
