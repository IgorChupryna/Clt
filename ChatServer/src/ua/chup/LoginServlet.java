package ua.chup;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class LoginServlet extends HttpServlet {
    private static boolean presentUser;
    private static boolean isList;
    private static String statUser="";
    private static UserList list=null;
    static {
       list = new UserList();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String login = req.getParameter("login");
        String pass = req.getParameter("pass");
        String status = req.getParameter("status");
        for (User u : UserList.getUsers()) {
            if (u.getName().equals(login) & u.getPass().equals(pass)  & status.equals("in")) {
                u.setStatus(Status.OnLine);
                presentUser = true;
                break;
            }else if(u.getName().equals(login) & u.getPass().equals(pass)  & status.equals("out")){
                u.setStatus(Status.OffLine);
                presentUser = true;
                break;
            }else if(u.getName().equals(login) & u.getPass().equals(pass) & status.equals("list")){
                u.setStatus(Status.OnLine);
                isList=true;
                break;
            }else if(u.getName().equals(login) & u.getPass().equals(pass) & status.equals("absent")){
                u.setStatus(Status.Absent);
                isList=true;
                break;
            }else if(u.getName().equals(login) & u.getPass().equals(pass) & status.equals("online")){
                u.setStatus(Status.OnLine);
                isList=true;
                break;
            }else if(u.getName().equals(login) & u.getPass().equals(pass) ){
                for (User i : UserList.getUsers()) {
                    if(status.equals(i.getName())){
                    statUser = i.getName() + "  " + i.getStatus();
                    break;
                    }
                }
                break;
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        OutputStream os = resp.getOutputStream();
        byte[] buf=null;
        if(isList){
            buf=list.toString().getBytes(StandardCharsets.UTF_8);
            isList=false;
        }else if(!statUser.isEmpty()){
            buf = statUser.getBytes(StandardCharsets.UTF_8);
            statUser="";
        }
        else{
        buf = (presentUser ? "yes" : "no").getBytes(StandardCharsets.UTF_8);
        presentUser = false;
        }
        os.write(buf);
    }


}
