/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import FitnessFunction.FitnessFunction;
import FitnessFunction.Statistics;
import BeeColonyOptimizationAlgorithm.BeeColonyOptimizationAlgorithm;
import FireFlyAlgorithm.FireFlyAlgorithm;
import GreyWolfOptimizationAlgorithm.GreyWolfOptimizationAlgorithm;
import MegroOptimizationAlgorithm.MegroOptimizationAlgorithm;
import MemeticAlgorithm.MemeticAlgorithm;
import PSOAlgorithm.PSOAlgorithm;
import PSOAlgorithm.PSOInstance;
//import com.sun.org.glassfish.external.statistics.Statistic;
import DifferentialEvolutionAlgorithm.DifferentialEvolutionAlgorithm;
import JasonParser.ProjectJsonParser;
import JasonParser.ProjectJsonParser2;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javafx.scene.control.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Tarek
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    
    
    
    public static void InsertToExcel( Map<String, Object[]> studentData) throws FileNotFoundException, IOException
    {
                  // workbook object
        XSSFWorkbook workbook = new XSSFWorkbook();
  
        // spreadsheet object
        XSSFSheet spreadsheet = workbook.createSheet(" ResultDataByCode ");
        XSSFRow row;
         Set<String> keyid = studentData.keySet();
  
        int rowid = 0;
  
        // writing the data into the sheets...
  
        for (String key : keyid) {
  
            row = spreadsheet.createRow(rowid++);
            Object[] objectArr = studentData.get(key);
            int cellid = 0;
  
            for (Object obj : objectArr) {
                XSSFCell cell = row.createCell(cellid++);
                cell.setCellValue((String)obj);
            }
        }
  
        // .xlsx is the format for Excel Sheets...
        // writing the workbook into the file...
        FileOutputStream out = new FileOutputStream(
            new File("C:\\Users\\Tim Arden\\Desktop\\MasterThesis\\master thesis2\\ExcelResult\\ResultsDataset1.xlsx"));
  
        workbook.write(out);
        out.close();
    
    }
  /*
    public static void main(String[] args) throws SQLException, java.text.ParseException, Exception {
        
        /* try {
            ProjectJsonParser PJP = new ProjectJsonParser();
            PJP.ParseProject("C:\\Users\\Tim Arden\\Desktop\\master thesis\\DataSet\\data.json");
        } catch (Exception e) {
            System.out.println(e);
        }*/
        /*
        try {
            ProjectJsonParser2 PJP = new ProjectJsonParser2();
            PJP.ParseProject("C:\\Users\\Tim Arden\\Desktop\\MasterThesis\\master thesis2\\DataSet\\data1-5p3c.json");
        } catch (Exception e) {
            System.out.println(e);
        }
    */
    
    
       /* try {
            ProjectJsonParser PJP = new ProjectJsonParser();
            PJP.ParseProject("C:\\Users\\Tim Arden\\Desktop\\master thesis\\DataSet\\data.json");
        } catch (Exception e) {
            System.out.println(e);
        }
        */
        /*
        try {
            ProjectJsonParser PJP = new ProjectJsonParser();
            PJP.ParseProject("C:\\Users\\Tim Arden\\Desktop\\MasterThesis\\master thesis2\\DataSet\\data.json");
        } catch (Exception e) {
            System.out.println(e);
        }*/
        
        
        
        /*
           ProjectJsonParser PJP = new ProjectJsonParser();
         PJP.ParseProject("C:\\Users\\Tarek\\Desktop\\master thesis\\DataSet\\data.json");
         PJP.ParseProject("C:\\Users\\Tarek\\Desktop\\master thesis\\DataSet\\data1-5p3c.json");
         PJP.ParseProject("C:\\Users\\Tarek\\Desktop\\master thesis\\DataSet\\data1-5p4c.json");
         PJP.ParseProject("C:\\Users\\Tarek\\Desktop\\master thesis\\DataSet\\data1-5p5c.json");
         PJP.ParseProject("C:\\Users\\Tarek\\Desktop\\master thesis\\DataSet\\data1-6p3c.json");
         PJP.ParseProject("C:\\Users\\Tarek\\Desktop\\master thesis\\DataSet\\data1-6p4c.json");
         PJP.ParseProject("C:\\Users\\Tarek\\Desktop\\master thesis\\DataSet\\data1-6p5c.json");*/
         
  // }
 
     public static void main(String[] args) throws SQLException, java.text.ParseException, Exception {
     
         

  

        Integer sheetRow=0;
  
  
        int count =1;
                Map<String, Object[]> studentData
            = new TreeMap<String, Object[]>();
  

        while(count<=10)
        {
            FitnessFunction f1;
            Double CF;
            Double SCF;
            // System.out.println("i: " +i);

            Long start , end , elapsedTime; 


            int P,I,N;
            P =25;
            I = 25;
            N=5;
            System.out.println("Count: " + count);
            /*
            System.out.println();
            System.out.println("MemeticAlgorithm: ");
            System.out.println();
            start = System.currentTimeMillis();

            MemeticAlgorithm MA = new MemeticAlgorithm(P);
            MA.Run(I,N);
            end = System.currentTimeMillis();
            f1 = new FitnessFunction (MA.getSolution());
            CF = f1.CalculateFitnessFunction();
            SCF =new Statistics(f1.GetAllPlayerBenefits()).getC();
            System.out.println(CF.toString());
            System.out.println(SCF.toString());

            elapsedTime = end - start;
            System.out.println("Time Diffrenece: " + elapsedTime);

           studentData.put(
            (sheetRow++).toString(),
            new Object[] { "Memetic", CF.toString(), SCF.toString(),elapsedTime.toString() });
           // InsertToExcel(studentData);

*/
            System.out.println();
            System.out.println("MegroOptimizationAlgorithm: ");
            System.out.println();

            start = System.currentTimeMillis();
            MegroOptimizationAlgorithm MOA = new MegroOptimizationAlgorithm(P);
            MOA.Run(I,N);
            end = System.currentTimeMillis();
            //MOA.PrintResult(MOA.getSolution());
            f1 = new FitnessFunction (MOA.getSolution());
            CF = f1.CalculateFitnessFunction();
            SCF =new Statistics(f1.GetAllPlayerBenefits()).getC();
            System.out.println(CF.toString());
            System.out.println(SCF.toString());

            elapsedTime = end - start;
            System.out.println("Time Diffrenece: " + elapsedTime);

           studentData.put(
            (sheetRow++).toString(),
            new Object[] { "Megro", CF.toString(), SCF.toString(),elapsedTime.toString() });

           
/*           
            System.out.println();       
            System.out.println("GreyWolfOptimizationAlgorithm: ");
            System.out.println();
            start = System.currentTimeMillis();
            GreyWolfOptimizationAlgorithm GWOA = new GreyWolfOptimizationAlgorithm(P);
            GWOA.Run(I);
            end = System.currentTimeMillis();
            //GWOA.PrintResult(GWOA.getSolution());
            f1 = new FitnessFunction (GWOA.getSolution());
            CF = f1.CalculateFitnessFunction();
            SCF =new Statistics(f1.GetAllPlayerBenefits()).getC();
            System.out.println(CF.toString());
            System.out.println(SCF.toString());

            elapsedTime = end - start;
            System.out.println("Time Diffrenece: " + elapsedTime);

           studentData.put(
            (sheetRow++).toString(),
            new Object[] { "Grey Wolf", CF.toString(), SCF.toString(),elapsedTime.toString() });

  

            System.out.println();
            System.out.println("DifferentialEvolutionAlgorithm: ");
            System.out.println();
            start = System.currentTimeMillis();
            DifferentialEvolutionAlgorithm DEA = new DifferentialEvolutionAlgorithm(P);
            DEA.Run(I, 0.2, 0.9);
            end = System.currentTimeMillis();
            //DEA.PrintResult(DEA.getSolution());
            f1 = new FitnessFunction (DEA.getSolution());
            CF = f1.CalculateFitnessFunction();
            SCF =new Statistics(f1.GetAllPlayerBenefits()).getC();
            System.out.println(CF.toString());
            System.out.println(SCF.toString());

            elapsedTime = end - start;
            System.out.println("Time Diffrenece: " + elapsedTime);

           studentData.put(
            (sheetRow++).toString(),
            new Object[] { "Differential Evolution", CF.toString(), SCF.toString(),elapsedTime.toString() });

  


            System.out.println();
            System.out.println("FireFlyAlgorithm: ");
            System.out.println();
            start = System.currentTimeMillis();
            FireFlyAlgorithm FFA = new FireFlyAlgorithm(P,I);
            end = System.currentTimeMillis();
            //FFA.PrintResult(FFA.getSolution());
            f1 = new FitnessFunction (FFA.getSolution());
            CF = f1.CalculateFitnessFunction();
            SCF =new Statistics(f1.GetAllPlayerBenefits()).getC();
            System.out.println(CF.toString());
            System.out.println(SCF.toString());

            elapsedTime = end - start;
            System.out.println("Time Diffrenece: " + elapsedTime);

           studentData.put(
            (sheetRow++).toString(),
            new Object[] { "Fire Fly", CF.toString(), SCF.toString(),elapsedTime.toString() });

            System.out.println();
            System.out.println("BeeColonyOptimizationAlgorithm: ");
            System.out.println();
            start = System.currentTimeMillis();
            BeeColonyOptimizationAlgorithm BCA = new BeeColonyOptimizationAlgorithm(P);
            BCA.Run(I);
            end = System.currentTimeMillis();
            //BCA.PrintResult(BCA.getSolution());
            f1 = new FitnessFunction (BCA.getSolution());
            CF = f1.CalculateFitnessFunction();
            SCF =new Statistics(f1.GetAllPlayerBenefits()).getC();
            System.out.println(CF.toString());
            System.out.println(SCF.toString());

            elapsedTime = end - start;
            System.out.println("Time Diffrenece: " + elapsedTime);

           studentData.put(
            (sheetRow++).toString(),
            new Object[] { "Bee Colony", CF.toString(), SCF.toString(),elapsedTime.toString() });



            System.out.println();
            System.out.println("PSOAlgorithm: ");
            System.out.println();
            start = System.currentTimeMillis();
            PSOAlgorithm PSO = new PSOAlgorithm(P);
            PSO.RunIteration(I);
            end = System.currentTimeMillis();
            //PSO.PrintResult();
            f1 = new FitnessFunction (PSOInstance.gBest);
           CF = f1.CalculateFitnessFunction();
            SCF =new Statistics(f1.GetAllPlayerBenefits()).getC();
            System.out.println(CF.toString());
            System.out.println(SCF.toString());

            elapsedTime = end - start;
            System.out.println("Time Diffrenece: " + elapsedTime);

           studentData.put(
            (sheetRow++).toString(),
            new Object[] { "PSO", CF.toString(), SCF.toString(),elapsedTime.toString() });
*/
            System.out.println("-----------------------------------");
            count++;
        }
        InsertToExcel(studentData);
        
     }
}
