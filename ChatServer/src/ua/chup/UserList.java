package ua.chup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserList {
    private final static List<User> users= new ArrayList();

    public UserList() {
        users.add(new User("admin","qwerty"));
        users.add(new User("oracle","123456"));
        users.add(new User("grub","ytrewq"));
        users.add(new User("root","654321"));
        users.add(new User("igor","pass"));
        users.add(new User("anton","pass"));
    }

    @Override
    public String toString() {
        String spaces = "                ";
        String str =  Arrays.deepToString(getUsers().toArray()).replaceAll(",","");
        return "\nUser"+spaces.substring(4)+"Status\n-----------------------"+str.substring(1,str.length()-1)+"\n";
    }

    public static List<User> getUsers() {
        return users;
    }

}
