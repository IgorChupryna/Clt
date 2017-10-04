package chup.com.ua;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.*;

@SuppressWarnings("serial")
public class ChatServlet extends HttpServlet {
	
	private Messages list = Messages.getInstance();
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		final String msg = req.getParameter("msg");
		final String login = req.getParameter("login");

		list.add(new StringBuilder()
			.append("<b>[")
			.append(new Date().toString()) 
			.append("] ")
			.append(login)
			.append(": ")
			.append("</b>")
			.append(msg)
			.toString());
		
		resp.sendRedirect("/chat.jsp");
	}
	
	
}
