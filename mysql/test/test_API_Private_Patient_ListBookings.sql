SET @logInStatusString = NULL;
SET @accountUUID = NULL;
SET @sessionToken = NULL;

CALL API_Public_Patient_LogIn('jb2378@kent.ac.uk', 'password', @logInStatusString, @accountUUID, @sessionToken);

CALL API_Private_Patient_MakeBooking(@accountUUID, @sessionToken, '2025-03-11', 8, @makeStatusString);

CALL API_Private_Patient_ListBookings(@accountUUID, @sessionToken, 4, 2025, @listStatusString);

SELECT
	@logInStatusString as LogInStatus,
    @makeStatusString as MakeStatus,
    @listStatusString as ListStatus;