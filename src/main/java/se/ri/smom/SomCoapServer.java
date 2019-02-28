package se.ri.smom;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.EndpointManager;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.server.resources.CoapExchange;

public class SomCoapServer extends CoapServer {
    
    private static final int COAP_PORT = NetworkConfig.getStandard().getInt(NetworkConfig.Keys.COAP_PORT);

    /*
     * Application entry point.
     */
    public static void main(String[] args) {

        try {
            // create server
            SomCoapServer server = new SomCoapServer();
            // add endpoints on all IP addresses
            server.addEndpoints();
            server.start();

        } catch (SocketException e) {
            System.err.println("Failed to initialize server: " + e.getMessage());
        }
    }

    /**
     * Add individual endpoints listening on default CoAP port on all IPv4
     * addresses of all network interfaces.
     */
    private void addEndpoints() {
        NetworkConfig config = NetworkConfig.getStandard();
        for (InetAddress addr : EndpointManager.getEndpointManager().getNetworkInterfaces()) {
            InetSocketAddress bindToAddress = new InetSocketAddress(addr, COAP_PORT);

            CoapEndpoint.Builder builder = new CoapEndpoint.Builder();
            builder.setInetSocketAddress(bindToAddress);
            builder.setNetworkConfig(config);
            addEndpoint(builder.build());
        }
    }

    /*
     * Constructor for a new Hello-World server. Here, the resources of the
     * server are initialized.
     */
    public SomCoapServer() throws SocketException {

        // provide an instance of a Hello-World resource
        add(new HelloWorldResource());
    }

    /*
     * Definition of the Hello-World Resource
     */
    class HelloWorldResource extends CoapResource {

        public HelloWorldResource() {

            // set resource identifier
            super("helloWorld");

            // set display name
            getAttributes().setTitle("Hello-World Resource");
        }

        @Override
        public void handleGET(CoapExchange exchange) {

            // respond to the request
            exchange.respond("Hello World!");
        }
        
        @Override
        public void handlePOST(CoapExchange exchange) {        
            exchange.respond(ResponseCode.CREATED, "Got your POST: " + exchange.getRequestText());
        }

    }
}
