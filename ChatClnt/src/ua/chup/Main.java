package ua.chup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    private static final String BEGIN = "Menu in this Chat\n\t" +
            "<DESC>                <Command>\n\t" +
            "---------------------------------------------\n\t" +
            "Private msg:          --<login> message\n\t" +
            "Change status:        s-- <STATUS(onLine,absent)>\n\t" +
            "Print user status:    s-- <user name>\n\t" +
            "Send msg to room:     ###<name>\n\t" +
            "Connect(add) to room: a-r <name>\n\t" +
            "Exit from room:       e-r <name>\n\t" +
            "List users/status:    list\n\t" +
            "Disconnect:           exit\n\t" +
            "Menu command:         --help\n";
    private static Scanner scanner = new Scanner(System.in);
    private static String login = "", pass = "";

    private static Set<String> rooms = new HashSet<>();

    static {
        rooms.add("ALL");
    }

    public static void main(String[] args) {
        try {
            connect();

            Thread th = new Thread(new GetThread());
            th.setDaemon(true);
            th.start();

            System.out.println(BEGIN);
            while (true) {
                String text = scanner.nextLine();
                String to = "", priv_msg = "";
                while (text.isEmpty()) {
                    text = scanner.nextLine();
                }
                if (text.equalsIgnoreCase("exit")) {
                    logout();
                    break;
                }
                if (text.equals("--help")) {
                    System.out.println("\n" + BEGIN);
                    continue;
                }
                if (text.trim().equals("list")) {
                    list();
                    continue;
                }

                Message m = null;
                if (text.trim().length() >= 5) {
                    if (text.trim().substring(0, 3).equals("s--")) {
                        String sts = text.trim().substring(4);
                        if (sts.equalsIgnoreCase("online")) {
                            chngStatus("online");
                        } else if (sts.equalsIgnoreCase("absent")) {
                            chngStatus("absent");
                        } else {
                            chngStatus(text.trim().substring(4));
                        }
                        continue;
                    } else if (text.trim().substring(0, 3).equals("###")) {
                        String sts = text.trim().substring(3, text.trim().indexOf(" "));
                        boolean t = false;
                        for (String r : rooms) {
                            if (r.equals(sts)) {
                                m = new Message(login, sts, text.trim().substring(text.trim().indexOf(" ")));
                                m.send("http://" + "10.91.5.112" + ":8082" + "/add");
                                t = true;
                                break;
                            }
                        }
                        if (!t) System.out.println("You must connect to this room!");
                        continue;
                    } else if (text.trim().substring(0, 2).equals("--")) {
                        to = text.substring(2, text.indexOf(" "));
                        priv_msg = text.substring(text.indexOf(" ") + 1);
                        m = new Message(login, to, priv_msg);
                    } else if (text.trim().substring(0, 3).equals("a-r")) {
                        String sts = text.trim().substring(4);
                        addRoom(sts);
                        text = login + " connected to room " + sts;
                        m = new Message(login, sts, text);
                    } else if (text.trim().substring(0, 3).equals("e-r")) {
                        String sts = text.trim().substring(4);
                        delRoom(sts);
                        text = login + " exited from room " + sts;
                        System.out.println(login + " exited from room " + sts);
                        m = new Message(login, sts, text);
                    }
                    else{
                        m = new Message(login, text);}
                } else
                    m = new Message(login, text);

                int res = m.send("http://" + "10.91.5.112" + ":8082" + "/add");

                if (res != 200) { System.out.println("HTTP error occured: " + res);return; }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static void connect() throws IOException {
        if (!logging()) {
            System.out.println("Login or password is wrong ");
            while (true) {
                System.out.println("Do you want enter <login&passworg> again ?(y/n)");
                String str = scanner.nextLine();
                if (str.equalsIgnoreCase("y")) {
                    logging();
                    break;
                } else if (str.equalsIgnoreCase("n")) {
                    System.exit(0);
                }
            }
        }

    }

    private static boolean logging() throws IOException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            while (true) {
                System.out.println("Enter login: ");
                login = scanner.nextLine();
                if (!login.isEmpty()) break;
            }
            while (true) {
                System.out.println("Enter pass: ");
                pass = scanner.nextLine();
                if (!pass.isEmpty()) break;
            }
            rooms.add(login);
            User auto = new User(login, pass);
            auto.send("http://" + "10.91.5.112" + ":8082" + "/login?login=" + login + "&pass=" + pass + "&status=in");

            URL url = new URL(Utils.getURL() + "/login");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream is = http.getInputStream();

            byte[] buf = requestBodyToArray(is);
            String strBuf = new String(buf, StandardCharsets.UTF_8);

            if (strBuf.equals("yes")) return true;
            else
                return false;
        }
    }

    private static void chngStatus(String sts) throws IOException {
        User auto = new User(login, pass);
        auto.send("http://" + "10.91.5.112" + ":8082" + "/login?login=" + login + "&pass=" + pass + "&status=" + sts);

        URL url = new URL(Utils.getURL() + "/login");
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        InputStream is = http.getInputStream();

        byte[] buf = requestBodyToArray(is);
        String strBuf = new String(buf, StandardCharsets.UTF_8);
        System.out.println(strBuf);
    }

    private static void list() throws IOException {
        User auto = new User(login, pass);
        auto.send("http://" + "10.91.5.112" + ":8082" + "/login?login=" + login + "&pass=" + pass + "&status=list");

        URL url = new URL(Utils.getURL() + "/login");
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        InputStream is = http.getInputStream();

        byte[] buf = requestBodyToArray(is);
        String strBuf = new String(buf, StandardCharsets.UTF_8);
        System.out.println(strBuf);
    }

    private static boolean logout() throws IOException {

        User auto = new User(login, pass);
        auto.send("http://" + "10.91.5.112" + ":8082" + "/login?login=" + login + "&pass=" + pass + "&status=out");

        URL url = new URL(Utils.getURL() + "/login");
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        InputStream is = http.getInputStream();

        byte[] buf = requestBodyToArray(is);
        String strBuf = new String(buf, StandardCharsets.UTF_8);

        if (strBuf.equals("yes")) return true;
        else
            return false;

    }

    private static void addRoom(String name) {
        rooms.add(name);
    }
    private static void delRoom(String name) { rooms.remove(name); }

    private static byte[] requestBodyToArray(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[10240];
        int r;

        do {
            r = is.read(buf);
            if (r > 0) bos.write(buf, 0, r);
        } while (r != -1);

        return bos.toByteArray();
    }

    public static String getLogin() {
        return login;
    }

    public static void setLogin(String login) {
        Main.login = login;
    }

    public static Set<String> getRooms() {
        return rooms;
    }
}
