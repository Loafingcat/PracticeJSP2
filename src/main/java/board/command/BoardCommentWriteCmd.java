package board.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.model.BoardDAO;

public class BoardCommentWriteCmd implements BoardCmd {
	
	public void execute (HttpServletRequest request, HttpServletResponse response) {
		
		String writer = request.getParameter("writer");
		String content = request.getParameter("content");
		
		BoardDAO dao = new BoardDAO();
		dao.writeComment(writer, content);
		
	}

}
