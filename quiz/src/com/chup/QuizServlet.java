package com.chup;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("serial")
public class QuizServlet extends HttpServlet {

    private File fileRes = new File("./results.txt");
    private File fileAnkets = new File("./ankets.txt");

    static final int Q1_YES = 0;
    static final int Q1_NO = 1;
    static final int Q2_YES = 2;
    static final int Q2_NO = 3;
    static final int Q3_YES = 4;
    static final int Q3_NO = 5;


    static final String TEMPLATE = "<html>" +
            "<head><title>Messi Vs Ronaldo Vs Neymar</title></head>" +
            "<body><h1>%s</h1></body></html>";

    static final int[] results = new int[6];
    static final Map<String, String> pair = new HashMap<>();



    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final String firstName = req.getParameter("first name");
        final String secondName = req.getParameter("second name");
        final int age = Integer.parseInt(req.getParameter("age"));
        String msg;

        if (fileAnkets.length() != 0)
        loadAnkets();

        for (Map.Entry<String, String> s : pair.entrySet()) {
            String key = s.getKey();
            String value = s.getValue();

            if (firstName.equals(key) & secondName.equals(value)) {
                msg = key + " " + value + " already answered this questions<br><br>Click this link to <a href=\"/quiz.html\">Previous page</a>";
                resp.getWriter().println(String.format(TEMPLATE, msg));
                return;
            }
        }
        saveAnket(firstName, secondName);

        final String q1 = req.getParameter("q1");
        final String q2 = req.getParameter("q2");
        final String q3 = req.getParameter("q3");


        final int idx1 = "yes".equals(q1) ? Q1_YES : Q1_NO;
        final int idx2 = "yes".equals(q2) ? Q2_YES : Q2_NO;
        final int idx3 = "yes".equals(q3) ? Q3_YES : Q3_NO;

        if (fileRes.length() != 0)
            loadRes();

        results[idx1]++;
        results[idx2]++;
        results[idx3]++;

        saveRes();
        resp.sendRedirect("/stat");
    }


    private void saveRes() throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(fileRes);
        pw.flush();
        for (int i : results) {
            pw.println(i);
        }
        pw.close();
    }

    private void loadRes() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileRes));
        for (int i = 0; i < results.length; i++) {
            results[i] = Integer.parseInt(br.readLine());
        }
    }

    private void saveAnket(String firstName, String secondName) throws IOException {
        Files.write(Paths.get(fileAnkets.getAbsolutePath()), (firstName + ";" + secondName+"\n\r").getBytes(), StandardOpenOption.APPEND);
        pair.put(firstName, secondName);
    }

    private void loadAnkets() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileAnkets));
        String line="";
        while((line=br.readLine())!=null){
            String[] s = line.split(";");
            if(s.length>1)
            pair.put(s[0],s[1]);
        }

    }

}

