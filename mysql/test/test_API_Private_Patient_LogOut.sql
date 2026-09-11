SET @logInStatusString = NULL;
SET @logOutStatusString = NULL;
SET @accountUUID = NULL;
SET @sessionToken = NULL;

CALL API_Public_Patient_LogIn('jb2378@kent.ac.uk', 'password', @logInStatusString, @accountUUID, @sessionToken);

CALL API_Private_Patient_LogOut(@accountUUID, @sessionToken, @logOutStatusString);

SELECT
	@logInStatusString as LogInStatus,
	@logOutStatusString as LogOutStatus,
    @accountUUID as Account_UUID,
    @sessionToken as Session_Token;