package board.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
			String sql = "SELECT * FROM BOARD where ISDELETED = '0' ORDER BY ref desc, step asc LIMIT ?,? ";
			
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
				LocalDateTime regDate = rs.getTimestamp("regdate").toLocalDateTime();
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
				writing.setRegDate(regDate);
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
			String dbURL = "jdbc:mariadb://localhost:3306/jspbook";
			String dbID = "root";
			String dbPassword = "junho";
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			
			String sql = "UPDATE BOARD SET READ_CNT = READ_CNT+1 WHERE NUM = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(inputNum));
			pstmt.executeUpdate();
			System.out.println(Integer.parseInt(inputNum));
			System.out.println(inputNum);
			
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
			String sql = "SELECT ref, lev, step FROM BOARD WHERE num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(inputNum));
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				int ref = rs.getInt(1);
				int lev = rs.getInt(2);
				int step = rs.getInt(3);
				boardDeleteChildCntUpdate(ref, lev, step);//답글을 삭제할 때 답글수를 줄여주는 기능이 작동한다
			}
			String dbURL = "jdbc:mariadb://localhost:3306/jspbook";
			String dbID = "root";
			String dbPassword = "junho";
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			sql = "UPDATE BOARD set ISDELETED = 1 WHERE num = ?";
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
			String sql = "SELECT child_cnt AS reply_check FROM BOARD WHERE num = ?";//답글이 있는지 없는지 체크하는 부분
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(inputNum));
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) replyCnt = rs.getInt("reply_check");
			if (replyCnt == 0) replyCheck = true;//답글이 0개라면 true로 삭제가 가능한 글이다
			
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
	//삭제하는 게시글이 답글일 경우, 원글들의 답글 개수를 줄여주는 기능 수행
	public void boardDeleteChildCntUpdate(int ref, int lev, int step) {
		String sql = null;
		try {
			String dbURL = "jdbc:mariadb://localhost:3306/jspbook";
			String dbID = "root";
			String dbPassword = "junho";
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			for(int updateLev=lev-1; updateLev>=0; updateLev--) {//답글을 삭제할 경우 lev를 줄여주는 부분
				sql = "SELECT MAX(step) FROM BOARD WHERE ref = ? and lev = ? and step < ?";//가장 큰 step값을 골라서 삭제한다.
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, ref);
				pstmt.setInt(2, updateLev);
				pstmt.setInt(3, step);
				
				rs = pstmt.executeQuery();
				int maxStep = 0;
				
				if(rs.next()) maxStep = rs.getInt(1);
				
				sql = "UPDATE BOARD SET child_cnt = child_cnt - 1 where ref = ? and lev = ? and step = ?";//답글 개수를 줄이는 부분
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
	//검색 기능 수행
	public ArrayList<BoardDTO> boardSearch(String searchOption, String searchWord) {
		ArrayList<BoardDTO> list = new ArrayList<BoardDTO>();
		
		try {
			String dbURL = "jdbc:mariadb://localhost:3306/jspbook";
			String dbID = "root";
			String dbPassword = "junho";
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			String sql = null;
			
			
			if(searchOption.equals("subject")) {
				sql = "SELECT * FROM BOARD WHERE subject LIKE ? ORDER BY ref desc, step asc"; 
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%" + searchWord + "%");
				
			} else if(searchOption.equals("content")) {
				sql = "SELECT * FROM BOARD WHERE content LIKE ? ORDER BY ref desc, step asc";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%" + searchWord + "%");
				
			} else if(searchOption.equals("name")) {
				sql = "SELECT * FROM BOARD WHERE name LIKE ? ORDER BY ref desc, step asc";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%" + searchWord + "%");
				
			} else if(searchOption.equals("both")) {
				sql = "SELECT * FROM BOARD WHERE subject LIKE ? OR content LIKE ? ORDER BY ref desc, step asc";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%" + searchWord + "%");
				pstmt.setString(2, "%" + searchWord + "%");
			}
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
		return list;
	}
	//답글 작성에 필요한 원글 데이터 조회 기능
	public BoardDTO boardReplyForm(String inputNum) {
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
				String subject ="RE:" + rs.getString("subject");//제목에 RE:가 달리게 된다.
				Date writeDate = rs.getDate("write_date");
				Date writeTime = rs.getDate("write_time");
				LocalDateTime regDate = rs.getTimestamp("regdate").toLocalDateTime();
				String content = "[원문:" + writeDate+" "+ regDate.format(DateTimeFormatter.ofPattern("HH:MM:ss")) +"작성됨]\n" + rs.getString("content");//답글에 원문을 들고온다
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
	//답글 등록 기능 수행
	public void boardReply(String num, String name, String subject, String content, String password, String ref, String step, String lev) {
		
		Connection conn = null;

		int replyNum = 0;
		int replyStep = 0;
		String sql = null;
		
		try {
			String dbURL = "jdbc:mariadb://localhost:3306/jspbook";
			String dbID = "root";
			String dbPassword = "junho";
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			
			//답글이 위치할 step 값을 가져옴
			replyStep = boardReplySearchStep(ref, lev, step);
			
			if(replyStep > 0) {//만약 replyStep값이 0보다 크다면
				sql = "UPDATE BOARD SET step = step + 1 where ref = ? and step >= ?";//step값을 +1 해준다
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, Integer.parseInt(ref));
				pstmt.setInt(2, replyStep);
				pstmt.executeUpdate();
			} else {//replyStep이 0인 경우
				sql = "SELECT MAX(STEP) FROM BOARD WHERE ref = ?";//maxstep을 가져와서
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, Integer.parseInt(ref));
				rs = pstmt.executeQuery();
				if(rs.next()) replyStep = rs.getInt(1) + 1;//maxstep값에 +1 해준다
			}
			
			sql = "SELECT MAX(num)+1 AS NUM FROM BOARD";//게시물 번호도 최대 num에서 +1 해준다
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) replyNum = rs.getInt("num");
			sql = "INSERT INTO BOARD";
			sql += "(num, name, password, subject, content, write_date, write_time, ref, step, lev, read_cnt, child_cnt)";
			sql += "values(?,?,?,?,?,curdate(),curtime(),?,?,?,0,0)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, replyNum);
			pstmt.setString(2, name);
			pstmt.setString(3, password);
			pstmt.setString(4, subject);
			pstmt.setString(5, content);
			pstmt.setInt(6, Integer.parseInt(ref));
			pstmt.setInt(7, replyStep);
			pstmt.setInt(8, Integer.parseInt(lev)+1);//답글이니까 답글의 깊이인 lev +1 해준다
			pstmt.executeUpdate();
			boardReplyChildCntUpdate(ref,lev,replyStep);//답글 갯수를 늘려주는건 여기서 한다
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
	//답글의 출력 위치(step) 선정 기능 수행
	public int boardReplySearchStep(String ref, String lev, String step) {
		int replyStep=0;
		
		try {
			String dbURL = "jdbc:mariadb://localhost:3306/jspbook";
			String dbID = "root";
			String dbPassword = "junho";
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			String sql = "SELECT IFNULL(MIN(step), 0) from BOARD WHERE ref = ? and lev <= ? and step > ?";//만약 step값이 null이면 0이 선택되고, 아니면 최소 step값이 선택된다.
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(ref));
			pstmt.setInt(2, Integer.parseInt(lev));
			pstmt.setInt(3, Integer.parseInt(step));
			rs = pstmt.executeQuery();
			
			if(rs.next()) replyStep = rs.getInt(1);//minStep이나 0값을 받아와서 replyStep으로 리턴
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
		return replyStep;
	}
	//답글 작성 후 원글들의 답글 개수를 늘려주는 기능 수행
	public void boardReplyChildCntUpdate(String ref, String lev, int replyStep) {
			String sql = null;
			
			try {
				String dbURL = "jdbc:mariadb://localhost:3306/jspbook";
				String dbID = "root";
				String dbPassword = "junho";
				conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
				for (int updateLev = Integer.parseInt(lev); updateLev>=0; updateLev--) {
					sql = "SELECT MAX(step) FROM BOARD WHERE ref = ? and lev = ? and step < ?";//최대 step을 확인하고
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, Integer.parseInt(ref));
					pstmt.setInt(2, updateLev);
					pstmt.setInt(3, replyStep);
					
					rs = pstmt.executeQuery();
					int maxStep = 0;
					
					if (rs.next()) maxStep = rs.getInt(1);//maxStep이 존재한다면
					sql = "UPDATE BOARD SET child_cnt = child_cnt + 1 where ref = ? and lev = ? and step = ?";//답글을 늘려줘요~
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, Integer.parseInt(ref));
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
	//댓글목록 조회 기능
	public ArrayList<Comment> getComment(String inputNum) {
		ArrayList<Comment> comment = new ArrayList<Comment>();
		
		try {
			String sql = "select * from board_comment where num = ? AND ISDELETED = '0' ORDER BY rref asc, rstep asc";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(inputNum));
			rs = pstmt.executeQuery();
			while(rs.next()) {
				int rno = rs.getInt("rno");
				int num = rs.getInt("num");
				String writer = rs.getString("writer");
				String content = rs.getString("content");
				Date regDate = rs.getDate("regdate");
				int rref = rs.getInt("rref");
				int rstep = rs.getInt("rstep");
				int rlev = rs.getInt("rlev");
				
				Comment cmt = new Comment();
				cmt.setRno(rno);
				cmt.setNum(num);
				cmt.setWriter(writer);
				cmt.setContent(content);
				cmt.setRegDate(regDate);
				cmt.setRref(rref);
				cmt.setRstep(rstep);
				cmt.setRlev(rlev);
				
				comment.add(cmt);
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
		return comment;
	}
	//댓글 등록 기능
	public void writeComment(String writer, String content, String rref, String rstep, String rlev) {
		
		int num = 1;
		int rno = 1;
		
		try {
			String sql = "SELECT IFNULL(MAX(num), 0)+1 AS NUM FROM BOARD";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				num = rs.getInt("num");
			}
			
			sql = "SELECT IFNULL(MAX(rno), 0)+1 AS rno FROM board_comment";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				rno = rs.getInt(rno);
			}
			sql = "INSERT INTO board_comment (rno, num, writer, content, regDate, rref, rstep, rlev) values(?,?,?,?,SYSDATE(),?,0,0)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, rno);
			pstmt.setInt(2, num);
			pstmt.setString(3, writer);
			pstmt.setString(4, content);
			pstmt.setInt(5, rno);
			
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
	//댓글 삭제 기능
	public void commentDelete(String inputNum) {
		try {
			String sql = "UPDATE board_comment set ISDELETED = 1 WHERE rno = ?";
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
	
	//대댓글 등록 기능
	public void replyComment(String writer, String content, String rref, String rstep, String rlev) {
		
		
		Connection conn = null;
		int num = 1;
		int rno = 1;
		int replyCommentStep = 0;
		String sql = null;
		
		try {
			String dbURL = "jdbc:mariadb://localhost:3306/jspbook";
			String dbID = "root";
			String dbPassword = "junho";
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			
			//답글이 위치할 step 값을 가져옴
			replyCommentStep = boardReplyCommentSearchStep(rref, rlev, rstep);
			
			if(replyCommentStep > 0) {//만약 replyStep값이 0보다 크다면
				sql = "UPDATE board_comment SET rstep = rstep + 1 where rref = ? and rstep >= ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, Integer.parseInt(rref));
				pstmt.setInt(2, replyCommentStep);
				pstmt.executeUpdate();
			} else {//replyStep이 0인 경우
				sql = "SELECT MAX(RSTEP) FROM board_comment WHERE rref = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, Integer.parseInt(rref));
				rs = pstmt.executeQuery();
				if(rs.next()) replyCommentStep = rs.getInt(1) + 1;
			}
			sql = "SELECT IFNULL(MAX(rno), 0)+1 AS rno FROM board_comment";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				rno = rs.getInt(rno);
			}
			sql = "INSERT INTO board_comment (rno, num, writer, content, regDate, rref, rstep, rlev) values(?,?,?,?,SYSDATE(),?,?,?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, rno);
			pstmt.setInt(2, num);
			pstmt.setString(3, writer);
			pstmt.setString(4, content);
			pstmt.setInt(5, Integer.parseInt(rref));
			pstmt.setInt(6, replyCommentStep);
			pstmt.setInt(7, Integer.parseInt(rlev)+1);
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
	//대댓글 위치 등록
	public int boardReplyCommentSearchStep(String rref, String rlev, String rstep) {
		int replyCommentStep=0;
	
		try {
			String dbURL = "jdbc:mariadb://localhost:3306/jspbook";
			String dbID = "root";
			String dbPassword = "junho";
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			String sql = "SELECT IFNULL(MIN(rstep), 0) from board_comment WHERE rref = ? and rlev <= ? and rstep > ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(rref));
			pstmt.setInt(2, Integer.parseInt(rlev));
			pstmt.setInt(3, Integer.parseInt(rstep));
			rs = pstmt.executeQuery();
		
			if(rs.next()) replyCommentStep = rs.getInt(1);
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
		return replyCommentStep;
	}
}