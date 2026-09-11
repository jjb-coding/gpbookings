SET @logInStatusString = NULL;
SET @accountUUID = NULL;
SET @sessionToken = NULL;

CALL API_Public_Patient_LogIn('jb2378@kent.ac.uk', 'password', @logInStatusString, @accountUUID, @sessionToken);

CALL API_Private_Patient_GetDetails(@accountUUID, @sessionToken, @firstName, @surname,
@title, @contactEmail, @contactPhoneNumber, @street1, @street2, @city, @county, @postcode, @detailsStatusString);

CALL API_Private_Patient_LogOut(@accountUUID, @sessionToken, @logOutStatusString);

SELECT
	@accountUUID,
    @sessionToken,
    @firstName,
    @surname,
	@title,
    @contactEmail,
    @contactPhoneNumber,
    @street1,
    @street2,
    @city,
    @county,
    @postcode,
    @detailsStatusString,
    @logInStatusString;