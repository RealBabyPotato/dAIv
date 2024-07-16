public class SetupManager {

    public static void setup(User user, int phase, String message){
        switch (phase) {
            case 0: // this is the user's first time messaging us, give them the preamble
                user.message("Hi there! Welcome to dAIv, an awesome AI-powered assistant initiated by APCSA and finished by Jaden. Because it doesn't seem like this account has messaged dAIv before, I will begin a brief setup process. First off, what would you like to be referred to as?\n\n(You can access a help menu using !help)");
                user.setSetupPhase(user.getSetupPhase() + 1); 
                break;
            
            case 1:
                user.setUsername(message);
                user.message("Great to meet you " + user.getUserName() + "! How can I assist you today?");
                user.setIsInSetup(false);
                break;
        
            default:
                user.message("We're sorry, but it seems like something went wrong with the account setup process. Reverting to the start. If this issue persists, please report it using !report {detailed account of issue}.");
                user.setIsInSetup(true);
                user.setSetupPhase(0);
                break;
        }
    }
}
