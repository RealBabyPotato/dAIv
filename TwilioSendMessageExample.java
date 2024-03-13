import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwilioSendMessageExample {

  // Find your Account Sid and Token at console.twilio.com
  public static final String ACCOUNT_SID = "YOUR_TWILIO_ACCOUNT_SID";
  public static final String AUTH_TOKEN = "YOUR_TWILIO_AUTH_TOKEN";

  public static void main(String[] args) {
    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

    Message message = Message
      .creator(
        new PhoneNumber("+12506613358"),
        "This is the ship that made the Kessel Run in fourteen parsecs?"
      )
      .create();

    System.out.println(message.getSid());
  }
}
