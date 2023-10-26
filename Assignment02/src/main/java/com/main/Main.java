package com.main;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.connection.DBConnection;
import com.dao.imp.DAOImpl;

public class Main extends JFrame {
	private JTable table;
	private JTextField txtName;
	private DefaultTableModel model;
	private JLabel lblFile;
	private String customerNo = "";

	Main() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Image icon = Toolkit.getDefaultToolkit().getImage("D:\\Kirshan Lal\\AS400 Project\\HBL-Logo.png");
		setIconImage(icon);
		setBounds(200, 200, 700, 500);
		setTitle("Habib Bank Limited");
		getContentPane().setLayout(null);
		// ScrollPane
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(120, 120, 320, 100);
		getContentPane().add(scrollPane);

		// Table
		table = new JTable();
		scrollPane.setViewportView(table);

		JTextField textField = new JTextField();
		textField.setBounds(150, 60, 200, 20);
		getContentPane().add(textField);

		JLabel lable = new JLabel("Enter Customer No:");
		lable.setBounds(18, 55, 200, 30);
		getContentPane().add(lable);

		JLabel heading = new JLabel("Customer Data Searching");
		heading.setBounds(180, 10, 200, 30);
		getContentPane().add(heading);
		// Button Save
		JButton btnSave = new JButton("Search");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				customerNo = textField.getText();
				try {
					String str = PopulateData();
					if (str.equals("SUCCESS")) {
						JOptionPane.showMessageDialog(null, "Customer Data Fetched");

					} else {
						JOptionPane.showMessageDialog(null, "Customer Number Not Found");
					}

				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

		btnSave.setBounds(390, 56, 80, 30);
		getContentPane().add(btnSave);

	}

	public String PopulateData() throws SQLException {

		table.setSize(800, 500);
		table.setModel(new DefaultTableModel());
		// Model for Table
		model = (DefaultTableModel) table.getModel();
		model.addColumn("Customer Short Name");
		model.addColumn("ACOUNT NO");

		Statement stmt = null;
		ResultSet rs = null;
		Connection con = DBConnection.getConnection();
		stmt = con.createStatement();

		String customerNumer = null;
		// String customerNo="MAIL67";
		// String customer="SELECT * FROM NEPF WHERE NEEAN='"+customerNo+"'";
		String customer = "SELECT * FROM SCPF WHERE SCAN ='" + customerNo + "'";
		rs = stmt.executeQuery(customer);

		while (rs.next()) {
			customerNumer = rs.getString("SCAN");
		}

		if (customerNumer != null) {
			String query = "SELECT sc.SCSHN, np.NEEAN FROM SCPF as sc INNER JOIN NEPF as np ON sc.SCAN=np.NEAN WHERE sc.SCAN='"
					+ customerNo + "'";
			rs = stmt.executeQuery(query);
			int row = 0;
			while ((rs != null) && (rs.next())) {
				String customerName = rs.getString("SCSHN");
				String accoutNo = rs.getString("NEEAN");
				System.out.println(customerName + " " + accoutNo);
				model.addRow(new Object[0]);
				model.setValueAt(rs.getString("SCSHN"), row, 0);
				model.setValueAt(rs.getString("NEEAN"), row, 1);
				row++;
			}
			table.getColumnModel().getColumn(0).setPreferredWidth(150);
			table.getColumnModel().getColumn(1).setPreferredWidth(150);
			rs.close();
		} else {
			return "ERROR";
		}
		return "SUCCESS";

	}

	public static void main(String[] args) {

		DBConnection db = new DBConnection();
		DBConnection.getConnection();
		DAOImpl dao = new DAOImpl();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Main frame = new Main();
				frame.setVisible(true);
			}
		});

		try {
			@SuppressWarnings("static-access")
			String data = dao.selectCustomerByNo();
			if (data.equals("SUCCESS")) {
				System.out.println("Customer Return::::::::::::::::: SUCCESS");
			} else
				System.out.println("Customer Return::::::::::::::::: ERROR");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
