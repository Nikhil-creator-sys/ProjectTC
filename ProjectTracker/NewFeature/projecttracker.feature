#Author: your.email@your.domain.com
#Keywords Summary :
#Feature: List of scenarios.
#Scenario: Business rule through list of steps with arguments.
#Given: Some precondition step
#When: Some key actions
#Then: To observe outcomes or validation
#And,But: To enumerate more Given,When,Then steps
#Scenario Outline: List of steps for data-driven as an Examples and <placeholder>
#Examples: Container for s table
#Background: List of steps run before each of the scenarios
#""" (Doc Strings)
#| (Data Tables)
#@ (Tags/Labels):To group Scenarios
#<> (placeholder)
#""
## (Comments)
#Sample Feature Definition Template

#@tag
#Feature: Total Hours and Cost Validation in PT

  #@tag1
  #Scenario: US and SME Total Hours validation
    #Given DB and Excel file connection
    #When Compare the Total Hours in DB and PT
    #Then Update the Result in Excel
    
   #  @tag2
  #Scenario: US and SME Total Cost validation
    #Given DB and Excel file connection For Total Cost
    #When Compare the Total Cost in DB and PT
   # Then Update the Total Cost Status in Excel

 @tag
 Feature: Project Data Validation in PT
 
 @tag1
  Scenario: Search Project Number and Validate
    Given DB and Excel file connection to validate the Project Number
    When Compare the Project Number in DB and PT
    Then Update the Project Number Status in Excel
    
@tag2
 Scenario: Search through Filter in PT
    Given the user should be able to open the Project Tracker award page
    When the data is selected from the filter dropdown and clicked on Search button
    Then the result should be displayed based on the search data
 
 @tag3
 Scenario: Search Client Manager through CM Filter in PT
    Given the user should be able to open the PT list page
    When the Client Manager is selected from the CM filter dropdown and clicked on Search button
    Then the result should be displayed based on the Client Manager selected in the CM filter
    
@tag4
 Scenario: Client Manager validation in PT and DB
    Given DB and Excel file connection to validate the Client Manager
    When Compare the Client Manager in DB and PT
    Then Update the Client Manager Data in Excel
    
@tag5
 Scenario: Search all the Projects of a Client Manager in DB and validate with PT
    Given DB and Excel file connection to validate the particular Client Manager
    When Compare the Client Manager results in DB and PT
    Then Update the Client Manager results in Excel
    
@tag6
 Scenario: Search Project Manager through PM Filter in PT
    Given the user should be able to connect to the Project Tracker award page
    When the Project Manager is selected from the PM filter dropdown and clicked on Search button
    Then the result should be displayed based on the Project Manager selected
    
@tag7
    Scenario: Search all the Projects of a Project Manager in DB and validate with PT
    Given DB and Excel file connection to validate the particular Project Manager
    When Compare the Project Manager results in DB and PT
    Then Update the Project Manager results in Excel
    
@tag8
    Scenario: Project Manager validation in PT and DB
    Given DB and Excel file connection to validate the Project Manager
    When Compare the Project Manager in DB and PT
    Then Update the Project Manager Data in Excel
    
@tag9
    Scenario: Search the Project through Status Filter in PT
    Given the user should be able to Navigate to the Project Tracker award page
    When the Status is selected from the Status filter dropdown and clicked on Search button
    Then the result should be displayed based on the Status selected
    
@tag10
   Scenario: Search the Projects based on Status in DB and validate with PT
    Given DB and Excel file connection to validate the Status of a Project
    When Compare the results of Project Status in DB and PT
    Then Update the results of Project Status in Excel
    
 @tag11
  Scenario: Search the Team Member through Team Member Filter in PT
    Given the user should be able to Navigate to the Project Tracker list page
    When the Team Member is selected from the Team Member filter dropdown and clicked on Search button
    Then the result should be displayed based on the Team Member selected
    
@tag12
  Scenario: Search the Projects of a Team Member in DB and validate with PT
    Given DB and Excel file connection to validate the Projects of a Team Member
    When Compare the results of Team Member projects in DB and PT
    Then Update the results of Team Member projects in Excel 
    
@tag13
  Scenario: Search the Projects by selecting the year through Year Filter in PT
    Given the user should be able to view the Search Filter page
    When the year is selected from the year filter dropdown and clicked on Search button
    Then the result should be displayed based on the year selected
    
@tag14
 Scenario: Search the Projects based on the year in DB and validate with PT
    Given DB and Excel file connection to validate the Projects of a particular year
    When Compare the results of projects year in DB and PT
    Then Update the results of projects year in Excel
    
@tag15
 Scenario: Search the Projects based on the Project Number in PT
    Given the user should be able to view the Project Tracker list page
    When the project number is entered in the Main Search bar and clicked on Search button
    Then the result should be displayed based on the Project Number provided
    
@tag16
 Scenario: Client Manager validation in PT ORG Chart with DB
    Given DB and Excel file connection to validate the Client Manager in ORG Chart
    When Compare the Client Manager Data in DB and PT ORG Chart
    Then Update the ORG Client Manager Data in Excel
    
@tag17
 Scenario: Employee validation in PT ORG Chart with DB
    Given DB and Excel file connection to validate the Employee in ORG Chart
    When Compare the Employees Data in DB and PT ORG Chart
    Then Update the Employees Data in Excel
 
@tag18
 Scenario: Project Manager validation in ORG Chart with DB
    Given DB and Excel file connection to validate the Project Manager in ORG Chart
    When Compare the Project Manager Data in DB and PT ORG Chart
    Then Update the ORG Project Manager Data in Excel
    
@tag19
 Scenario: Project Number validation in Financials Page with DB
    Given DB and Excel file connection to validate the Project Number in ORG Chart
    When Compare the Project Number Data in DB and PT Financials Page
    Then Update the Project Number Data in Excel
    
@tag20
 Scenario: Setty Awards list validation in PT
    Given the user should be able to view the Setty Awards list page
    When the user clicks on Export to Excel button
    Then the user should be able to see the Excel downloaded and the list of Setty Awards 
 