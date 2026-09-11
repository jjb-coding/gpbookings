SET @newStatus = NULL;
SET @secondAttempt = NULL;

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
	'email@kent.ac.uk',
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

CALL API_Public_Patient_New
(
	'email@kent.ac.uk',
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
    @secondAttempt
);

SELECT
	@newStatus,
    @secondAttempt;
    