package bt.gov.dzongkha.dzongkhaenglishphrasebook.FeedBackActivity;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Created by sangay on 1/24/2018.
 */

public class SMTPAuthenticator extends Authenticator {

    public SMTPAuthenticator() {
        super();
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        String username = "sanglim2012@gmail.com";
        String password = "Sangay@1995@16923882";
        if ((username != null) && (username.length() > 0) && (password != null)
                && (password.length() > 0)) {

            return new PasswordAuthentication(username, password);
        }
        return null;
    }
}

