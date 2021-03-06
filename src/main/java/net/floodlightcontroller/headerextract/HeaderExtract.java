package net.floodlightcontroller.headerextract;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import org.openflow.protocol.OFMatch;
import org.openflow.protocol.OFMessage;
import org.openflow.protocol.OFPacketIn;
import org.openflow.protocol.OFType;
import org.openflow.util.HexString;
import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IFloodlightProviderService;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.packet.Ethernet;
import net.floodlightcontroller.packet.IPv4;
	
public class HeaderExtract implements IFloodlightModule, IOFMessageListener {
	public final int DEFAULT_CACHE_SIZE = 10;
	protected IFloodlightProviderService floodlightProvider;
	
	@Override
	public String getName() {
		return "Names";
	}
	
	@Override
	public boolean isCallbackOrderingPrereq(OFType type, String name) {
		return false;
	}
	
	@Override
	public boolean isCallbackOrderingPostreq(OFType type, String name) {
		return false;
	}
	
	//This is where we pull fields from the packet-in
	@Override
	public Command receive(IOFSwitch sw, OFMessage msg, FloodlightContext cntx) {
		//Instantiate two objects for OFMatch and OFPacketIn
		OFPacketIn pin = (OFPacketIn) msg;
		OFMatch match = new OFMatch();
		match.loadFromPacket(pin.getPacketData(), pin.getInPort());
		//Destination IP Address for each packet-in
		System.out.println("$$$$$-Get the Destination IP Address-$$$$$");
		System.out.println(IPv4.fromIPv4Address(match.getNetworkDestination()));
		// Source MAC Address for each packet-in
		System.out.println("$$$$$-Mac Address Destination-$$$$$$");
		Long sourceMACHash = Ethernet.toLong(match.getDataLayerDestination());
		System.out.println(HexString.toHexString(sourceMACHash));
		/**
		//time that the transactions have taken
		System.out.println((sw.getNextTransactionId()));
		//attributes of switch
		System.out.println("$$$$$$-Switch Attributes-$$$$$");
		System.out.println((sw.getAttributes()));
		//date and time of all methods
		System.out.println("$$$$$$-Date-$$$$$");
		System.out.println((sw.getConnectedSince()));
		//the identification of the transaction
		System.out.println("$$$$$$-Transaction ID-$$$$$");
		System.out.println((sw.getNextTransactionId()));
		//the data path id 
		System.out.println("$$$$$$-Data Path ID-$$$$$");
		System.out.println((sw.getId()));
		//Here we print the entire packet-in array which has all matchable fields
		System.out.println("$$$$$-PacketIn ARRAY-$$$$$");
		System.out.println(Arrays.asList(match));
		**/
		return Command.CONTINUE;
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
		Collection<Class<? extends IFloodlightService>> l = new ArrayList<Class<? extends IFloodlightService>>();
		l.add(IFloodlightProviderService.class);
		return l;
	}
	 
	@Override
	public void init(FloodlightModuleContext context) throws FloodlightModuleException {
		floodlightProvider = context.getServiceImpl(IFloodlightProviderService.class);
	}
	@Override
	public void startUp(FloodlightModuleContext context) {
		floodlightProvider.addOFMessageListener(OFType.PACKET_IN, this);
	}
}