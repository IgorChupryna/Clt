package ua.hp.chup.conn;

import com.hp.itsm.api.*;
import com.hp.itsm.api.interfaces.*;

/**
 * Created by Chupryna-IV on 18.11.16.
 */
public class HpApiSession {


    public static void main(String args[]) {

        String server = "911.usb";
        String username = "hp90554";
        String password = "12345678";
        ApiSDSession session = null;

        try {
            session = ApiSDSession.openSession(server, username, password);
        } catch (RuntimeException e) {
            /*	Connecting can go wrong for various reasons. E.G. No server is
            *	running on this particular computer/port combination
            *	or the user/password combination was wrong. Catch the exception and
            *	print an error message for the user or for the log. The Web-API makes
            *	an effort to give sensible messages in the exceptions that it throws,
            *	and if possible, the messages are localised.
            *
            *	NOTE that using System.out.println() can be problematic in some
            *	applications.
            */
            System.out.println(e.getMessage());
            return;
        }

        // Get the account using this session.
        // This illustrates how to retrieve a related or aggregated object.
        IAccount account = session.getCurrentAccount();

        // What is the display name of this account?
        // This illustrates how to retrieve properties of an object.
        String userDisplayName = account.getDisplayName();
        System.out.println("Display name of this account = " + userDisplayName);

        // Get the language for this account
        ILocale locale = account.getDefaultLanguage();

        // Get the name of the language
        String userLanguage = locale.getShorttext();
        System.out.println("The language of this user = " + userLanguage);

        // Get the email address of this account
        String userEmail = account.getEmail();
        System.out.println("The email address of this user = " + userEmail);

        // Get all persons using this account.
        // This illustrates how to get a set of related objects via a
        // 1 to many relationship.
        IPerson[] persons = account.getPerson_Account();

        // Show names of these persons
        System.out.println("Persons using the same account :");
        for (int i = 0; i < persons.length; i++) {
            System.out.println("  " + persons[i].getName());
        }

        // Get the timezone of this account
        ITimeZones timeZone = account.getDefaultTimeZone();

        // Get the searchcode of this timezone
        String sTimeZone = timeZone.getSearchcode();
        System.out.println("The timezone of this user = " + sTimeZone);
    }

    public static final String REVISION = "$Revision: 3 $";
}


