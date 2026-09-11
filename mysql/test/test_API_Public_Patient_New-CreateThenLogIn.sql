SET @newStatus = NULL;
SET @logInStatus = NULL;
SET @accountUUID = NULL;
SET @sessionToken = NULL;

SELECT
	AccountUUID
INTO
	@DoctorUUID
FROM
	Account a
JOIN
	Doctor d
ON
	d.AccountID = a.AccountID
LIMIT 1;

CALL API_Public_Patient_New
(
	'login@kent.ac.uk',
    'password',
    @DoctorUUID,
    'John',
    'Smith',
    'Mr.',
    '07000000000',
    '57 Long Street',
    '',
    'York',
    'Yorkshire',
    'YK13 8AY',
    @newStatus
);

CALL API_Public_Patient_LogIn
(
	'login@kent.ac.uk',
    'password',
    @logInStatus,
    @accountUUID,
    @sessionToken
);

SELECT
	@newStatus,
    @logInStatus,
    @accountUUID,
    @sessionToken;
    