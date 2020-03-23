package stepdefinition;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class stepfile 
{

	public static WebDriver driver;
	public static XSSFWorkbook wb, wb1;
	public static FileInputStream fis1;
	public static Sheet sheet, sheet1;
	public static FileOutputStream fileOut, fileOut1;
	public String sql, sql1;
	public Connection conn, conn1;
	public String  data3, data4, data2, smefilter, usfilter, pno, pname;
	   
	 public Double data, data1, smedouble, usdouble, total, totalhr, rounded, smevalue, usvalue;
	 public String  data13, data14, smefilter1, usfilter1, data12, pno1, pname1;
	 public Double data10, data11, smedouble1, usdouble1, total1, totalhr1, rounded1;
	 
	@Given("^DB and Excel file connection$")
	public void DB_and_Excel_file_connection() throws Throwable 
	{
		fis1 = new FileInputStream("C:\\Users\\devaiah.nb\\Desktop\\Latest\\Modified\\Combined.xlsx");
		wb = new XSSFWorkbook(fis1);
		sheet = wb.getSheet("Three");
		conn = DriverManager.getConnection("jdbc:sqlserver://FFX-SQL\\SETTYDB;databaseName=ptpd_feb2020","pt_migration","migration@pass");
		
		
		
		sql = "select Top 2 ut.USProjectID, st.SMEProjectID, ut.USHrs,st.SMEHrs,PR.project_number,PR.project_name\r\n" + 
				"from (select sum(cast(Hours as float)) as USHrs, ProjectID as USProjectID\r\n" + 
				"from tbl_ushours group by ProjectID)ut\r\n" + 
				"Join (select sum(cast(TotalHours as float)) as SMEHrs,\r\n" + 
				"ProjectID as SMEProjectID from tbl_Timesheet  group by ProjectID)\r\n" + 
				"st on ut.USProjectID=st.SMEProjectID\r\n" + 
				"join (select project_id,project_number,project_name from project)as PR\r\n" + 
				"on PR.project_id=st.SMEProjectID";
		  

		 
	}

	@When("^Compare the Total Hours in DB and PT$")
	public void Compare_the_Total_Hours_in_DB_and_PT() throws Throwable 
	{
		PreparedStatement ps =  conn.prepareStatement(sql);
		ResultSet resultSet = ps.executeQuery();  
		
		  int row = 2;
		  List<Double> al = new ArrayList<Double>(); 
		  List<Double> al2 = new ArrayList<Double>();
		  List<String> al3 = new ArrayList<String>();
		  List<String> al4 = new ArrayList<String>();
		  List<String> al5 = new ArrayList<String>();
		  
		  
		  System.out.println("-----Total Hours-----");
		  
		  while(resultSet.next()) 
		  {
			  
			System.out.println(resultSet.getDouble("USHrs")+"     "+resultSet.getDouble("SMEHrs"));
		    
		    al.add(resultSet.getDouble("SMEHrs"));
		    al2.add(resultSet.getDouble("USHrs"));
		    al3.add(resultSet.getString("SMEProjectID"));
		    al4.add(resultSet.getString("project_number"));
		    al5.add(resultSet.getString("project_name"));
		    

		  }
		  
	       //System.out.println(al);
	       System.setProperty("webdriver.chrome.driver", "./Drivers/chromedriver.exe");
		   driver = new ChromeDriver();
		   driver.manage().window().maximize();
		   
		   for (int i = 0; i < al.size(); i++) 
	        {
	  	     Row dataRow = sheet.createRow(row);
	  	     data = al.get(i);
	  	     data1 =al2.get(i);
	  	     data2 =al3.get(i);
	  	     pno = al4.get(i);
	  	     pname =al5.get(i);
	  	     //System.out.println("Project Manager: "+name);
	  	    
	  	     driver.get("http://ffx-web/TrackerQC/projecttracker.aspx?ProjectID="+data2+"");

			     //System.out.println("Inside");
	  	      Cell dataID = dataRow.createCell(1);
	  	      dataID.setCellValue(data2);
	  	      Cell datapno = dataRow.createCell(2);
	  	      datapno.setCellValue(pno);
	  	   
	  	      Cell datapname = dataRow.createCell(3);
	  	      datapname.setCellValue(pname);
	  	      Cell dataNameCell = dataRow.createCell(4);
			  dataNameCell.setCellValue("DB: "+data);
			  Cell dataAddressCell = dataRow.createCell(5);
			  dataAddressCell.setCellValue("DB: "+data1);
			     
			     data3 = driver.findElement(By.xpath("//a[contains(@id,'ahGrandTotal')]")).getText();
			     
			     smefilter = data3.replaceAll("[^a-zA-Z0-9]", " ");  
			     
			     System.out.println(smefilter);

	           //BigDecimal smevalue = new BigDecimal(smefilter.replace(",", ""));
			     smevalue = Double.parseDouble(smefilter);
			     smedouble = smevalue.doubleValue();
			     System.out.println("smevalue: "+smedouble);
			     
			     data4 = driver.findElement(By.xpath("//a[contains(@id,'usAhGrandTotal')]")).getText();
			     
			     usfilter = data4.replaceAll("[^a-zA-Z0-9]", " "); 
			 
			     System.out.println(usfilter);
			     
			     //BigDecimal usvalue = new BigDecimal(usfilter.replace(",", ""));
			     usvalue = Double.parseDouble(usfilter);
			     usdouble = usvalue.doubleValue();
			     System.out.println("usvalue: "+usdouble);

			     Cell SMCell = dataRow.createCell(6);
			     SMCell.setCellValue("PT: "+smedouble);
			     
			     Cell USCell = dataRow.createCell(7);
			     USCell.setCellValue("PT: "+usdouble);

			     total = data + data1;
			     System.out.println("DB: "+total);
			     
			     Cell totalDBCell = dataRow.createCell(8);
			     totalDBCell.setCellValue("DB: "+total);
			     
			     totalhr = smedouble + usdouble;
			     System.out.println("PT: "+totalhr);
			     
			     
			     Cell totalPTCell = dataRow.createCell(9);
			     totalPTCell.setCellValue("PT: "+totalhr);
			     
	           row = row + 1;

	           rounded = (double) Math.round(total * 100) / 100;
	           System.out.println(rounded);
	           
	           if(rounded.equals(totalhr))
	           {
	          	 Cell Result1 = dataRow.createCell(10);
	          	 Result1.setCellValue("PASS"); 
	           }
	           else
	           {
	          	 Cell Result2 = dataRow.createCell(10);
	          	 Result2.setCellValue("FAIL"); 
	           }
	      
	        }

		   
	}

	@Then("^Update the Result in Excel$")
	public void Update_the_Result_in_Excel() throws Throwable 
	{
		// Not Needed
	    /*String outputDirPath = "C:\\Users\\devaiah.nb\\Desktop\\Latest\\Modified\\Combined.xlsx";
	    fileOut = new FileOutputStream(outputDirPath);
	    wb.write(fileOut);
	    fileOut.close();*/
	}
	

   @Given("^DB and Excel file connection For Total Cost$")
    public void DB_and_Excel_file_connection_For_Total_Cost() throws Throwable 
    {  
	   // Not Needed                                      
	     /*FileInputStream fis = new FileInputStream("C:\\Users\\devaiah.nb\\Desktop\\Latest\\Modified\\Combined.xlsx");
		 wb1 = new XSSFWorkbook(fis1);
		 sheet1 = wb.getSheet("Three");*/
		conn1 = DriverManager.getConnection("jdbc:sqlserver://FFX-SQL\\SETTYDB;databaseName=ptpd_feb2020","pt_migration","migration@pass");
		
		 sql1 = "select Top 2 ut.USProjectID, st.SMEProjectID, ut.USCost, st.SMECost, PR.project_number,PR.project_name\r\n" + 
		 		"from (select sum(cast(Hours as float)*110) as USCost, ProjectID as USProjectID from tbl_ushours group by ProjectID)ut \r\n" + 
		 		"				Join (select sum(cast(TotalHours as float)*30) as SMECost, ProjectID as SMEProjectID from tbl_Timesheet group by ProjectID)\r\n" + 
		 		"				st on ut.USProjectID=st.SMEProjectID\r\n" + 
		 		"				join (select project_id,project_number,project_name from project)as PR\r\n" + 
		 		"on PR.project_id=st.SMEProjectID";
    }

   @When("^Compare the Total Cost in DB and PT$")
   public void Compare_the_Total_Cost_in_DB_and_PT() throws Throwable 
   {
	   PreparedStatement ps1 =  conn1.prepareStatement(sql1);
		ResultSet resultSet1 = ps1.executeQuery(); 
		
		  int row = 6;
		  List<Double> al = new ArrayList<Double>(); 
		  List<Double> al2 = new ArrayList<Double>();
		  List<String> al3 = new ArrayList<String>();
		  List<String> al4 = new ArrayList<String>();
		  List<String> al5 = new ArrayList<String>();
		  
		  System.out.println("-----Total Cost-----");
		  
		  while(resultSet1.next()) 
		  {
			
			System.out.println(resultSet1.getDouble("USCost")+"     "+resultSet1.getDouble("SMECost"));
		    
		    al.add(resultSet1.getDouble("SMECost"));
		    al2.add(resultSet1.getDouble("USCost"));
		    al3.add(resultSet1.getString("SMEProjectID"));
		    al4.add(resultSet1.getString("project_number"));
		    al5.add(resultSet1.getString("project_name"));

		  }
		  

		   for (int i = 0; i < al.size(); i++) 
	          {
	    	     Row dataRow = sheet.createRow(row);
	    	     data10 = al.get(i);
	    	     data11 =al2.get(i);
	    	     data12 = al3.get(i);
	    	     pno1 = al4.get(i);
		  	     pname1 =al5.get(i);
	    	     //System.out.println("Project Manager: "+name);
	    	    
	    	     driver.get("http://ffx-web/TrackerQC/projecttracker.aspx?ProjectID="+data12+"");
	 
			     //System.out.println("Inside");
	    	    Cell dataID = dataRow.createCell(1);
	    	    dataID.setCellValue(data12);
	  	  	    Cell datapno1 = dataRow.createCell(2);
	  	  	    datapno1.setCellValue(pno1);
	    	     
	    	     Cell datapname1 = dataRow.createCell(3);
	    	     datapname1.setCellValue(pname1);
	    	     Cell dataNameCell = dataRow.createCell(4);
			     dataNameCell.setCellValue("DB: "+data10);
			     Cell dataAddressCell = dataRow.createCell(5);
			     dataAddressCell.setCellValue("DB: "+data11);
			     
			     
			     
			     data13 = driver.findElement(By.xpath("//span[contains(@id,'ahGrandTotalDollars')]")).getText();
			     smefilter1 = data13.substring(1);
			     
                 BigDecimal smevalue1 = new BigDecimal(smefilter1.replace(",", ""));
			     
			     smedouble1 = smevalue1.doubleValue();
			     System.out.println("smevalue: "+smedouble1);
			     
			     data14 = driver.findElement(By.xpath("//span[contains(@id,'usAhGrandTotalDollars')]")).getText();
			     usfilter1 = data14.substring(1);
			     
			     BigDecimal usvalue = new BigDecimal(usfilter1.replace(",", ""));
			     
			     usdouble1 = usvalue.doubleValue();
			     System.out.println("usvalue: "+usdouble1);

			     Cell SMCell = dataRow.createCell(6);
			     SMCell.setCellValue("PT: "+smedouble1);
			     
			     Cell USCell = dataRow.createCell(7);
			     USCell.setCellValue("PT: "+usdouble1);

			     total1 = data10 + data11;
			     System.out.println("DB: "+total1);
			     
			     Cell totalDBCell = dataRow.createCell(8);
			     totalDBCell.setCellValue("DB: "+total1);
			     
			     totalhr1 = smedouble1 + usdouble1;
			     System.out.println("PT: "+totalhr1);
			     
			     
			     Cell totalPTCell = dataRow.createCell(9);
			     totalPTCell.setCellValue("PT: "+totalhr1);
			     
	             row = row + 1;

	             rounded = (double) Math.round(total1 * 100) / 100;
	             System.out.println(rounded);
	             
	             if(rounded.equals(totalhr1))
	             {
	            	 Cell Result1 = dataRow.createCell(10);
	            	 Result1.setCellValue("PASS"); 
	             }
	             else
	             {
	            	 Cell Result2 = dataRow.createCell(10);
	            	 Result2.setCellValue("FAIL"); 
	             }
	        
	          }
   }

   @Then("^Update the Total Cost Status in Excel$")
   public void Update_the_Total_Cost_Status_in_Excel() throws Throwable 
   {
	   String outputDirPath = "C:\\Users\\devaiah.nb\\Desktop\\Latest\\Modified\\Combined.xlsx";
	   fileOut1 = new FileOutputStream(outputDirPath);
	    wb.write(fileOut1);
	    fileOut1.close();
   
   }
}
