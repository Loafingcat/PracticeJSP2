package board.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface BoardCmd {
	
	public void execute (HttpServletRequest request, HttpServletResponse response);
}
