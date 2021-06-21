
/* Users Assign tasks*/

Delete FROM [Three_automation.InspectionPlatform].[UserManagement].[AssignAction];

Delete FROM [Camunda_BPM_DB].[dbo].[ACT_RU_IDENTITYLINK]  where  TENANT_ID_='3604bbb8-0601-414e-8360-24328a1b837f'
Delete FROM [Camunda_BPM_DB].[dbo].[ACT_RU_TASK]  where  TENANT_ID_='3604bbb8-0601-414e-8360-24328a1b837f'

Delete FROM [Camunda_BPM_DB].[dbo].[ACT_RU_TASK]  where ASSIGNEE_ = 'InspectionApprover@elm.sa' and DESCRIPTION_ like '%Facility_%';

/* process*/

Delete FROM [Three_automation.InspectionPlatform].[Process].[InspectionProcessViolation];
Delete FROM [Three_automation.InspectionPlatform].[Process].[OwnerSignInfo];
Delete FROM [Three_automation.InspectionPlatform].[Process].[InspectionProcessReport];
Delete FROM [Three_automation.InspectionPlatform].[Process].[InspectionProcessInspector];
Delete FROM [Three_automation.InspectionPlatform].[Process].[OwnerSignInfo];
Delete FROM [Three_automation.InspectionPlatform].[Process].[InspectionProcessFile];
Delete FROM [Three_automation.InspectionPlatform].[Process].[GenericAction];
Delete FROM [Three_automation.InspectionPlatform].[Process].[InspectionProcess];


/*inspection item*/

Delete FROM [Three_automation.InspectionPlatform].[Penalties].[InspectionItemProcessType];
Delete FROM [Three_automation.InspectionPlatform].[Penalties].[InspectionItemSpecialization];
Delete FROM [Three_automation.InspectionPlatform].[Penalties].[InspectionItemActivity];
Delete FROM [Three_automation.InspectionPlatform].[Penalties].[InspectionItem];


/*Delete Facility */
Delete FROM [three_automation.InspectionPlatform].[Facility].[FacilityLicense] ;

Delete FROM [three_automation.InspectionPlatform].[Facility].[FacilityOwner];

Delete FROM [three_automation.InspectionPlatform].[Facility].[FacilityWorker];

Delete FROM [three_automation.InspectionPlatform].[Facility].[WorkTime];

Delete FROM [three_automation.InspectionPlatform].[Facility].[FacilityActivity];

Delete FROM  [Three_automation.InspectionPlatform].[Facility].[GenericAction];

Delete FROM  [Three_automation.InspectionPlatform].[Facility].[FacilityViolations];

Delete FROM [three_automation.InspectionPlatform].[Facility].[FacilityVisits];

Delete FROM [three_automation.InspectionPlatform].[Facility].[OrganizationPhoto];

Delete FROM [three_automation.InspectionPlatform].[Facility].[Audit];

Delete FROM [three_automation.InspectionPlatform].[Facility].[FacilityClosure];

Delete FROM [three_automation.InspectionPlatform].[Facility].[Facility];


/*incednets*/

Delete  FROM [Three_automation.InspectionPlatform].[Incident].[IncidentsReport];
Delete  FROM [Three_automation.InspectionPlatform].[Incident].[Incidents];

/*Survey tasks*/



/*Delete Violations*/

Delete FROM [three_automation.InspectionPlatform].[Penalties].[ViolationTypeVersions];

Delete FROM [three_automation.InspectionPlatform].[Penalties].[ViolationType];

Delete FROM [three_automation.InspectionPlatform].[Penalties].[Log];

/*delete items categories*/

Delete From [Three_automation.InspectionPlatform].[Penalties].[InspectionCategory]   where id > 6;


/****** Script for SelectTopNRows command from SSMS  ******/
SELECT *  FROM [Camunda_BPM_DB].[dbo].[ACT_RU_TASK]  
where DESCRIPTION_ like '%Facility_zwyzemgbm%'   
order by CREATE_TIME_ desc;

SELECT *  FROM [Camunda_BPM_DB].[dbo].[ACT_RU_TASK]  order by CREATE_TIME_ desc;


  /****** Script for SelectTopNRows command from SSMS  ******/
SELECT * FROM [Three_automation.InspectionPlatform].[Process].[InspectionProcess]
Where Status = 'Processed' order by CreationTime desc


  /****** Script for SelectTopNRows command from SSMS  ******/
SELECT *   FROM [Three_automation.InspectionPlatform].[Penalties].[InspectionItem]
where Status = 'Active' AND ClientConfiguration Not Like '%shape%' Order by CreationTime Desc ;


/****** Script for SelectTopNRows command from SSMS  ******/
SELECT *  FROM [Three_automation.InspectionPlatform].[FieldSupport].[FieldSupportProcess]
  where FacilityName like '%Facility_zwyzemgbm%'



  SELECT TOP (1) * FROM [Camunda_BPM_DB].[dbo].[ACT_RU_TASK] where TASK_DEF_KEY_ = 'InspectorTask' AND DESCRIPTION_ LIKE '%Facility_zwyzemgbm%' order by CREATE_TIME_ desc