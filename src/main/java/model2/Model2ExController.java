package model2;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model1.StudentBean;

@WebServlet("/Model2ExController")
public class Model2ExController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public Model2ExController() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String studentId = request.getParameter("studentId");
		StudentBean studentBean = new StudentBean();
		studentBean.selectStudent (studentId);
		request.setAttribute("student", studentBean);
		RequestDispatcher dispatcher = request.getRequestDispatcher("Model2.jsp");
		dispatcher.forward(request, response);
	}
	protected void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
}
