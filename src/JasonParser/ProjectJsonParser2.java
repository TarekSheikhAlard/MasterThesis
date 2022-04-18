/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JasonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Tim Arden
 */
public class ProjectJsonParser2 {
    
    SqlOperation SO = new MySqlOperations();

    public void ParseProject(String ProjectPath) throws java.text.ParseException {
        JSONParser parser = new JSONParser();
        SO.Connect();
        this.RefreshTables();
        try {
            Object obj = parser.parse(new FileReader(ProjectPath));

            JSONObject jsonObject = (JSONObject) obj;

            Long project_id = (Long) jsonObject.get("project_id");
            System.out.println(project_id);

            Double inflation_rate = (Double) jsonObject.get("inflation_rate");
            System.out.println(inflation_rate);

            String start_date = (String) jsonObject.get("start_date");
            System.out.println(start_date);

            DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse(start_date);

            SO.InsertToTable("project",
                    new String[]{"project_id", "inflation_rate", "start_date"},
                    new String[]{project_id.toString(), inflation_rate.toString(), "\"" + new SimpleDateFormat("yyyy-MM-dd").format(date) + "\""});

            System.out.println("After Insert To PRoject");
            this.ParseProducts(jsonObject, project_id);
            System.out.println("After Insert To Product");

            this.ParseContractors(jsonObject, project_id);
            System.out.println("After Insert To Contractors");
            this.ParsePackages(jsonObject, project_id,date);
             System.out.println("After Insert To Packages");

            // loop array
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SO.CloseConnection();
    }

    public void ParseProducts(JSONObject jsonObject, Long project_id) {
        System.out.println("ParseProducts");
        JSONArray packages = (JSONArray) jsonObject.get("product");
        Iterator<JSONObject> iterator = packages.iterator();
        while (iterator.hasNext()) {
            JSONObject packagesObject = iterator.next();
            System.out.println("So : " + SO);
            SO.InsertToTable("product", new String[]{"project_id", "product_id", "description", "unit"},
                    new String[]{project_id.toString(), SqlStringQueryOperation.AddQouts(packagesObject.get("product_id").toString()), SqlStringQueryOperation.AddQouts(packagesObject.get("description").toString()), SqlStringQueryOperation.AddQouts(packagesObject.get("unit").toString())});

            System.out.println(packagesObject.toJSONString());
        }
    }

    public void ParsePackages(JSONObject jsonObject, Long project_id,Date startDate) throws java.text.ParseException {
        JSONArray packages = (JSONArray) jsonObject.get("packages");
        Iterator<JSONObject> iterator = packages.iterator();
        while (iterator.hasNext()) {
            JSONObject packagesObject = iterator.next();
            System.out.println("ParsePackages 1");
            System.out.println("ParsePackages 2");
            DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date executiontimeObject = sdf.parse((String) packagesObject.get("execution_time"));

            //Date timeline_from = sdf.parse((String) timelineObject.get("from"));
           // Date timeline_to = sdf.parse((String) executiontimeObject.get("to"));
            System.out.println("time line : " + new SimpleDateFormat("yyyy-MM-dd").format(executiontimeObject));
            
            Date referenceDate = executiontimeObject;
            Calendar c = Calendar.getInstance(); 
            c.setTime(referenceDate); 
            c.add(Calendar.MONTH, -1);

  
            startDate = c.getTime();
           // startDate.setTime(executiontimeObject.getTime()- (1000*60*60*24*30));
            //System.out.println("startDate: "+startDate + " , " + startDate.getTime());
            System.out.println("executiontimeObject: "+executiontimeObject + " , " + executiontimeObject.getTime());
            SO.InsertToTable("packages",
                    new String[]{
                        "project_id",
                        "package_id",
                        "estimated_cost",
                        "timeline_from",
                        "timeline_to"
                    },
                    new String[]{project_id.toString(),
                        packagesObject.get("package_id").toString(),
                        packagesObject.get("estimated_cost").toString(),
                        SqlStringQueryOperation.AddQouts(new SimpleDateFormat("yyyy-MM-dd").format(startDate)),
                        SqlStringQueryOperation.AddQouts(new SimpleDateFormat("yyyy-MM-dd").format(executiontimeObject)),});
            System.out.println("ParsePackages 3");
            this.ParsePackagesProducts(packagesObject, (Long) packagesObject.get("package_id"));
            this.ParsePackagesContractors(packagesObject, (Long) packagesObject.get("package_id"));
            System.out.println(packagesObject.toJSONString());
            
            System.out.println("ParsePackages 4");
        }
    }

    public void ParseContractors(JSONObject jsonObject, Long project_id) throws java.text.ParseException {
        JSONArray contractors = (JSONArray) jsonObject.get("contractors");
        Iterator<JSONObject> iterator = contractors.iterator();
        while (iterator.hasNext()) {
            JSONObject contractorsObject = iterator.next();
            /*System.out.println(Arrays.toString(new String[]{
                
             contractorsObject.get("contractor_id").toString(),
             contractorsObject.get("quality").toString(),
             SqlStringQueryOperation.AddQouts(contractorsObject.get("description").toString()),
             contractorsObject.get("relationship").toString(),
             project_id.toString()
             }));*/

            SO.InsertToTable("contractors", new String[]{
                "contructor_id",
                "quality",
                "description",
                "relationship",
                "project_id"
            },
                    new String[]{
                        contractorsObject.get("contractor_id").toString(),
                        contractorsObject.get("capacity").toString(),
                        SqlStringQueryOperation.AddQouts(contractorsObject.get("description").toString()),
                        contractorsObject.get("relationship").toString(),
                        project_id.toString()
                    });

            this.ParseContractorsProducts(contractorsObject, (Long) contractorsObject.get("contractor_id"));
            System.out.println(contractorsObject.toJSONString());
               
           this.ParseStrategies((JSONObject) contractorsObject, (Long) contractorsObject.get("contractor_id"));
        }
    }

    public void ParseContractorsProducts(JSONObject jsonObject, Long contractor_id) throws java.text.ParseException {
        JSONArray packages = (JSONArray) jsonObject.get("products");
        Iterator<JSONObject> iterator = packages.iterator();
        while (iterator.hasNext()) {
            JSONObject packagesObject = iterator.next();
            SO.InsertToTable("contractors_products", new String[]{
                "product_id",
                "sell_price",
                "buy_price",
                "contractor_id"
            },
                    new String[]{
                        packagesObject.get("product_id").toString(),
                        packagesObject.get("sell_price").toString(),
                        packagesObject.get("buy_price").toString(),
                        contractor_id.toString()
                    });

           
            System.out.println("ParseContractorsProducts : " + packagesObject.toJSONString());
        }
     
    }

    public void ParsePackagesProducts(JSONObject jsonObject, Long package_id) {
        JSONArray packageProducts = (JSONArray) jsonObject.get("products");
        Iterator<JSONObject> iterator = packageProducts.iterator();
        while (iterator.hasNext()) {
            JSONObject packageProductObject = iterator.next();
            SO.InsertToTable("packages_products",
                    new String[]{
                        "package_id",
                        "product_id",
                        "quantity"
                    },
                    new String[]{package_id.toString(),
                        packageProductObject.get("product_id").toString(),
                        packageProductObject.get("quantity").toString()
                    });
            System.out.println(packageProductObject.toJSONString());
        }
    }

    public void ParsePackagesContractors(JSONObject jsonObject, Long package_id) {
        JSONArray packageContractors = (JSONArray) jsonObject.get("joined_contractors");
        Iterator<Long> iterator = packageContractors.iterator();
        while (iterator.hasNext()) {
            Long packageContractorsObject = iterator.next();
            SO.InsertToTable("packages_contractors",
                    new String[]{
                        "package_id",
                        "contractors_id"
                    },
                    new String[]{
                        package_id.toString(),
                        packageContractorsObject.toString(),});
            System.out.println(packageContractorsObject);
        }
    }

    
    public void ParseStrategies(JSONObject jsonObject, Long contractor_id) throws java.text.ParseException {
    
        System.out.println("ParseStrategies Start");
        
        JSONArray strategiesContractors = (JSONArray) jsonObject.get("strategies");
        System.out.println(strategiesContractors.toString());
        Iterator<JSONObject> iterator = strategiesContractors.iterator();
        while (iterator.hasNext()) {
            JSONObject strategiesObject  =iterator.next();
            JSONArray  strategiesProducts = (JSONArray) strategiesObject.get("products");
            System.out.println(strategiesProducts.toString());

            Iterator<JSONObject> iterator2 = strategiesProducts.iterator();
                while (iterator2.hasNext()) {
                     JSONObject productsObject  = iterator2.next();
                     System.out.println(productsObject.toString());
                      this.ParseDiscounts((JSONObject) productsObject, contractor_id);
                }

        }
        System.out.println("ParseStrategies End");

    }
    public void ParseDiscounts(JSONObject jsonObject, Long contractor_id) throws java.text.ParseException {
        
        System.out.println("Discounts Start");
        JSONArray discounts = (JSONArray) jsonObject.get("discounts");
        System.out.println(jsonObject.toString());
          System.out.println("1");
        Iterator<JSONObject> iterator = discounts.iterator();
        while (iterator.hasNext()) {
            JSONObject discountsObject = iterator.next();
            System.out.println("2");
            DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date from = sdf.parse(discountsObject.get("from").toString());
            System.out.println("3");
            Date to = sdf.parse(discountsObject.get("to").toString());
            System.out.println("4");
            System.out.println(Arrays.toString(new String[]{
                SqlStringQueryOperation.AddQouts(new SimpleDateFormat("yyyy-MM-dd").format(from)),
                SqlStringQueryOperation.AddQouts(new SimpleDateFormat("yyyy-MM-dd").format(to)),
                discountsObject.get("rate").toString(),
                contractor_id.toString(),
                jsonObject.get("product_id").toString()
            }));
            System.out.println("5");
            SO.InsertToTable("discounts", new String[]{
                "dis_from",
                "dis_to",
                "rate",
                "contractor_id",
                "product_id"
            },
                    new String[]{
                        SqlStringQueryOperation.AddQouts(new SimpleDateFormat("yyyy-MM-dd").format(from)),
                        SqlStringQueryOperation.AddQouts(new SimpleDateFormat("yyyy-MM-dd").format(to)),
                        discountsObject.get("rate").toString(),
                        contractor_id.toString(),
                        jsonObject.get("product_id").toString()
                    });
            System.out.println(discountsObject.toJSONString());
        }
        
        System.out.println("Discounts End");


    }

    public void RefreshTables() {
        SO.DeleteFromTable("contractors");
        SO.DeleteFromTable("contractors_products");
        SO.DeleteFromTable("discounts");
        SO.DeleteFromTable("packages");
        SO.DeleteFromTable("packages_contractors");
        SO.DeleteFromTable("packages_products");
        SO.DeleteFromTable("product");
        SO.DeleteFromTable("project");

    }
}
