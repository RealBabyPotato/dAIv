public class Main {
 
  public static void main(String[] args) throws Exception {
    
    // Create a TwilioSMS instance to begin an HTTP service and Authenticate Client
    
    //System.out.println(TwilioServer.send("+12506613358","HTTP Service is Running!"));

    TwilioServer service = new TwilioServer();

    // Send a test Message
    System.out.println(service);

    
  }

}