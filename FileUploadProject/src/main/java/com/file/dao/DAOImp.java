package com.file.dao;

import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.file.db.connection.DBConnection;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ErrorCompletingRequestException;

public class DAOImp {
	static Connection con = DBConnection.getConnection();
	static Statement stmt = null;

	public static void saveData() throws IOException, SQLException, AS400SecurityException, ErrorCompletingRequestException, InterruptedException, PropertyVetoException {
		@SuppressWarnings("deprecation")
		CommandCall command = new CommandCall(new AS400("10.200.65.227", "TSTOWNER", "TSTOWNER"));
		FileInputStream fstream = new FileInputStream("D:\\StopPayments.txt");
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		int count = 0;
		int result = 0;
		stmt = con.createStatement();
		command.run("CLRPFM FILE(IFALIB/PCSTPF)");
		
		while ((strLine = br.readLine()) != null) {

			if (count != 0) {

				String BRANCHCODE = strLine.substring(0, 4);
				String ACNO = strLine.substring(5, 19);
				String FIRSTCHEQUENO = strLine.substring(20, 32);
				String LASTCHEQUENO = strLine.substring(33, 44);
				String STOPCURRENCY = strLine.substring(46, 49);
				System.out.println("STR: " + BRANCHCODE + "\tSTR2:  " + ACNO + "\tSTR3: " + FIRSTCHEQUENO + "\tSTR4:"
						+ LASTCHEQUENO + "\tSTR5: " + STOPCURRENCY);

				String query = "INSERT INTO PCSTPF (PCBRNM,PCEXAC,PCFRCQ,PCLSCQ,PCCUR)VALUES('" + BRANCHCODE + "','"
					+ ACNO + "','" + FIRSTCHEQUENO + "','" + LASTCHEQUENO + "','" + STOPCURRENCY + "')";

				result = stmt.executeUpdate(query);

				count++;
			} else {
				count++;
			}
			if (result > 0) {
				System.out.println("Data Inserted Successfully....!");
			} else
				System.out.println("Data Not Inserted Successfully....!");

		}
		System.out.println("Total Records: " + (count - 1));
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "null" })
	public static void PopulateData() {
		JTable table = new JTable();
		DefaultTableModel model;

		table.setModel(new DefaultTableModel()); // Clear model
		// Model for Table
		model = (DefaultTableModel) table.getModel();
		model.addColumn("PCBRNM");
		model.addColumn("PCEXAC");
		model.addColumn("PCFRCQ");
		model.addColumn("PCLSCQ");
		model.addColumn("PCCUR");
		try {
			stmt = con.createStatement();
			String sql = "SELECT * FROM  PCSTPF";
			ResultSet res = stmt.executeQuery(sql);
			int row = 0;

			while ((res != null) && (res.next())) {
				model.addRow(new Object[0]);
				model.setValueAt(res.getString("PCBRNM"), row, 0);
				model.setValueAt(res.getString("PCEXAC"), row, 1);
				model.setValueAt(res.getString("PCFRCQ"), row, 2);
				model.setValueAt(res.getString("PCLSCQ"), row, 3);
				model.setValueAt(res.getString("PCCUR"), row, 4);

				row++;
			}
			res.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}

	}
}
