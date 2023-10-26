package com.file.main;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.file.dao.DAOImp;
import com.file.db.connection.DBConnection;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.ErrorCompletingRequestException;

public class MainCLontroller extends JFrame {

	private JTable table;

	private JTextField txtName;

	private DefaultTableModel model;

	private JLabel lblFile;

	public MainCLontroller() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Image icon = Toolkit.getDefaultToolkit().getImage("D:\\Kirshan Lal\\AS400 Project\\HBL-Logo.png");
		setIconImage(icon);
		setBounds(200, 200, 700, 500);
		setTitle("Habib Bank Limited");
		getContentPane().setLayout(null);
		// ScrollPane
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(62, 30, 600, 300);
		getContentPane().add(scrollPane);

		// Table
		table = new JTable();
		scrollPane.setViewportView(table);
		// Label File
		lblFile = new JLabel("File");
		lblFile.setBounds(200, 360, 180, 14);
		getContentPane().add(lblFile);
		// Button Choose

		JButton btnChoose = new JButton("Browse");

		btnChoose.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				JFileChooser fileopen = new JFileChooser();
				int ret = fileopen.showDialog(null, "Choose File");
				if (ret == JFileChooser.APPROVE_OPTION) {
					lblFile.setText(fileopen.getSelectedFile().toString());
				}
			}
		});
		btnChoose.setBounds(101, 350, 80, 30);
		getContentPane().add(btnChoose);

		// Button Save
		JButton btnSave = new JButton("Upload");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					try {
						DAOImp.saveData();
					} catch (AS400SecurityException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ErrorCompletingRequestException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (PropertyVetoException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					JOptionPane.showMessageDialog(null, "Data Uploaded Successfully");
				} catch (java.io.IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (java.sql.SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} // Save Data
				PopulateData();
				JOptionPane.showMessageDialog(null, "Data Populated Successfully");
			}
		});

		btnSave.setBounds(430, 350, 80, 30);
		getContentPane().add(btnSave);
		// Windows Loaded

		/*
		 * addWindowListener(new WindowAdapter() {
		 * 
		 * @Override public void windowOpened(WindowEvent arg0) { PopulateData(); } });
		 */
	}

	public void PopulateData() {
		table.setSize(800, 500);
		table.setModel(new DefaultTableModel());
		// Model for Table
		model = (DefaultTableModel) table.getModel();
		model.addColumn("BRANCH CODE");
		model.addColumn("ACOUNT NO");
		model.addColumn("FIRST CHEQUE");
		model.addColumn("LAST CHEQUE");
		model.addColumn("STOP CURRENCY");

		Statement stmt = null;
		Connection con = DBConnection.getConnection();
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

			table.getColumnModel().getColumn(0).setPreferredWidth(150);
			table.getColumnModel().getColumn(1).setPreferredWidth(150);
			table.getColumnModel().getColumn(2).setPreferredWidth(150);
			table.getColumnModel().getColumn(3).setPreferredWidth(150);
			table.getColumnModel().getColumn(4).setPreferredWidth(150);

			res.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		DAOImp d = new DAOImp();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				MainCLontroller frame = new MainCLontroller();
				frame.setVisible(true);
			}
		});

	}

}
