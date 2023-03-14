package board.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BoardDeletePasswordCmd implements BoardCmd {
	
	public boolean password_check;
	
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		String inputNum = request.getParameter("num");
		request.setAttribute("num", inputNum);
	}

}
