package vc.voice;

import java.sql.*;

public class SQLHandler {
	static void sqluserinsert(String name, String id, String password, String index) {
		Connection con = null; // 데이터 베이스와 연결을 위한 객체
		PreparedStatement pstmt = null; // SQL 문을 데이터베이스에 보내기위한 객체
		// 1. JDBC Driver Class - com.mysql.cj.jdbc.Driver
		String driver = "com.mysql.cj.jdbc.Driver";
		// 2. 데이터베이스에 연결하기 위한 정보
		String url = "jdbc:mysql://220.67.128.176:3306/voicecheck"; // 연결문자열
		String user = "root"; // 데이터베이스 ID
		String pw = "Bbrother123"; // 데이터베이스 PW
		String SQL = "INSERT INTO user VALUES(?, ?, ?, ?)";
		try { // 1. JDBC 드라이버 로딩 - MySQL JDBC 드라이버의 Driver Class 로딩
			Class.forName(driver);

			// 2. Connection 생성 - .getConnection(연결문자열, DB-ID, DB-PW)
			con = DriverManager.getConnection(url, user, pw);

			// 3. PreParedStatement 객체 생성, 객체 생성시 SQL 문장 저장
			pstmt = con.prepareStatement(SQL);

			// 4. pstmt.set<데이터타입>(? 순서, 값) ex).setString(), .setInt ...
			pstmt.setString(1, name);
			pstmt.setString(2, id);
			pstmt.setString(3, password);
			pstmt.setString(4, index);

			// 5. SQL 문장을 실행하고 결과를 리턴 - SQL 문장 실행 후, 변경된 row 수 int type 리턴
			int r = pstmt.executeUpdate();
			// pstmt.excuteQuery() : select
			// pstmt.excuteUpdate() : insert, update, delete ..
			System.out.println("변경된 row : " + r);
		} catch (SQLException e) {
			System.out.println("[SQL Error : " + e.getMessage() + "]");
		} catch (ClassNotFoundException e1) {
			System.out.println("[JDBC Connector Driver 오류 : " + e1.getMessage() + "]");
		} finally {
			// 사용순서와 반대로 close 함
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	static void insertswearingtext(String id, String swt, String time, String file_name, int lnum) {
		Connection con = null; // 데이터 베이스와 연결을 위한 객체
		PreparedStatement pstmt = null; // SQL 문을 데이터베이스에 보내기위한 객체
		// 1. JDBC Driver Class - com.mysql.cj.jdbc.Driver
		String driver = "com.mysql.cj.jdbc.Driver";
		// 2. 데이터베이스에 연결하기 위한 정보
		String url = "jdbc:mysql://220.67.128.176:3306/voicecheck"; // 연결문자열
		String user = "root"; // 데이터베이스 ID
		String pw = "Bbrother123"; // 데이터베이스 PW
		String SQL = "INSERT INTO log_"+lnum+" VALUES(?, ?, ?, ?)";
		try { // 1. JDBC 드라이버 로딩 - MySQL JDBC 드라이버의 Driver Class 로딩
			Class.forName(driver);

			// 2. Connection 생성 - .getConnection(연결문자열, DB-ID, DB-PW)
			con = DriverManager.getConnection(url, user, pw);

			// 3. PreParedStatement 객체 생성, 객체 생성시 SQL 문장 저장
			pstmt = con.prepareStatement(SQL);

			// 4. pstmt.set<데이터타입>(? 순서, 값) ex).setString(), .setInt ...
			pstmt.setString(1, id);
			System.out.println("id = "+id);
			pstmt.setString(2, swt);
			System.out.println("text = "+swt);
			pstmt.setString(3, time);
			System.out.println("time = "+time);
			pstmt.setString(4, file_name);
			System.out.println("filename = "+file_name);

			// 5. SQL 문장을 실행하고 결과를 리턴 - SQL 문장 실행 후, 변경된 row 수 int type 리턴
			int r = pstmt.executeUpdate();
			// pstmt.excuteQuery() : select
			// pstmt.excuteUpdate() : insert, update, delete ..
			System.out.println("변경된 row : " + r);
		} catch (SQLException e) {
			System.out.println("[SQL Error : " + e.getMessage() + "]");
		} catch (ClassNotFoundException e1) {
			System.out.println("[JDBC Connector Driver 오류 : " + e1.getMessage() + "]");
		} finally {
			// 사용순서와 반대로 close 함
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}


	static int getlognum() {
		System.out.println("i'm in lognum");
		Connection con = null; // 데이터 베이스와 연결을 위한 객체
		Statement stmt = null; // SQL 문을 데이터베이스에 보내기위한 객체
		int result = 0;
		// 1. JDBC Driver Class - com.mysql.cj.jdbc.Driver
		String driver = "com.mysql.cj.jdbc.Driver";
		// 2. 데이터베이스에 연결하기 위한 정보
		String url = "jdbc:mysql://220.67.128.176:3306/voicecheck"; // 연결문자열
		String user = "root"; // 데이터베이스 ID
		String pw = "Bbrother123"; // 데이터베이스 PW
		String SQL = "SELECT lognum FROM lognumber;";
		try { // 1. JDBC 드라이버 로딩 - MySQL JDBC 드라이버의 Driver Class 로딩
			Class.forName(driver);
			// 2. Connection 생성 - .getConnection(연결문자열, DB-ID, DB-PW)
			con = DriverManager.getConnection(url, user, pw);

			// 3. PreParedStatement 객체 생성, 객체 생성시 SQL 문장 저장
			stmt = con.createStatement();
			ResultSet rset = stmt.executeQuery(SQL);
			while (rset.next()) {
				result = rset.getInt(1);
			}
			con.close();
		} catch (SQLException e) {
			System.out.println("[SQL Error : " + e.getMessage() + "]");
		} catch (ClassNotFoundException e1) {
			System.out.println("[JDBC Connector Driver 오류 : " + e1.getMessage() + "]");
		} finally {
			// 사용순서와 반대로 close 함
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	static int getlogcount(int lognum) {
		Connection con = null; // 데이터 베이스와 연결을 위한 객체
		Statement stmt = null; // SQL 문을 데이터베이스에 보내기위한 객체
		int result = 0;
		// 1. JDBC Driver Class - com.mysql.cj.jdbc.Driver
		String driver = "com.mysql.cj.jdbc.Driver";
		// 2. 데이터베이스에 연결하기 위한 정보
		String url = "jdbc:mysql://220.67.128.176:3306/voicecheck"; // 연결문자열
		String user = "root"; // 데이터베이스 ID
		String pw = "Bbrother123"; // 데이터베이스 PW
		String SQL = "SELECT COUNT(*) FROM log_" + lognum + ";";
		try { // 1. JDBC 드라이버 로딩 - MySQL JDBC 드라이버의 Driver Class 로딩
			Class.forName(driver);

			// 2. Connection 생성 - .getConnection(연결문자열, DB-ID, DB-PW)
			con = DriverManager.getConnection(url, user, pw);

			// 3. PreParedStatement 객체 생성, 객체 생성시 SQL 문장 저장
			stmt = con.createStatement();
			ResultSet rset = stmt.executeQuery(SQL);
			while (rset.next()) {
				result = rset.getInt(1);
			}
			con.close();
		} catch (SQLException e) {
			System.out.println("[SQL Error : " + e.getMessage() + "]");
		} catch (ClassNotFoundException e1) {
			System.out.println("[JDBC Connector Driver 오류 : " + e1.getMessage() + "]");
		} finally {
			// 사용순서와 반대로 close 함
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	static void CreateTable(String tName) {
		Connection conn = null; // 데이터 베이스와 연결을 위한 객체
		PreparedStatement pstmt = null; // SQL 문을 데이터베이스에 보내기위한 객체
		ResultSet rs = null, rs2 = null;
		// 1. JDBC Driver Class - com.mysql.cj.jdbc.Driver
		String driver = "com.mysql.cj.jdbc.Driver";
		// 2. 데이터베이스에 연결하기 위한 정보
		String url = "jdbc:mysql://220.67.128.176:3306/voicecheck"; // 연결문자열
		String DbName = "voicecheck";
		String user = "root"; // 데이터베이스 ID
		String pw = "Bbrother123"; // 데이터베이스 PW
		try {
			Class.forName(driver);

			// 2. Connection 생성 - .getConnection(연결문자열, DB-ID, DB-PW)
			conn = DriverManager.getConnection(url, user, pw);
			// information_schema.tables 로 부터 테이블의 존재 유무 확인
			String tableSql = "SELECT table_name FROM information_schema.tables where table_schema = ? and table_name = ?";
			pstmt = conn.prepareStatement(tableSql);
			pstmt.setString(1, DbName);
			pstmt.setString(2, tName);
			rs = pstmt.executeQuery();

			// 테이블이 없다면 테이블 생성
			if (!rs.next()) {
				Statement stmt = conn.createStatement();
				String sql = "create table " + tName + "(" +  "user_id varchar(20), swearing_text text, date datetime"
						+ ");";
				rs2 = stmt.executeQuery(sql);
				stmt.close();
				System.out.println(rs2);
			}
		} catch (Exception e) {
			System.out.println("CreateTable err : " + e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					pstmt.close();
			} catch (Exception e) {
			}
		}
	}

	static Boolean checkuser(String id, String password) {
		Connection con = null; // 데이터 베이스와 연결을 위한 객체
		Statement stmt = null; // SQL 문을 데이터베이스에 보내기위한 객체
		// 1. JDBC Driver Class - com.mysql.cj.jdbc.Driver
		String driver = "com.mysql.cj.jdbc.Driver";
		Boolean result = false;
		// 2. 데이터베이스에 연결하기 위한 정보
		String url = "jdbc:mysql://220.67.128.176:3306/voicecheck"; // 연결문자열
		String user = "root"; // 데이터베이스 ID
		String pw = "Bbrother123"; // 데이터베이스 PW
		String SQL = "select exists (select id,password from user where id = '" + id + "' and password = '" + password
				+ "')as success;";
		try { // 1. JDBC 드라이버 로딩 - MySQL JDBC 드라이버의 Driver Class 로딩
			Class.forName(driver);

			// 2. Connection 생성 - .getConnection(연결문자열, DB-ID, DB-PW)
			con = DriverManager.getConnection(url, user, pw);

			// 3. PreParedStatement 객체 생성, 객체 생성시 SQL 문장 저장
			stmt = con.createStatement();
			ResultSet rset = stmt.executeQuery(SQL);
			while (rset.next()) {
				result = rset.getBoolean(1);
			}
			con.close();
		} catch (SQLException e) {
			System.out.println("[SQL Error : " + e.getMessage() + "]");
		} catch (ClassNotFoundException e1) {
			System.out.println("[JDBC Connector Driver 오류 : " + e1.getMessage() + "]");
		} finally {
			// 사용순서와 반대로 close 함
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		if (result) {
			return true;
		} else {
			return false;
		}
	}

	static Boolean checkduplicate(String id) {
		Connection con = null; // 데이터 베이스와 연결을 위한 객체
		Statement stmt = null; // SQL 문을 데이터베이스에 보내기위한 객체
		// 1. JDBC Driver Class - com.mysql.cj.jdbc.Driver
		String driver = "com.mysql.cj.jdbc.Driver";
		Boolean result = false;
		// 2. 데이터베이스에 연결하기 위한 정보
		String url = "jdbc:mysql://220.67.128.176:3306/voicecheck"; // 연결문자열
		String user = "root"; // 데이터베이스 ID
		String pw = "Bbrother123"; // 데이터베이스 PW
		String SQL = "select exists (select id from user where id = '" + id + "')as success;";
		try { // 1. JDBC 드라이버 로딩 - MySQL JDBC 드라이버의 Driver Class 로딩
			Class.forName(driver);

			// 2. Connection 생성 - .getConnection(연결문자열, DB-ID, DB-PW)
			con = DriverManager.getConnection(url, user, pw);

			// 3. PreParedStatement 객체 생성, 객체 생성시 SQL 문장 저장
			stmt = con.createStatement();
			ResultSet rset = stmt.executeQuery(SQL);
			while (rset.next()) {
				result = rset.getBoolean(1);
			}
			con.close();
		} catch (SQLException e) {
			System.out.println("[SQL Error : " + e.getMessage() + "]");
		} catch (ClassNotFoundException e1) {
			System.out.println("[JDBC Connector Driver 오류 : " + e1.getMessage() + "]");
		} finally {
			// 사용순서와 반대로 close 함
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		if (result) {
			return true;
		} else {
			return false;
		}
	}

	static Boolean checkuserban(String id) {
		Connection con = null; // 데이터 베이스와 연결을 위한 객체
		Statement stmt = null; // SQL 문을 데이터베이스에 보내기위한 객체
		// 1. JDBC Driver Class - com.mysql.cj.jdbc.Driver
		String driver = "com.mysql.cj.jdbc.Driver";
		int result = 0;
		// 2. 데이터베이스에 연결하기 위한 정보
		String url = "jdbc:mysql://220.67.128.176:3306/voicecheck"; // 연결문자열
		String user = "root"; // 데이터베이스 ID
		String pw = "Bbrother123"; // 데이터베이스 PW
		String SQL = "select count(*) as cnt from log_1 where user_id = '" + id + "';";
		try { // 1. JDBC 드라이버 로딩 - MySQL JDBC 드라이버의 Driver Class 로딩
			Class.forName(driver);

			// 2. Connection 생성 - .getConnection(연결문자열, DB-ID, DB-PW)
			con = DriverManager.getConnection(url, user, pw);

			// 3. PreParedStatement 객체 생성, 객체 생성시 SQL 문장 저장
			stmt = con.createStatement();
			ResultSet rset = stmt.executeQuery(SQL);
			while (rset.next()) {
				result = rset.getInt(1);
			}
			con.close();
		} catch (SQLException e) {
			System.out.println("[SQL Error : " + e.getMessage() + "]");
		} catch (ClassNotFoundException e1) {
			System.out.println("[JDBC Connector Driver 오류 : " + e1.getMessage() + "]");
		} finally {
			// 사용순서와 반대로 close 함
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		if (result>5) {
			return true;
		} else {
			return false;
		}
	}
	
	static int countusertext (String id) {
		Connection con = null; // 데이터 베이스와 연결을 위한 객체
		Statement stmt = null; // SQL 문을 데이터베이스에 보내기위한 객체
		// 1. JDBC Driver Class - com.mysql.cj.jdbc.Driver
		String driver = "com.mysql.cj.jdbc.Driver";
		int result = 0;
		// 2. 데이터베이스에 연결하기 위한 정보
		String url = "jdbc:mysql://220.67.128.176:3306/voicecheck"; // 연결문자열
		String user = "root"; // 데이터베이스 ID
		String pw = "Bbrother123"; // 데이터베이스 PW
		String SQL = "select count(*) as cnt from log_1 where user_id = '" + id + "';";
		try { // 1. JDBC 드라이버 로딩 - MySQL JDBC 드라이버의 Driver Class 로딩
			Class.forName(driver);

			// 2. Connection 생성 - .getConnection(연결문자열, DB-ID, DB-PW)
			con = DriverManager.getConnection(url, user, pw);

			// 3. PreParedStatement 객체 생성, 객체 생성시 SQL 문장 저장
			stmt = con.createStatement();
			ResultSet rset = stmt.executeQuery(SQL);
			while (rset.next()) {
				result = rset.getInt(1);
			}
			con.close();
		} catch (SQLException e) {
			System.out.println("[SQL Error : " + e.getMessage() + "]");
		} catch (ClassNotFoundException e1) {
			System.out.println("[JDBC Connector Driver 오류 : " + e1.getMessage() + "]");
		} finally {
			// 사용순서와 반대로 close 함
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	static UserLog showtext(String id) {
		Connection con = null; // 데이터 베이스와 연결을 위한 객체
		Statement stmt = null; // SQL 문을 데이터베이스에 보내기위한 객체
		UserLog ul = new UserLog(id);
		int i = 0;
		// 1. JDBC Driver Class - com.mysql.cj.jdbc.Driver
		String driver = "com.mysql.cj.jdbc.Driver";
		String result = "";
		String time = "";
		// 2. 데이터베이스에 연결하기 위한 정보
		String url = "jdbc:mysql://220.67.128.176:3306/voicecheck"; // 연결문자열
		String user = "root"; // 데이터베이스 ID
		String pw = "Bbrother123"; // 데이터베이스 PW
		String SQL = "select swaearing_text, date from log_1 where user_id = '" + id + "';";
		try { // 1. JDBC 드라이버 로딩 - MySQL JDBC 드라이버의 Driver Class 로딩
			Class.forName(driver);

			// 2. Connection 생성 - .getConnection(연결문자열, DB-ID, DB-PW)
			con = DriverManager.getConnection(url, user, pw);

			// 3. PreParedStatement 객체 생성, 객체 생성시 SQL 문장 저장
			stmt = con.createStatement();
			ResultSet rset = stmt.executeQuery(SQL);
			while (rset.next()) {
				ul.setid(rset.getString(1), i);
				ul.setdate(rset.getString(2), i);
				i++;
			}
			System.out.println(result);
			con.close();
		} catch (SQLException e) {
			System.out.println("[SQL Error : " + e.getMessage() + "]");
		} catch (ClassNotFoundException e1) {
			System.out.println("[JDBC Connector Driver 오류 : " + e1.getMessage() + "]");
		} finally {
			// 사용순서와 반대로 close 함
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return ul;
	}
	
	static int getindex(String ID) {
		Connection con = null; // 데이터 베이스와 연결을 위한 객체
		Statement stmt = null; // SQL 문을 데이터베이스에 보내기위한 객체
		// 1. JDBC Driver Class - com.mysql.cj.jdbc.Driver
		String driver = "com.mysql.cj.jdbc.Driver";
		int result = 1;
		// 2. 데이터베이스에 연결하기 위한 정보
		String url = "jdbc:mysql://220.67.128.176:3306/voicecheck"; // 연결문자열
		String user = "root"; // 데이터베이스 ID
		String pw = "Bbrother123"; // 데이터베이스 PW
		String SQL = "select recnum from user where id = '" + ID + "';";
		try { // 1. JDBC 드라이버 로딩 - MySQL JDBC 드라이버의 Driver Class 로딩
			Class.forName(driver);

			// 2. Connection 생성 - .getConnection(연결문자열, DB-ID, DB-PW)
			con = DriverManager.getConnection(url, user, pw);

			// 3. PreParedStatement 객체 생성, 객체 생성시 SQL 문장 저장
			stmt = con.createStatement();
			ResultSet rset = stmt.executeQuery(SQL);
			while (rset.next()) {
				result = rset.getInt(1);
			}
			con.close();
		} catch (SQLException e) {
			System.out.println("[SQL Error : " + e.getMessage() + "]");
		} catch (ClassNotFoundException e1) {
			System.out.println("[JDBC Connector Driver 오류 : " + e1.getMessage() + "]");
		} finally {
			// 사용순서와 반대로 close 함
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	static void updateindex(String ID, int rec_num) {
		Connection con = null; // 데이터 베이스와 연결을 위한 객체
		PreparedStatement pstmt = null; // SQL 문을 데이터베이스에 보내기위한 객체
		// 1. JDBC Driver Class - com.mysql.cj.jdbc.Driver
		String driver = "com.mysql.cj.jdbc.Driver";
		// 2. 데이터베이스에 연결하기 위한 정보
		String url = "jdbc:mysql://220.67.128.176:3306/voicecheck"; // 연결문자열
		String user = "root"; // 데이터베이스 ID
		String pw = "Bbrother123"; // 데이터베이스 PW
		String SQL = "update user set recnum=? where id=?;";
		try { // 1. JDBC 드라이버 로딩 - MySQL JDBC 드라이버의 Driver Class 로딩
			Class.forName(driver);

			// 2. Connection 생성 - .getConnection(연결문자열, DB-ID, DB-PW)
			con = DriverManager.getConnection(url, user, pw);

			// 3. PreParedStatement 객체 생성, 객체 생성시 SQL 문장 저장
			pstmt = con.prepareStatement(SQL);

			// 4. pstmt.set<데이터타입>(? 순서, 값) ex).setString(), .setInt ...
			pstmt.setInt(1, rec_num);
			pstmt.setString(2, ID);

			// 5. SQL 문장을 실행하고 결과를 리턴 - SQL 문장 실행 후, 변경된 row 수 int type 리턴
			int r = pstmt.executeUpdate();
			// pstmt.excuteQuery() : select
			// pstmt.excuteUpdate() : insert, update, delete ..
			System.out.println("변경된 row : " + r);
		} catch (SQLException e) {
			System.out.println("[SQL Error : " + e.getMessage() + "]");
		} catch (ClassNotFoundException e1) {
			System.out.println("[JDBC Connector Driver 오류 : " + e1.getMessage() + "]");
		} finally {
			// 사용순서와 반대로 close 함
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
