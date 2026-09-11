SET @logInStatusString = NULL;
SET @accountUUID = NULL;
SET @sessionToken = NULL;

CALL API_Public_Patient_LogIn('jb2378@kent.ac.uk', 'password', @logInStatusString, @accountUUID, @sessionToken);

CALL API_Private_Patient_SetDetails(@accountUUID, @sessionToken, 'Mark', 'Smith', 'Dr.', 'mark.smith@gmail.com', 
'07000000000', '12 Parkway Drive', '', 'Australia City', 'New Zealand County', 'CT1 1TR', @setDetailsStatusString);

CALL API_Private_Patient_LogOut(@accountUUID, @sessionToken, @logOutStatusString);

SELECT
	@setDetailsStatusString