package com.dao.imp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.connection.DBConnection;

public class DAOImpl {
	static ResultSet rs = null;
	static Statement stmt = null;

	@SuppressWarnings("unused")
	public static String selectCustomerByNo() throws SQLException {

		String customerNumer = null;
		Connection con = DBConnection.getConnection();
		stmt = con.createStatement();
		String customerNo = "MAIL67";
		// String customer="SELECT * FROM NEPF WHERE NEEAN='"+customerNo+"'";
		String customer = "SELECT * FROM SCPF WHERE SCAN ='" + customerNo + "'";
		rs = stmt.executeQuery(customer);
		while (rs.next()) {
			customerNumer = rs.getString("SCAN");
		}

		if (customerNumer != null) {
			// String query="SELECT sc.SCSHN, np.NEEAN FROM SCPF as sc INNER JOIN NEPF as np
			// ON sc.SCAB=np.NEAB AND sc.SCAN=np.NEEAN AND sc.SCAS=np.NEAS";
			String query = "SELECT sc.SCSHN, np.NEEAN FROM SCPF as sc INNER JOIN NEPF as np ON sc.SCAN=np.NEEAN";
			rs = stmt.executeQuery(query);

			if (rs != null) {
				while (rs.next()) {
					String customerName = rs.getString("SCSHN");
					String accoutNo = rs.getString("NEEAN");
					System.out.println(customerName + " " + accoutNo);

				}
			} else {
				return "ERROR";
			}
		} else {
			System.out.println("CustomerNo Not Found.");
			return "ERROR";
		}

		return "SUCCESS";

	}

}
