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
	public ArrayList<BoardDTO> boardList(String curPage) {
		
		ArrayList<BoardDTO> list = new ArrayList<BoardDTO>();
		
		try {
			String sql = "SELECT * FROM BOARD ORDER BY ref desc, step asc LIMIT ?,? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, WRITING_PER_PAGE * (Integer.parseInt(curPage) - 1));
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
			String dbURL = "jdbc:mariadb://localhost:3306/jspbook";
			String dbID = "root";
			String dbPassword = "junho";
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
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
			String sql = "UPDATE BOARD SET READ_CNT = READ_CNT+1 WHERE NUM = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(inputNum));
			pstmt.executeUpdate();
			
			sql = "SELECT * FROM BOARD WHERE NUM = ?";
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
	//게시글 수정 기능 수행
	public void boardUpdate (String inputNum, String inputSubject, String inputContent, String inputName, String inputPassword) {
		
		try {
			String sql = "UPDATE BOARD SET subject=?, content=?, name=?, password=? WHERE num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, inputSubject);
			pstmt.setString(2, inputContent);
			pstmt.setString(3, inputName);
			pstmt.setString(4, inputPassword);
			pstmt.setInt(5, Integer.parseInt(inputNum));
			
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
	//게시글 수정 및 삭제를 위한 비밀번호 확인 기능 조회
	public boolean boardPasswordCheck (String inputNum, String inputPassword) {
		boolean passwordOk = false;
		int passwordCheck = 0;
		
		try {
			String dbURL = "jdbc:mariadb://localhost:3306/jspbook";
			String dbID = "root";
			String dbPassword = "junho";
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			String sql = "SELECT COUNT(*) AS password_check FROM BOARD WHERE NUM=? and  password=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(inputNum));
			pstmt.setString(2, inputPassword);
			rs = pstmt.executeQuery();
			
			if (rs.next()) passwordCheck = rs.getInt("password_check");
			if (passwordCheck > 0) passwordOk = true;
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
			return passwordOk;
	}
	//글 수정 화면에 필요한 원글 데이터 조회 기능
	public BoardDTO boardUpdateForm(String inputNum) {
		
		BoardDTO writing = new BoardDTO();
		
		try {
			String dbURL = "jdbc:mariadb://localhost:3306/jspbook";
			String dbID = "root";
			String dbPassword = "junho";
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			String sql = "SELECT * FROM BOARD WHERE NUM = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(inputNum));
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
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
	//글 삭제 기능 수행
	public void boardDelete(String inputNum) {
		try {
			String dbURL = "jdbc:mariadb://localhost:3306/jspbook";
			String dbID = "root";
			String dbPassword = "junho";
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			String sql = "Selete ref, lev, step FROM BOARD WHERE num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(inputNum));
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				int ref = rs.getInt(1);
				int lev = rs.getInt(2);
				int step = rs.getInt(3);
				boardDeleteChildCntUpdate(ref, lev, step);
			}
			sql = "DELETE FROM BOARD WHERE num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(inputNum));
			
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
	//삭제 대상인 게시글에 답글의 존재 유무를 검사
	public boolean boardReplyCheck(String inputNum) {
		boolean replyCheck = false;
		int replyCnt = 0;
		
		try {
			String dbURL = "jdbc:mariadb://localhost:3306/jspbook";
			String dbID = "root";
			String dbPassword = "junho";
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			String sql = "SELETE child_cnt AS reply_check FROM BOARD WHERE num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(inputNum));
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) replyCnt = rs.getInt("reply_check");
			if (replyCnt == 0) replyCheck = true;
			
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
		return replyCheck;
	}
	//게시글이 답글일 경우, 원글들의 답글 개수를 줄여주는 기능 수행
	public void boardDeleteChildCntUpdate(int ref, int lev, int step) {
		String sql = null;
		try {
			String dbURL = "jdbc:mariadb://localhost:3306/jspbook";
			String dbID = "root";
			String dbPassword = "junho";
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			for(int updateLev=lev-1; updateLev>=0; updateLev--) {
				sql = "SELETE MAX(step) FROM BOARD WHERE ref = ? and lev = ? and step < ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, ref);
				pstmt.setInt(2, updateLev);
				pstmt.setInt(3, step);
				
				rs = pstmt.executeQuery();
				int maxStep = 0;
				
				if(rs.next()) maxStep = rs.getInt(1);
				sql = "UPDATE BOARD SET child_cnt = child_cnt - 1 where ref = ? and lev = ? and step = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, ref);
				pstmt.setInt(2, updateLev);
				pstmt.setInt(3, maxStep);
				pstmt.executeUpdate();
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
	}
}