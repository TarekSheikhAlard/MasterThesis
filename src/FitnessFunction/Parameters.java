/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FitnessFunction;

import PSOAlgorithm.PSOInstance;
import static PSOAlgorithm.PSOInstance.chromosomeLength;
import JasonParser.MySqlOperations;
import JasonParser.SqlOperation;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.json.simple.parser.ParseException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.LinkedList;

/**
 *
 * @author Tarek
 */
public class Parameters {

    private static  SqlOperation SO;
    private static ResultSet resultSet;
    public static String startDate = "";
    public static Double inflationRate;
    public static int contractorsCount = 0;
    public static int packagesCount = 0;
    public static String finishDate = "";
    public static String [] timeFrom, timeTo;
    public static  LinkedList<Integer> ll [][];
    public static int chromosomeLength=0;
       public static void iniProjectValue() throws SQLException {
        SO = new MySqlOperations();
        SO.Connect();
        resultSet = SO.SelectFromTable("SELECT * From project where project_id = 0");
        resultSet.next();
        inflationRate = resultSet.getDouble("inflation_rate");
        startDate = resultSet.getString("start_date");
        
                          
        resultSet =   SO.SelectFromTable("select MAX(timeline_to)  as timeline_to  from packages");
        resultSet.next();
        finishDate = resultSet.getString("timeline_to"); 
        
        
        resultSet =   SO.SelectFromTable("SELECT Count(*) chromosomeLength  From contractors cs, packages_contractors pc, packages_products pp , discounts dis where pc.contractors_id = cs.contructor_id AND pp.package_id = pc.package_id AND dis.contractor_id = cs.contructor_id AND dis.product_id = pp.product_id");
        resultSet.next();
        chromosomeLength = resultSet.getInt("chromosomeLength");
        
        
        resultSet =   SO.SelectFromTable("select count(*) contractorsCount from contractors");
        resultSet.next();
        contractorsCount = resultSet.getInt("contractorsCount");
  
        resultSet =   SO.SelectFromTable("select Count(*) packagesCount from packages;");
        resultSet.next();
        packagesCount = resultSet.getInt("packagesCount");
        SO.CloseConnection();
    
    } 
       
       
       
       public static void initializePackages() throws SQLException, java.text.ParseException
      {
         
              ll = new LinkedList [10][50];
              for(int i=0;i<10;i++)
                  for(int j=0;j<50;j++)
                      ll[i][j] = new LinkedList<Integer> ();
              SO.Connect();
              timeFrom = new String [10];
              timeTo = new String [10];
              ResultSet RS = SO.SelectFromTable("select pkc.contractors_id, pkp.product_id  ,pk.package_id , timeline_from , timeline_to from packages pk,packages_contractors pkc,packages_products pkp where pk.package_id = pkc.package_id and pkp.package_id = pk.package_id ");
              while(RS.next())
              {
                String timeline_from =  RS.getString("timeline_from");
                String timeline_to =  RS.getString("timeline_to");
                
                int contractors_id =  RS.getInt("contractors_id");
                int product_id =  RS.getInt("product_id");
                int package_id=  RS.getInt("package_id");
              
                ll[contractors_id][product_id].add(package_id);
                timeFrom[package_id] = timeline_from;
                timeTo[package_id]   = timeline_to;

              }
              SO.CloseConnection();
    }
       
       
       public static    boolean CompareDate(String package_from,String package_to,String dis_from,String dis_to) throws java.text.ParseException
    {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = sdf.parse(package_from);
        Date date2 = sdf.parse(package_to);
        Date date3 = sdf.parse(dis_from);
        Date date4 = sdf.parse(dis_to);
       // System.out.println("date1 : " + date1 + " , " +"date2 : " + date2 + " , date3 : " + date3 + " , date4 : " + date4 + " , " +" date3.before(date1) : " +date3.before(date1) + " , " + " date4.after(date2) : " + date4.after(date2));
        return date3.before(date1) && date4.after(date2);
    }
}
