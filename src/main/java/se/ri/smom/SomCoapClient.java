package se.ri.smom;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

public class SomCoapClient {

    public static void main(String args[]) {

        URI uri = null; // URI parameter of the request

        if (args.length > 0) {

            // input URI from command line arguments
            try {
                uri = new URI(args[0]);
            } catch (URISyntaxException e) {
                System.err.println("Invalid URI: " + e.getMessage());
                System.exit(-1);
            }

            boolean get = true;
            String payload = null;
            
            if (args.length > 1) {
                if (args[1].equals("-p")) {//It's a POST
                    get = false;
                    if (args.length > 2) {
                        payload = args[2];
                    } else {
                        System.err.println("Payload required for POST");
                        System.exit(-1);
                    }
                } else {
                    System.err.println("Unrecognized parameter: " + args[1]);
                }
            }
            
            CoapClient client = new CoapClient(uri);

            CoapResponse response = null;
            if (get) {
                response = client.get();
            } else {//POST
                response = client.post(payload, MediaTypeRegistry.TEXT_PLAIN);
            }

            if (response!=null) {

                System.out.println(response.getCode());
                System.out.println(response.getOptions());
                System.out.println(response.getResponseText());
            } else {
                System.out.println("No response received.");
            }

        } else {
            // display help
            System.out.println("Californium (Cf) GET/POST Client");
            System.out.println("Usage : " + SomCoapClient.class.getSimpleName() + "URI [-p] [payload]");
            System.out.println("  URI : The CoAP URI of the remote resource to GET/POST");
            System.out.println("  -p  : optional flag to use POST (GET is default)");
            System.out.println("payload : if the -p flag is set this parameter is the payload");
        }
    }
}
