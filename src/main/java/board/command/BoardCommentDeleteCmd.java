package board.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.model.BoardDAO;

public class BoardCommentDeleteCmd implements BoardCmd {
	
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		
		String inputNum = request.getParameter("rno");
		
		BoardDAO dao = new BoardDAO();
		dao.commentDelete(inputNum);
	}
}
