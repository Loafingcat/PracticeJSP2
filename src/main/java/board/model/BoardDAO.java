package board.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BoardDAO {

	public static final int WRITING_PER_PAGE = 10;
	
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	public BoardDAO() {
		try {
			String dbURL = "jdbc:mariadb://localhost:3306/jspbook";
			String dbID = "root";
			String dbPassword = "junho";
			Class.forName("org.mariadb.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//게시판 목록 조회 기능 수행
	public ArrayList<BoardDTO> boardList(String curpage) {
		
		ArrayList<BoardDTO> list = new ArrayList<BoardDTO>();
		
		try {
			String sql = "SELECT * FROM BOARD ORDER BY ref desc, step asc LIMIT ?,? ";
			
			
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, WRITING_PER_PAGE * (Integer.parseInt(curpage) - 1));
			pstmt.setInt(2, WRITING_PER_PAGE);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				int num = rs.getInt("num");
				String name = rs.getString("name");
				String password = rs.getString("password"); 
				String subject = rs.getString("subject");
				String content = rs.getString("content");
				Date writeDate = rs.getDate("write_date");
				Date writeTime = rs.getDate("write_time");
				int ref = rs.getInt("ref");
				int step = rs.getInt("step");
				int lev = rs.getInt("lev");
				int readCnt = rs.getInt("read_cnt");
				int childCnt = rs.getInt("child_cnt");
				
				BoardDTO writing = new BoardDTO();
				writing.setNum(num);
				writing.setName(name);
				writing.setPassword(password);
				writing.setSubject(subject);
				writing.setContent(content);
				writing.setWriteDate(writeDate);
				writing.setWriteTime(writeTime);
				writing.setRef(ref);
				writing.setStep(step);
				writing.setLev(lev);
				writing.setReadCnt(readCnt);
				writing.setChildCnt(childCnt);
				
				list.add(writing);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	//게시판의 페이징 처리를 위한 기능 수행
	public int boardPageCnt() {
		int pageCnt = 0;
		
		try {
			String sql = "SELECT COUNT(*) AS num FROM BOARD";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				pageCnt = rs.getInt("num") / WRITING_PER_PAGE + 1;
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return pageCnt;
	}
	//게시글 등록 기능 수행
	public void boardWrite(String name, String subject, String content, String password) {
		
		int num = 1;
		
		try {
			String sql = "SELECT IFNULL(MAX(num), 0)+1 AS NUM FROM BOARD";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				num = rs.getInt("num");
			}
			sql = "INSERT INTO BOARD (num, name, password, subject, content, write_date, write_time, ref, step, lev, read_cnt, child_cnt) values(?,?,?,?,?,curdate(), curtime(),?,0,0,0,0)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			pstmt.setString(2, name);
			pstmt.setString(3, password);
			pstmt.setString(4, subject);
			pstmt.setString(5, content);
			pstmt.setInt(6, num);
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	//게시글 열람 기능 수행
	public BoardDTO boardRead(String inputNum) {
		BoardDTO writing = new BoardDTO();
		
		try {
			String sql = "UPDATE BOARD SET READ_CNT = READ_CNT +1 WHERE NUM = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(inputNum));
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				int num = rs.getInt("num");
				String name = rs.getString("name");
				String password = rs.getString("password"); 
				String subject = rs.getString("subject");
				String content = rs.getString("content");
				Date writeDate = rs.getDate("write_date");
				Date writeTime = rs.getDate("write_time");
				int ref = rs.getInt("ref");
				int step = rs.getInt("step");
				int lev = rs.getInt("lev");
				int readCnt = rs.getInt("read_cnt");
				int childCnt = rs.getInt("child_cnt");
				
				writing.setNum(num);
				writing.setName(name);
				writing.setPassword(password);
				writing.setSubject(subject);
				writing.setContent(content);
				writing.setWriteDate(writeDate);
				writing.setWriteTime(writeTime);
				writing.setRef(ref);
				writing.setStep(step);
				writing.setLev(lev);
				writing.setReadCnt(readCnt);
				writing.setChildCnt(childCnt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return writing;
	}
}
