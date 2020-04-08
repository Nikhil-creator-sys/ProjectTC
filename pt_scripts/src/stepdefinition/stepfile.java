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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class stepfile 
{

	public static WebDriver driver;
	public static XSSFWorkbook wb, wb1;
	public static FileInputStream fis1, fis2;
	public static Sheet sheet, sheet1, sheet2, sheet3, sheet4, sheet5, sheet6;
	public static FileOutputStream fileOut, fileOut1;
	public String sql, sql1, sql2, sql3, sql4, sql5, sql6;
	public static double sum;
	public Connection conn, conn1, conn2, conn3, conn4, conn5, conn6;
	public String  data3, data4, data2, smefilter, usfilter, pno, pname;
	   
	 public Double data, data1, smedouble, usdouble, total, totalhr, rounded, smevalue, usvalue;
	 public String  data13, data14, smefilter1, usfilter1, data12, pno1, pname1;
	 public Double data10, data11, smedouble1, usdouble1, total1, totalhr1, rounded1;
	 
	@Given("^DB and Excel file connection$")
	public void DB_and_Excel_file_connection() throws Throwable 
	{
		fis1 = new FileInputStream("C:\\Users\\devaiah.nb\\Desktop\\Latest\\Modified\\Combined.xlsx");
		wb = new XSSFWorkbook(fis1);
		sheet = wb.getSheet("Hours");
		conn = DriverManager.getConnection("jdbc:sqlserver://FFX-SQL\\SETTYDB;databaseName=ptpd_march2020","ptpd_marchUser","migration@pass");

		sql = "select Top 10 ut.USProjectID, st.SMEProjectID, ut.USHrs,st.SMEHrs,PR.project_number,PR.project_name, ut.USDate, st.SMEDate\r\n" + 
				"from (select sum(cast(Hours as float)) as USHrs, ProjectID as USProjectID, Todaydate as USDate\r\n" + 
				"from tbl_ushours where Todaydate > '2013-12-31' group by ProjectID,Todaydate)ut\r\n" + 
				"Join (select sum(cast(TotalHours as float)) as SMEHrs,\r\n" + 
				"ProjectID as SMEProjectID, Todaydate as SMEDate from tbl_Timesheet where Todaydate > '2013-12-31' group by ProjectID,Todaydate)\r\n" + 
				"st on ut.USProjectID=st.SMEProjectID  \r\n" + 
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
	  	    
	  	     driver.get("http://ffx-web/TrackerQC/projecttracker.aspx?ProjectID="+data2+"");

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

			     smevalue = Double.parseDouble(smefilter);
			     smedouble = smevalue.doubleValue();
			     System.out.println("smevalue: "+smedouble);
			     
			     data4 = driver.findElement(By.xpath("//a[contains(@id,'usAhGrandTotal')]")).getText();
			     
			     usfilter = data4.replaceAll("[^a-zA-Z0-9]", " "); 
			 
			     System.out.println(usfilter);

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
	  
		sheet1 = wb.getSheet("Cost");
		conn1 = DriverManager.getConnection("jdbc:sqlserver://FFX-SQL\\SETTYDB;databaseName=ptpd_march2020","ptpd_marchUser","migration@pass");
		
		 sql1 = "select Top 10 ut.USProjectID, st.SMEProjectID, ut.USCost, st.SMECost, PR.project_number,PR.project_name, ut.USDate, st.SMEDate\r\n" + 
		 		"from (select sum(cast(Hours as float)*110) as USCost, ProjectID as USProjectID, Todaydate as USDate from tbl_ushours where Todaydate > '2013-12-31' group by ProjectID, Todaydate)ut \r\n" + 
		 		"	Join (select sum(cast(TotalHours as float)*30) as SMECost, ProjectID as SMEProjectID, Todaydate as SMEDate from tbl_Timesheet where Todaydate > '2013-12-31' group by ProjectID,TodayDate) \r\n" + 
		 		"		st on ut.USProjectID=st.SMEProjectID join (select project_id,project_number,project_name from project)as PR\r\n" + 
		 		"		 		on PR.project_id=st.SMEProjectID";
    }

   @When("^Compare the Total Cost in DB and PT$")
   public void Compare_the_Total_Cost_in_DB_and_PT() throws Throwable 
   {
	    PreparedStatement ps1 =  conn1.prepareStatement(sql1);
		ResultSet resultSet1 = ps1.executeQuery(); 
		
		  int row = 2;
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
	    	     Row dataRow1 = sheet1.createRow(row);
	    	     data10 = al.get(i);
	    	     data11 =al2.get(i);
	    	     data12 = al3.get(i);
	    	     pno1 = al4.get(i);
		  	     pname1 =al5.get(i);

	    	    
	    	     driver.get("http://ffx-web/TrackerQC/projecttracker.aspx?ProjectID="+data12+"");

	    	    Cell dataID = dataRow1.createCell(1);
	    	    dataID.setCellValue(data12);
	  	  	    Cell datapno1 = dataRow1.createCell(2);
	  	  	    datapno1.setCellValue(pno1);
	    	     
	    	     Cell datapname1 = dataRow1.createCell(3);
	    	     datapname1.setCellValue(pname1);
	    	     Cell dataNameCell = dataRow1.createCell(4);
			     dataNameCell.setCellValue("DB: "+data10);
			     Cell dataAddressCell = dataRow1.createCell(5);
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

			     Cell SMCell = dataRow1.createCell(6);
			     SMCell.setCellValue("PT: "+smedouble1);
			     
			     Cell USCell = dataRow1.createCell(7);
			     USCell.setCellValue("PT: "+usdouble1);

			     total1 = data10 + data11;
			     System.out.println("DB: "+total1);
			     
			     Cell totalDBCell = dataRow1.createCell(8);
			     totalDBCell.setCellValue("DB: "+total1);
			     
			     totalhr1 = smedouble1 + usdouble1;
			     System.out.println("PT: "+totalhr1);
			     
			     
			     Cell totalPTCell = dataRow1.createCell(9);
			     totalPTCell.setCellValue("PT: "+totalhr1);
			     
	             row = row + 1;

	             rounded = (double) Math.round(total1 * 100) / 100;
	             System.out.println(rounded);
	             
	             if(rounded.equals(totalhr1))
	             {
	            	 Cell Result1 = dataRow1.createCell(10);
	            	 Result1.setCellValue("PASS"); 
	             }
	             else
	             {
	            	 Cell Result2 = dataRow1.createCell(10);
	            	 Result2.setCellValue("FAIL"); 
	             }
	        
	          }
   }

   @Then("^Update the Total Cost Status in Excel$")
   public void Update_the_Total_Cost_Status_in_Excel() throws Throwable 
   {
	  /* String outputDirPath = "C:\\Users\\devaiah.nb\\Desktop\\Latest\\Modified\\Combined.xlsx";
	   fileOut = new FileOutputStream(outputDirPath);
	   wb.write(fileOut);
	   fileOut.close();*/
   
   }
   
   @Given("^DB and Excel file connection For Project Margins$")
   public void DB_and_Excel_file_connection_For_Project_Margins() throws Throwable 
   {
	   sheet2 = wb.getSheet("Margin");
	   conn2 = DriverManager.getConnection("jdbc:sqlserver://FFX-SQL\\SETTYDB;databaseName=ptpd_march2020","ptpd_marchUser","migration@pass");
		
	   sql2 = "select Top 10 A.project_id,sum(A.key_value) as 'Key Values',P.project_number\r\n" + 
	   		"from tbl_projectAllocation A left join project P on A.project_id=P.project_id\r\n" + 
	   		"where A.key_name IN  ('task_10' , 'task_20' , 'task_30','task_40','task_50',\r\n" + 
	   		"'task_8','task_80','task_9','task_90') AND\r\n" + 
	   		"convert(varchar,P.create_date,23) > '2013-12-31'\r\n" + 
	   		"group by A.project_id,P.project_number";
   }

   @When("^Compare the Margins present in DB and PT$")
   public void Compare_the_Margins_present_in_DB_and_PT() throws Throwable 
   {
        PreparedStatement ps2 =  conn2.prepareStatement(sql2);
		ResultSet resultSet2 = ps2.executeQuery();    

		int row = 2;
		List<String> al = new ArrayList<String>();  
		List<Double> al2 = new ArrayList<Double>();
		List<String> al3 = new ArrayList<String>();
		
		System.out.println("-----Project Margin-----");
		
		while(resultSet2.next()) 
		  {
			System.out.println(resultSet2.getString("project_id")+"     "+resultSet2.getString("project_number")+"    "+resultSet2.getString("Key Values"));
		    
			al.add(resultSet2.getString("project_id"));
			al2.add(resultSet2.getDouble("Key Values"));
			al3.add(resultSet2.getString("project_number"));
	
		  }
		   String data, data1;
		   Double data2;
	         
	         for (int i = 0; i < al2.size(); i++) 
	          {
	        	     sum=0;
		    	     Row dataRow2 = sheet2.createRow(row);
		    	     data = al.get(i);
		    	     data1 = al3.get(i);
		    	     data2 =al2.get(i);
		    	     sum = sum + al2.get(i);

			         driver.get("http://ffx-web/TrackerQC/projecttracker.aspx?ProjectID="+data+"");
			         
			         String value = driver.findElement(By.xpath("(//span[contains(@id,'taskPending')])[1]")).getText();
			         Thread.sleep(2000);
			         
			         Double d = Double.valueOf(value);
			         
			         System.out.println("% Margin: "+d);

			         System.out.println("\n"+sum+"");
			          
			          if(sum==100)
			          {
			        	  System.out.println("\n"+"No % Margin");
			          }
			          else if(sum<100)
			          {
			        	  System.out.println("\n"+"+"+(100-sum)+" Margin");
			          }
			          else
			          {
			        	  System.out.println("\n"+"-"+(sum-100)+" Margin");
			        	  
			          }
			         
		    	       Cell dataNameCell = dataRow2.createCell(1);
				       dataNameCell.setCellValue(data);
				       Cell Cell1 = dataRow2.createCell(2);
				       Cell1.setCellValue(data1);
				       Cell dataAddressCell = dataRow2.createCell(3);
				       dataAddressCell.setCellValue(data2);
				       Cell Cell2 = dataRow2.createCell(4);
				       Cell2.setCellValue("PT: "+d);
				       Cell Cell3 = dataRow2.createCell(5);
				       Cell3.setCellValue("DB: "+(100-sum));
				       
				       if(d.equals((100-sum)))
				       {
				    	   Cell Cell4 = dataRow2.createCell(6);
					       Cell4.setCellValue("Same Margin");
				       }
				       else
				       {
				    	   Cell Cell5 = dataRow2.createCell(6);
					       Cell5.setCellValue("FAIL");
				       }
				       
				       row = row + 1;
	          }
   }

   @Then("^Update the existing Project Margins in Excel$")
   public void Update_the_existing_Project_Margins_in_Excel() throws Throwable 
   {
	   /*String outputDirPath = "C:\\Users\\devaiah.nb\\Desktop\\Latest\\Modified\\Combined.xlsx";
	   fileOut = new FileOutputStream(outputDirPath);
	   wb.write(fileOut);
	   fileOut.close();*/
   }
   
   @Given("^DB and Excel file connection For Project Fees$")
   public void DB_and_Excel_file_connection_For_Project_Fees() throws Throwable 
   {
	    sheet3 = wb.getSheet("Fees");
		conn3 = DriverManager.getConnection("jdbc:sqlserver://FFX-SQL\\SETTYDB;databaseName=ptpd_march2020","ptpd_marchUser","migration@pass");
		
		sql3 = "Select Top 10 * FROM project  WHERE create_date> '2013-12-31'  and design_fee is not NULL and Status IS NOT NULL AND project_number like 'S%'";
   }

   @When("^Compare the Project Fees present in DB and PT$")
   public void Compare_the_Project_Fees_present_in_DB_and_PT() throws Throwable 
   {
	   
	    PreparedStatement ps3 =  conn3.prepareStatement(sql3);
		ResultSet resultSet3 = ps3.executeQuery();   

		  int row = 2;
		  List<String> al = new ArrayList<String>();  
		  List<String> al2 = new ArrayList<String>();
		  List<String> al3 = new ArrayList<String>();
		  
		  System.out.println("-----Project Fees-----");
		  while(resultSet3.next()) 
		  {
			System.out.println(resultSet3.getString("project_number")+"     "+resultSet3.getString("design_fee"));
		    
		    al.add(resultSet3.getString("project_number"));
		    al2.add(resultSet3.getString("design_fee"));
		    al3.add(resultSet3.getString("project_id"));
	
		  }
		  
		   driver.get("http://ffx-web/TrackerQC/");
		   Thread.sleep(3000);
		    
		   driver.findElement(By.id("tobesearch")).sendKeys(" ");
		   driver.findElement(By.id("searchforrecordbtn")).click();
		   Thread.sleep(5000);
		   
		   String data, data1, data2;
		   
	          for (int i = 0; i < al.size(); i++) 
	          {
	    	     Row dataRow3 = sheet3.createRow(row);
	    	     data = al.get(i);
	    	     data1 =al2.get(i);
	    	     data2 =al3.get(i);
	    	   
	             String cost = driver.findElement(By.xpath("(//a[contains(@href,'/TrackerQC/projecttracker.aspx?ProjectID="+data2+"')])[8]")).getText();
			    
	             Cell IDdata = dataRow3.createCell(1);
	             IDdata.setCellValue(data2);
	    	     Cell dataNameCell = dataRow3.createCell(2);
			     dataNameCell.setCellValue(data);
			     Cell dataAddressCell = dataRow3.createCell(3);
			     dataAddressCell.setCellValue("DB: "+data1);
			     Cell Cell1 = dataRow3.createCell(4);
			     Cell1.setCellValue("PT: "+cost);

	             row = row + 1;
	             
	             if(data1.equals(cost))
	             {
	            	 Cell Cell2 = dataRow3.createCell(5);
				     Cell2.setCellValue("Same Fees");
	             }
	             else
	             {
	            	 Cell Cell2 = dataRow3.createCell(5);
				     Cell2.setCellValue("FAIL");
	             }
	          }
   }

   @Then("^Update the Project Fees details in Excel$")
   public void Update_the_Project_Fees_details_in_Excel() throws Throwable 
   {
	 /*  String outputDirPath = "C:\\Users\\devaiah.nb\\Desktop\\Latest\\Modified\\Combined.xlsx";
	   fileOut = new FileOutputStream(outputDirPath);
	   wb.write(fileOut);
	   fileOut.close();*/
   }
   
   @Given("^DB and Excel file connection For Project Background$")
   public void DB_and_Excel_file_connection_For_Project_Background() throws Throwable 
   {
	   sheet4 = wb.getSheet("Background");
	   conn4 = DriverManager.getConnection("jdbc:sqlserver://FFX-SQL\\SETTYDB;databaseName=ptpd_march2020","ptpd_marchUser","migration@pass");
		
	   sql4 = "select Top 10 P.project_number, G.* from tbl_googlemap G LEFT JOIN \r\n" + 
	   		"project P on G.Projectid = P.project_id where googlemapvalue like '<iframe src=%' or Websitevalue like 'http://www.%' and\r\n" + 
	   		"convert(varchar,P.create_date,23) > '2013-12-31'";
   }

   @When("^Compare the Project Background present in DB and PT$")
   public void Compare_the_Project_Background_present_in_DB_and_PT() throws Throwable 
   {
	     PreparedStatement ps4 =  conn4.prepareStatement(sql4);
		 ResultSet resultSet4 = ps4.executeQuery();    

		  int row = 2;
		  List<String> al = new ArrayList<String>();  
		  List<String> al2 = new ArrayList<String>();
		  List<String> al3 = new ArrayList<String>();
		  List<String> al4 = new ArrayList<String>();
		  
		  System.out.println("-----Project Background-----");
		  
		  while(resultSet4.next()) 
		  {
			System.out.println(resultSet4.getString("project_number"));
		    
		    al.add(resultSet4.getString("Projectid"));
		    al2.add(resultSet4.getString("project_number"));
		    al3.add(resultSet4.getString("googlemapvalue"));
		    al4.add(resultSet4.getString("Websitevalue"));
	
		  }
		  
		   String data, data1, data2, data3, map, web, map1, web1;
		   
	          for (int i = 0; i < al2.size(); i++) 
	          {
	    	     Row dataRow4 = sheet4.createRow(row);
	    	     data = al.get(i);
	    	     data1 =al2.get(i);
	    	     data2 =al3.get(i);
	    	     data3 =al4.get(i);
    
	    	     driver.get("http://ffx-web/TrackerQC/projecttracker.aspx?ProjectID="+data+"");

	    	     Cell ProjectIDCell = dataRow4.createCell(1);
	    	     ProjectIDCell.setCellValue(data);
	    	     Cell dataNameCell = dataRow4.createCell(2);
			     dataNameCell.setCellValue(data1);
			     Cell dataAddressCell = dataRow4.createCell(3);
			     dataAddressCell.setCellValue("DB: "+data2);
			     Cell webDBCell = dataRow4.createCell(4);
			     webDBCell.setCellValue("DB: "+data3);
    
			     driver.findElement(By.xpath("//input[contains(@value,'Edit Background')]")).click();
			     
			        
			     WebElement text = driver.findElement(By.id("projectlocation"));
			     map = text.getAttribute("value");
			     
			     Thread.sleep(2000);
			     System.out.println(map);
			     WebElement text1 = driver.findElement(By.xpath("//input[contains(@value,'"+data3+"')]"));
			     web = text1.getAttribute("value");
			     System.out.println(web);
			    	 
			     WebElement e1 = driver.findElement(By.xpath("((//input[contains(@value,'Cancel')]))[4]"));
			     e1.click();
			    	 
			     Cell mapinPTCell = dataRow4.createCell(5);
			     mapinPTCell.setCellValue("PT: "+map);
				 Cell webinPTCell = dataRow4.createCell(6);
				 webinPTCell.setCellValue("PT: "+web);
				 
				 if(data2.equals(map))
		    	 {
		    		 Cell mapStatus = dataRow4.createCell(7);
			    	 mapStatus.setCellValue("Same Map");
			    	 
			    	 if(data3.equals(web))
			    	 {
			    		 Cell webStatus = dataRow4.createCell(8);
			    		 webStatus.setCellValue("Same Web");
			    		 
			    		 Cell Status = dataRow4.createCell(9);
			    		 Status.setCellValue("Both Map and Web");
			    	 }
		    	 }

				 else if (data3.equals(web))
				 {
					 Cell webStatus1 = dataRow4.createCell(8);
		    		 webStatus1.setCellValue("Same Web");
		    		 
		    		 Cell Status1 = dataRow4.createCell(9);
		    		 Status1.setCellValue("Only Web");
				 }
				 
				 else
				 {
					 Cell Status2 = dataRow4.createCell(9);
		    		 Status2.setCellValue("No Map and No Web");
				 }

	             row = row + 1;
   
	          }
       
   }

   @Then("^Update the Project Background details in Excel$")
   public void Update_the_Project_Background_details_in_Excel() throws Throwable 
   {
	   /*String outputDirPath = "C:\\Users\\devaiah.nb\\Desktop\\Latest\\Modified\\Combined.xlsx";
	   fileOut = new FileOutputStream(outputDirPath);
	   wb.write(fileOut);
	   fileOut.close();*/
   }
   
   @Given("^DB and Excel file connection For Project Schedules$")
   public void DB_and_Excel_file_connection_For_Project_Schedules() throws Throwable 
   {
	    sheet5 = wb.getSheet("Schedule");
	    conn5 = DriverManager.getConnection("jdbc:sqlserver://FFX-SQL\\SETTYDB;databaseName=ptpd_march2020","ptpd_marchUser","migration@pass");
		
	    sql5 = "	select Top 10 D.*,P.project_id as Project_Id, P.create_date from project P Left Join tbl_deadlinecalendar D on P.project_number=D.projectcode where P.create_date > '2013-12-31' and D.projectcode is not null";
   }

   @When("^Compare the Project Schedules prsent in DB and PT$")
   public void Compare_the_Project_Schedules_prsent_in_DB_and_PT() throws Throwable 
   {
	   PreparedStatement ps5 =  conn5.prepareStatement(sql5);
	   ResultSet resultSet5 = ps5.executeQuery();

		  int row = 2;
		  List<String> al = new ArrayList<String>();  
		  List<String> al2 = new ArrayList<String>();
		  List<String> al3 = new ArrayList<String>();
		  List<String> al4 = new ArrayList<String>();
		  List<String> al5 = new ArrayList<String>();
		  
		  System.out.println("-----Project Schedule-----"); 
		  while(resultSet5.next()) 
		  {
			System.out.println(resultSet5.getString("projectcode")+"   "+resultSet5.getString("title"));
		    
		    al.add(resultSet5.getString("Project_Id"));
		    al2.add(resultSet5.getString("StartDate"));
		    al3.add(resultSet5.getString("EndDate"));
		    al4.add(resultSet5.getString("projectcode"));
		    al5.add(resultSet5.getString("title"));
	
		  }

		   String data, data1, data2, data3, data4;
		   
	          for (int i = 0; i < al2.size(); i++) 
	          {
	    	     Row dataRow5 = sheet5.createRow(row);
	    	     data = al.get(i);
	    	     data1 =al2.get(i);
	    	     data2 =al3.get(i);
	    	     data3 =al4.get(i);
	    	     data4 =al5.get(i);
    
	    	     driver.get("http://ffx-web/TrackerQC/projecttracker.aspx?ProjectID="+data+"");
	    
	 	         String title = driver.findElement(By.xpath("//input[contains(@value,'"+data4+"')]")).getText();
	 	         Cell Cell4 = dataRow5.createCell(6);
				 Cell4.setCellValue("PT: "+title);

	    	     Cell dataNameCell = dataRow5.createCell(1);
			     dataNameCell.setCellValue(data);
			     Cell dataAddressCell = dataRow5.createCell(2);
			     dataAddressCell.setCellValue(data1);
			     Cell Cell1 = dataRow5.createCell(3);
			     Cell1.setCellValue(data2);
			     Cell Cell2 = dataRow5.createCell(4);
			     Cell2.setCellValue(data3);
			     Cell Cell3 = dataRow5.createCell(5);
				 Cell3.setCellValue("DB: "+data4);
			     
			     
			     if(title.equals(data4))
			     {
			    	 
			    	 Cell Cell5 = dataRow5.createCell(7);
				     Cell5.setCellValue("Same Deadline");
			     }
			     else
			     {
			    	 Cell Cell5 = dataRow5.createCell(7);
				     Cell5.setCellValue("FAIL");
			     }

	             row = row + 1;
	              
	          }
   }

   @Then("^Update the Project Schedules in Excel$")
   public void Update_the_Project_Schedules_in_Excel() throws Throwable 
   {
	   /*String outputDirPath = "C:\\Users\\devaiah.nb\\Desktop\\Latest\\Modified\\Combined.xlsx";
	   fileOut = new FileOutputStream(outputDirPath);
	   wb.write(fileOut);
	   fileOut.close();*/
   }
   
   @Given("^DB and Excel file connection For Add Services$")
   public void DB_and_Excel_file_connection_For_Add_Services() throws Throwable 
   {
	   sheet6 = wb.getSheet("AddService");
	   conn6 = DriverManager.getConnection("jdbc:sqlserver://FFX-SQL\\SETTYDB;databaseName=ptpd_march2020","ptpd_marchUser","migration@pass");
		
	   sql6 = "select Top 10 *,(select project_number from project where project_id= tp.parent_proj_id) as parent_proj_no, (select project_number from project where project_id=tp.child_proj_id) as child_Proj_no\r\n" + 
				" from tbl_project_tree tp ORDER BY parent_proj_no";
   }

   @When("^Compare the Add Services present in DB and PT$")
   public void Compare_the_Add_Services_present_in_DB_and_PT() throws Throwable 
   {
	   PreparedStatement ps6 =  conn6.prepareStatement(sql6);
		ResultSet resultSet6 = ps6.executeQuery();    

		  int row = 2;
		  List<String> al = new ArrayList<String>();  
		  List<String> al2 = new ArrayList<String>();
		  List<String> al3 = new ArrayList<String>();
		  List<String> al4 = new ArrayList<String>();
		  
		  System.out.println("-----Add Services-----");
		  while(resultSet6.next()) 
		  {
			System.out.println(resultSet6.getString("parent_proj_no")+"   "+resultSet6.getString("child_Proj_no"));
		    
		    al.add(resultSet6.getString("parent_proj_id"));
		    al2.add(resultSet6.getString("parent_proj_no"));
		    al3.add(resultSet6.getString("child_proj_id"));
		    al4.add(resultSet6.getString("child_Proj_no"));
	
		  }
		  
		   String data, data1, data2, data3;
		   
	          for (int i = 0; i < al2.size(); i++) 
	          {
	    	     Row dataRow6 = sheet6.createRow(row);
	    	     data = al.get(i);
	    	     data1 =al2.get(i);
	    	     data2 =al3.get(i);
	    	     data3 =al4.get(i);
    
	    	     driver.get("http://ffx-web/TrackerQC/projecttracker.aspx?ProjectID="+data+"");

	             //String addfile = driver.findElement(By.xpath("//a[contains(text(),'"+data3+"')]")).getText();
	    	     String addfile = driver.findElement(By.xpath("//td[contains(text(),'"+data3+"')]")).getText();
			    
			     //System.out.println("Inside");
	    	     Cell dataNameCell = dataRow6.createCell(1);
			     dataNameCell.setCellValue(data);
			     Cell dataAddressCell = dataRow6.createCell(2);
			     dataAddressCell.setCellValue(data1);
			     Cell Cell1 = dataRow6.createCell(3);
			     Cell1.setCellValue(data2);
			     Cell Cell2 = dataRow6.createCell(4);
			     Cell2.setCellValue(data3);
			     
			     if(addfile.equals(data3))
			     {
			    	 driver.get("http://ffx-web/TrackerQC/projecttracker.aspx?ProjectID="+data2+"");
			    	 driver.findElement(By.xpath("//span[contains(text(),'Project Number : ')]")).click();
			    	 Cell Cell3 = dataRow6.createCell(5);
				     Cell3.setCellValue("Child Project Exists");
			     }
			     else
			     {
			    	 Cell Cell3 = dataRow6.createCell(5);
				     Cell3.setCellValue("FAIL");
			     }

	             row = row + 1;
	              
	          }
   }

   @Then("^Update the Add Services details in Excel$")
   public void Update_the_Add_Services_details_in_Excel() throws Throwable 
   {
	   String outputDirPath = "C:\\Users\\devaiah.nb\\Desktop\\Latest\\Modified\\Combined.xlsx";
	   fileOut = new FileOutputStream(outputDirPath);
	   wb.write(fileOut);
	   fileOut.close();
   }
}
