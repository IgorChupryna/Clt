package com.chup;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StatServlet extends HttpServlet {
    static final String TEMPLATE = "<html>" +
            "<head><title>Messi Vs Ronaldo Vs Neymar</title></head>" +
            "<body><h1>%s</h1></body></html>";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tableStart = "<html><head><title>Messi Vs Ronaldo Vs Neymar</title></head><body>\n" +
                "  <table width=\"400\" border=\"1\">\n" +
                "   <caption><strong>Rating of the best players</strong></caption>\n" +
                "  <tr bgcolor=\"#ffcc00\">\n" +
                "    <th weight=\"80\">Name</th>\n" +
                "    <th>YES</th>\n" +
                "    <th>NO</th>\n" +
                "   </tr>";

        String td = "<tr><td bgcolor=\"#c0c0c0\">Messi</td><td>" + QuizServlet.results[0] + "</td><td>" + QuizServlet.results[1] + "</td></tr>";
        td += "<tr><td bgcolor=\"#c0c0c0\">Ronaldo</td><td>" + QuizServlet.results[2] + "</td><td>" + QuizServlet.results[3] + "</td></tr>";
        td += "<tr><td bgcolor=\"#c0c0c0\">Neymar</td><td>" + QuizServlet.results[4] + "</td><td>" + QuizServlet.results[5] + "</td></tr></table>";

        String logOut="<br><br>Click this link to <a href=\"/\">logout</a>";

        String tableFinish = "</body></html>";

        String res = tableStart+td+logOut+tableFinish;


        resp.getWriter().println(String.format(TEMPLATE, res));
    }
}
