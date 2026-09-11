SET @statusString = NULL;
SET @accountUUID = NULL;
SET @sessionToken = NULL;

CALL API_Public_Patient_LogIn('jb2378@kent.ac.uk', 'password', @statusString, @accountUUID, @sessionToken);

SELECT
	@statusString as Status,
    @accountUUID as Account_UUID,
    @sessionToken as Session_Token;