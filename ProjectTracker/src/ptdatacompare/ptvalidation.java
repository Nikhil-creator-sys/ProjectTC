package ptdatacompare;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ptvalidation 
{

	public static WebDriver driver;
	public static XSSFWorkbook wb, wb1;
	public static FileInputStream fis;
	public static Sheet sheet, sheet1;
	public static FileOutputStream fileOut, fileOut1;
	public String sql, sql1;
	public Connection conn, conn1;
	public String data3, data4, data2, smefilter, usfilter, pno, pname;
	   
	// public Double data, data1, smedouble, usdouble, total, totalhr, rounded, smevalue, usvalue;
	// public String  data13, data14, smefilter1, usfilter1, data12, pno1, pname1;
	 public Double data10, data11, smedouble1, usdouble1, total1, totalhr1, rounded1;
	 
	 Properties prop = new Properties();
	 
//--------------------------------------------@tag1--------------------Start--------------------------------------------
	 
@Given("^DB and Excel file connection to validate the Project Number$")
public void DB_and_Excel_file_connection_to_validate_the_Project_Number() throws Throwable {
	{
		
		//Properties prop = new Properties();
		InputStream	input = new FileInputStream(".\\src\\config\\config.properties");
		prop.load(input);
		
		//FileInputStream fis = new FileInputStream("C:\\Users\\chitra.s\\DatabaseReport\\MainSearch.xlsx");
		FileInputStream fis = new FileInputStream(".\\src\\PTFiles\\MainSearch.xlsx");
	    wb = new XSSFWorkbook(fis);
	    sheet =  wb.getSheet("ProjectNumber");
		//Connection conn = DriverManager.getConnection("jdbc:sqlserver://FFX-SQL\\SETTYDB;databaseName=ptpd_feb2020","pt_migration","migration@pass");
		
		String url = prop.getProperty("QCdatabaseurl");
		String username = prop.getProperty("QCdbusername");
		String password = prop.getProperty("QCdbpassword");

		conn = DriverManager.getConnection(url, username, password);
		
		sql = "Select * FROM project  WHERE create_date between  DATEADD(m, -2,GETDATE()) and GETDATE()";
	} 
	}


@When("^Compare the Project Number in DB and PT$")
public void Compare_the_Project_Number_in_DB_and_PT() throws Throwable 
	{
		PreparedStatement ps =  conn.prepareStatement(sql);
		ResultSet resultSet = ps.executeQuery();    

		  int row = 0;
		  List<String> al = new ArrayList<String>();  
		  List<String> a2 = new ArrayList<String>();  
		  while(resultSet.next()) 
		  {
			System.out.println(resultSet.getString("project_number")+"     "+resultSet.getString("Status"));
		  
		    al.add(resultSet.getString("Status"));
		    a2.add(resultSet.getString("project_number"));
		    
		  }
		  
	       System.out.println(al);
	       System.out.println("launching Chrome browser");
	       System.setProperty("webdriver.chrome.driver", "./Driver/chromedriver.exe");
		    driver = new ChromeDriver();
		    driver.manage().window().maximize();
		
		    //driver.get("http://ffx-web/TrackerQC/");
		    driver.get(prop.getProperty("QCURL"));
		    Thread.sleep(3000);
		    
		    driver.findElement(By.id("tobesearch")).sendKeys(" ");
			driver.findElement(By.id("searchforrecordbtn")).click();
			Thread.sleep(5000);
	       for (int i = 0; i < al.size(); i++) 
	       {
	    	   Row dataRow = sheet.createRow(row);
	    	   
	    	   String data = a2.get(i);
	    	   System.out.println("List Value "+data);
	    	   
	       if(driver.findElements(By.xpath("(//a[contains(text(),'"+data+"')])[1]")).size() > 0)
			{
			 
	    	   Cell dataNameCell = dataRow.createCell(1);
			    dataNameCell.setCellValue(a2.get(i));
			    Cell dataAddressCell = dataRow.createCell(2);
			    dataAddressCell.setCellValue("Present");
				
			}
			
			else
			{
				
				Cell dataNameCell = dataRow.createCell(1);
			    dataNameCell.setCellValue(a2.get(i));
			    Cell dataAddressCell = dataRow.createCell(2);
			    dataAddressCell.setCellValue("Not Present");
			}
	       row = row + 1;
	       }
	       
		    System.out.println();
		    System.out.println("Rows Count: "+(row));
		  
	}

@Then("^Update the Project Number Status in Excel$")
public void Update_the_Result_in_Excel() throws Throwable 
{
	 String outputDirPath = ".\\src\\PTFiles\\MainSearch.xlsx";
	    FileOutputStream fileOut = new FileOutputStream(outputDirPath);
	    wb.write(fileOut);
	    fileOut.close();
	    driver.close();
}



//--------------------------------------------------@tag1----------------End------------------------correct----------------------

//-----------------------------------------------@tag2--------------------Start---------------------------------------------

@Given("^the user should be able to open the Project Tracker award page$")
public void the_user_should_be_able_to_open_the_Project_Tracker_award_page() throws Throwable 
{
	//Properties prop = new Properties();
		InputStream	input = new FileInputStream(".\\src\\config\\config.properties");
		prop.load(input);
		
		System.out.println("launching Chrome browser");
		  
		System.setProperty("webdriver.chrome.driver", "./Driver/chromedriver.exe");
	  	driver = new ChromeDriver();
	  	driver.manage().window().maximize();
	  	//driver.manage().window().maximize();
		//driver.get("http://ffx-web/TrackerQC/");
	  	driver.get(prop.getProperty("QCURL"));
	  	Thread.sleep(3000);
	  	
}

@When("^the data is selected from the filter dropdown and clicked on Search button$")
public void the_data_is_selected_from_the_filter_dropdown_and_clicked_on_Search_button() throws Throwable 
{
	Select drpStatus = new Select(driver.findElement(By.name("selstatus")));
	String Status = "In Design";
    drpStatus.selectByVisibleText(Status);
    System.out.println("Status is: "+Status);
		
	Select drpClientManager = new Select(driver.findElement(By.name("selpd")));
	String ClientManager = "Anastase Ioannidis";
	drpClientManager.selectByVisibleText(ClientManager);
	System.out.println("Client Manager is: "+ClientManager);
		
	Select drpPM = new Select(driver.findElement(By.name("selpm")));
	String ProjectManager = "Shari Sharafi";
	drpPM.selectByVisibleText(ProjectManager);
	System.out.println("Project Manager is: "+ProjectManager);
		
	Select drpTM = new Select(driver.findElement(By.name("seltm")));
	String TeamMember = "Soham Babu";
	drpTM.selectByVisibleText(TeamMember);
	System.out.println("Team Member is: "+TeamMember);
		
	Select drpYear = new Select(driver.findElement(By.name("selyear")));
	String Year = "2018";
	drpYear.selectByVisibleText(Year);
	System.out.println("Year is: "+Year);
		
	WebElement Search_Button = driver.findElement(By.id("advancesearchbtn"));
	Search_Button.click();
		
	List<WebElement> elements = driver.findElements(By.className("Contents"));
		
	int SearchCount = elements.size();
    	
	if(SearchCount == 0)
	 {
	   System.out.println("Number of elements:" +elements.size());
	   System.out.println("Search Result is - No-records-found");
	 }
		
	else if(SearchCount > 0)
	 {
	   int ProjectCount = elements.size()/2;
	   ProjectCount = ProjectCount/9;
			
	   System.out.println("Project is available");
	   System.out.println("Number of Project:" +ProjectCount);	
	   
	   WebElement htmltable=driver.findElement(By.xpath("//*[@id=\"recenttopproject\"]"));
		    
	   List<WebElement> rows=htmltable.findElements(By.tagName("tr"));
	   List<WebElement> col=htmltable.findElements(By.tagName("td"));
	    
	   //System.out.println("Number of rows is: "+rows.size());
	    
	   for(int rnum=1;rnum<rows.size();rnum++)	 
  	    {
	      //System.out.println("Number of rows is: "+rows.size());
	      List<WebElement> columns=rows.get(rnum).findElements(By.tagName("td"));
	      //System.out.println("Number of columns:"+columns.size());
  	     	
	     for(int cnum=0;cnum<columns.size();cnum++)    		
	      {	
	       System.out.println(columns.get(cnum).getText());
	      }    	
	   
  	    }
	 }
	driver.close();
}


@Then("^the result should be displayed based on the search data$")
public void the_result_should_be_displayed_based_on_the_search_data() throws Throwable 
{
	
}


//------------------------------------------------------------@tag2--End---correct---------------------------------------------------


//------------------------------------------------@tag3-------------Start------------------------------------------------------------


@Given("^the user should be able to open the PT list page$")
public void the_user_should_be_able_to_open_the_PT_list_page() throws Throwable 
{

	InputStream	input = new FileInputStream(".\\src\\config\\config.properties");
	prop.load(input);
	
	System.out.println("launching Chrome browser");
	  
	System.setProperty("webdriver.chrome.driver", "./Driver/chromedriver.exe");
    driver = new ChromeDriver();
  	driver.manage().window().maximize();
	//driver.get("http://ffx-web/TrackerQC/");
  	driver.get(prop.getProperty("QCURL"));
  
	
}

@When("^the Client Manager is selected from the CM filter dropdown and clicked on Search button$")
public void the_Client_Manager_is_selected_from_the_CM_filter_dropdown_and_clicked_on_Search_button() throws Throwable 
{

	Select drpClientManager = new Select(driver.findElement(By.name("selpd")));
	String ClientManager = "Anastase Ioannidis";
	drpClientManager.selectByVisibleText(ClientManager);
	System.out.println("Client Manager is: "+ClientManager);
	 
	 WebElement Search_Button = driver.findElement(By.id("advancesearchbtn"));
	 Search_Button.click();
			
		List<WebElement> elements = driver.findElements(By.className("Contents"));
			
		int SearchCount = elements.size();
	    	
		if(SearchCount == 0)
		 {
		   System.out.println("Number of elements:" +elements.size());
		   System.out.println("Search Result is - No-records-found");
		 }
			
		else if(SearchCount > 0)
		 {
		   int ProjectCount = elements.size()/2;
		   ProjectCount = ProjectCount/9;
				
		   System.out.println("Project is available");
		   System.out.println("Number of Project:" +ProjectCount);	
		   
		   WebElement htmltable=driver.findElement(By.xpath("//*[@id=\"recenttopproject\"]"));
			    
		   List<WebElement> rows=htmltable.findElements(By.tagName("tr"));
		   List<WebElement> col=htmltable.findElements(By.tagName("td"));
		    
		   //System.out.println("Number of rows is: "+rows.size());
		    
		   for(int rnum=1;rnum<rows.size();rnum++)	 
	  	    {
		      //System.out.println("Number of rows is: "+rows.size());
		      List<WebElement> columns=rows.get(rnum).findElements(By.tagName("td"));
		      //System.out.println("Number of columns:"+columns.size());
	  	     	
		     for(int cnum=0;cnum<columns.size();cnum++)    		
		      {	
		       System.out.println(columns.get(cnum).getText());
		      }    	
		   
	  	    }
		 }
		driver.close();
	  }


@Then("^the result should be displayed based on the Client Manager selected in the CM filter$")
public void the_result_should_be_displayed_based_on_the_Client_Manager_selected_in_the_CM_filter() throws Throwable 
{
	
}


//---------------------------------------------------------@tag3------end --correct--------------------------------------------------------------

//-------------------------------------------------------@tag4----Start-------------------------------------------------------------------

@Given("^DB and Excel file connection to validate the Client Manager$")
public void DB_and_Excel_file_connection_to_validate_the_Client_Manager() throws Throwable 
{
	
	InputStream	input = new FileInputStream(".\\src\\config\\config.properties");
	prop.load(input);
	
	FileInputStream fis = new FileInputStream(".\\src\\PTFiles\\MainSearch.xlsx");
	 wb = new XSSFWorkbook(fis);
	 sheet = wb.getSheet("ClientManager1");
	//Connection conn = DriverManager.getConnection("jdbc:sqlserver://FFX-SQL\\SETTYDB;databaseName=ptpd_feb2020","pt_migration","migration@pass");
	
	String url = prop.getProperty("QCdatabaseurl");
	String username = prop.getProperty("QCdbusername");
	String password = prop.getProperty("QCdbpassword");

	 conn = DriverManager.getConnection(url, username, password);
	
	 sql = "select P.project_number,P.project_name,E.FirstName,P.Status,p.create_date,T.team_designation\r\n" + 
			"from project P left join tbl_project_team T on P.project_id=T.team_project_id\r\n" + 
			"left join employee E on T.team_emp_id=E.EmployeeID where T.team_designation='client manager' and create_date between DATEADD(m, -6,GETDATE()) and GETDATE()";
	
}

@When("^Compare the Client Manager in DB and PT$")
public void Compare_the_Client_Manager_in_DB_and_PT() throws Throwable 
{

	PreparedStatement ps =  conn.prepareStatement(sql);
	ResultSet resultSet = ps.executeQuery();    

	 int row = 0;
	 List<String> al = new ArrayList<String>();  
	  
	  while(resultSet.next()) 
	  {
		System.out.println(resultSet.getString("project_number")+"     "+resultSet.getString("team_designation")+"     "+resultSet.getString("FirstName"));
	
	    al.add(resultSet.getString("project_number") + " - " + resultSet.getString("team_designation")+"     "+resultSet.getString("FirstName")); 
	    
	  }
	  
	  
       System.out.println(al);
       System.setProperty("webdriver.chrome.driver", "./Driver/chromedriver.exe");
	    driver = new ChromeDriver();
	    driver.manage().window().maximize();
	
	    //driver.get("http://ffx-web/TrackerQC/");
	    //driver.get("http://ffx-web/newptlayout/");
	    driver.get(prop.getProperty("QCURL"));
	    Thread.sleep(3000);
	   
	    WebElement Search_Button = driver.findElement(By.id("advancesearchbtn"));
		Search_Button.click();
	    Thread.sleep(5000);
		
       for (int i = 0; i < al.size(); i++) 
       {
    	   Row dataRow = sheet.createRow(row);
    	   
    	   String data = al.get(i);
    	   System.out.println("List Value "+data);
    	   
       if(driver.findElements(By.xpath("(//a[contains(text(),'"+data+"')])[1]")).size() > 0)
		{
		
    	   Cell dataNameCell = dataRow.createCell(1);
		    dataNameCell.setCellValue(al.get(i));
		    //Cell dataAddressCell = dataRow.createCell(2);
		    //dataAddressCell.setCellValue("Present");
			
		}
		
		else
		{
			
			Cell dataNameCell = dataRow.createCell(1);
		    dataNameCell.setCellValue(al.get(i));
		    //Cell dataAddressCell = dataRow.createCell(2);
		    //dataAddressCell.setCellValue("Not Present");
		}
       row = row + 1;
       }
       
	    System.out.println();
	    System.out.println("Rows Count: "+(row));
	    
	  
}


@Then("^Update the Client Manager Data in Excel$")
public void Update_the_Client_Manager_Data_in_Excel() throws Throwable 
{

	String outputDirPath = ".\\src\\PTFiles\\MainSearch.xlsx";
    FileOutputStream fileOut = new FileOutputStream(outputDirPath);
    wb.write(fileOut);
    fileOut.close();
    driver.close();
}


//------------------------------------------------------------tag4 --------End---------Correct-----------------------------

//-------------------------------------------------------------@tag5-------Start------------------------------------------------------

@Given("^DB and Excel file connection to validate the particular Client Manager$")
public void DB_and_Excel_file_connection_to_validate_the_particular_Client_Manager() throws Throwable 
{
  
	InputStream	input = new FileInputStream(".\\src\\config\\config.properties");
	prop.load(input);
	
	FileInputStream fis = new FileInputStream(".\\src\\PTFiles\\MainSearch.xlsx");
    wb = new XSSFWorkbook(fis);
    sheet = wb.getSheet("ClientManager2");
	
	String url = prop.getProperty("QCdatabaseurl");
	String username = prop.getProperty("QCdbusername");
	String password = prop.getProperty("QCdbpassword");

    conn = DriverManager.getConnection(url, username, password);
	
	 sql = "select P.project_number,P.project_name,T.team_designation,E.FirstName as EmpName\r\n" + 
			"from tbl_project_team T left join project P on T.team_project_id=P.project_id\r\n" + 
			"left join employee E on E.EmployeeID=T.team_emp_id\r\n" + 
			"where team_emp_id=389";
	
}

@When("^Compare the Client Manager results in DB and PT$")
public void Compare_the_Client_Manager_results_in_DB_and_PT() throws Throwable 
{
 
	PreparedStatement ps =  conn.prepareStatement(sql);
	ResultSet resultSet = ps.executeQuery();    

	 int row = 0;
	 List<String> al = new ArrayList<String>();  
	  
	  while(resultSet.next()) 
	  {
		System.out.println(resultSet.getString("project_number")+"     "+resultSet.getString("team_designation")+"     "+resultSet.getString("EmpName"));
	
	    al.add(resultSet.getString("project_number") + " - " + resultSet.getString("team_designation")+"     "+resultSet.getString("EmpName")); 
	    
	  }
	  
	  
       System.out.println(al);
       System.setProperty("webdriver.chrome.driver", "./Driver/chromedriver.exe");
	    driver = new ChromeDriver();
	    driver.manage().window().maximize();
	
	   // driver.get("http://ffx-web/TrackerQC/");
	    driver.get(prop.getProperty("QCURL"));
	    
	    Thread.sleep(3000);
	   
	    WebElement Search_Button = driver.findElement(By.id("advancesearchbtn"));
		Search_Button.click();
	    Thread.sleep(5000);
		
       for (int i = 0; i < al.size(); i++) 
       {
    	   Row dataRow = sheet.createRow(row);
    	   
    	   String data = al.get(i);
    	   System.out.println("List Value "+data);
    	   
       if(driver.findElements(By.xpath("(//a[contains(text(),'"+data+"')])[1]")).size() > 0)
		{
		
    	   Cell dataNameCell = dataRow.createCell(1);
		    dataNameCell.setCellValue(al.get(i));
		    //Cell dataAddressCell = dataRow.createCell(2);
		    //dataAddressCell.setCellValue("Present");
			
		}
		
		else
		{
			
			Cell dataNameCell = dataRow.createCell(1);
		    dataNameCell.setCellValue(al.get(i));
		    //Cell dataAddressCell = dataRow.createCell(2);
		    //dataAddressCell.setCellValue("Not Present");
		}
       row = row + 1;
       }
       
	    System.out.println();
	    System.out.println("Rows Count: "+(row));
	    
	    
}


@Then("^Update the Client Manager results in Excel$")
public void Update_the_Client_Manager_results_in_Excel() throws Throwable 
{
	String outputDirPath = ".\\src\\PTFiles\\MainSearch.xlsx";
    FileOutputStream fileOut = new FileOutputStream(outputDirPath);
    wb.write(fileOut);
    fileOut.close();
    driver.close();


}

//-------------------------------------------------------------@tag5--------End-----correct------------------------------------------


//-------------------------------------------------------------@tag6---------------Start-----------------------------------------

@Given("^the user should be able to connect to the Project Tracker award page$")
public void the_user_should_be_able_to_connect_to_the_Project_Tracker_award_page() throws Throwable 
{
	
	InputStream	input = new FileInputStream(".\\src\\config\\config.properties");
	prop.load(input);
	
	System.out.println("launching Chrome browser");
	  
	System.setProperty("webdriver.chrome.driver", "./Driver/chromedriver.exe");
  	 driver = new ChromeDriver();
  	driver.manage().window().maximize();
	//driver.get("http://ffx-web/TrackerQC/");
  	driver.get(prop.getProperty("QCURL"));
	
	
}

@When("^the Project Manager is selected from the PM filter dropdown and clicked on Search button$")
public void the_Project_Manager_is_selected_from_the_PM_filter_dropdown_and_clicked_on_Search_button() throws Throwable 
{
 
	Select drpPM = new Select(driver.findElement(By.name("selpm")));
	String ProjectManager = "Shari Sharafi";
	drpPM.selectByVisibleText(ProjectManager);
	System.out.println("Project Manager is: "+ProjectManager);
	 
	 WebElement Search_Button = driver.findElement(By.id("advancesearchbtn"));
	 Search_Button.click();
			
		List<WebElement> elements = driver.findElements(By.className("Contents"));
			
		int SearchCount = elements.size();
	    	
		if(SearchCount == 0)
		 {
		   System.out.println("Number of elements:" +elements.size());
		   System.out.println("Search Result is - No-records-found");
		 }
			
		else if(SearchCount > 0)
		 {
		   int ProjectCount = elements.size()/2;
		   ProjectCount = ProjectCount/9;
				
		   System.out.println("Project is available");
		   System.out.println("Number of Project:" +ProjectCount);	
		   
		   WebElement htmltable=driver.findElement(By.xpath("//*[@id=\"recenttopproject\"]"));
			    
		   List<WebElement> rows=htmltable.findElements(By.tagName("tr"));
		   List<WebElement> col=htmltable.findElements(By.tagName("td"));
		    
		   //System.out.println("Number of rows is: "+rows.size());
		    
		   for(int rnum=1;rnum<rows.size();rnum++)	 
	  	    {
		      //System.out.println("Number of rows is: "+rows.size());
		      List<WebElement> columns=rows.get(rnum).findElements(By.tagName("td"));
		      //System.out.println("Number of columns:"+columns.size());
	  	     	
		     for(int cnum=0;cnum<columns.size();cnum++)    		
		      {	
		       System.out.println(columns.get(cnum).getText());
		      }    	
		   
	  	    }
		 }
		driver.close();
	  }


@Then("^the result should be displayed based on the Project Manager selected$")
public void the_result_should_be_displayed_based_on_the_Project_Manager_selected() throws Throwable 
{

	
	
}


//---------------------------------------------------------@tag6------------------End---------correct----------------------------

//--------------------------------------------------------@tag7------Start-------------------------------------------------

@Given("^DB and Excel file connection to validate the particular Project Manager$")
public void DB_and_Excel_file_connection_to_validate_the_particular_Project_Manager() throws Throwable 
{
	
	InputStream	input = new FileInputStream(".\\src\\config\\config.properties");
	prop.load(input);
	
	FileInputStream fis = new FileInputStream(".\\src\\PTFiles\\MainSearch.xlsx");
	 wb = new XSSFWorkbook(fis);
	 sheet = wb.getSheet("ProjectManager1");
	
	String url = prop.getProperty("QCdatabaseurl");
	String username = prop.getProperty("QCdbusername");
	String password = prop.getProperty("QCdbpassword");

	 conn = DriverManager.getConnection(url, username, password);
	
	//String sql = "select * from project where Status = 'Job not started' And create_date between DATEADD(m, -2,GETDATE()) and GETDATE()";
	 sql = "select P.project_manager,E.FirstName,P.* from project P Left join employee E on P.project_manager=E.EmployeeID \r\n" +
			"where E.EmployeeID=45  and create_date between DATEADD(m, -60,GETDATE()) and GETDATE()";
	
	
}

@When("^Compare the Project Manager results in DB and PT$")
public void Compare_the_Project_Manager_results_in_DB_and_PT() throws Throwable 
{

	PreparedStatement ps =  conn.prepareStatement(sql);
	ResultSet resultSet = ps.executeQuery();    

	 int row = 0;
	 List<String> al = new ArrayList<String>();  
	  
	  while(resultSet.next()) 
	  {
		System.out.println(resultSet.getString("project_number")+"     "+resultSet.getString("FirstName"));
	
	    al.add(resultSet.getString("project_number") + " - " + resultSet.getString("FirstName"));   
	    
	  }
	  
	  
       System.out.println(al);
       System.setProperty("webdriver.chrome.driver", "./Driver/chromedriver.exe");
	    driver = new ChromeDriver();
	    driver.manage().window().maximize();
	
	    //driver.get("http://ffx-web/TrackerQC/");
	    driver.get(prop.getProperty("QCURL"));
	    Thread.sleep(3000);
	   
	    WebElement Search_Button = driver.findElement(By.id("advancesearchbtn"));
		Search_Button.click();
	    Thread.sleep(5000);
		
       for (int i = 0; i < al.size(); i++) 
       {
    	   Row dataRow = sheet.createRow(row);
    	   
    	   String data = al.get(i);
    	   System.out.println("List Value "+data);
    	   
       if(driver.findElements(By.xpath("(//a[contains(text(),'"+data+"')])[1]")).size() > 0)
		{
		
    	   Cell dataNameCell = dataRow.createCell(1);
		    dataNameCell.setCellValue(al.get(i));
		    //Cell dataAddressCell = dataRow.createCell(2);
		    //dataAddressCell.setCellValue("Present");
			
		}
		
		else
		{
			
			Cell dataNameCell = dataRow.createCell(1);
		    dataNameCell.setCellValue(al.get(i));
		    //Cell dataAddressCell = dataRow.createCell(2);
		    //dataAddressCell.setCellValue("Not Present");
		}
       row = row + 1;
       }
       
	    System.out.println();
	    System.out.println("Rows Count: "+(row));
	    
}


@Then("^Update the Project Manager results in Excel$")
public void Update_the_Project_Manager_results_in_Excel() throws Throwable 
{
 
	 String outputDirPath = ".\\src\\PTFiles\\MainSearch.xlsx";
	    FileOutputStream fileOut = new FileOutputStream(outputDirPath);
	    wb.write(fileOut);
	    fileOut.close();
	    driver.close();
	
}

//-------------------------------------------------@tag7----------End-----------Correct-----------------------------------------


//------------------------------------------------@tag8-------------Start---------------------------------------------------------

@Given("^DB and Excel file connection to validate the Project Manager$")
public void DB_and_Excel_file_connection_to_validate_the_Project_Manager() throws Throwable 
{
  
	InputStream	input = new FileInputStream(".\\src\\config\\config.properties");
	prop.load(input);
	
	FileInputStream fis = new FileInputStream(".\\src\\PTFiles\\MainSearch.xlsx");
     wb = new XSSFWorkbook(fis);
	 sheet = wb.getSheet("ProjectManager2");
	
	String url = prop.getProperty("QCdatabaseurl");
	String username = prop.getProperty("QCdbusername");
	String password = prop.getProperty("QCdbpassword");

	 conn = DriverManager.getConnection(url, username, password);
	
	 sql = "select P.project_number,P.project_name,E.FirstName,P.Status,p.create_date,T.team_designation\r\n" + 
			"from project P left join tbl_project_team T on P.project_id=T.team_project_id\r\n" + 
			"left join employee E on T.team_emp_id=E.EmployeeID where T.team_designation='project manager' and create_date between DATEADD(m, -6,GETDATE()) and GETDATE()";
	
	
}

@When("^Compare the Project Manager in DB and PT$")
public void Compare_the_Project_Manager_in_DB_and_PT() throws Throwable 
{

	PreparedStatement ps =  conn.prepareStatement(sql);
	ResultSet resultSet = ps.executeQuery();    

	 int row = 0;
	 List<String> al = new ArrayList<String>();  
	  
	  while(resultSet.next()) 
	  {
		System.out.println(resultSet.getString("project_number")+"     "+resultSet.getString("team_designation")+"     "+resultSet.getString("FirstName"));
	
	    al.add(resultSet.getString("project_number") + " - " + resultSet.getString("team_designation")+ " - " + resultSet.getString("FirstName"));    
	    
	  }
	  	  
       System.out.println(al);
       System.setProperty("webdriver.chrome.driver", "./Driver/chromedriver.exe");
	    driver = new ChromeDriver();
	    driver.manage().window().maximize();
	
	    //driver.get("http://ffx-web/TrackerQC/");
	    driver.get(prop.getProperty("QCURL"));
	    Thread.sleep(3000);
	   
	    WebElement Search_Button = driver.findElement(By.id("advancesearchbtn"));
		Search_Button.click();
	    Thread.sleep(5000);
		
       for (int i = 0; i < al.size(); i++) 
       {
    	   Row dataRow = sheet.createRow(row);
    	   
    	   String data = al.get(i);
    	   System.out.println("List Value "+data);
    	   
       if(driver.findElements(By.xpath("(//a[contains(text(),'"+data+"')])[1]")).size() > 0)
		{
		
    	   Cell dataNameCell = dataRow.createCell(1);
		    dataNameCell.setCellValue(al.get(i));
		    //Cell dataAddressCell = dataRow.createCell(2);
		    //dataAddressCell.setCellValue("Present");
			
		}
		
		else
		{
			
			Cell dataNameCell = dataRow.createCell(1);
		    dataNameCell.setCellValue(al.get(i));
		    //Cell dataAddressCell = dataRow.createCell(2);
		    //dataAddressCell.setCellValue("Not Present");
		}
       row = row + 1;
       }
       
	    System.out.println();
	    System.out.println("Rows Count: "+(row));
	    
	   
}


@Then("^Update the Project Manager Data in Excel$")
public void Update_the_Project_Manager_Data_in_Excel() throws Throwable 
{
  
	 String outputDirPath = ".\\src\\PTFiles\\MainSearch.xlsx";
	    FileOutputStream fileOut = new FileOutputStream(outputDirPath);
	    wb.write(fileOut);
	    fileOut.close();
	    driver.close();

}


//----------------------------------------------@tag8------End------correct------------------------------------------------------


//---------------------------------------------@tag9-----------------Start--------------------------------------------------

@Given("^the user should be able to Navigate to the Project Tracker award page$")
public void the_user_should_be_able_to_Navigate_to_the_Project_Tracker_award_page() throws Throwable 
{

	InputStream	input = new FileInputStream(".\\src\\config\\config.properties");
	prop.load(input);
	
	System.out.println("launching Chrome browser");
	  
	System.setProperty("webdriver.chrome.driver", "./Driver/chromedriver.exe");
    driver = new ChromeDriver();
  	driver.manage().window().maximize();
  	driver.get(prop.getProperty("QCURL"));
	
}

@When("^the Status is selected from the Status filter dropdown and clicked on Search button$")
public void the_Status_is_selected_from_the_Status_filter_dropdown_and_clicked_on_Search_button() throws Throwable 
{

	Select drpStatus = new Select(driver.findElement(By.name("selstatus")));
	 String Status = "Job not started";
	 drpStatus.selectByVisibleText(Status);
	 System.out.println("Project Status is: "+Status);
	 
	 WebElement Search_Button = driver.findElement(By.id("advancesearchbtn"));
	 Search_Button.click();
			
		List<WebElement> elements = driver.findElements(By.className("Contents"));
			
		int SearchCount = elements.size();
	    	
		if(SearchCount == 0)
		 {
		   System.out.println("Number of elements:" +elements.size());
		   System.out.println("Search Result is - No-records-found");
		 }
			
		else if(SearchCount > 0)
		 {
		   int ProjectCount = elements.size()/2;
		   ProjectCount = ProjectCount/9;
				
		   System.out.println("Project is available");
		   System.out.println("Number of Project:" +ProjectCount);	
		   
		   WebElement htmltable=driver.findElement(By.xpath("//*[@id=\"recenttopproject\"]"));
			    
		   List<WebElement> rows=htmltable.findElements(By.tagName("tr"));
		   List<WebElement> col=htmltable.findElements(By.tagName("td"));
		    
		   //System.out.println("Number of rows is: "+rows.size());
		    
		   for(int rnum=1;rnum<rows.size();rnum++)	 
	  	    {
		      //System.out.println("Number of rows is: "+rows.size());
		      List<WebElement> columns=rows.get(rnum).findElements(By.tagName("td"));
		      //System.out.println("Number of columns:"+columns.size());
	  	     	
		     for(int cnum=0;cnum<columns.size();cnum++)    		
		      {	
		       System.out.println(columns.get(cnum).getText());
		      }    	
		   
	  	    }
		 }
		driver.close();
	  }


@Then("^the result should be displayed based on the Status selected$")
public void the_result_should_be_displayed_based_on_the_Status_selected() throws Throwable 
{
 
	
}


//-------------------------------------------------@tag9--------End------Correct-----------------------------------------------


//------------------------------------------------@tag10-------------------Start-----------------------------------

@Given("^DB and Excel file connection to validate the Status of a Project$")
public void DB_and_Excel_file_connection_to_validate_the_Status_of_a_Project() throws Throwable 
{
	
	InputStream	input = new FileInputStream(".\\src\\config\\config.properties");
	prop.load(input);
	
	FileInputStream fis = new FileInputStream(".\\src\\PTFiles\\MainSearch.xlsx");
    wb = new XSSFWorkbook(fis);
	sheet = wb.getSheet("ProjectStatus");
	
	String url = prop.getProperty("QCdatabaseurl");
	String username = prop.getProperty("QCdbusername");
	String password = prop.getProperty("QCdbpassword");

	conn = DriverManager.getConnection(url, username, password);
			
	 sql = "select * from project where Status = 'Job not started' And create_date between DATEADD(m, -2,GETDATE()) and GETDATE()";
	
}


@When("^Compare the results of Project Status in DB and PT$")
public void Compare_the_results_of_Project_Status_in_DB_and_PT() throws Throwable
{
	PreparedStatement ps =  conn.prepareStatement(sql);
	ResultSet resultSet = ps.executeQuery();    

	 int row = 0;
	 List<String> al = new ArrayList<String>();  
	  
	  while(resultSet.next()) 
	  {
		System.out.println(resultSet.getString("project_number")+"     "+resultSet.getString("Status"));
	
	    al.add(resultSet.getString("project_number") + " - " + resultSet.getString("Status"));
	    
	  }
	  
	  
       System.out.println(al);
       System.setProperty("webdriver.chrome.driver", "./Driver/chromedriver.exe");
	    driver = new ChromeDriver();
	    driver.manage().window().maximize();
	
	    //driver.get("http://ffx-web/TrackerQC/");
	    driver.get(prop.getProperty("QCURL"));
	    Thread.sleep(3000);
	   
	    WebElement Search_Button = driver.findElement(By.id("advancesearchbtn"));
		Search_Button.click();
	    Thread.sleep(5000);
		
       for (int i = 0; i < al.size(); i++) 
       {
    	   Row dataRow = sheet.createRow(row);
    	   
    	   String data = al.get(i);
    	   System.out.println("List Value "+data);
    	   
       if(driver.findElements(By.xpath("(//a[contains(text(),'"+data+"')])[1]")).size() > 0)
		{
		
    	   Cell dataNameCell = dataRow.createCell(1);
		    dataNameCell.setCellValue(al.get(i));
		    //Cell dataAddressCell = dataRow.createCell(2);
		    //dataAddressCell.setCellValue("Present");
			
		}
		
		else
		{
			
			Cell dataNameCell = dataRow.createCell(1);
		    dataNameCell.setCellValue(al.get(i));
		    //Cell dataAddressCell = dataRow.createCell(2);
		    //dataAddressCell.setCellValue("Not Present");
		}
       row = row + 1;
       }
       
	    System.out.println();
	    System.out.println("Rows Count: "+(row));
	    
	
}


@Then("^Update the results of Project Status in Excel$")
public void Update_the_results_of_Project_Status_in_Excel() throws Throwable 
{

    String outputDirPath = ".\\src\\PTFiles\\MainSearch.xlsx";
    FileOutputStream fileOut = new FileOutputStream(outputDirPath);
    wb.write(fileOut);
    fileOut.close();
    driver.close();

}


//------------------------------------------------@tag10-----------End--------------------Correct------------------------------

//--------------------------------------------------@tag11---------------Start--------------------------------------------

@Given("^the user should be able to Navigate to the Project Tracker list page$")
public void the_user_should_be_able_to_Navigate_to_the_Project_Tracker_list_page() throws Throwable 
{

	InputStream	input = new FileInputStream(".\\src\\config\\config.properties");
	prop.load(input);
	
	System.out.println("launching Chrome browser");
	  
	System.setProperty("webdriver.chrome.driver", "./Driver/chromedriver.exe");
    driver = new ChromeDriver();
  	driver.manage().window().maximize();
  	driver.get(prop.getProperty("QCURL"));
	
}

@When("^the Team Member is selected from the Team Member filter dropdown and clicked on Search button$")
public void the_Team_Member_is_selected_from_the_Team_Member_filter_dropdown_and_clicked_on_Search_button() throws Throwable 
{

	 Select drpTM = new Select(driver.findElement(By.name("seltm")));
		String TeamMember = "Soham Babu";
		drpTM.selectByVisibleText(TeamMember);
		System.out.println("Team Member is: "+TeamMember);
		 
		 WebElement Search_Button = driver.findElement(By.id("advancesearchbtn"));
		 Search_Button.click();
				
			List<WebElement> elements = driver.findElements(By.className("Contents"));
				
			int SearchCount = elements.size();
		    	
			if(SearchCount == 0)
			 {
			   System.out.println("Number of elements:" +elements.size());
			   System.out.println("Search Result is - No-records-found");
			 }
				
			else if(SearchCount > 0)
			 {
			   int ProjectCount = elements.size()/2;
			   ProjectCount = ProjectCount/9;
					
			   System.out.println("Project is available");
			   System.out.println("Number of Project:" +ProjectCount);	
			   
			   WebElement htmltable=driver.findElement(By.xpath("//*[@id=\"recenttopproject\"]"));
				    
			   List<WebElement> rows=htmltable.findElements(By.tagName("tr"));
			   List<WebElement> col=htmltable.findElements(By.tagName("td"));
			    
			   //System.out.println("Number of rows is: "+rows.size());
			    
			   for(int rnum=1;rnum<rows.size();rnum++)	 
		  	    {
			      //System.out.println("Number of rows is: "+rows.size());
			      List<WebElement> columns=rows.get(rnum).findElements(By.tagName("td"));
			      //System.out.println("Number of columns:"+columns.size());
		  	     	
			     for(int cnum=0;cnum<columns.size();cnum++)    		
			      {	
			       System.out.println(columns.get(cnum).getText());
			      }    	
			   
		  	    }
			 }
			driver.close();
		  }


@Then("^the result should be displayed based on the Team Member selected$")
public void the_result_should_be_displayed_based_on_the_Team_Member_selected() throws Throwable 
{

}

//----------------------------------------------------@tag11-------End--------correct--------------------------------------------------


//-----------------------------------------------@tag12--------Start----------------------------------------------------------


@Given("^DB and Excel file connection to validate the Projects of a Team Member$")
public void DB_and_Excel_file_connection_to_validate_the_Projects_of_a_Team_Member() throws Throwable 
{
	InputStream	input = new FileInputStream(".\\src\\config\\config.properties");
	prop.load(input);
	
	FileInputStream fis = new FileInputStream(".\\src\\PTFiles\\MainSearch.xlsx");
    wb = new XSSFWorkbook(fis);
    sheet = wb.getSheet("TeamMember");
	
	String url = prop.getProperty("QCdatabaseurl");
	String username = prop.getProperty("QCdbusername");
	String password = prop.getProperty("QCdbpassword");

	 conn = DriverManager.getConnection(url, username, password);
	
	 sql = "select P.project_number,P.project_name,T.team_designation,E.FirstName as EmpName\r\n" + 
			"from tbl_project_team T left join project P on T.team_project_id=P.project_id \r\n" + 
			"left join employee E on E.EmployeeID=T.team_emp_id\r\n" + 
			"where team_emp_id=16 and create_date between DATEADD(m, -48,GETDATE()) and GETDATE()";
	
}


@When("^Compare the results of Team Member projects in DB and PT$")
public void Compare_the_results_of_Team_Member_projects_in_DB_and_PT() throws Throwable 
{

	PreparedStatement ps =  conn.prepareStatement(sql);
	ResultSet resultSet = ps.executeQuery();    

	 int row = 0;
	 List<String> al = new ArrayList<String>();  
	  
	  while(resultSet.next()) 
	  {
		System.out.println(resultSet.getString("project_number")+"     "+resultSet.getString("EmpName"));
	
	    al.add(resultSet.getString("project_number") + " - " + resultSet.getString("EmpName"));
	    
	  }
	  
       System.out.println(al);
       System.setProperty("webdriver.chrome.driver", "./Driver/chromedriver.exe");
	    driver = new ChromeDriver();
	    driver.manage().window().maximize();
	
	    //driver.get("http://ffx-web/TrackerQC/");
	    driver.get(prop.getProperty("QCURL"));
	    Thread.sleep(3000);
	   
	    WebElement Search_Button = driver.findElement(By.id("advancesearchbtn"));
		Search_Button.click();
	    Thread.sleep(5000);
		
       for (int i = 0; i < al.size(); i++) 
       {
    	   Row dataRow = sheet.createRow(row);
    	   
    	   String data = al.get(i);
    	   System.out.println("List Value "+data);
    	   
       if(driver.findElements(By.xpath("(//a[contains(text(),'"+data+"')])[1]")).size() > 0)
		{
		
    	   Cell dataNameCell = dataRow.createCell(1);
		    dataNameCell.setCellValue(al.get(i));
		    //Cell dataAddressCell = dataRow.createCell(2);
		    //dataAddressCell.setCellValue("Present");
			
		}
		
		else
		{
			
			Cell dataNameCell = dataRow.createCell(1);
		    dataNameCell.setCellValue(al.get(i));
		    //Cell dataAddressCell = dataRow.createCell(2);
		    //dataAddressCell.setCellValue("Not Present");
		}
       row = row + 1;
       }
       
	    System.out.println();
	    System.out.println("Rows Count: "+(row));
	    
	
}


@Then("^Update the results of Team Member projects in Excel$")
public void Update_the_results_of_Team_Member_projects_in_Excel() throws Throwable
{

    String outputDirPath = ".\\src\\PTFiles\\MainSearch.xlsx";
    FileOutputStream fileOut = new FileOutputStream(outputDirPath);
    wb.write(fileOut);
    fileOut.close();
    driver.close();

	
}

//-----------------------------------------------@tag12-----------End-----------correct--------------------------------------------

//---------------------------------------------------@tag13-------------Start-------------------------------------------------

@Given("^the user should be able to view the Search Filter page$")
public void the_user_should_be_able_to_view_the_Search_Filter_page() throws Throwable 
{
	
	InputStream	input = new FileInputStream(".\\src\\config\\config.properties");
	prop.load(input);
	
	System.out.println("launching Chrome browser");
	  
	System.setProperty("webdriver.chrome.driver", "./Driver/chromedriver.exe");
    driver = new ChromeDriver();
  	driver.manage().window().maximize();
  	driver.get(prop.getProperty("QCURL"));
	
}

@When("^the year is selected from the year filter dropdown and clicked on Search button$")
public void the_year_is_selected_from_the_year_filter_dropdown_and_clicked_on_Search_button() throws Throwable 
{
	Select drpYear = new Select(driver.findElement(By.name("selyear")));
	String Year = "2020";
	drpYear.selectByVisibleText(Year);
	System.out.println("Year is: "+Year);
	 
	 WebElement Search_Button = driver.findElement(By.id("advancesearchbtn"));
	 Search_Button.click();
			
		List<WebElement> elements = driver.findElements(By.className("Contents"));
			
		int SearchCount = elements.size();
	    	
		if(SearchCount == 0)
		 {
		   System.out.println("Number of elements:" +elements.size());
		   System.out.println("Search Result is - No-records-found");
		 }
			
		else if(SearchCount > 0)
		 {
		   int ProjectCount = elements.size()/2;
		   ProjectCount = ProjectCount/9;
				
		   System.out.println("Project is available");
		   System.out.println("Number of Project:" +ProjectCount);	
		   
		   WebElement htmltable=driver.findElement(By.xpath("//*[@id=\"recenttopproject\"]"));
			    
		   List<WebElement> rows=htmltable.findElements(By.tagName("tr"));
		   List<WebElement> col=htmltable.findElements(By.tagName("td"));
		    
		   //System.out.println("Number of rows is: "+rows.size());
		    
		   for(int rnum=1;rnum<rows.size();rnum++)	 
	  	    {
		      //System.out.println("Number of rows is: "+rows.size());
		      List<WebElement> columns=rows.get(rnum).findElements(By.tagName("td"));
		      //System.out.println("Number of columns:"+columns.size());
	  	     	
		     for(int cnum=0;cnum<columns.size();cnum++)    		
		      {	
		       System.out.println(columns.get(cnum).getText());
		      }    	
		   
	  	    }
		 }
		driver.close();
	  }


@Then("^the result should be displayed based on the year selected$")
public void the_result_should_be_displayed_based_on_the_year_selected() throws Throwable 
{

	
}

//------------------------------------------------@tag13----------End------------------Correct---------------------------------------

//------------------------------------------------------@tag14------------------------Start------------------------------------

@Given("^DB and Excel file connection to validate the Projects of a particular year$")
public void DB_and_Excel_file_connection_to_validate_the_Projects_of_a_particular_year() throws Throwable 
{
	
	InputStream	input = new FileInputStream(".\\src\\config\\config.properties");
	prop.load(input);
	
	FileInputStream fis = new FileInputStream(".\\src\\PTFiles\\MainSearch.xlsx");
    wb = new XSSFWorkbook(fis);
    sheet = wb.getSheet("Year");
	
	String url = prop.getProperty("QCdatabaseurl");
	String username = prop.getProperty("QCdbusername");
	String password = prop.getProperty("QCdbpassword");

	 conn = DriverManager.getConnection(url, username, password);
	
	 sql = "select * from project where year(create_date)=2020";
	
	
}

@When("^Compare the results of projects year in DB and PT$")
public void Compare_the_results_of_projects_year_in_DB_and_PT() throws Throwable 
{

	PreparedStatement ps =  conn.prepareStatement(sql);
	ResultSet resultSet = ps.executeQuery();    

	 int row = 0;
	 List<String> al = new ArrayList<String>();  
	  
	  while(resultSet.next()) 
	  {
		System.out.println(resultSet.getString("project_number")+"     "+resultSet.getString("create_date"));
	
	    al.add(resultSet.getString("project_number") + " - " + resultSet.getString("create_date"));
	       
	  }
	   
       System.out.println(al);
       System.setProperty("webdriver.chrome.driver", "./Driver/chromedriver.exe");
	    driver = new ChromeDriver();
	    driver.manage().window().maximize();
	
	    driver.get(prop.getProperty("QCURL"));
	    Thread.sleep(3000);
	   
	    WebElement Search_Button = driver.findElement(By.id("advancesearchbtn"));
		Search_Button.click();
	    Thread.sleep(5000);
		
       for (int i = 0; i < al.size(); i++) 
       {
    	   Row dataRow = sheet.createRow(row);
    	   
    	   String data = al.get(i);
    	   System.out.println("List Value "+data);
    	   
       if(driver.findElements(By.xpath("(//a[contains(text(),'"+data+"')])[1]")).size() > 0)
		{
		
    	   Cell dataNameCell = dataRow.createCell(1);
		    dataNameCell.setCellValue(al.get(i));
		    //Cell dataAddressCell = dataRow.createCell(2);
		    //dataAddressCell.setCellValue("Present");
			
		}
		
		else
		{
			
			Cell dataNameCell = dataRow.createCell(1);
		    dataNameCell.setCellValue(al.get(i));
		    //Cell dataAddressCell = dataRow.createCell(2);
		    //dataAddressCell.setCellValue("Not Present");
		}
       row = row + 1;
       }
       
	    System.out.println();
	    System.out.println("Rows Count: "+(row));
	    
	  
}


@Then("^Update the results of projects year in Excel$")
public void Update_the_results_of_projects_year_in_Excel() throws Throwable 
{
	 String outputDirPath = ".\\src\\PTFiles\\MainSearch.xlsx";
	    FileOutputStream fileOut = new FileOutputStream(outputDirPath);
	    wb.write(fileOut);
	    fileOut.close();
	    driver.close();
	
	
}

//---------------------------------------------@tag14-----------End-------------------correct--------------------------------------------

//---------------------------------------------@tag15-----------Start--------------------------------------------------------------

@Given("^the user should be able to view the Project Tracker list page$")
public void the_user_should_be_able_to_view_the_Project_Tracker_list_page() throws Throwable 
{

	InputStream	input = new FileInputStream(".\\src\\config\\config.properties");
	prop.load(input);

    System.out.println("launching Chrome browser");
  
    System.setProperty("webdriver.chrome.driver", "./Driver/chromedriver.exe");
    driver = new ChromeDriver();
	driver.manage().window().maximize();
	driver.get(prop.getProperty("QCURL"));
	
}

@When("^the project number is entered in the Main Search bar and clicked on Search button$")
public void the_project_number_is_entered_in_the_Main_Search_bar_and_clicked_on_Search_button() throws Throwable 
{

	String InputProjectNumber = "SAPX206049.00";
	  
	//Input to the Search field 
	WebElement txtbox_Search = driver.findElement(By.id("tobesearch"));
	txtbox_Search.sendKeys(InputProjectNumber);
	driver.findElement(By.id("searchforrecordbtn")).click();
				
	List<WebElement> elements = driver.findElements(By.className("Contents"));
			
	int SearchCount = elements.size();
	   	
	if(SearchCount == 0)
	 {
	   System.out.println("Number of Project:" +elements.size());
	   System.out.println("Search Result is - No-records-found");
	 }
	
	else if(SearchCount > 0)
	 {
	   int ProjectCount = elements.size()/2;
	   ProjectCount = ProjectCount/9;
				
	   System.out.println("Project is available");
	   System.out.println("Number of Project:" +ProjectCount);		
			
	   WebElement htmltable=driver.findElement(By.xpath("//*[@id=\"recenttopproject\"]"));
		    
	   List<WebElement> rows=htmltable.findElements(By.tagName("tr"));
	   List<WebElement> col=htmltable.findElements(By.tagName("td"));
		    
	   //System.out.println("Number of rows is: "+rows.size());
		    
	   for(int rnum=1;rnum<rows.size();rnum++)	 
	    {
		  //System.out.println("Number of rows is: "+rows.size());
		  List<WebElement> columns=rows.get(rnum).findElements(By.tagName("td"));
		  //System.out.println("Number of columns:"+columns.size());
	    	     	
		    for(int cnum=0;cnum<columns.size();cnum++)    		
		     {	
		       System.out.println(columns.get(cnum).getText());
		     }    	
		}
	 
	  driver.close();
  
}

}

@Then("^the result should be displayed based on the Project Number provided$")
public void the_result_should_be_displayed_based_on_the_Project_Number_provided() throws Throwable 
{
	
}

//----------------------------------------------@tag15-----------End----------------------correct----------------------------------------

//------------------------------------------------@tag16------------Start-----------------------------------------------------------

@Given("^DB and Excel file connection to validate the Client Manager in ORG Chart$")
public void DB_and_Excel_file_connection_to_validate_the_Client_Manager_in_ORG_Chart() throws Throwable 
{

	InputStream	input = new FileInputStream(".\\src\\config\\config.properties");
	prop.load(input);
	
	FileInputStream fis = new FileInputStream(".\\src\\PTFiles\\MainSearch.xlsx");
    wb = new XSSFWorkbook(fis);
    sheet = wb.getSheet("ORGClientManager");
	
	String url = prop.getProperty("QCdatabaseurl");
	String username = prop.getProperty("QCdbusername");
	String password = prop.getProperty("QCdbpassword");

	 conn = DriverManager.getConnection(url, username, password);
	
	 sql = "select P.project_number,P.project_id,P.project_name,CONCAT(E.FirstName,+' '+E.MiddleName,+' '+E.LastName) As FirstName, P.Status,p.create_date,T.team_designation from project P left join tbl_project_team T on P.project_id=T.team_project_id left join employee E on T.team_emp_id=E.EmployeeID where T.team_designation='client manager' and create_date between DATEADD(m, -2,GETDATE()) and GETDATE() AND project_number like 'S%'";
	
}

@When("^Compare the Client Manager Data in DB and PT ORG Chart$")
public void Compare_the_Client_Manager_Data_in_DB_and_PT_ORG_Chart() throws Throwable 
{

	PreparedStatement ps =  conn.prepareStatement(sql);
	ResultSet resultSet = ps.executeQuery();    
	int row = 2;
	  List<String> al = new ArrayList<String>();  
	  List<String> al2 = new ArrayList<String>();
	  List<String> al3 = new ArrayList<String>();
	  while(resultSet.next()) 
	  {
		System.out.println(resultSet.getString("project_number")+"     "+resultSet.getString("FirstName"));
	    
	    al.add(resultSet.getString("project_number"));
	    al2.add(resultSet.getString("FirstName"));
	    al3.add(resultSet.getString("project_id"));
	    
	  }

	       System.out.println(al);
	       System.setProperty("webdriver.chrome.driver", "./Driver/chromedriver.exe");
    	   
		   driver = new ChromeDriver();
		   driver.manage().window().maximize();
		  
		   // data, data1, data2;
		   
	          for (int i = 0; i < al3.size(); i++) 
	          {
	    	     Row dataRow = sheet.createRow(row);
	    	 String data = al.get(i);
	    	     String data1 =al2.get(i);
	    	     data2 =al3.get(i);
	    	     //System.out.println("Project Manager: "+name);
				 
	    	    driver.get("http://ffx-web/TrackerQC/projecttracker.aspx?ProjectID="+data2);
				Thread.sleep(2000);
				driver.findElement(By.xpath("//a[contains(text(),'Org Chart')]")).click();
	    	    Thread.sleep(3000);   
	    	    String clientmanager = driver.findElement(By.xpath("//div[@class='panel-body ClientManager'][1]")).getText();
	            
	    	     Cell dataNameCell = dataRow.createCell(1);
			     dataNameCell.setCellValue(data);
			     Cell dataAddressCell = dataRow.createCell(2);
			     dataAddressCell.setCellValue("DB: "+data1);
			     Cell Cell1 = dataRow.createCell(3);
			     Cell1.setCellValue("PT: "+clientmanager);
			     

	             row = row + 1;
	             
	             if(data1.equals(clientmanager))
	             {
	            	 Cell Cell2 = dataRow.createCell(4);
				     Cell2.setCellValue("Correct Client Manager");
	             }
	             else
	             {
	            	 Cell Cell2 = dataRow.createCell(4);
				     Cell2.setCellValue("FAIL");
	             }
	             System.out.println(clientmanager);
	          }
	         
		    System.out.println();
		    //System.out.println("Rows Count: "+(row-2));
		   
	}


@Then("^Update the ORG Client Manager Data in Excel$")
public void Update_the_ORG_Client_Manager_Data_in_Excel() throws Throwable 
{
	 
    String outputDirPath = ".\\src\\PTFiles\\MainSearch.xlsx";
    FileOutputStream fileOut = new FileOutputStream(outputDirPath);
    wb.write(fileOut);
    fileOut.close();
    driver.close();
	
	
}

//----------------------------------------------------------@tag16--------End------correct---------------------------------------------------

//-------------------------------------------------------------@tag17--------------start------------------------------------------------
@Given("^DB and Excel file connection to validate the Employee in ORG Chart$")
public void DB_and_Excel_file_connection_to_validate_the_Employee_in_ORG_Chart() throws Throwable 
{

	InputStream	input = new FileInputStream(".\\src\\config\\config.properties");
	prop.load(input);
	 
	FileInputStream fis = new FileInputStream(".\\src\\PTFiles\\MainSearch.xlsx");
    wb = new XSSFWorkbook(fis);
    sheet = wb.getSheet("ORGEmployee");
	
	String url = prop.getProperty("QCdatabaseurl");
	String username = prop.getProperty("QCdbusername");
	String password = prop.getProperty("QCdbpassword");

	 conn = DriverManager.getConnection(url, username, password);
	
	 sql = "select P.project_number, CONCAT(E.FirstName,+' '+E.MiddleName,+' '+E.LastName) As FirstName,T.team_designation\r\n" + 
			"from project P left join tbl_project_team T on P.project_id=T.team_project_id\r\n" + 
			"left join employee E on E.EmployeeID=T.team_emp_id\r\n" + 
			"where P.project_id=14553";
	
}

@When("^Compare the Employees Data in DB and PT ORG Chart$")
public void Compare_the_Employees_Data_in_DB_and_PT_ORG_Chart() throws Throwable 
{

	PreparedStatement ps =  conn.prepareStatement(sql);
	ResultSet resultSet = ps.executeQuery();    
	 int row = 2;
	  List<String> al = new ArrayList<String>();  
	  List<String> al2 = new ArrayList<String>();
	  List<String> al3 = new ArrayList<String>();
	 // List<String> al4 = new ArrayList<String>();
	  
	  while(resultSet.next()) 
	  {
		System.out.println(resultSet.getString("project_number")+"     "+resultSet.getString("FirstName"));
	    
	    al.add(resultSet.getString("project_number"));
	    al2.add(resultSet.getString("FirstName"));
	    //al3.add(resultSet.getString("project_id"));
	    al3.add(resultSet.getString("team_designation"));
	    
	  }

	       System.out.println(al);
	       System.setProperty("webdriver.chrome.driver", "./Driver/chromedriver.exe");
    	   
		   driver = new ChromeDriver();
		   driver.manage().window().maximize();
		  
		   //String data, data1, data2, data3;
		   
	          for (int i = 0; i < al2.size(); i++) 
	          {
	    	     Row dataRow = sheet.createRow(row);
	    	    String data = al.get(i);
	    	  String data1 =al2.get(i);
	    	     data2 =al3.get(i);
	    	     //data3 =al4.get(i);
	    	     //System.out.println("Project Manager: "+name);
	    	     
	    	    driver.get("http://ffx-web/TrackerQC/projecttracker.aspx?ProjectID=14553");
				Thread.sleep(2000);
				driver.findElement(By.xpath("//a[contains(text(),'Org Chart')]")).click();
	    	    Thread.sleep(3000);   
	    	    
	    	    String projectmanager = driver.findElement(By.xpath("//div[@class = 'panel-body ProjectManager'][1]")).getText();
	    	    System.out.println("Project Manager is: "+projectmanager);
	    	    
	    	    String assisprojectmanager = driver.findElement(By.xpath("//div[@class='panel-body APM']")).getText();
	    	    System.out.println("Assistant Project Manager is: "+assisprojectmanager);
	    	    
	    	    String clientmanager = driver.findElement(By.xpath("//div[@class='panel-body ClientManager'][1]")).getText();
	    	    System.out.println("Client Manager is: "+clientmanager);
	    	    
	    	    String engineersofrecord = driver.findElement(By.xpath("//div[@class='panel-body Engineersrecord']")).getText();
	    	    System.out.println("Engineers of record: "+engineersofrecord);
	    	    
	    	    String mechus = driver.findElement(By.xpath("//div[@class='panel-body MechanicalUS']")).getText();
	    	    System.out.println("Mechanical US employees: "+mechus);
	    	    
	    	    String mechsme = driver.findElement(By.xpath("//div[@class='panel-body MechanicalSME']")).getText();
	    	    System.out.println("Mechanical SME employees: "+mechsme);
	    	    
	    	    String mechcaddsme = driver.findElement(By.xpath("//div[@class='panel-body Mechanical-caddSME']")).getText();
	    	    System.out.println("Mechanical cad SME employees: "+mechcaddsme);
	    	    
	    	    String electricalus = driver.findElement(By.xpath("//div[@class='panel-body ElectricalUS']")).getText();
	    	    System.out.println("Electrical US employees: "+electricalus);
	    	    
	    	    String electricalsme = driver.findElement(By.xpath("//div[@class='panel-body ElectricalSME']")).getText();
	    	    System.out.println("Electrical SME employees: "+electricalsme);
	    	    
	    	    String electricalcaddsme = driver.findElement(By.xpath("//div[@class='panel-body Electrical-caddSME']")).getText();
	    	    System.out.println("Electrical cad SME employees: "+electricalcaddsme);
	    	    
	    	    String plumbingus = driver.findElement(By.xpath("//div[@class='panel-body PlumbingUS']")).getText();
	    	    System.out.println("Plumbing US employees: "+plumbingus);
	    	    
	    	    String plumbingsme = driver.findElement(By.xpath("//div[@class='panel-body PlumbingSME']")).getText();
	    	    System.out.println("Plumbing SME employees: "+plumbingsme);
	    	    
	    	    String plumbingcaddsme = driver.findElement(By.xpath("//div[@class='panel-body PlumbingSME']")).getText();
	    	    
	    	    String qcus = driver.findElement(By.xpath("//div[@class='panel-body QCUS']")).getText();
	    	    System.out.println("QC US employees: "+qcus);
	    	    
	    	    String qcsme = driver.findElement(By.xpath("//div[@class='panel-body QCSME']")).getText();
	    	    System.out.println("QC SME employees: "+qcsme);
	    	   
	          }
	}


@Then("^Update the Employees Data in Excel$")
public void Update_the_Employees_Data_in_Excel() throws Throwable 
{

	 String outputDirPath = ".\\src\\PTFiles\\MainSearch.xlsx";
	    FileOutputStream fileOut = new FileOutputStream(outputDirPath);
	    wb.write(fileOut);
	    fileOut.close();
	    driver.close();
	
}

//--------------------------------------------------------@tag17------End------correct-------------------------------------------------------------


//--------------------------------------------------------@tag18------Start----------------------------------------------------------------------

@Given("^DB and Excel file connection to validate the Project Manager in ORG Chart$")
public void DB_and_Excel_file_connection_to_validate_the_Project_Manager_in_ORG_Chart() throws Throwable 
{
	
	InputStream	input = new FileInputStream(".\\src\\config\\config.properties");
	prop.load(input);
	
	FileInputStream fis = new FileInputStream(".\\src\\PTFiles\\MainSearch.xlsx");
    wb = new XSSFWorkbook(fis);
    sheet = wb.getSheet("ORGProjectManager");
	
	String url = prop.getProperty("QCdatabaseurl");
	String username = prop.getProperty("QCdbusername");
	String password = prop.getProperty("QCdbpassword");

	 conn = DriverManager.getConnection(url, username, password);
	
	 sql = "select P.project_number,P.project_id,P.project_name,CONCAT(E.FirstName,+' '+E.MiddleName,+' '+E.LastName) As FirstName, P.Status,p.create_date,T.team_designation from project P left join tbl_project_team T on P.project_id=T.team_project_id left join employee E on T.team_emp_id=E.EmployeeID where T.team_designation='project manager' and create_date between DATEADD(m, -1,GETDATE()) and GETDATE() AND project_number like 'S%'";
	
}

@When("^Compare the Project Manager Data in DB and PT ORG Chart$")
public void Compare_the_Project_Manager_Data_in_DB_and_PT_ORG_Chart() throws Throwable 
{
	PreparedStatement ps =  conn.prepareStatement(sql);
	ResultSet resultSet = ps.executeQuery();    
	 int row = 2;
	  List<String> al = new ArrayList<String>();  
	  List<String> al2 = new ArrayList<String>();
	  List<String> al3 = new ArrayList<String>();
	  while(resultSet.next()) 
	  {
		System.out.println(resultSet.getString("project_number")+"     "+resultSet.getString("FirstName"));
	    
	    al.add(resultSet.getString("project_number"));
	    al2.add(resultSet.getString("FirstName"));
	    al3.add(resultSet.getString("project_id"));
	    
	  }

	       System.out.println(al);
	       System.setProperty("webdriver.chrome.driver", "./Driver/chromedriver.exe");
    	   
		   driver = new ChromeDriver();
		   driver.manage().window().maximize();
		  
		  // String data, data1, data2;
		   
	          for (int i = 0; i < al3.size(); i++) 
	          {
	    	     Row dataRow = sheet.createRow(row);
	    	   String  data = al.get(i);
	    	   String  data1 =al2.get(i);
	    	     data2 =al3.get(i);
				 
	    	    driver.get("http://ffx-web/TrackerQC/projecttracker.aspx?ProjectID="+data2);
				Thread.sleep(2000);
				driver.findElement(By.xpath("//a[contains(text(),'Org Chart')]")).click();
	    	    Thread.sleep(3000);   
	    	    String projectmanager = driver.findElement(By.xpath("//div[@class = 'panel-body ProjectManager'][1]")).getText();
	            
	    	     Cell dataNameCell = dataRow.createCell(1);
			     dataNameCell.setCellValue(data);
			     Cell dataAddressCell = dataRow.createCell(2);
			     dataAddressCell.setCellValue("DB: "+data1);
			     Cell Cell1 = dataRow.createCell(3);
			     Cell1.setCellValue("PT: "+projectmanager);
			     

	             row = row + 1;
	             
	             if(data1.equals(projectmanager))
	             {
	            	 Cell Cell2 = dataRow.createCell(4);
				     Cell2.setCellValue("Correct Project Manager");
	             }
	             else
	             {
	            	 Cell Cell2 = dataRow.createCell(4);
				     Cell2.setCellValue("FAIL");
	             }
	             System.out.println(projectmanager);
	          }
	         
		    System.out.println();
		    //System.out.println("Rows Count: "+(row-2));
		    
		    
	}


@Then("^Update the ORG Project Manager Data in Excel$")
public void Update_the_ORG_Project_Manager_Data_in_Excel() throws Throwable 
{

	String outputDirPath = ".\\src\\PTFiles\\MainSearch.xlsx";
    FileOutputStream fileOut = new FileOutputStream(outputDirPath);
    wb.write(fileOut);
    fileOut.close();
    driver.close();

	
}



//---------------------------------------------------------@tag18---------End--------------correct-------------------------

//------------------------------------------------------@tag19---------Start-------------------------------------------

@Given("^DB and Excel file connection to validate the Project Number in ORG Chart$")
public void DB_and_Excel_file_connection_to_validate_the_Project_Number_in_ORG_Chart() throws Throwable 
{
	InputStream	input = new FileInputStream(".\\src\\config\\config.properties");
	prop.load(input);
	
	FileInputStream fis = new FileInputStream(".\\src\\PTFiles\\MainSearch.xlsx");
	 wb = new XSSFWorkbook(fis);
	 sheet = wb.getSheet("FPProjectNumber");
	
	String url = prop.getProperty("QCdatabaseurl");
	String username = prop.getProperty("QCdbusername");
	String password = prop.getProperty("QCdbpassword");

	 conn = DriverManager.getConnection(url, username, password);
	
	 sql = "Select * FROM project  WHERE create_date between  DATEADD(m, -2,GETDATE()) and GETDATE()";
	
}

@When("^Compare the Project Number Data in DB and PT Financials Page$")
public void Compare_the_Project_Number_Data_in_DB_and_PT_Financials_Page() throws Throwable 
{
	PreparedStatement ps =  conn.prepareStatement(sql);
	ResultSet resultSet = ps.executeQuery();    

	  int row = 0;
	  List<String> al = new ArrayList<String>();  
	  while(resultSet.next()) 
	  {
		System.out.println(resultSet.getString("project_number")+"     "+resultSet.getString("Status"));
	  
	    al.add(resultSet.getString("project_number"));
	    
	  }
	  
       System.out.println(al);
       System.setProperty("webdriver.chrome.driver", "./Driver/chromedriver.exe");
	    driver = new ChromeDriver();
	    driver.manage().window().maximize();
	
	    /*driver.get("http://ffx-web/TrackerQC/");
	    Thread.sleep(3000);
	    
	    driver.findElement(By.id("tobesearch")).sendKeys(" ");
		driver.findElement(By.id("searchforrecordbtn")).click();
		Thread.sleep(5000);*/
		
       for (int i = 0; i < al.size(); i++) 
       {
    	 
    	   Row dataRow = sheet.createRow(row);
    	   
    	   String data = al.get(i);
    	   System.out.println("List Value "+data);
    	  
    	   //driver.get("http://ffx-web/TrackerQC/");
    	   driver.get(prop.getProperty("QCURL"));
		    Thread.sleep(3000);
		    
		    driver.findElement(By.id("tobesearch")).sendKeys(" ");
			driver.findElement(By.id("searchforrecordbtn")).click();
			Thread.sleep(5000);
    		  driver.findElement(By.xpath("//a[contains(text(),'"+al.get(i)+"')]")).click();
    	   
       if(driver.findElements(By.xpath("(//a[contains(text(),'"+data+"')])[1]")).size() > 0)
		{
    	 
    	   Cell dataNameCell = dataRow.createCell(1);
		    dataNameCell.setCellValue(al.get(i));
		    Cell dataAddressCell = dataRow.createCell(2);
		    dataAddressCell.setCellValue("Present");
			
		}
		
		else
		{
			
			Cell dataNameCell = dataRow.createCell(1);
		    dataNameCell.setCellValue(al.get(i));
		    Cell dataAddressCell = dataRow.createCell(2);
		    dataAddressCell.setCellValue("Not Present");
		}
       //driver.findElement(By.xpath("//a[contains(text(),'"+data+"')][1]")).click();
       row = row + 1;
       
       }
       
	    System.out.println();
	    System.out.println("Rows Count: "+(row));
	    
	  
}


@Then("^Update the Project Number Data in Excel$")
public void Update_the_Project_Number_Data_in_Excel() throws Throwable 
{
	    String outputDirPath = ".\\src\\PTFiles\\MainSearch.xlsx";
	    FileOutputStream fileOut = new FileOutputStream(outputDirPath);
	    wb.write(fileOut);
	    fileOut.close();
	    driver.close();
	
}


//----------------------------------------------------@tag19----------End--------------------------Correct------------------

//--------------------------------------@tag20--------------------------Start-----------------------------------

@Given("^the user should be able to view the Setty Awards list page$")
public void the_user_should_be_able_to_view_the_Setty_Awards_list_page() throws Throwable 
{
	InputStream	input = new FileInputStream(".\\src\\config\\config.properties");
	prop.load(input);
	
    System.out.println("launching Chrome browser");
  
    System.setProperty("webdriver.chrome.driver", "./Driver/chromedriver.exe");
    driver = new ChromeDriver();
	driver.manage().window().maximize();
	driver.get(prop.getProperty("QCURL"));
	
}

@When("^the user clicks on Export to Excel button$")
public void the_user_clicks_on_Export_to_Excel_button() throws Throwable 
{
	WebElement htmltable=driver.findElement(By.xpath("//*[@id=\"recenttopproject\"]"));
    
    List<WebElement> rows=htmltable.findElements(By.tagName("tr"));
    int rowscount = rows.size()-1;
    System.out.println("Number of Project is: "+rowscount);
   // List<WebElement> col=htmltable.findElements(By.tagName("td"));
    /*int Colcount = col.size();
    System.out.println("Number of Columns is: "+Colcount);	*/
    
    for(int rnum=1;rnum<rows.size();rnum++)
	 {
       List<WebElement> columns=rows.get(rnum).findElements(By.tagName("td"));
       //System.out.println("Number of columns:"+columns.size());
       for(int cnum=0;cnum<columns.size();cnum++)		    	
    	{
    	System.out.println(columns.get(cnum).getText());
    	}	
     }	 
    
    System.out.println("Verifying Export To Excel Function");
    	
	WebElement ExportToExcel = driver.findElement(By.id("btnExporttoExcel"));
	ExportToExcel.click();
		
	driver.get("chrome://downloads/");
	//driver.findElement(By.id("file-link")).click();
		
	System.out.println("File Download is Successfull");	 
	driver.close();
	
}


@Then("^the user should be able to see the Excel downloaded and the list of Setty Awards$")
public void the_user_should_be_able_to_see_the_Excel_downloaded_and_the_list_of_Setty_Awards() throws Throwable 
{
	
}

}