package com.salt.shop;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class DB<T> {
	
	private Connection connection = null;
	private String tableName;
	private ArrayList<T> dataList;

	DB(){
		
	}
	
	DB(String tableName){
		this.tableName = tableName;
	}
	
	/**
	 * DB에 접속한다.
	 */
	private void open() {
		try {
			String db_url = "jdbc:oracle:thin:@//3.35.62.110:1521/XE";
			String db_id = "SYSTEM";
			String db_pw = "0000";
			this.connection = DriverManager.getConnection(db_url, db_id, db_pw);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * DB 접속을 해제한다.
	 */
	private void close() {
		try {
			if(connection != null) {
				connection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}				
	}
	
	/**
	 * 컬럼명 columnName을 가지고 가장 마지막 인덱스 + 1의 값을 가져온다.
	 * @param columnName
	 */
	public String selectCustNo(String columnName) {
		String userNo = "";
		try {
			
			if (this.connection == null) {
				this.open();
			}
			
			String query = "SELECT NVL(MAX(" + columnName + "), 0) + 1 FROM " + this.tableName;
			PreparedStatement preparedStatement = this.connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				userNo = resultSet.getString(1);
			}
			resultSet.close();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.close();
		}
		return userNo;
	}
	
	
	
	/**
	 * 고객 정보를 가져온다.(고객등급 변형 후)
	 * @param t
	 */
	public void selectUserData(T t) {
		try {
			Class<?> dataClass = t.getClass();
			Field[] dataClassFields = dataClass.getDeclaredFields();
			this.dataList = new ArrayList<T>();

			if (this.connection == null) {
				this.open();
			}
			String query = "SELECT CUSTNO, CUSTNAME, PHONE, ADDRESS, JOINDATE, GRADE_NAME AS GRADE, CITY FROM " + this.tableName + " A LEFT JOIN MEMBER_TBL_GRADE_MASTER B ON A.GRADE = B.GRADE ORDER BY CUSTNO DESC";
			PreparedStatement preparedStatement = this.connection.prepareStatement(query); // 쿼리 전송기능 준비
			ResultSet resultSet = preparedStatement.executeQuery(); // executeQuery 통해서 DB에 명령. select나 show문은 이거 사용. 결과는 resultSet에 담긴다
			while (resultSet.next()) { // 내려갈 다음행이 있다면 true 아니면 false
				T fieldData = (T) dataClass.getDeclaredConstructor().newInstance(); // 생성자를 통해 newInstance 함수를 호출해 인스턴스(객체) 생성
				for (Field field : dataClassFields) {
					String fieldType = field.getType().toString();
					String fieldName = field.getName();
					//System.out.println("fieldType & fieldName : " + fieldType + ", " + fieldName);
					if (fieldType.matches("int")) {
						field.setInt(fieldData, resultSet.getInt(fieldName));
					} else if (fieldType.matches(".*Date")){
						field.set(fieldData, resultSet.getDate(fieldName));
					} else {
						field.set(fieldData, resultSet.getString(fieldName));
					}
				}
				this.dataList.add(fieldData);
			}
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.close();
		}
	}
	
	/**
	 * 고객 정보 리스트를 가져온다.
	 * @param t
	 */
	public ArrayList<T> selectUserArrayList(T t) {
		this.selectUserData(t);
		return this.dataList;
	}
	
	/**
	 * 유저 아이디에 맞는 정보를 가져온다.
	 * @param t
	 */
	public void selectThisUser(T t) {
		try {
			Class<?> dataClass = t.getClass();
			Field[] dataClassFields = dataClass.getDeclaredFields();
			this.dataList = new ArrayList<T>();

			if (this.connection == null) {
				this.open();
			}
			String query = "SELECT * FROM " + this.tableName + " WHERE CUSTNO = ?";
			PreparedStatement preparedStatement = this.connection.prepareStatement(query); // 쿼리 전송기능 준비
			for (Field field : dataClassFields) {
				String fieldType = field.getType().toString();
				String fieldName = field.getName();
				System.out.println(fieldName);
				if (fieldName.equals("custNo")) {
					preparedStatement.setInt(1, field.getInt(t));
					System.out.println(field.getInt(t));
				}
			}
			
			ResultSet resultSet = preparedStatement.executeQuery(); 
			while (resultSet.next()) { // 내려갈 다음행이 있다면 true 아니면 false
				T fieldData = (T) dataClass.getDeclaredConstructor().newInstance(); // 생성자를 통해 newInstance 함수를 호출해 인스턴스(객체) 생성
				for (Field field : dataClassFields) {
					String fieldType = field.getType().toString();
					String fieldName = field.getName();
					System.out.println(fieldName);
					//System.out.println("fieldType & fieldName : " + fieldType + ", " + fieldName);
					if (fieldType.matches("int")) {
						field.setInt(fieldData, resultSet.getInt(fieldName));
					} else if (fieldType.matches(".*Date")){
						field.set(fieldData, resultSet.getDate(fieldName));
					} else {
						field.set(fieldData, resultSet.getString(fieldName));
					}
				}
				System.out.println(query);
				this.dataList.add(fieldData);
			}
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.close();
		}
	}
	
	/**
	 * 해당 유저 리스트를 가져온다.
	 * @param t
	 */
	public ArrayList<T> selectThisUserArrayList(T t) {
		this.selectThisUser(t);
		return this.dataList;
	}
	
	
	/**
	 * 고객 매출 정보를 가져온다.
	 * @param t
	 */
	public void selectUserSalesData(T t) {
		try {
			Class<?> dataClass = t.getClass();
			System.out.println(dataClass);
			Field[] dataClassFields = dataClass.getDeclaredFields();
			this.dataList = new ArrayList<T>();

			if (this.connection == null) {
				this.open();
			}
			String query = "SELECT CUSTNO, CUSTNAME, GRADE, SALES FROM (SELECT A.CUSTNO, A.CUSTNAME, B.GRADE_NAME AS GRADE, (SELECT SUM(C.PRICE * C.AMOUNT) FROM MONEY_TBL_02 C WHERE A.CUSTNO = C.CUSTNO GROUP BY C.CUSTNO) AS SALES FROM MEMBER_TBL_02 A LEFT JOIN MEMBER_TBL_GRADE_MASTER B ON A.GRADE = B.GRADE ORDER BY SALES DESC) WHERE SALES IS NOT NULL";
			PreparedStatement preparedStatement = this.connection.prepareStatement(query); // 쿼리 전송기능 준비
			ResultSet resultSet = preparedStatement.executeQuery(); // executeQuery 통해서 DB에 명령. select나 show문은 이거 사용. 결과는 resultSet에 담긴다
			while (resultSet.next()) { // 내려갈 다음행이 있다면 true 아니면 false
				T fieldData = (T) dataClass.getDeclaredConstructor().newInstance(); // 생성자를 통해 newInstance 함수를 호출해 인스턴스(객체) 생성
				for (Field field : dataClassFields) {
					String fieldType = field.getType().toString();
					String fieldName = field.getName();
					//System.out.println("fieldType & fieldName : " + fieldType + ", " + fieldName);
					if (fieldType.matches("int")) {
						field.setInt(fieldData, resultSet.getInt(fieldName));
					} else if (fieldType.matches(".*Date")){
						field.set(fieldData, resultSet.getDate(fieldName));
					} else {
						field.set(fieldData, resultSet.getString(fieldName));
					}
				}
				this.dataList.add(fieldData);
			}
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.close();
		}
	}
	
	/**
	 * 고객 매출 정보 리스트를 가져온다.
	 * @param t
	 */
	public ArrayList<T> selectUserSalesArrayList(T t) {
		this.selectUserSalesData(t);
		return this.dataList;
	}
	
	/**
	 * 정보를 삽입한다.
	 * @param t
	 */
	public boolean insertData(T t) {
		boolean isSuccess = false;
		try {
			Class<?> dataClass = t.getClass();
			Field[] dataClassFields = dataClass.getDeclaredFields();
			String fieldString = "";
			String valueString = "";
			for (Field field : dataClassFields) {
				if (!fieldString.isEmpty()) {
					fieldString = fieldString + ",";
				}
				String fieldType = field.getType().toString();
				System.out.println(fieldType);
				String fieldName = field.getName();
				fieldString = fieldString + fieldName;
				if (!valueString.isEmpty()) {
					valueString = valueString + ",";
				}
				if (fieldType.matches(".*String") || fieldType.matches(".*Date")) {
					valueString = valueString + "'" + field.get(t) + "'";
				} else {
					valueString = valueString + field.get(t);
				}
			}
			
			if (this.connection == null) {
				this.open();
			}
			
			String query = "INSERT INTO " + this.tableName + "(" + fieldString + ") VALUES(" + valueString + ")";
			System.out.println(query);
			Statement statement = this.connection.createStatement();
			int result = statement.executeUpdate(query);
			statement.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.close();
		}
		return isSuccess;
	}	
	
	/**
	 * 유저 정보를 수정한다.
	 * @param t
	 */
	public boolean updateData(T t) {
		boolean isSuccess = false;
		try {
			Class<?> dataClass = t.getClass();
			Field[] dataClassFields = dataClass.getDeclaredFields();
			String fieldString = "";
			String valueString = "";
			String queryString = "";
			int custNo = 0;
			
			for (Field field : dataClassFields) {
				if (!queryString.isEmpty()) {
					queryString = queryString + ",";
				}
				String fieldType = field.getType().toString();
				String fieldName = field.getName();
				queryString = queryString + fieldName + " = ";
				if (fieldType.matches(".*String") || fieldType.matches(".*Date")) {
					queryString = queryString + "'" + field.get(t) + "'";
				} else {
					queryString = queryString + field.get(t);
				}
				if (fieldName.equals("custNo")) {
					custNo = field.getInt(t);
				}
				//System.out.println(queryString);
			}
			
			if (this.connection == null) {
				this.open();
			}
			
			String query = "UPDATE " + this.tableName + " SET " + queryString + " WHERE custNo=" + custNo;
			System.out.println(query);
			PreparedStatement preparedStatement = this.connection.prepareStatement(query);
			int result = preparedStatement.executeUpdate();
			preparedStatement.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.close();
		}
		
		return isSuccess;
	}
	
}


