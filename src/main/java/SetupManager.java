public class SetupManager {

    public static void setup(User user, int phase, String message){
        switch (phase) {
            case 0: // this is the user's first time messaging us, give them the preamble
                user.message("Hi there! Welcome to dAIv, an awesome AI-powered assistant initiated by APCSA and finished by Jaden BN. Because it doesn't seem like this account has messaged us before, I will begin a brief setup process. First off, what would you like to be referred to as?");
                user.setSetupPhase(user.getSetupPhase() + 1); 
                break;
            
            case 1:
                System.out.println("case 1 message: " + message);
                user.setUsername(message);
                System.out.println("1");
                user.message("Great to meet you " + user.getUserName() + "! How can I assist you today?");
                System.out.println("2");
                user.setIsInSetup(false);

                System.out.println("user pn: " + user.getPhoneNumberAsString());
                break;
        
            default:
                System.out.println("default wtff?");
                user.setIsInSetup(false);
                break;
        }
    }
}
