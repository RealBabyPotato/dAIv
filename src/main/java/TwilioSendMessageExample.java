import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.twilio.exception.ApiException;


public class TwilioSendMessageExample {

  // Find your Account Sid and Token at console.twilio.com
  public static final String ACCOUNT_SID = "YOUR_TWILIO_ACCOUNT_SID";
  public static final String AUTH_TOKEN = "YOUR_TWILIO_AUTH_TOKEN";

  public static final PhoneNumber serverPhoneNumber = new PhoneNumber("YOUR_TWILIO_PHONE_NUMBER");

  public static void main(String[] args) {
    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    try {
      Message message = Message
      .creator(
        new PhoneNumber("2506613358"),
        new PhoneNumber("YOUR_TWILIO_PHONE_NUMBER"),
        "This is the ship that made the Kessel Run in fourteen parsecs?"
      )
      .create();
    } catch (final ApiException e) {
      System.err.println(e);
    }
  }

  // we should move this method out of this class soon.
  public static void messageUser(User user, String text){
    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    try {
      Message message = Message
              .creator(user.getPhoneNumber(), serverPhoneNumber, text)
              .create();
      System.out.println("Message sent with ID: " + message.getSid());
    } catch (final ApiException e) {
      // error
      System.err.println(e);
    }
  }

  public static void messageNumber(PhoneNumber num, String text){
  Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
      try {
        Message message = Message
                .creator(num, serverPhoneNumber, text)
                .create();
        System.out.println("Message sent with ID: " + message.getSid());
      } catch (final ApiException e) {
        // error
        System.err.println(e);
      }
    }
}
