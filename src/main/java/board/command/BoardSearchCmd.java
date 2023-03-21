package board.command;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.model.BoardDAO;
import board.model.BoardDTO;

public class BoardSearchCmd implements BoardCmd {

	public void execute (HttpServletRequest request, HttpServletResponse response) {
		BoardDAO dao = new BoardDAO();
		String searchOption = request.getParameter("searchOption");
		String searchWord = request.getParameter("searchWord");
		
		ArrayList<BoardDTO> list = dao.boardSearch(searchOption, searchWord);
		request.setAttribute("boardList", list);
	}
}
