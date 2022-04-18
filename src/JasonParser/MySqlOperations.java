/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JasonParser;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tarek
 */
public class MySqlOperations implements SqlOperation {

    Connection con;
    Statement stmt;

    public void Connect() {
        //System.out.println("Connecting");
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/multiroundprocurement", "root", "");
            stmt = con.createStatement();

            //con.close();  
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void CloseConnection() {
        //System.out.println("CloseConnection");
        try {
            // stmt.close();
//            con.commit();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(MySqlOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void InsertToTable(String table, String[] Columns, String[] Values) {

        System.out.println("InsertToTable");
        try {
            String InsertValues = "";
            for (int i = 0; i < Values.length - 1; i++) {
                InsertValues += (Values[i] + ",");
            }
            InsertValues += Values[Values.length - 1];

            String ColumnsValues = "";
            for (int i = 0; i < Columns.length - 1; i++) {
                ColumnsValues += (Columns[i] + ",");
            }
            ColumnsValues += Columns[Columns.length - 1];

            String Query = "insert into " + table + "(" + ColumnsValues + ")" + " values( " + InsertValues + " )";
            System.out.println(Query);
            stmt.executeUpdate(Query);
        } catch (SQLException ex) {
            ex.printStackTrace();

            System.out.println("Whkra");
        }
    }

    public void DeleteFromTable(String Table) {
        try {
            stmt.executeUpdate("Delete From " + Table);
        } catch (SQLException ex) {
            Logger.getLogger(MySqlOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public ResultSet SelectFromTable(String Query) {
        // Open a connection
        try {
            ResultSet rs = stmt.executeQuery(Query);
            return rs;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
