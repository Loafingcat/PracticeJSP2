package board.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.model.BoardDAO;
import board.model.BoardDTO;

public class BoardReplyFormCmd implements BoardCmd {
	
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		String inputNum = request.getParameter("num");
		BoardDAO dao = new BoardDAO();
		
		BoardDTO writing = dao.boardReplyForm(inputNum);
		request.setAttribute("boardReplyForm", writing);
	}

}
