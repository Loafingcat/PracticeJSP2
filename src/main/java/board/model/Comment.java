package board.model;

import java.util.Date;

public class Comment {
	
	private int rno;//댓글번호
	private int num;//게시물번호
	private String writer;//작성자
	private String content;//내용
	private Date regDate;//작성일
	private int rref;
	private int rstep;
	private int rlev;
	public int getRref() {
		return rref;
	}
	public void setRref(int rref) {
		this.rref = rref;
	}
	public int getRstep() {
		return rstep;
	}
	public void setRstep(int rstep) {
		this.rstep = rstep;
	}
	public int getRlev() {
		return rlev;
	}
	public void setRlev(int rlev) {
		this.rlev = rlev;
	}
	public int getRno() {
		return rno;
	}
	public void setRno(int rno) {
		this.rno = rno;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
}
