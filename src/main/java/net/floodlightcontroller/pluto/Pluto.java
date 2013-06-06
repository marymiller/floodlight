//net.floodlightcontroller.names is a created package for something else, not part of FloodLight to make sure there isn't any confusion.
package net.floodlightcontroller.pluto;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.openflow.protocol.OFFlowMod;
import org.openflow.protocol.OFMatch;
import org.openflow.protocol.OFMessage;
import org.openflow.protocol.OFPacketIn;
import org.openflow.protocol.OFType;
import org.openflow.protocol.action.OFAction;
import org.openflow.protocol.action.OFActionOutput;
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
import net.floodlightcontroller.staticflowentry.IStaticFlowEntryPusherService;
//import net.floodlightcontroller.staticflowentry.StaticFlowEntryPusher;

public class Pluto implements IFloodlightModule, IOFMessageListener {
        protected IFloodlightProviderService floodlightProvider;
        protected IStaticFlowEntryPusherService staticFlowEntryPusher;
        protected static int count = 1;
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
        @Override
        public net.floodlightcontroller.core.IListener.Command receive(
                        IOFSwitch sw, OFMessage msg, FloodlightContext cntx ) {
                if (count == 3){
                        List<String> orderedFlows = new ArrayList<String>();
                        String dp = "00:00:00:00:00:00:00:01";
                        OFPacketIn pi = (OFPacketIn) msg;
                        OFMatch match = new OFMatch();
                        match.loadFromPacket(pi.getPacketData(), (short) 0);
                        System.out.println("$$$$$$$$$OFMATCH$$$$$$$$$$$");
                        System.out.println(match);
                        System.out.println("$$$$$$$$$IP-Destination$$$$$$$$$$$");
                        System.out.println("$$$$$$$$$IP-Destination$$$$$$$$$$$");
                        System.out.println(IPv4.fromIPv4Address(match.getNetworkDestination()));

                        for (int m = 0; m < 15000; m++)
                                orderedFlows.add(m, "a");

                        for (int i = 0; i < 15000; i++){
                                //IPv4-To
                                List<OFAction> actionsTo = new ArrayList<OFAction>();
                                // Declare the flow
                                OFFlowMod fmTo = new OFFlowMod();
                                fmTo.setType(OFType.FLOW_MOD);
                                // Declare the action
                                OFAction outputTo = new OFActionOutput((short) 9);
                                actionsTo.add(outputTo);
                                // Declare the match
                                OFMatch mTo = new OFMatch();
                                mTo.setNetworkDestination(IPv4.toIPv4Address("10.0.0.3"));
                                mTo.setDataLayerType(Ethernet.TYPE_IPv4);
                                fmTo.setActions(actionsTo);
                                fmTo.setMatch(mTo);
                                // Push the flow
                                Integer j = i;
                                staticFlowEntryPusher.addFlow(j.toString(), fmTo, dp);
                        }
                        //System.out.println(staticFlowEntryPusher.getFlows());
                        String flows = staticFlowEntryPusher.getFlows().toString();

                        int index1 = flows.indexOf("OFFlowMod", 0);
                        int index2 = flows.indexOf("OFFlowMod", index1 + 1);
                        int numLength;
                        int flowIndex;
                        String soloFlow;

                        while (index2 != -1){
                                numLength = 0;
                                for (int k = index1 - 2; Character.isDigit(flows.charAt(k)) && k >= 0; k--)
                                                numLength++;

                                soloFlow = flows.substring(index1 - (1 + numLength), index2 - 4);
                                flowIndex = Integer.parseInt(soloFlow.substring(0, numLength));
                                orderedFlows.set(flowIndex, flows.substring(index1 - (1 + numLength), index2 - 4));
                                index1 = flows.indexOf("OFFlowMod", index2 - 4);
                                index2 = flows.indexOf("OFFlowMod", index1 + 1);
                        }
                        numLength = 0;
                        for (int k = index1 - 2; Character.isDigit(flows.charAt(k)) && k >= 0; k--)
                                        numLength++;
                        soloFlow = flows.substring(index1 - (1 + numLength));
                        flowIndex = Integer.parseInt(soloFlow.substring(0, numLength));
                        orderedFlows.set(flowIndex, flows.substring(index1 - (1 + numLength)));
                        for (int n = 0; n < orderedFlows.size(); n++)
                                System.out.println(orderedFlows.get(n));

                        /*//flows match?
                        String newFlows = staticFlowEntryPusher.getFlows().toString();
                        index1 = newFlows.indexOf("OFFlowMod", 0);
                        index2 = newFlows.indexOf("OFFlowMod", index1 + 1);
                        boolean unChanged = true;
                        while (index2 != -1){
                                numLength = 0;
                                for (int k = index1 - 2; Character.isDigit(newFlows.charAt(k)) && k >= 0; k--)
                                                numLength++;

                                soloFlow = newFlows.substring(index1 - (1 + numLength), index2 - 4);
                                flowIndex = Integer.parseInt(soloFlow.substring(0, numLength));
                                if (flows.indexOf(newFlows.substring(index1 - (1 + numLength), index2 - 4)) == -1)
                                        unChanged = false;
                                index1 = newFlows.indexOf("OFFlowMod", index2 - 4);
                                index2 = newFlows.indexOf("OFFlowMod", index1 + 1);
                        }
                        numLength = 0;
                        for (int k = index1 - 2; Character.isDigit(newFlows.charAt(k)) && k >= 0; k--)
                                        numLength++;
                        soloFlow = newFlows.substring(index1 - (1 + numLength));
                        flowIndex = Integer.parseInt(soloFlow.substring(0, numLength));
                        //orderedFlows.set(flowIndex, flows.substring(index1 - (1 + numLength)));

                        System.out.println(unChanged);*/
                }

                /*System.out.println("-----Count------");
                System.out.println(count);*/
                count++;

                /*//IPv4-From
                List actionsFrom = new ArrayList();
                // Declare the flow
                OFFlowMod fmFrom = new OFFlowMod();
                fmFrom.setType(OFType.FLOW_MOD);
                // Declare the action
                OFAction outputFrom = new OFActionOutput((short) 1);
                actionsFrom.add(outputFrom);
                // Declare the match
                OFMatch mFrom = new OFMatch();
                mFrom.setNetworkDestination(IPv4.toIPv4Address("10.0.0.2"));
                mFrom.setDataLayerType(Ethernet.TYPE_IPv4);
                fmFrom.setActions(actionsFrom);
                fmFrom.setMatch(mFrom);
                // Push the flow
                staticFlowEntryPusher.addFlow("FlowFrom", fmFrom, dp);

                //ToArp
                List actionsToArp = new ArrayList();
                // Declare the flow
                OFFlowMod fmToArp = new OFFlowMod();
                fmToArp.setType(OFType.FLOW_MOD);
                // Declare the action
                OFAction outputToArp = new OFActionOutput((short) 2);
                actionsToArp.add(outputToArp);
                // Declare the match
                OFMatch mToArp = new OFMatch();
                mToArp.setNetworkDestination(IPv4.toIPv4Address("10.0.0.3"));
                mToArp.setDataLayerType(Ethernet.TYPE_ARP);
                fmToArp.setActions(actionsToArp);
                fmToArp.setMatch(mToArp);

                // Push the flow
                staticFlowEntryPusher.addFlow("FlowToArp", fmToArp, dp);
                //ArpFrom
                List actionsFromArp = new ArrayList();
                // Declare the flow
                OFFlowMod fmFromArp = new OFFlowMod();
                fmFromArp.setType(OFType.FLOW_MOD);
                // Declare the action
                OFAction outputFromArp = new OFActionOutput((short) 1);
                actionsFromArp.add(outputFromArp);
                // Declare the match
                OFMatch mFromArp = new OFMatch();
                mFromArp.setNetworkDestination(IPv4.toIPv4Address("10.0.0.2"));
                mFromArp.setDataLayerType(Ethernet.TYPE_ARP);
                fmFromArp.setActions(actionsFromArp);
                fmFromArp.setMatch(mFromArp);*/

                /*// Push the flow
                staticFlowEntryPusher.addFlow("FlowFromArp", fmFromArp, dp);
                System.out.println("$$$$$$$$$OFMATCH$$$$$$$$$$$");
                System.out.println(mTo);*/

                /*System.out.println("-------GetFloWs--------");
                System.out.println(staticFlowEntryPusher.getFlows());*/
                //return Command.CONTINUE;
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
        //Service Class l.add(IStaticFlowEntryPusherService.class);
        @Override
        public Collection<Class<? extends IFloodlightService>> getModuleDependencies() {
                Collection<Class<? extends IFloodlightService>> l = new ArrayList<Class<? extends IFloodlightService>>();
                l.add(IFloodlightProviderService.class);
                l.add(IStaticFlowEntryPusherService.class);
                return l;
        }
        // Context object SFP Class
        @Override
        public void init(FloodlightModuleContext context)
                        throws FloodlightModuleException {
                floodlightProvider = context.getServiceImpl(IFloodlightProviderService.class);
                staticFlowEntryPusher = context.getServiceImpl(IStaticFlowEntryPusherService.class);
        }
        @Override
        public void startUp(FloodlightModuleContext context) {
                floodlightProvider.addOFMessageListener(OFType.PACKET_IN, this);
        }
        public void deleteAll(){
        	
        }
        
        
}