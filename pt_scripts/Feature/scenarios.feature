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
@tag
Feature: PT Test Scenarios Validation

  @tag1
  Scenario: US and SME Total Hours validation
    Given DB and Excel file connection
    When Compare the Total Hours in DB and PT
    Then Update the Result in Excel
   
   @tag2
   Scenario: US and SME Total Cost validation
    Given DB and Excel file connection For Total Cost
    When Compare the Total Cost in DB and PT
    Then Update the Total Cost Status in Excel
    
   @tag3
   Scenario: Project Margin validation
    Given DB and Excel file connection For Project Margins
    When Compare the Margins present in DB and PT
    Then Update the existing Project Margins in Excel
    
   @tag4
   Scenario: Project Fees validation
    Given DB and Excel file connection For Project Fees
    When Compare the Project Fees present in DB and PT
    Then Update the Project Fees details in Excel
    
   @tag5
   Scenario: Project Background validation
    Given DB and Excel file connection For Project Background
    When Compare the Project Background present in DB and PT
    Then Update the Project Background details in Excel
    
   @tag6
   Scenario: Project Schedule validation
    Given DB and Excel file connection For Project Schedules
    When Compare the Project Schedules prsent in DB and PT
    Then Update the Project Schedules in Excel
    
   @tag7
   Scenario: Add Services validation
    Given DB and Excel file connection For Add Services
    When Compare the Add Services present in DB and PT
    Then Update the Add Services details in Excel
    
    @tag8
   Scenario: Proposal FileName validation
    Given DB and Excel file connection For Proposal File
    When Compare the Proposal FileName present in DB and PT
    Then Update the Proposal details in Excel
    
    @tag9
   Scenario: Client RFP FileName validation
    Given DB and Excel file connection For Client RFP
    When Compare the Client RFP FileName present in DB and PT
    Then Update the Client RFP details in Excel
    
    
    
    
    
    