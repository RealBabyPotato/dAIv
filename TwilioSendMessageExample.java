import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.twilio.exception.ApiException;


public class TwilioSendMessageExample {

  // Find your Account Sid and Token at console.twilio.com
  public static final String ACCOUNT_SID = "YOUR_TWILIO_ACCOUNT_SID";
  public static final String AUTH_TOKEN = "YOUR_TWILIO_AUTH_TOKEN";

  public static void main(String[] args) {
    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    try {
      Message message = Message
      .creator(
        new PhoneNumber("2508806642"),
        new PhoneNumber("YOUR_TWILIO_PHONE_NUMBER"),
        "This is the ship that made the Kessel Run in fourteen parsecs?"
      )
      .create();
      System.out.println(message.getSid());
    } catch (final ApiException e) {
      System.err.println(e);
    }
  }
}
