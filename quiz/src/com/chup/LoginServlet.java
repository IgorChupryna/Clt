package com.chup;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {

    static final String TEMPLATE = "<html>" +
            "<head><title>Messi Vs Ronaldo Vs Neymar</title></head>" +
            "<body><h1>%s</h1></body></html>";

    static final Map<String, String> cred = new HashMap<String, String>();

    static {
        cred.put("user","qwerty");
        cred.put("oradb","123456");
        cred.put("oracle","oracle");
        cred.put("root","654321");
        cred.put("admin","qazwsx");
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String login = req.getParameter("login");
        String pass = req.getParameter("password");
        String msg;
        String temp = cred.get(login);

        if (pass.equals(temp))
            resp.sendRedirect("quiz.html");
        else{
            msg = "Denied";
            resp.getWriter().println(String.format(TEMPLATE, msg));
        }
    }

}
