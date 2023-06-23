package board.command;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.model.BoardDAO;
import board.model.BoardDTO;
import board.model.Comment;

public class BoardReadCmd implements BoardCmd {
	
	public void execute (HttpServletRequest request, HttpServletResponse response) {
		String inputNum = request.getParameter("num");
		BoardDAO dao = new BoardDAO();
		ArrayList<Comment> comment;
		comment = dao.getComment(inputNum);
		BoardDTO writing = dao.boardRead(inputNum);
		
		
		request.setAttribute("getComment", comment);
		request.setAttribute("boardRead", writing);
		
		
	}

}
