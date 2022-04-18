/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JasonParser;

import java.sql.ResultSet;

/**
 *
 * @author Tarek
 */
public interface SqlOperation {

    public void InsertToTable(String table, String[] Columns, String[] Values);

    public void Connect();

    public void CloseConnection();

    public void DeleteFromTable(String Table);

    public ResultSet SelectFromTable(String Query);
}
