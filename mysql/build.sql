-- -------------
-- --- CLEANUP PREVIOUS SCRIPT
-- -------------
DROP DATABASE IF EXISTS db;
DROP USER IF EXISTS 'patient'@'localhost';

-- -------------
-- --- CREATE DATABASE
-- -------------
CREATE DATABASE db;
USE db;

-- -------------
-- --- RECORD-LEVEL CHECK CONSTRAINT FUNCTIONS
-- -------------
DELIMITER //

-- Validates usernames.
-- @param userName The username to validate.
-- @return 1 for True, 0 for False.
CREATE FUNCTION IsUserNameValid (
	_userName VARCHAR(255) CHARACTER SET utf8mb4
)
RETURNS BIT
NO SQL
DETERMINISTIC
BEGIN
	-- Cannot be less than 5 characters
    IF LENGTH(_userName) <= 5 THEN
		RETURN 0;
	END IF;
    
    -- Must begin with a letter, end with a letter or a number, and
    -- only have letters, numbers and certain special characters otherwise.
    RETURN CASE WHEN
        (_userName REGEXP '^[A-Za-z][-.$&a-zA-Z0-9]*[a-zA-Z0-9]$')
    THEN 1 ELSE 0 END;
END//

-- Validates personal names, first and last.
-- @param name The name to validate.
-- @return 1 for True, 0 for False.
CREATE FUNCTION IsNameValid(
	_name varchar(255) character set utf8mb4
)
RETURNS bit
NO SQL
DETERMINISTIC
BEGIN
	-- Cannot have 1 or 0 characters.
	IF LENGTH(_name) <= 1 THEN
		RETURN 0;
	END IF;
    
    -- Must begin with a capital letter, end with a letter, and
    -- have only letters, dashes and spaces otherwise.
	RETURN CASE WHEN
		(_name REGEXP '[A-Z]([- a-zA-z])*[a-zA-Z]')
	THEN 1 ELSE 0 END;
END//


-- Validates phone numbers, local and international.
-- Does not check country codes.
-- @param email The phone number to validate.
-- @return 1 for True, 0 for False.
CREATE FUNCTION IsPhoneNumberValid
(
	_phoneNumber varchar(255) character set utf8mb4
)
RETURNS bit
NO SQL
DETERMINISTIC
BEGIN
	RETURN CASE WHEN
			-- National phone number case
			((LENGTH(_phoneNumber) = 11)
			AND 
			(_phoneNumber REGEXP '[0-9]*'))
		OR
			-- International phone number case
			((LENGTH(_phoneNumber) = 13)
			AND
			(_phoneNumber REGEXP '+[0-9]*'))
	THEN 1 ELSE 0 END;
END//


-- Validates email addresses.
-- @param email The email to validate.
-- @return 1 for True, 0 for False.
CREATE FUNCTION IsEmailValid
(
	_Email varchar(255) character set utf8mb4
)
RETURNS bit
NO SQL
DETERMINISTIC
BEGIN
	-- Enforces conventional email rules
	RETURN _Email REGEXP
    '[A-Za-z0-9]*[-A-Za-z0-9]*[A-Za-z0-9]@([A-Za-z0-9]*[-A-Za-z0-9]*[A-Za-z0-9].)*[A-Za-z0-9]*[-A-Za-z0-9]*[A-Za-z0-9]';
END//

-- Validates email addresses.
-- (for those 0.1% of cases)
-- @param email The email to validate.
-- @return 1 for True, 0 for False.
CREATE FUNCTION IsEmailValidB
(
	_Email varchar(255) character set utf8mb4
)
RETURNS bit
NO SQL
DETERMINISTIC
BEGIN
	-- Declarations
	DECLARE
		_a, _b, _c, _d, _Index
		int unsigned;
	DECLARE
		_Char
		char(1);
        
	-- Initialise variables
	SET _a = 0;
	SET _b = 0;
	SET _c = 0;
	SET _d = 0;
	SET _Index = 0;

	-- For each character
	WHILE (_Index < LENGTH(_email)) DO
		SET _Char = SUBSTRING(_Email, _Index, 1);

		-- Identifier case
		IF _a = 0 THEN
		BEGIN
			SET _b = CASE WHEN _Char = '.' THEN 1 ELSE 0 END;
			IF _b = 1 AND _Index = 0 THEN
				RETURN 0;
			END IF;
			IF _Char NOT LIKE '[-0-9A-Za-z!#$%&''*+/=?^_`{|}~].' THEN
				SET _a = 1;
			ELSE
				SET _c = 1;
			END IF;
		END;
        
		-- Identifier-domain separator '_' case
		ELSEIF _a = 1 THEN
		BEGIN
			IF _c = 0 THEN
				RETURN 0;
			END IF;
			IF _b = 1 THEN
				RETURN 0;
			END IF;
			IF _Char != '_' THEN
				RETURN 0;
			END IF;
			SET _a = 2;
			SET _c = 0;
		END;
        
        -- Domain label case
		ELSEIF _a = 2 THEN
		BEGIN
			SET _b = CASE WHEN _Char = '-' THEN 1 ELSE 0 END;
			IF _b = 1 AND _c = 0 THEN
				RETURN 0;
			END IF;
			IF _Char NOT LIKE '[-A-Za-z0-9]' THEN
				SET _a = 3;
			ELSE
				SET _c = 1;
			END IF;
		END;
        -- Domain label separator '.' case
		ELSEIF _a = 3 THEN
		BEGIN
			IF _c = 0 THEN
				RETURN 0;
			END IF;
			IF _b = 1 THEN
				RETURN 0;
			END IF;
			IF _Char != '.' THEN
				RETURN 0;
			END IF;
			SET _d = _d + 1;
			SET _a = 2;
			SET _c = 0;
		END;
        END IF;

		SET _Index = _Index + 1;
	END WHILE;

	-- Verify iteration terminated in the state
	RETURN CASE WHEN _a = 2 AND _c = 1 AND _d >= 1 THEN 1 ELSE 0 END;
END//

-- Validates UK post codes.
-- Assumes that the post codes are already right-trimmed.
-- @param email The post code to validate.
-- @return 1 for True, 0 for False.
CREATE FUNCTION IsPostCodeValid
(
	_postCode varchar(255) character set utf8mb4
)
RETURNS bit
NO SQL
DETERMINISTIC
BEGIN
	-- [REF: https://assets.publishing.service.v.uk/media/5a81ebbded915d74e6234d42/Appendix_C_ILR_2017_to_2018_v1_Published_28April17.pdf]
	RETURN CASE WHEN
			(_postCode REGEXP '[A-Z][0-9] [0-9][A-Z][A-Z]')
		OR	(_postCode REGEXP '[A-Z][0-9][0-9] [0-9][A-Z][A-Z]')
		OR	(_postCode REGEXP '[A-Z][A-Z][0-9] [0-9][A-Z][A-Z]')
		OR	(_postCode REGEXP '[A-Z][A-Z][0-9][0-9] [0-9][A-Z][A-Z]')
		OR	(_postCode REGEXP '[A-Z][0-9][A-Z] [0-9][A-Z][A-Z]')
		OR	(_postCode REGEXP '[A-Z][A-Z][0-9][A-Z] [0-9][A-Z][A-Z]')
	THEN 1 ELSE 0 END;
END//
DELIMITER ;


-- -------------
-- --- TABLES
-- -------------
-- TYPES: 
--  bigint unsigned AUTO_INCREMENT 		: key
--  char(36)                    		: uuid
--  varchar(64)							: identifiers


-- The structure of DataBlob is agnostic to the database i.e. it is a binary
-- structure that the client can convert to an object or set of files. The 
-- client should fetch the most recent version of this record of the same ClientVersion.
-- Serves data to a Download endpoint.
CREATE TABLE ClientData (
	Version bigint unsigned AUTO_INCREMENT PRIMARY KEY,
	ClientVersion int unsigned,
	DataBlob BLOB
);

-- Logical description of a day class. Has a text identifier.
-- Each time slot has an index [0..(n-1)].
-- Serves data to a Download endpoint.
CREATE TABLE PracticeDayDescription (
	PracticeDayDescriptionID bigint unsigned AUTO_INCREMENT PRIMARY KEY,
	Identifier varchar(64) character set utf8mb4 NOT NULL,
	OpenTime time NOT NULL,
	NumberOfSlots int unsigned NOT NULL,
	LunchStart int unsigned,
	LunchEnd int unsigned
);

-- Contains constants that the MySQL server logic uses. Only the most recent
-- version should be considered.
-- Serves data to a Download endpoint.
CREATE TABLE Practice (
	Version bigint unsigned AUTO_INCREMENT PRIMARY KEY,
	GracePeriod time NOT NULL,
	SlotLength time NOT NULL,
	MondayID bigint unsigned,
		FOREIGN KEY (MondayID) REFERENCES PracticeDayDescription (PracticeDayDescriptionID),
	TuesdayID bigint unsigned,
		FOREIGN KEY (TuesdayID) REFERENCES PracticeDayDescription (PracticeDayDescriptionID),
	WednesdayID bigint unsigned,
		FOREIGN KEY (WednesdayID) REFERENCES PracticeDayDescription (PracticeDayDescriptionID),
	ThursdayID bigint unsigned,
		FOREIGN KEY (ThursdayID) REFERENCES PracticeDayDescription (PracticeDayDescriptionID),
	FridayID bigint unsigned,
		FOREIGN KEY (FridayID) REFERENCES PracticeDayDescription (PracticeDayDescriptionID),
	SaturdayID bigint unsigned,
		FOREIGN KEY (SaturdayID) REFERENCES PracticeDayDescription (PracticeDayDescriptionID),
	SundayID bigint unsigned,
		FOREIGN KEY (SundayID) REFERENCES PracticeDayDescription (PracticeDayDescriptionID)
);

-- Contains a message template with up to 3 distinct {param_name} parameters.
-- The Identifier is the superclass i.e. 'BookingMade'; the Subtype is the subclass
-- i.e. 'Doctor', and may be null. All Identifier + Subtype combinations used by the
-- database are expected; no records may be deleted after initialisation. The FixedSubject
-- and TemplatedBody fields can be changed.
CREATE TABLE MessageTemplate (
	MessageTemplateID bigint unsigned AUTO_INCREMENT PRIMARY KEY,
	Identifier varchar(255) character set utf8mb4,
	Subtype varchar(255) character set utf8mb4,
	FixedSubject varchar(255) character set utf8mb4 NOT NULL,
	TemplatedBody MEDIUMTEXT NOT NULL
);

-- Enumerable of all valid titles.
-- Serves data to a Download endpoint.
CREATE TABLE Title (
	Identifier varchar(64) character set utf8mb4 PRIMARY KEY
);

-- Supertype of Doctor, Patient. Manages both authentication and authorisation information,
-- and some generic personal details shared across all subtypes.
CREATE TABLE Account (
	AccountID bigint unsigned AUTO_INCREMENT PRIMARY KEY,
    AccountUUID char(36) character set utf8mb4 UNIQUE NOT NULL,
    
    SessionToken char(36) character set utf8mb4,
	SessionTimestamp timestamp,
    
    SecurityEmail varchar(255) character set utf8mb4 NOT NULL,
	PasswordHash varchar(64) character set utf8mb4 NOT NULL,
    
	FirstName varchar(255) character set utf8mb4 NOT NULL,
	Surname varchar(255) character set utf8mb4 NOT NULL,
	ContactEmail varchar(255) character set utf8mb4 NOT NULL
);

-- Manages a password reset request.
CREATE TABLE PasswordResetRequest (
	PasswordResetRequestID bigint unsigned AUTO_INCREMENT PRIMARY KEY,
	AccountID bigint unsigned NOT NULL,
		FOREIGN KEY (AccountID) REFERENCES Account(AccountID),
	Verification char(18) NOT NULL,
	RequestTimestamp timestamp NOT NULL
);

-- Subtype of Account. Contains Doctor-specific fields.
CREATE TABLE Doctor (
	AccountID bigint unsigned PRIMARY KEY,
		FOREIGN KEY (AccountID) REFERENCES Account(AccountID),
	Specialism varchar(255) character set utf8mb4 NOT NULL
);

-- Subtype of Account. Contains Patient-specific fields.
CREATE TABLE Patient (
	AccountID bigint unsigned PRIMARY KEY,
		FOREIGN KEY (AccountID) REFERENCES Account(AccountID),
	
	DoctorID bigint unsigned NOT NULL,
		FOREIGN KEY (DoctorID) REFERENCES Account(AccountID),
	
	Title varchar(64) character set utf8mb4 NOT NULL,
		FOREIGN KEY (Title) REFERENCES Title(Identifier),
	IsEsquire bit NOT NULL DEFAULT 0,
    
	ContactPhoneNumber varchar(255) character set utf8mb4 NOT NULL,
	Street1 varchar(255) character set utf8mb4 NOT NULL,
	Street2 varchar(255) character set utf8mb4 NOT NULL,
	City varchar(255) character set utf8mb4 NOT NULL,
	County varchar(255) character set utf8mb4 NOT NULL,
	PostCode varchar(255) character set utf8mb4 NOT NULL
);

-- Supertype of AttendedBooking. Covers both past and present bookings. 
CREATE TABLE Booking (
	BookingID bigint unsigned AUTO_INCREMENT PRIMARY KEY,
    BookingUUID char(36) character set utf8mb4 UNIQUE,
    
	PatientID bigint unsigned NOT NULL,
		FOREIGN KEY (PatientID) REFERENCES Account(AccountID),
	DoctorID bigint unsigned NOT NULL,
		FOREIGN KEY (DoctorID) REFERENCES Account(AccountID),
	BookingTimestamp timestamp NOT NULL,
    Slot int unsigned,
    WeekDay varchar(255) character set utf8mb4,
    ModifiedTimestamp timestamp NOT NULL
);

-- Subtype of Booking. For cases where a booking was attended.
CREATE TABLE AttendedBooking (
	BookingID bigint unsigned PRIMARY KEY,
		FOREIGN KEY (BookingID) REFERENCES Booking(BookingID),
	Summary MEDIUMTEXT,
	PrescriptionsJSON MEDIUMTEXT
);

-- Stores all messages associated with an account. Includes generation metadata
-- for logging purposes.
CREATE TABLE Message (
	MessageID bigint unsigned AUTO_INCREMENT PRIMARY KEY,
    MessageUUID char(36) UNIQUE,
    
    AccountID bigint unsigned,
		FOREIGN KEY (AccountID) REFERENCES Account(AccountID),
    
	HasBeenReceived bit NOT NULL,
    SentTimestamp timestamp NOT NULL,
    MessageSubject varchar(255) character set utf8mb4 NOT NULL,
    MessageBody MEDIUMTEXT NOT NULL,
	
	MessageTemplateIdentifier varchar(255) character set utf8mb4 NOT NULL,
	MessageTemplateSubtype varchar(255) character set utf8mb4
);

-- Logs every call made to an API procedure.
CREATE TABLE LogCall (
	LogID bigint unsigned AUTO_INCREMENT PRIMARY KEY,
	AccountUUID char(36) character set utf8mb4,
	EndpointCalled varchar(255) character set utf8mb4 NOT NULL,
	InformationJSON varchar(4192) character set utf8mb4,
	LogTimestamp timestamp NOT NULL
);

-- -------------
-- --- TRIGGERS
-- -------------
DELIMITER //

-- -- Account
-- INSERT
CREATE TRIGGER InsertValidateAccount
BEFORE INSERT ON Account
FOR EACH ROW
BEGIN
    IF (IsEmailValid(NEW.SecurityEmail) = 0) THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Security Email Invalid';
    END IF;
    IF (IsEmailValid(NEW.ContactEmail) = 0) THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Contact Email Invalid';
    END IF;
    IF (IsNameValid(NEW.FirstName) = 0) THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'First Name Invalid';
    END IF;
    IF (IsNameValid(NEW.Surname) = 0) THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Surname Invalid';
    END IF;
END//

-- UPDATE
CREATE TRIGGER UpdateValidateAccount
BEFORE UPDATE ON Account
FOR EACH ROW
BEGIN
    IF (IsEmailValid(NEW.ContactEmail) = 0) THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Contact Email Invalid';
    END IF;
    IF (IsNameValid(NEW.FirstName) = 0) THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'First Name Invalid';
    END IF;
    IF (IsNameValid(NEW.Surname) = 0) THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Surname Invalid';
    END IF;
END//

-- -- Account
CREATE TRIGGER InsertValidatePatient
BEFORE INSERT ON Patient
FOR EACH ROW
BEGIN
    IF (IsPhoneNumberValid(NEW.ContactPhoneNumber) = 0) THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Phone Number Invalid';
    END IF;
    IF (IsPostCodeValid(NEW.PostCode) = 0) THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Postcode Invalid';
    END IF;
END//

CREATE TRIGGER UpdateValidatePatient
BEFORE UPDATE ON Patient
FOR EACH ROW
BEGIN
    IF (IsPhoneNumberValid(NEW.ContactPhoneNumber) = 0) THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Phone Number Invalid';
    END IF;
    IF (IsPostCodeValid(NEW.PostCode) = 0) THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Postcode Invalid';
    END IF;
END//

DELIMITER ;


-- -------------
-- --- VIEWS
-- -------------

-- Shows bookings and attaches WasAttended and IsPast data.
CREATE VIEW BookingView AS
SELECT
    b.BookingUUID,
    b.BookingTimestamp,
    b.PatientID,
    b.DoctorID,
    CASE WHEN a.BookingID IS NOT NULL THEN 1 ELSE 0 END AS WasAttended
FROM
    Booking b
LEFT JOIN
    AttendedBooking a ON b.BookingID = a.BookingID;

-- Shows the most recent Practice record.
CREATE VIEW PracticeRecent AS
SELECT
	*
FROM
	Practice
WHERE
	Version = (SELECT MAX(Version) FROM Practice)
LIMIT 1;

-- Shows the most recent ClientData records for each unique client version index.
CREATE VIEW ClientDataRecent AS
SELECT
	a.*
FROM
	ClientData a
LEFT JOIN
	ClientData b
ON
		a.ClientVersion = b.ClientVersion
	AND a.Version < b.Version
WHERE
	b.Version IS NULL;

-- -------------
-- --- API FUNCTIONS / PROCEDURES
-- -------------
DELIMITER //

-- Determines if an Account is of Patient subtype.
-- @param	_AccountID		The ID of the Account.
-- @return 					Whether it is a patient.
CREATE FUNCTION IsPatient(
	_AccountID bigint unsigned
)
RETURNS bit
READS SQL DATA
NOT DETERMINISTIC
BEGIN
	-- Declarations
	DECLARE
		_Exists
        bit;
        
	-- Fill _is conditionally
	SELECT EXISTS (
		SELECT 1 
		FROM Patient 
		WHERE AccountID = _AccountID
	) INTO _Exists;
    
    RETURN _Exists;
END//

-- Determines if an Account is of Doctor subtype.
-- @param 	_AccountID		The ID of the Account.
-- @return 					Whether it is a patient.
CREATE FUNCTION IsDoctor(
	_AccountID bigint unsigned
)
RETURNS bit
READS SQL DATA
NOT DETERMINISTIC
BEGIN
	-- Declarations
	DECLARE
		_Exists
        bit;
        
	-- Fill _is conditionally
	SELECT EXISTS (
		SELECT 1 
		FROM Doctor 
		WHERE AccountID = _AccountID
	) INTO _Exists;
    
    RETURN _Exists;
END//

-- Determines an account subtype.
-- @param	_AccountID 		The ID of the account.
-- @return 					'doctor' | 'patient'
CREATE FUNCTION GetAccountType(
	_AccountID bigint unsigned
)
RETURNS varchar(255) character set utf8mb4
READS SQL DATA
NOT DETERMINISTIC
BEGIN
	IF IsDoctor(_AccountID) THEN
		RETURN 'doctor';
	ELSEIF IsPatient(_AccountID) THEN
		RETURN 'patient';
	ELSE
		RETURN NULL;
    END IF;
END//

-- Formats a Doctor's name with "Dr. " as a standard title.
-- @param	_AccountID		The ID of the Doctor's account.
-- @return 	The name.
CREATE FUNCTION GetDoctorName
(
	_AccountID bigint unsigned
)
RETURNS varchar(255) character set utf8mb4
READS SQL DATA
NOT DETERMINISTIC
BEGIN
	-- Declarations
	DECLARE
		_FirstName, _Surname
		varchar(255) character set utf8mb4;

	-- Get name components
	SELECT
		FirstName,
		Surname
	INTO
		_FirstName,
        _Surname
	FROM
		Account
	WHERE
		AccountID = _AccountID;

	-- Format & return
	RETURN CONCAT_WS(' ', 'Dr.', _FirstName, _Surname);
END//

-- Formats a Patient's name with their title & optional postfix.
-- Will fail to attach a title if the AccountID is not subtyped Patient.
-- @param	_AccountID 		The ID of the Patient's account.
-- @return					The name.
CREATE FUNCTION GetPatientName(
	_AccountID bigint unsigned
)
RETURNS varchar(255) character set utf8mb4
READS SQL DATA
NOT DETERMINISTIC
BEGIN
	-- Declarations
	DECLARE
		_FirstName, _Surname, _Title
        varchar(255) character set utf8mb4;
    DECLARE
		_IsEsquire
        bit;

	-- Get name components
	SELECT
		a.FirstName,
		a.Surname,
		p.Title,
		p.IsEsquire
	INTO
		_FirstName,
        _Surname,
        _Title,
        _IsEsquire
	FROM
		Account a
	JOIN
		Patient p ON p.AccountID = a.AccountID
	WHERE
		a.AccountID = _AccountID;
	
    -- Format & return
	RETURN CONCAT_WS(' ', _Title, _FirstName, _Surname, if(_IsEsquire = 1, ' Esq.', ''));
END//

-- Formats an account name. Will discover the subtype.
-- @param	_AccountID 		The ID of the Patient's account.
-- @return 					The name.
CREATE FUNCTION GetAccountName(
	_AccountID bigint unsigned
)
RETURNS varchar(255) character set utf8mb4
READS SQL DATA
NOT DETERMINISTIC
BEGIN
	-- Declarations
	DECLARE
		_type
        varchar(255) character set utf8mb4;
        
	-- Resolve account type
	SET _type = GetAccountType(_AccountID);
	
    -- Select function & return
    RETURN CASE
		WHEN _type = 'patient' THEN GetPatientName(_Account)
        WHEN _type = 'doctor' THEN GetDoctorName(_Account)
    END;
END//

-- Gets the contact email of any account type.
-- @param	_AccountID 		The ID of the account.
-- @return	 				The contact email address.
CREATE FUNCTION GetAccountContactEmail
(
	_AccountID bigint unsigned
)
RETURNS varchar(255) character set utf8mb4
READS SQL DATA
NOT DETERMINISTIC
BEGIN
	RETURN (SELECT ContactEmail FROM Account WHERE AccountID = _AccountID);
END//

-- Gets the security email of any account type.
-- @param	_AccountID 		The ID of the account.
-- @return 					The contact email address.
CREATE FUNCTION GetAccountSecurityEmail
(
	_AccountID bigint unsigned
)
RETURNS varchar(255) character set utf8mb4
READS SQL DATA
NOT DETERMINISTIC
BEGIN
	RETURN (SELECT SecurityEmail FROM Account WHERE AccountID = _AccountID);
END//

-- Gets an email from an account, using a specifier.
-- @param	_AccountID 		The ID of the Account.
-- @param	_Specifier 		'contact' | 'security'
-- @return					The email address. NULL if _Specifier is invalid.
CREATE FUNCTION GetAccountEmail
(
	_AccountID bigint unsigned,
    _Specifier varchar(255) character set utf8mb4
)
RETURNS varchar(255) character set utf8mb4
READS SQL DATA
NOT DETERMINISTIC
BEGIN
	RETURN CASE
		WHEN _Specifier = 'contact' THEN GetAccountContactEmail(_AccountID)
        WHEN _Specifier = 'security' THEN GetAccountSecurityEmail(_AccountID)
        ELSE NULL
	END;
END//

-- Formats and dispatches a message. 
-- Adds it to the message queue of an account, and emails it.
-- @param	_AccountID 		The recipient.
-- @param	_EmailSpecifier	'contact' | 'security'
-- @param	_Template 		The message template identifier.
-- @param	_Subtype 		The message template subtype.
-- @param	_param1Name		The name of the first parameter {name} -> value.
-- @param	_param1Value	The value of the first parameter.
-- @param	_param2Name		The name of the second parameter {name} -> value.
-- @param	_param2Value	The value of the second parameter.
-- @param	_param3Name		The name of the third parameter {name} -> value.
-- @param	_param3Value	The value of the third parameter.
CREATE PROCEDURE SendFormattedMessage
(
	IN _AccountID bigint unsigned,
    IN _EmailSpecifier varchar(255) character set utf8mb4,
	IN _Template varchar(255) character set utf8mb4,
	IN _Subtype varchar(255) character set utf8mb4,
    IN _param1Name varchar(255) character set utf8mb4,
	IN _param1Value varchar(255) character set utf8mb4,
    IN _param2Name varchar(255) character set utf8mb4,
	IN _param2Value varchar(255) character set utf8mb4,
    IN _param3Name varchar(255) character set utf8mb4,
	IN _param3Value varchar(255) character set utf8mb4
)
MODIFIES SQL DATA
NOT DETERMINISTIC
BEGIN
	-- Declarations
	DECLARE
		_Subject, _EmailAddress
        varchar(255) character set utf8mb4;
	DECLARE
		_Body
        MEDIUMTEXT;
	
    -- Load template
	SELECT
		FixedSubject,
		TemplatedBody
	INTO
		_Subject,
        _Body
	FROM
		MessageTemplate
	WHERE
		Identifier = _Template
		AND Subtype = _Subtype;
		
	-- Get email address
	SET _EmailAddress = GetAccountEmail(_AccountID, _EmailSpecifier);
    
    -- Format body
    IF (_param1Name IS NOT NULL) THEN
		SET _Body = REPLACE(_Body, CONCAT('{', _param1Name, '}'), _param1Value);
	END IF;
    IF (_param2Name IS NOT NULL) THEN
		SET _Body = REPLACE(_Body, CONCAT('{', _param2Name, '}'), _param2Value);
	END IF;
    IF (_param3Name IS NOT NULL) THEN
		SET _Body = REPLACE(_Body, CONCAT('{', _param3Name, '}'), _param3Value);
	END IF;
    
	-- Mail
    -- TODO
    
    -- Add to message queue
    INSERT INTO Message
	(
		MessageUUID,
        AccountID,
        HasBeenReceived,
        SentTimestamp,
        MessageSubject,
        MessageBody,
        MessageTemplateIdentifier,
        MessageTemplateSubtype
	)
	VALUES
	(
		UUID(),
        _AccountID,
        0,
        UTC_TIMESTAMP(),
        _Subject,
        _Body,
        _Template,
        _Subtype
	);
END//

-- Dispatches Patient-Doctor event notifications.
-- @param	_Identifier		The event identifier.
-- @param	_DoctorID		The ID of the ingoing Doctor.
-- @param	_PatientID		The ID of the Patient.
CREATE PROCEDURE SendPDNotification
(
	IN _Identifier varchar(255) character set utf8mb4,
	IN _DoctorID bigint unsigned,
	IN _PatientID bigint unsigned
)
MODIFIES SQL DATA
NOT DETERMINISTIC
BEGIN
	-- Declarations
	DECLARE
		_DoctorName, _PatientName
        varchar(255) character set utf8mb4;
		
	-- Get formatted names
	SET _DoctorName = GetDoctorName(_DoctorID);
	SET _PatientName = GetPatientName(_PatientID);

	-- Send Doctor message
	CALL SendFormattedMessage
    (
		_DoctorID,
        'contact',
        _Identifier,
        'Doctor',
        
        'doctor_name',
        _DoctorName,
        'patient_name',
        _PatientName,
        NULL,
        NULL
    );    
    
    -- Send Patient message
	CALL SendFormattedMessage
    (
		_PatientID,
        'contact',
        _Identifier,
        'Patient',
        
        'doctor_name',
        _DoctorName,
        'patient_name',
        _PatientName,
        NULL,
        NULL
    );
    
END//

-- Dispatches Patient-IngoingDoctor-OutgoingDoctor event notifications.
-- @param 	_Identifier 		The event identifier.
-- @param 	_IngoingDoctorID 	The ID of the ingoing Doctor.
-- @param 	_OutgoingDoctorID	The ID of the outgoing Doctor.
-- @param 	_PatientID 			The ID of the Patient.
CREATE PROCEDURE SendPDDNotification
(
	IN _Identifier varchar(255) character set utf8mb4,
	IN _IngoingDoctorID bigint unsigned,
	IN _OutgoingDoctorID bigint unsigned,
	IN _PatientID bigint unsigned
)
MODIFIES SQL DATA
NOT DETERMINISTIC
BEGIN
	-- Declarations
	DECLARE
		_IngoingDoctorName,	_OutgoingDoctorName, _PatientName
        varchar(255) character set utf8mb4;
		
	-- Get formatted names
	SET _IngoingDoctorName = GetDoctorName(_IngoingDoctorID);
	SET _OutgoingDoctorName = GetDoctorName(_OutgoingDoctorID);
	SET _PatientName = GetPatientName(_PatientID);

	-- Send ingoing Doctor message
	CALL SendFormattedMessage
    (
		_IngoingDoctorID,
        'contact',
        _Identifier,
        'IngoingDoctor',
        
        'doctor_name',
        _IngoingDoctorName,
        'patient_name',
        _PatientName,
        NULL,
        NULL
    );
    
    -- Send outgoing Doctor message
    CALL SendFormattedMessage
    (
		_OutgoingDoctorID,
        'contact',
        _Identifier,
        'OutgoingDoctor',
        
        'doctor_name',
        _OutgoingDoctorName,
        'patient_name',
        _PatientName,
        NULL,
        NULL
    );
    
    -- Send Patient message
	CALL SendFormattedMessage
    (
		_PatientID,
        'contact',
        _Identifier,
        'Patient',
        
        'doctor_name',
        _IngoingDoctorName,
        'patient_name',
        _PatientName,
        NULL,
        NULL
    );
END//

-- Dispatches Account event notifications.
-- @param	_Identifier		The event identifier.
-- @param	_AccountID		The ID of the Account.
CREATE PROCEDURE SendAccountMessage
(
	IN _Identifier varchar(255) character set utf8mb4,
    IN _AccountID bigint unsigned
)
MODIFIES SQL DATA
NOT DETERMINISTIC
SQL SECURITY DEFINER
BEGIN
	-- Declarations
	DECLARE
		_AccountName varchar(255)
        character set utf8mb4;
		
	-- Get formatted name, agnostic of Account type
	SET _AccountName = GetAccountName(_AccountID);

	-- Send Account message
	CALL SendFormattedMessage
    (
		_AccountName,
        'security',
        _Identifier,
        NULL,
        
        'account_name',
        _AccountName,
        NULL,
        NULL,
        NULL,
        NULL
    );
END//

-- Determines if a booking slot index is appropriate for the given day,
-- calculates its timestamp, and checks whether the Doctor associated
-- with a Patient has no booking at that time.
-- @param	_AccountID		The Patient making the Booking.
-- @param	_Date		 	The date of the Booking.
-- @param	_Index			The index of the Booking slot.
-- @return 	_Status			"DOCTOR_ALREADY_BOOKED" | "TOO_CLOSE_TO_NOW" | "BOOKING_SLOT_INVALID" | "CLOSED_ON_WEEKDAY" | "SUCCESS"
-- @return	_Timestamp		The time the Booking slot starts.
-- @return	_DoctorID		The ID of the Doctor associated with the Patient.
CREATE PROCEDURE GetTimeForBookingSlot
(
    IN _AccountID bigint unsigned,
    IN _Date date,
	IN _Index int unsigned,
	OUT _Status varchar(255) character set utf8mb4,
	OUT _Timestamp timestamp,
    OUT _DoctorID bigint unsigned
)
READS SQL DATA
NOT DETERMINISTIC
GetTimeForBookingSlot: BEGIN
	-- Declarations
	DECLARE
		_MondayID, _TuesdayID, _WednesdayID, _ThursdayID,
        _FridayID, _SaturdayID, _SundayID, _DayID
        bigint unsigned;
	DECLARE
		_NumberOfSlots, _LunchStart, _LunchEnd
        int unsigned;
	DECLARE
		_OpenTime,
        _SlotLength,
        _GracePeriod,
        _Time
        time;
	DECLARE
		_BookingExists
        bit;
	DECLARE
		_WeekDay
        varchar(255) character set utf8mb4;
        
	-- Initialise outputs
	SET _Time = NULL;

	-- Get the day of the week
    SET _WeekDay = DAYNAME(_Date);

	-- Get the description for the selected day
	SELECT
		MondayID,
		TuesdayID,
		WednesdayID,
		ThursdayID,
		FridayID,
		SaturdayID,
		SundayID,
        SlotLength
	INTO
		_MondayID,
        _TuesdayID,
        _WednesdayID,
        _ThursdayID,
        _FridayID,
        _SaturdayID,
        _SundayID,
        _SlotLength
	FROM
		PracticeRecent;

	SET _DayID = NULL;
	SET _DayID = CASE
		WHEN _WeekDay = 'Monday' THEN _MondayID
		WHEN _WeekDay = 'Tuesday' THEN _TuesdayID
		WHEN _WeekDay = 'Wednesday' THEN _WednesdayID
		WHEN _WeekDay = 'Thursday' THEN _ThursdayID
		WHEN _WeekDay = 'Friday' THEN _FridayID
		WHEN _WeekDay = 'Saturday' THEN _SaturdayID
		WHEN _WeekDay = 'Sunday' THEN _SundayID
	END;

	-- Closed on this day case
	IF _DayID IS NULL THEN
		SET _Status = 'CLOSED_ON_WEEKDAY';
		LEAVE GetTimeForBookingSlot;
	END IF;

	-- Get the description for the selected day
	SELECT
		NumberOfSlots,
		LunchStart,
		LunchEnd,
        OpenTime
	INTO
		_NumberOfSlots,
        _LunchStart,
        _LunchEnd,
        _OpenTime
	FROM
		PracticeDayDescription
	WHERE
		PracticeDayDescriptionID = _DayID;

	-- Test if slot is possible
    IF NOT ((_Index < _NumberOfSlots) AND NOT (_Index >= _LunchStart AND _Index <= _LunchEnd)) THEN
		SET _Status = "BOOKING_SLOT_INVALID";
		LEAVE GetTimeForBookingSlot;
	END IF;
    
	-- Determine if too close to present or in past
    SET _Time = SEC_TO_TIME(TIME_TO_SEC(_OpenTime) + (TIME_TO_SEC(_SlotLength) * _Index));
    SET _Timestamp = TIMESTAMP(_Date, _Time);
	SELECT
		GracePeriod
	INTO
		_GracePeriod
	FROM
		PracticeRecent
	LIMIT 1;
    
    IF (ADDTIME(UTC_TIMESTAMP(), _GracePeriod) > _Timestamp) THEN
		SET _Status = 'TOO_CLOSE_TO_NOW';
        LEAVE GetTimeForBookingSlot;
    END IF;
    
    -- Get the Patient's current Doctor
    SELECT
		DoctorID
	INTO
		_DoctorID
	FROM
		Patient
	WHERE
		AccountID = _AccountID;
    
    -- Check if Doctor is available at that time
    SELECT EXISTS(
		SELECT 1
        FROM
			Booking
		WHERE
				BookingTimestamp = _Timestamp
			AND	DoctorID = _DoctorID
    ) INTO _BookingExists;
    
    IF _bookingExists = 1 THEN
		SET _Status = 'DOCTOR_ALREADY_BOOKED';
        LEAVE GetTimeForBookingSlot;
	END IF;
    
    SET _Status = 'SUCCESS';
END//

-- Resolves a Booking from UUID to ID, in the contex of a Patient. In doing so,
-- determines whether Patient is authorised to modify that Booking.
-- @param	_AccountID		The owning account.
-- @param	_BookingUUID	The UUID of the Booking, supplied by the client.
-- @return					The Booking ID, or NULL if the Patient is not authorised.
CREATE FUNCTION GetBookingIDForPatient
(
	_AccountID bigint unsigned,
	_BookingUUID char(36) character set utf8mb4
)
RETURNS bigint unsigned
READS SQL DATA
NOT DETERMINISTIC
BEGIN
	-- Declarations
	DECLARE
		_BookingID
        bigint unsigned;

	-- Get booking
	SELECT
		BookingID
	INTO
		_BookingID
	FROM
		Booking
	WHERE
			BookingUUID = _BookingUUID
		AND	PatientID = _AccountID;
	
	RETURN _BookingID;
END//

-- Maps an Account UUID to an Account ID for a Doctor.
-- @param	The Doctor's UUID.
-- @return	The Account ID, or null if no Doctor could be found. 
CREATE FUNCTION GetDoctorByUUID
(
	_DoctorUUID char(36) character set utf8mb4
)
RETURNS bigint unsigned
READS SQL DATA
NOT DETERMINISTIC
BEGIN
	-- Declarations
	DECLARE
		_DoctorID
        bigint unsigned;

    -- Transactions
    SELECT
		a.AccountID
	INTO
		_DoctorID
	FROM
		Doctor d
	JOIN
		Account a
    ON	
		a.AccountID = d.AccountID
	WHERE
		a.AccountUUID = _DoctorUUID
	LIMIT 1;
    
    RETURN _DoctorID;
END//

-- -------------
-- --- AUTHENTICATION & LOGGING
-- -------------

-- Logging procedure used by all endpoints. Attaches a timestamp.
-- @param	_AccountUUID 		The Account UUID credential supplied by the requester.
-- @param	_EndpointCalled 	The endpoint that was accessed.
-- @param	_InformationJSON 	Any information the endpoint chose to record, including parameters and returns.
CREATE PROCEDURE LogRequest(
	IN _AccountUUID char(36) character set utf8mb4,
	IN _EndpointCalled varchar(255) character set utf8mb4,
	IN _InformationJSON varchar(255) character set utf8mb4
)
MODIFIES SQL DATA
NOT DETERMINISTIC
BEGIN
	INSERT INTO LogCall
	(
		AccountUUID,
        EndpointCalled,
        InformationJSON,
        LogTimestamp
	)
	VALUES
	(
		_AccountUUID,
        _EndpointCalled,
        _InformationJSON,
        UTC_TIMESTAMP()
	);
END//

-- Authentication procedure used by all private (authenticated) API procedures.
-- @param	_AccountUUID	The Account UUID supplied by the requester.
-- @param	_SessionToken	The Session Token supplied by the requester.
-- @return	_Status			"SESSION_TIMEOUT" | "INVALID_CREDENTIALS" | "AUTHENTICATED"
-- @return	_AccountID		The resolved Account ID, if successful.
CREATE PROCEDURE Authenticate
(
	IN _AccountUUID char(36) character set utf8mb4,
	IN _SessionToken char(36) character set utf8mb4,
	OUT _Status varchar(255) character set utf8mb4,
	OUT _AccountID bigint unsigned
)
READS SQL DATA
NOT DETERMINISTIC
Authenticate: BEGIN
	-- Declarations
	DECLARE
		_ActualSessionToken
        char(36);
	DECLARE
		_SessionTimestamp
        timestamp;
        
	-- Initialise output variables
    SET _Status = NULL;
    SET _AccountID = NULL;

	-- Find credentials
	SELECT
		AccountID,
		SessionToken,
		SessionTimestamp
	INTO
		_AccountID,
        _ActualSessionToken,
        _SessionTimestamp
	FROM
		Account
	WHERE
		AccountUUID = _AccountUUID AND
        SessionToken = _SessionToken;
       
	-- If credentials can't be found
	IF (FOUND_ROWS() = 0) THEN
		SET _Status = 'INVALID_CREDENTIALS';
        LEAVE Authenticate;
    END IF;
	
	-- If session token is old
    IF ((UNIX_TIMESTAMP(UTC_TIMESTAMP()) - _SessionTimestamp) >= (60 * 60 * 24 * 3)) THEN
		SET _Status = 'SESSION_TIMEOUT';
        LEAVE Authenticate;
	END IF;
    
    SET _Status = 'AUTHENTICATED';
END//

-- Salts and hashes the password.
-- @param	_input		44-character input, generally as a base64-encoded string of a SHA-256 hashing function.	
-- @return				The native output of SHA2 i.e. 32 bytes.
CREATE FUNCTION HashPassword
(
	_input varbinary(255)
)
RETURNS varchar(64) character set utf8mb4
NO SQL
DETERMINISTIC
BEGIN
	RETURN SHA2(CONCAT(_input, '4q3u2PtybgmmiBJl9GSxOA=='), 256);
END//

-- -------------
-- --- API ENDPOINTS
-- -------------

-- Returns Patient details.
-- @param 	_AccountUUID 	The Account UUID of the requester.
-- @param 	_SessionToken 	The Session Token of the requester.
-- @return	_FirstName		Their first name.
-- @return	_Surname		Their surname.
-- @return	_Title			Their title. Must be a valid Title.
-- @return	_ContactEmail	Their contact email.
-- @return	_ContactPhoneNumber	Their phone number.
-- @return	_Street1		The first line of their address.
-- @return	_Street2		The second line of their address.
-- @return	_City			Their city.
-- @return	_County			Their county.
-- @return	_Postcode		Their postcode.
-- @return	_Status			{AUTHENTICATION} | "INCORRECT_ACCOUNT_TYPE" | "SUCCESS"
CREATE PROCEDURE API_Private_Patient_GetDetails
(
	IN _AccountUUID char(36) character set utf8mb4,
    IN _SessionToken char(36) character set utf8mb4,
    OUT _Status varchar(255) character set utf8mb4,
    OUT _FirstName varchar(255) character set utf8mb4,
    OUT _Surname varchar(255) character set utf8mb4,
    OUT _Title varchar(255) character set utf8mb4,
    OUT _ContactEmail varchar(255) character set utf8mb4,
    OUT _ContactPhoneNumber varchar(255) character set utf8mb4,
    OUT _Street1 varchar(255) character set utf8mb4,
    OUT _Street2 varchar(255) character set utf8mb4,
    OUT _City varchar(255) character set utf8mb4,
    OUT _County varchar(255) character set utf8mb4,
    OUT _PostCode varchar(255) character set utf8mb4
)
READS SQL DATA
NOT DETERMINISTIC
API_Private_Patient_GetDetails: BEGIN
	-- Declarations
    DECLARE
		_AccountID
        bigint unsigned;
	-- DECLARE _logData varchar(4192) character set utf8mb4;
        
    -- Log
    -- SET _logData = CAST(JSON_OBJECT() AS CHAR);
    CALL LogRequest(_AccountUUID, 'Private_Patient_GetDetails', NULL);
    
    -- Authenticate
    CALL Authenticate(_AccountUUID, _SessionToken, _Status, _AccountID);
    IF (_Status != 'AUTHENTICATED') THEN
		LEAVE API_Private_Patient_GetDetails;
	END IF;
    
    -- Get details in Account
    SELECT
		FirstName,
        Surname,
        ContactEmail
	INTO
		_FirstName,
        _Surname,
        _ContactEmail
	FROM
		Account
	WHERE
		AccountID = _AccountID
	LIMIT 1;
        
	-- Get details in Patient
    SELECT
		Title,
        ContactPhoneNumber,
        Street1,
        Street2,
        City,
        County,
        PostCode
	INTO
		_Title,
        _ContactPhoneNumber,
        _Street1,
        _Street2,
        _City,
        _County,
        _PostCode
	FROM
		Patient
	WHERE
		AccountID = _AccountID
	LIMIT 1;
        
	-- If not a patient
    IF NOT (FOUND_ROWS() > 0) THEN
		SET _Status = 'INCORRECT_ACCOUNT_TYPE';
        LEAVE API_Private_Patient_GetDetails;
	END IF;
        
	-- Return
    SET _Status = 'SUCCESS';
END//

-- Sets the Patient's details. Enforces validation logic.
-- @param	_AccountUUID	The Account UUID of the requester.
-- @param	_SessionToken	The Session Token of the requester.
-- @param	_FirstName		Their first name.
-- @param	_Surname		Their surname.
-- @param	_Title			Their title. Must be a valid Title.
-- @param	_ContactEmail	Their contact email.
-- @param	_ContactPhoneNumber	Their phone number.
-- @param	_Street1		The first line of their address.
-- @param	_Street2		The second line of their address.
-- @param	_City			Their city.
-- @param	_County			Their county.
-- @param	_Postcode		Their postcode.
-- @return	_Status			{AUTHENTICATION} | "INVALID_ACCOUNT_TYPE" | "VALIDATION_ERROR" | "SUCCESS"
CREATE PROCEDURE API_Private_Patient_SetDetails
(
	IN _AccountUUID char(36) character set utf8mb4,
    IN _SessionToken char(36) character set utf8mb4,
    IN _FirstName varchar(255) character set utf8mb4,
    IN _Surname varchar(255) character set utf8mb4,
    IN _Title varchar(255) character set utf8mb4,
    IN _ContactEmail varchar(255) character set utf8mb4,
    IN _ContactPhoneNumber varchar(255) character set utf8mb4,
    IN _Street1 varchar(255) character set utf8mb4,
    IN _Street2 varchar(255) character set utf8mb4,
    IN _City varchar(255) character set utf8mb4,
    IN _County varchar(255) character set utf8mb4,
    IN _PostCode varchar(255) character set utf8mb4,
    OUT _Status varchar(255) character set utf8mb4
)
MODIFIES SQL DATA
NOT DETERMINISTIC
API_Private_Patient_SetDetails: BEGIN
	-- Declarations
    DECLARE
		_AccountID
        bigint unsigned;
	-- DECLARE _logData varchar(4192) character set utf8mb4;
	DECLARE CONTINUE HANDLER FOR 45000
		BEGIN
			SET _Status = 'VALIDATION_ERROR';
        END;
	DECLARE CONTINUE HANDLER FOR 1452 
		BEGIN
			SET _Status = 'VALIDATION_ERROR';
		END;
    
    -- Log
    -- SET _logData = CAST(JSON_OBJECT() AS CHAR);
    CALL LogRequest(_AccountUUID, 'Private_Patient_SetDetails', NULL);
        
    -- Authenticate
    CALL Authenticate(_AccountUUID, _SessionToken, _Status, _AccountID);
    IF (_Status != 'AUTHENTICATED') THEN
		LEAVE API_Private_Patient_SetDetails;
	END IF;
    
    -- Transaction
    START TRANSACTION;
		-- Set details in Account
		UPDATE
			Account
		SET
			FirstName = _FirstName,
			Surname = _Surname,
			ContactEmail = _ContactEmail
		WHERE
			AccountID = _AccountID
		LIMIT 1;
        
		-- Set details in Patient
		UPDATE
			Patient
		SET
			Title = _Title,
			ContactPhoneNumber = _ContactPhoneNumber,
			Street1 = _Street1,
			Street2 = _Street2,
			City = _City,
			County = _County,
			PostCode = _PostCode
		WHERE
			AccountID = _AccountID
		LIMIT 1;
			
		-- If not a patient
		IF FOUND_ROWS() = 0 THEN
			SET _Status = 'INVALID_ACCOUNT_TYPE';
		END IF;
		
        -- If not a patient, or if validation violation occured
        IF _Status != 'AUTHENTICATED' THEN
			ROLLBACK;
		ELSE
			COMMIT;
            SET _Status = 'SUCCESS';
		END IF;
END//

-- Gets the Doctor associated with the Patient.
-- @param	_AccountUUID	The Account UUID of the requester.
-- @param 	_SessionToken	The Session Token of the requester.
-- @return 	_DoctorUUID		The Doctor's UUID.
-- @return	_Status			{AUTHENTICATION} | "SUCCESS"
CREATE PROCEDURE API_Private_Patient_GetDoctor
(
	IN _AccountUUID char(36) character set utf8mb4,
    IN _SessionToken char(36) character set utf8mb4,
    OUT _Status varchar(255) character set utf8mb4,
    OUT _DoctorUUID char(36) character set utf8mb4
)
MODIFIES SQL DATA
NOT DETERMINISTIC
API_Private_Patient_GetDoctor: BEGIN
	-- Declarations
    DECLARE
		_DoctorID,
        _AccountID
		bigint unsigned;
        
    -- Log
    -- SET _logData = CAST(JSON_OBJECT() AS CHAR);
    CALL LogRequest(_AccountUUID, 'Private_Patient_GetDoctor', NULL);
	
    -- Authenticate
    CALL Authenticate(_AccountUUID, _SessionToken, _Status, _AccountID);
    IF _Status != 'AUTHENTICATED' THEN
		LEAVE API_Private_Patient_GetDoctor;
	END IF;
    
    -- Get ID
    START TRANSACTION;
		SELECT
			DoctorID
		INTO
			_DoctorID
		FROM
			Patient
		WHERE
			AccountID = _AccountID;
            
		SELECT
			AccountUUID
		INTO
			_DoctorUUID
		FROM
			Account
		WHERE
			AccountID = _DoctorID;
    COMMIT;
    
    -- SUCCESS & return
    SET _Status = 'SUCCESS';
END//

-- Sets the Doctor associated with the Patient.
-- @param	_AccountUUID	The Account UUID of the requester.
-- @param 	_SessionToken	The Session Token of the requester.
-- @param 	_DoctorUUID		The Doctor's UUID.
-- @param	_Status			{AUTHENTICATION} | "NOT_A_VALID_DOCTOR" | "SUCCESS"
CREATE PROCEDURE API_Private_Patient_SetDoctor
(
	IN _AccountUUID char(36) character set utf8mb4,
    IN _SessionToken char(36) character set utf8mb4,
    IN _DoctorUUID char(36) character set utf8mb4,
    OUT _Status varchar(255) character set utf8mb4
)
MODIFIES SQL DATA
NOT DETERMINISTIC
API_Private_Patient_SetDoctor: BEGIN
	-- Declarations
    DECLARE
		_AccountID,
        _IngoingDoctorID,
        _OutgoingDoctorID
        bigint unsigned;
	-- DECLARE _logData varchar(4192) character set utf8mb4;
    
    -- Log
    -- SET _logData = CAST(JSON_OBJECT() AS CHAR);
    CALL LogRequest(_AccountUUID, 'Private_Patient_SetDoctor', NULL);
	
    -- Authenticate
    CALL Authenticate(_AccountUUID, _SessionToken, _Status, _AccountID);
    IF _Status != 'AUTHENTICATED' THEN
		LEAVE API_Private_Patient_SetDoctor;
	END IF;
    
    -- Get Doctor account ID
    SET _IngoingDoctorID = GetDoctorByUUID(_DoctorUUID);
    IF _IngoingDoctorID IS NULL THEN
		SET _Status = 'NOT_A_VALID_DOCTOR';
        LEAVE API_Private_Patient_SetDoctor;
	END IF;
    
	-- Get existing Doctor & Set new
    START TRANSACTION;
		-- Get
		SELECT
			DoctorID
		INTO
			_OutgoingDoctorID
		FROM
			Patient
		WHERE
			AccountID = _AccountID;
        
        -- Set
		UPDATE
			Patient
		SET
			DoctorID = _IngoingDoctorID
		WHERE
			AccountID = _AccountID
		LIMIT 1;
	COMMIT;
    
    -- Transactions
    Set _Status = 'SUCCESS';
    CALL SendPDDNotification('DoctorChanged', _IngoingDoctorID, _OutgoingDoctorID, _AccountID);
END//

-- Gets a list of bookings.
-- @param	_AccountUUID	The Account UUID of the requester.
-- @param	_SessionToken	The Session Token of the requester.
-- @param	_Month			The month to search in.
-- @param	_Year			The year to search in.
-- @return	_Status			{AUTHENTICATION} | "SUCCESS"
-- @return	[result_set]	BookingUUID, DoctorUUID, Timestamp, WasAttended.
CREATE PROCEDURE API_Private_Patient_ListBookings
(
	IN _AccountUUID char(36) character set utf8mb4,
    IN _SessionToken char(36) character set utf8mb4,
    IN _Month int,
    IN _Year int,
    OUT _Status varchar(255) character set utf8mb4
)
READS SQL DATA
NOT DETERMINISTIC
API_Private_Patient_ListBookings: BEGIN
	-- Declarations
    DECLARE
		_AccountID
        bigint unsigned;
	-- DECLARE _logData varchar(4192) character set utf8mb4;
    
    -- Log
    -- SET _logData = CAST(JSON_OBJECT() AS CHAR);
    CALL LogRequest(_AccountUUID, 'Private_Patient_ListBookings', NULL);
	
    -- Authenticate
    CALL Authenticate(_AccountUUID, _SessionToken, _Status, _AccountID);
    IF (_Status != 'AUTHENTICATED') THEN
		LEAVE API_Private_Patient_ListBookings;
	END IF;
    
    -- Finalise status
	SET _Status = 'SUCCESS';
    
    -- Get bookings
    SELECT
		b.BookingUUID as BookingUUID,
        d.AccountUUID as DoctorUUID,
        b.BookingTimestamp as BookingTimestamp,
        b.WasAttended as WasAttended
	FROM
		BookingView b
	JOIN
		Account d
	ON d.AccountID = b.DoctorID
	WHERE
			b.PatientID = _AccountID
        AND	YEAR(b.BookingTimestamp) = _Year
        AND	MONTH(b.BookingTimestamp) = _Month;
END//

-- Gets details about an attended booking.
-- @param	_AccountUUID			The Account UUID of the requester.
-- @param	_SessionToken			The Session Token of the requester.
-- @param	_BookingUUID			The UUID of the booking. Some fields will be null if this is not of AttendedBooking subtype.
-- @return	_BookingTimestamp		The timestamp of the booking.
-- @return	_ModifiedTimestamp		The time the booking was last modified or when it was created.
-- @return	_DoctorUUID				The Doctor the booking was made with.
-- @return	_Summary				A summary of the booking.
-- @return	_PrescriptionsJSON		The prescriptions from the booking.
-- @return	_Status					{AUTHENTICATION} | "BOOKING_UUID_INVALID" | "SUCCESS"
CREATE PROCEDURE API_Private_Patient_GetAttendedBooking
(
	IN _AccountUUID char(36) character set utf8mb4,
    IN _SessionToken char(36) character set utf8mb4,
    IN _BookingUUID char(36) character set utf8mb4,
    OUT _Status varchar(255) character set utf8mb4,
    OUT _BookingTimestamp timestamp,
    OUT _ModifiedTimestamp timestamp,
    OUT _DoctorUUID char(36) character set utf8mb4,
    OUT _Summary MEDIUMTEXT,
    OUT _PrescriptionsJSON MEDIUMTEXT
)
MODIFIES SQL DATA
NOT DETERMINISTIC
API_Private_Patient_GetAttendedBooking: BEGIN
	-- Declarations
    DECLARE
		_AccountID,
        _BookingID,
        _DoctorID
        bigint unsigned;
	DECLARE
		_DoctorUUID
        char(36) character set utf8mb4;
	-- DECLARE _logData varchar(4192) character set utf8mb4;
    
    -- Log
    -- SET _logData = CAST(JSON_OBJECT() AS CHAR);
    CALL LogRequest(_AccountUUID, 'Private_Patient_GetBookingDetails', NULL);
	
    -- Authenticate
    CALL Authenticate(_AccountUUID, _SessionToken, _Status, _AccountID);
    IF (_Status != 'AUTHENTICATED') THEN
		LEAVE API_Private_Patient_GetAttendedBooking;
	END IF;
    
	-- Get the bookingID & determine ownership
    SET _BookingID = GetBookingIDForPatient(_AccountID, _BookingUUID);
    
    IF _BookingID IS NULL THEN
		SET _Status = 'BOOKING_UUID_INVALID';
		LEAVE API_Private_Patient_GetAttendedBooking;
    END IF;
    
    -- Get booking details
    SELECT
		b.BookingTimestamp,
        b.ModifiedTimestamp,
        b.DoctorID,
        a.Summary,
        a.PrescriptionsJSON
	INTO
		_BookingTimestamp,
        _ModifiedTimestamp,
        _DoctorID,
        _Summary,
        _PrescriptionsJSON
	FROM
		Booking b
	JOIN
		AttendedBooking a
	ON
		a.BookingID = b.BookingID
	WHERE
		b.BookingID = _BookingID;
        
	-- Get doctor UUID from ID
    SELECT
		AccountUUID
	INTO
		_DoctorUUID
	FROM
		Account
	WHERE
		AccountID = _DoctorID;
        
	-- Set status & return
    SET _Status = 'SUCCESS';
END//

-- Makes a booking.
-- @param	_AccountUUID	The Account UUID of the requester.
-- @param	_SessionToken	The Session Token of the requester.
-- @param	_Date			The date of the booking.
-- @param	_Index			The index of the booking slot, [0..n).
-- @return	_Status			{AUTHENTICATION} | {BOOKING_SLOT} | "SUCCESS"
CREATE PROCEDURE API_Private_Patient_MakeBooking
(
	IN _AccountUUID char(36) character set utf8mb4,
    IN _SessionToken char(36) character set utf8mb4,
    IN _Date date,
    IN _Index int unsigned,
    OUT _Status varchar(255) character set utf8mb4
)
MODIFIES SQL DATA
NOT DETERMINISTIC
API_Private_Patient_MakeBooking: BEGIN
	-- Declarations
    DECLARE
		_AccountID,
        _DoctorID
        bigint unsigned;
	DECLARE
		_BookingTimestamp
        timestamp;
	-- DECLARE _logData varchar(4192) character set utf8mb4;
    
    -- Log
    -- SET _logData = CAST(JSON_OBJECT() AS CHAR);
    CALL LogRequest(_AccountUUID, 'Private_Patient_MakeBooking', NULL);
	
    -- Authenticate
    CALL Authenticate(_AccountUUID, _SessionToken, _Status, _AccountID);
    IF (_Status != 'AUTHENTICATED') THEN
		LEAVE API_Private_Patient_MakeBooking;
	END IF;
    
    -- Determine if booking slot is valid & get time
    CALL GetTimeForBookingSlot(_AccountID, _Date, _Index, _Status, _BookingTimestamp, _DoctorID);
    
    IF (_Status != 'SUCCESS') THEN
        LEAVE API_Private_Patient_MakeBooking;
	END IF;
    
    -- Insert the Booking
    INSERT INTO Booking
	(
        BookingUUID,
        PatientID,
        DoctorID,
        BookingTimestamp,
        Slot,
        ModifiedTimestamp
	)
	VALUES
	(
		UUID(),
        _AccountID,
        _DoctorID,
        _BookingTimestamp,
        _Index,
        UTC_TIMESTAMP()
	);
    
    -- Return
    SET _Status = 'SUCCESS';
    CALL SendPDNotification('BookingMade', _DoctorID, _AccountID);
END//

-- Reschedules a booking.
-- @param 	_AccountUUID 	The Account UUID of the requester.
-- @param 	_SessionToken 	The Session Token of the requester.
-- @param	_BookingUUID	The UUID of the Booking, maintained by the client.
-- @param	_Date			The date of the booking.
-- @param	_Index			The index of the booking slot, [0..n).
-- @return	_Status			{AUTHENTICATION} | "BOOKING_UUID_INVALID" | {BOOKING_SLOT} | "SUCCESS"
CREATE PROCEDURE API_Private_Patient_RescheduleBooking
(
	IN _AccountUUID char(36) character set utf8mb4,
    IN _SessionToken char(36) character set utf8mb4,
    IN _BookingUUID char(36) character set utf8mb4,
    IN _Date date,
    IN _Index int unsigned,
    OUT _Status varchar(255) character set utf8mb4
)
MODIFIES SQL DATA
NOT DETERMINISTIC
API_Private_Patient_RescheduleBooking: BEGIN
	-- Declarations
    DECLARE
		_AccountID,
        _DoctorID,
        _BookingID
        bigint unsigned;
	DECLARE
		_BookingTimestamp
        timestamp;
	-- DECLARE _logData varchar(4192) character set utf8mb4;
    
    -- Log
    -- SET _logData = CAST(JSON_OBJECT() AS CHAR);
    CALL LogRequest(_AccountUUID, 'Private_Patient_RescheduleBooking', NULL);
	
    -- Authenticate
    CALL Authenticate(_AccountUUID, _SessionToken, _Status, _AccountID);
    IF (_Status != 'AUTHENTICATED') THEN
		LEAVE API_Private_Patient_RescheduleBooking;
	END IF;
    
    -- Get the bookingID & determine ownership
    SET _BookingID = GetBookingIDForPatient(_AccountID, _BookingUUID);
    
    IF _BookingID IS NULL THEN
		SET _Status = 'BOOKING_UUID_INVALID';
		LEAVE API_Private_Patient_RescheduleBooking;
    END IF;
    
    -- Determine if booking slot is valid & get time
    CALL GetTimeForBookingSlot(_AccountID, _Date, _Index, _Status, _BookingTimestamp, _DoctorID);
	
    IF (_Status != 'SUCCESS') THEN
		LEAVE API_Private_Patient_RescheduleBooking;
	END IF;
    
    -- Insert the Booking
    UPDATE
		Booking
	SET
        DoctorID = _DoctorID,
        BookingTimestamp = TIMESTAMP(_Date, _Time),
        Slot = _Index,
        ModifiedTimestamp = UTC_TIMESTAMP()
    WHERE
		BookingID = _BookingID;
    
    -- SUCCESS & Send Message
    SET _Status = 'SUCCESS';
    CALL SendPDNotification('BookingRescheduled', _DoctorID, _AccountID);
END//

-- Reschedules a booking.
-- @param	_AccountUUID	The Account UUID of the requester.
-- @param	_SessionToken	The Session Token of the requester.
-- @return	_Status			{AUTHENTICATION} | "SUCCESS"
CREATE PROCEDURE API_Private_Patient_LogOut
(
	IN _AccountUUID char(36) character set utf8mb4,
    IN _SessionToken char(36) character set utf8mb4,
    OUT _Status varchar(255) character set utf8mb4
)
MODIFIES SQL DATA
NOT DETERMINISTIC
API_Private_Patient_LogOut: BEGIN
	-- Declarations
    DECLARE
		_AccountID,
        _DoctorID,
        _BookingID
        bigint unsigned;
	DECLARE
		_BookingTimestamp
        timestamp;
	-- DECLARE _logData varchar(4192) character set utf8mb4;
    
    -- Log
    -- SET _logData = CAST(JSON_OBJECT() AS CHAR);
    CALL LogRequest(_AccountUUID, 'Private_Patient_LogOut', NULL);
	
    -- Authenticate
    CALL Authenticate(_AccountUUID, _SessionToken, _Status, _AccountID);
    IF (_Status != 'AUTHENTICATED') THEN
		LEAVE API_Private_Patient_LogOut;
	END IF;
    
    -- Invalidate session token
    UPDATE
		Account
	SET
		SessionToken = NULL
	WHERE
		AccountID = _AccountID
	LIMIT 1;
	
    SET _Status = 'SUCCESS';
END;

-- Logs the user in.
-- @param	_SecurityEmail	The user's login email.
-- @param	_Password		The user's password.
-- @return	_Status			"LOGIN_DETAILS_INVALID" | "SUCCESS"
-- @return	_AccountUUID	Account UUID credential information, or NULL if not SUCCESS.
-- @return	_SessionToken	Session Token credential information, or NULL if not SUCCESS.
CREATE PROCEDURE API_Public_Patient_LogIn
(
	IN _SecurityEmail varchar(255) character set utf8mb4,
    IN _Password varbinary(255),
    OUT _Status varchar(255) character set utf8mb4,
    OUT _AccountUUID char(36) character set utf8mb4,
    OUT _SessionToken char(36) character set utf8mb4
)
MODIFIES SQL DATA
NOT DETERMINISTIC
API_Public_Patient_LogIn: BEGIN
	-- Declarations
    DECLARE
		_AccountID
        bigint unsigned;
	DECLARE
		_PasswordHash
        varchar(64) character set utf8mb4;
	-- DECLARE _logData varchar(4192) character set utf8mb4;
    
    -- Log
    -- SET _logData = CAST(JSON_OBJECT() AS CHAR);
    CALL LogRequest(NULL, 'Patient_Public_LogIn', NULL);
	
    -- Hash password
    SET _PasswordHash = HashPassword(_Password);
    
    -- Select account based on credentials
    SELECT
		AccountID,
        AccountUUID
	INTO
		_AccountID,
        _AccountUUID
	FROM
		Account
	WHERE
			SecurityEmail = _SecurityEmail
		AND	PasswordHash = _PasswordHash
	LIMIT 1;
	
    IF NOT (FOUND_ROWS() > 0) THEN
		SET _Status = 'LOGIN_DETAILS_INVALID';
        LEAVE API_Public_Patient_LogIn;
    END IF;
    
    -- Issue new Session Token
    SET _SessionToken = UUID();
    UPDATE
		Account
	SET
		SessionToken = _SessionToken
	WHERE
		AccountID = _AccountID;
        
	-- SUCCESS & return
	SET _Status = 'SUCCESS';
END//

-- Creates a new patient.
-- @param	_SecurityEmail	Their security email. Also becomes their contact email by default.
-- @param	_Password		Their password.
-- @param	_DoctorUUID		Their Doctor, specified by UUID.
-- @param	_FirstName		Their first name.
-- @param	_Surname		Their surname.
-- @param	_Title			Their title.
-- @param	_ContactPhoneNumber	Their contact phone number.
-- @param	_Street1		Their street address, first line.
-- @param	_Street2		Their street address, second line.
-- @param	_City			Their city.
-- @param	_PostCode		Their post code.
-- @return	_Status			'VALIDATION_ERROR' | 'NOT_A_VALID_DOCTOR' | 'ALREADY_EXISTS' |'SUCCESS'
CREATE PROCEDURE API_Public_Patient_New
(
	IN _SecurityEmail varchar(255) character set utf8mb4,
    IN _Password varbinary(255),
	IN _DoctorUUID varchar(255) character set utf8mb4,
    IN _FirstName varchar(255) character set utf8mb4,
    IN _Surname varchar(255) character set utf8mb4,
    IN _Title varchar(255) character set utf8mb4,
    IN _ContactPhoneNumber varchar(255) character set utf8mb4,
    IN _Street1 varchar(255) character set utf8mb4,
    IN _Street2 varchar(255) character set utf8mb4,
    IN _City varchar(255) character set utf8mb4,
    IN _County varchar(255) character set utf8mb4,
    IN _PostCode varchar(255) character set utf8mb4,
    OUT _Status varchar(255) character set utf8mb4
)
API_Public_Patient_New: BEGIN
	-- Declarations
    DECLARE
		_PasswordHash
        varchar(64) character set utf8mb4;
	DECLARE
		_DoctorID,
        _AccountID
        bigint unsigned;
	DECLARE
		_exists
        bit;
        
    -- Handlers
	DECLARE CONTINUE HANDLER FOR 45000
		BEGIN
			SET _Status = 'VALIDATION_ERROR';
        END;
	DECLARE CONTINUE HANDLER FOR 1452 
		BEGIN
			SET _Status = 'VALIDATION_ERROR';
		END;
	
    -- Log
    CALL LogRequest(NULL, 'Public_Patient_New', NULL);
    
    -- Initialise status
    SET _Status = 'SUCCESS';
    
    -- Hash password
    SET _PasswordHash = HashPassword(_Password);
    
    -- Transaction
	START TRANSACTION;
		-- Get Doctor
		SET _DoctorID = GetDoctorByUUID(_DoctorUUID);
		IF (_DoctorID IS NULL) THEN
			SET _Status = 'NOT_A_VALID_DOCTOR';
			LEAVE API_Public_Patient_New;
		END IF;
        
        -- Test for preexistence of [email, password] combination
        SELECT EXISTS (
			SELECT 1
            FROM
				Account
            WHERE
				SecurityEmail = _SecurityEmail
        ) INTO _exists;
		
        IF _exists = 1 THEN
			SET _Status = 'ALREADY_EXISTS';
			LEAVE API_Public_Patient_New;
		END IF;
    
		-- Add Account
		INSERT INTO	Account
		(
			AccountUUID,
            SecurityEmail,
            PasswordHash,
            FirstName,
            Surname,
            ContactEmail
        )
        VALUES
        (
			UUID(),
            _SecurityEmail,
            _PasswordHash,
            _FirstName,
            _Surname,
            _SecurityEmail
        );
        
        -- Capture account ID
        SET _AccountID = LAST_INSERT_ID();
        
		-- Add Patient
		INSERT INTO Patient
		(
			AccountID,
            DoctorID,
            Title,
            ContactPhoneNumber,
            Street1,
            Street2,
            City,
            County,
            PostCode
        )
		VALUES
        (
			_AccountID,
            _DoctorID,
            _Title,
            _ContactPhoneNumber,
            _Street1,
            _Street2,
            _City,
            _County,
            _PostCode
        );
		
        -- If a validation error occurred
        IF _Status != 'SUCCESS' THEN
			ROLLBACK;
            LEAVE API_Public_Patient_New;
		END IF;
	
    -- Commit & send message
	COMMIT;
    CALL SendPDNotification('NewPatient', _DoctorID, _AccountID);
END//

-- TODO
CREATE PROCEDURE API_Public_Patient_RequestPasswordReset()
BEGIN

END//

-- Downloads all Doctors in the database.
-- @return 	[result_set]	UUID, Specialism, FirstName and Surname.
CREATE PROCEDURE API_Public_Patient_DownloadDoctors()
BEGIN
	SELECT
		a.AccountUUID as UUID,
		d.Specialism as Specialism,
        a.FirstName as FirstName,
        a.Surname as Surname
    FROM
		Doctor d
	INNER JOIN
		Account a
	ON a.AccountID = d.AccountID;
END//

-- Downloads all Titles in the database.
-- @return	 [result_set]	dummy UUID, Identifier.
CREATE PROCEDURE API_Public_Patient_DownloadTitles()
BEGIN
	SELECT
		Identifier AS Identifier
    FROM
		Title;
END//

-- Downloads the latest Practice - business logic - in the database.
-- @return 	[result_set]	Practice
-- @return 	[result_set]	PracticeDayDescription	
CREATE PROCEDURE API_Public_Patient_DownloadPractice()
BEGIN
	-- Result Set 1: Practice, single row
    SELECT
		GracePeriod,
        SlotLength,
        MondayID,
        TuesdayID,
        WednesdayID,
        ThursdayID,
        FridayID,
        SaturdayID,
        SundayID
    FROM
		PracticeRecent p;
        
	-- Result Set 2: PracticeDayDescription, all needed rows
    SELECT
		d.PracticeDayDescriptionID AS PracticeDayDescriptionID,
        d.Identifier AS Identifier,
        d.OpenTime AS OpenTime,
        d.NumberOfSlots AS NumberOfSlots,
        d.LunchStart AS LunchStart,
        d.LunchEnd AS LunchEnd
    FROM
		PracticeDayDescription d
	JOIN
		PracticeRecent p
	ON
			d.PracticeDayDescriptionID = p.MondayID
		OR	d.PracticeDayDescriptionID = p.TuesdayID
        OR	d.PracticeDayDescriptionID = p.WednesdayID
        OR	d.PracticeDayDescriptionID = p.ThursdayID
        OR	d.PracticeDayDescriptionID = p.FridayID
        OR	d.PracticeDayDescriptionID = p.SaturdayID
        OR	d.PracticeDayDescriptionID = p.SundayID;
END//

-- Downloads the latest client data package.
-- @param	_ClientVersion	The version of the requesting client.
-- @return 	_DataBlob 		A blob in a format recognisable by the client version.
CREATE PROCEDURE API_Public_Patient_DownloadData
(
	IN _ClientVersion int unsigned,
    OUT _DataBlob BLOB
)
BEGIN
	SELECT
		DataBlob
	INTO
		_DataBlob
	FROM
		ClientDataRecent
	WHERE
		ClientVersion = _ClientVersion;
END//

DELIMITER ;


-- -------------
-- --- DEFAULT DATABASE
-- -------------

-- Initialisation procedure.
DELIMITER //

-- Initialises data tables, without which the database cannot function correctly
-- i.e. procedures will not work.
CREATE PROCEDURE InitialiseDefaultDatabase()
BEGIN
	-- Declarations
	DECLARE
		_a, _b
        bigint unsigned;
	
    -- Create Weekday & Weekend 
	INSERT INTO PracticeDayDescription
		(Identifier, OpenTime, NumberOfSlots, LunchStart, LunchEnd)
	VALUES
		('Weekday', '09:00:00', 16, 7, 8);
	SET _a = LAST_INSERT_ID();

	INSERT INTO PracticeDayDescription
		(Identifier, OpenTime, NumberOfSlots, LunchStart, LunchEnd)
	VALUES
		('Weekend', '10:00:00', 11, 4, 5);
	SET _b = LAST_INSERT_ID();
	
    -- Create Practice
	INSERT INTO Practice
		(GracePeriod, SlotLength, MondayID, TuesdayID, WednesdayID, ThursdayID, FridayID, SaturdayID, SundayID)
	VALUES
		('00:15', '00:30', _a, _a, null, _a, _a, _b, _b);

	-- Create Message Templates
	INSERT INTO MessageTemplate
		(Identifier, Subtype, FixedSubject, TemplatedBody)
	VALUES
		-- TODO
		('NewPatient', 'Patient', 'You have been successfully registered.', '{patient_name}:\nYou have been successfully registered. Your doctor is {doctor_name}.'),
		('NewPatient', 'Doctor', 'You have a new patient.', '{patient_name} has selected you as their doctor.'),
		('DoctorChanged', 'Patient', 'You have changed your doctor.', 'You have chosen a new doctor, {doctor_name}. All future appointments will be made with this doctor. Reschedule an existing appointment if you want it to be held with your new doctor.'),
		('DoctorChanged', 'IngoingDoctor', 'You have a new patient.', '{patient_name} has chosen you to be their doctor from now on.'),
		('DoctorChanged', 'OutgoingDoctor', 'One of your patients has changed doctors.', '{patient_name} has decided to choose a different doctor. This does not affect existing appointments.'),
		('BookingRescheduled', 'Patient', 'You have rescheduled a booking.', 'Your booking has been successfully rescheduled.'),
		('BookingRescheduled', 'Doctor', 'One of your bookings has been rescheduled.', 'Please check your future bookings for updates.'),
		('BookingMade', 'Patient', 'You have made a booking.', 'Your booking has been successfully scheduled.'),
		('BookingMade', 'Doctor', 'You have a new booking.', 'Please check your future bookings for updates.');
	
END//
DELIMITER ;

-- Call initialisation procedure.
Call InitialiseDefaultDatabase();

-- -------------
-- --- EXAMPLE DATABASE
-- -------------

-- Initialisation procedure.
DELIMITER //

-- Initialises a Doctor for the example database. Password is default.
-- @param	_Email			Their email address, both security & contact.
-- @param	_FirstName		Their first name.
-- @param	_Surname		Their surname.
-- @param	_Specialism		Their specialism.
CREATE PROCEDURE InitialiseExampleDoctor
(
	_Email varchar(255) character set utf8mb4,
    _FirstName varchar(255) character set utf8mb4,
    _Surname varchar(255) character set utf8mb4,
    _Specialism varchar(255) character set utf8mb4
)
BEGIN
	-- Declarations
    DECLARE
		_DoctorID
        bigint unsigned;

	-- Create Account
	INSERT INTO Account
	(
		AccountUUID,
        SecurityEmail,
        PasswordHash,
        FirstName,
        Surname,
        ContactEmail
	)
	VALUES
	(
		UUID(),
		_Email,
		0x0000000000000000000000000000000000000000000000000000000000000000,
		_FirstName,
		_Surname,
		_Email
	);
	
    -- Create Doctor
	SET _DoctorID = LAST_INSERT_ID();
	
	INSERT INTO Doctor
	(
		AccountID,
        Specialism
	)
	VALUES
	(
		_DoctorID,
		_Specialism
	);
END//

-- Initialises an example database. This does not need to be run in order for
-- the database to function, although without any Doctor or Title records,
-- account creation is impossible.
CREATE PROCEDURE InitialiseExampleDatabase()
BEGIN
	-- Declare
    -- _Status is ignored, as we presume API calls are successful
	DECLARE
		_Status
        varchar(255) character set utf8mb4;
	DECLARE
		_DefaultDoctorUUID
		varchar(36) character set utf8mb4;

    -- Create Titles
	-- [REF: https://en.wikipedia.org/wiki/English_honorifics]
	INSERT INTO Title
		(Identifier)
	VALUES
		-- ('Master'),
        ('Dr.'),
		('Mr.'),
		('Miss'),
		('Mrs.'),
		('Ms.'),
		('Mx.'),
		('Madam');
        -- unused:
		-- ('Sir'),
		-- ('Dame'),
		-- ('Excellency'),
		-- ('HHJ'),
		-- ('Rt. Hon'),
		-- ('The Hon.'),
		-- ('Lord'),
		-- ('Lady'),
		-- ('The Most Honourable'),
		-- ('The Much Honoured'),

	-- Create Doctors
    CALL InitialiseExampleDoctor
    (
		'greg.house@princetonsurgery.com',
        'Gregory',
        'House',
        'Nephrology & Infectious Diseases'
    );
        
    CALL InitialiseExampleDoctor
    (
		'eric.foreman@princetonsurgery.com',
        'Eric',
        'Foreman',
        'Neurology'
    );
    
    CALL InitialiseExampleDoctor
    (
		'allison.cameron@princetonsurgery.com',
        'Allison',
        'Cameron',
        'Immunology'
    );
    
    CALL InitialiseExampleDoctor
    (
		'robert.chase@princetonsurgery.com',
        'Robert',
        'Chase',
        'Cardiology'
    );
    
    CALL InitialiseExampleDoctor
    (
		'james.wilson@princetonsurgery.com',
        'James',
        'Wilson',
        'Oncology'
    );
	
    -- Create Patients
    -- 	[SHA256('password' + salt) = '670c108937a8f046bad253e565c8743b99a4e39fa33d3ec9b348fa8dd4feea8b']
    -- 	ej239@kent.ac.uk	Mofi Jaiyesimi
    -- 	nk479@kent.ac.uk	Nathaniel Kisakye
    -- 	ba419@kent.ac.uk	Brian Addo
    -- 	jb2378@kent.ac.uk Joseph Blair
    
    -- Pick any doctor
    SELECT
		a.AccountUUID
	INTO
		_DefaultDoctorUUID
	FROM
		Doctor d
	JOIN
		Account a
	ON
		a.AccountID = d.AccountID
	LIMIT 1;
    
    -- 	ej239@kent.ac.uk	Mofi Jaiyesimi
    CALL API_Public_Patient_New
    (
		'ej239@kent.ac.uk',
        'password',
        _DefaultDoctorUUID,
        'Mofi',
        'Jaiyesimi',
        'Mr.',
        '07000000000',
        'University of Kent',
        '',
        'Canterbury',
        'Kent',
        'CT2 7NZ',
        _Status
    );
    
    -- 	nk479@kent.ac.uk	Nathaniel Kisakye
    CALL API_Public_Patient_New
    (
		'nk479@kent.ac.uk',
        'password',
        _DefaultDoctorUUID,
        'Nathaniel',
        'Kisakye',
        'Mr.',
        '07000000000',
        'University of Kent',
        '',
        'Canterbury',
        'Kent',
        'CT2 7NZ',
        _Status
    );
    
    -- 	ba419@kent.ac.uk	Brian Addo
    CALL API_Public_Patient_New
    (
		'ba419@kent.ac.uk',
        'password',
        _DefaultDoctorUUID,
        'Brian',
        'Addo',
        'Mr.',
        '07000000000',
        'University of Kent',
        '',
        'Canterbury',
        'Kent',
        'CT2 7NZ',
        _Status
    );
    
    -- 	jb2378@kent.ac.uk Joseph Blair
    CALL API_Public_Patient_New
    (
		'jb2378@kent.ac.uk',
        'password',
        _DefaultDoctorUUID,
        'Joseph',
        'Blair',
        'Mr.',
        '07000000000',
        'University of Kent',
        '',
        'Canterbury',
        'Kent',
        'CT2 7NZ',
        _Status
    );
END//
DELIMITER ;

-- Call initialisation procedure.
Call InitialiseExampleDatabase();

-- -------------
-- --- CONFIGURE ROLES & ACCOUNTS
-- -------------

CREATE USER 'patient'@'localhost' IDENTIFIED BY 'password';

GRANT EXECUTE ON PROCEDURE db.API_Public_Patient_DownloadData TO 'patient'@'localhost';
GRANT EXECUTE ON PROCEDURE db.API_Public_Patient_DownloadTitles TO 'patient'@'localhost';
GRANT EXECUTE ON PROCEDURE db.API_Public_Patient_DownloadDoctors TO 'patient'@'localhost';
GRANT EXECUTE ON PROCEDURE db.API_Public_Patient_DownloadPractice TO 'patient'@'localhost';
GRANT EXECUTE ON PROCEDURE db.API_Public_Patient_RequestPasswordReset TO 'patient'@'localhost';
GRANT EXECUTE ON PROCEDURE db.API_Public_Patient_New TO 'patient'@'localhost';
GRANT EXECUTE ON PROCEDURE db.API_Public_Patient_LogIn TO 'patient'@'localhost';
GRANT EXECUTE ON PROCEDURE db.API_Private_Patient_LogOut TO 'patient'@'localhost';
GRANT EXECUTE ON PROCEDURE db.API_Private_Patient_RescheduleBooking TO 'patient'@'localhost';
GRANT EXECUTE ON PROCEDURE db.API_Private_Patient_MakeBooking TO 'patient'@'localhost';
GRANT EXECUTE ON PROCEDURE db.API_Private_Patient_ListBookings TO 'patient'@'localhost';
GRANT EXECUTE ON PROCEDURE db.API_Private_Patient_SetDoctor TO 'patient'@'localhost';
GRANT EXECUTE ON PROCEDURE db.API_Private_Patient_GetDoctor TO 'patient'@'localhost';
GRANT EXECUTE ON PROCEDURE db.API_Private_Patient_SetDetails TO 'patient'@'localhost';
GRANT EXECUTE ON PROCEDURE db.API_Private_Patient_GetDetails TO 'patient'@'localhost';
GRANT EXECUTE ON PROCEDURE db.API_Private_Patient_GetAttendedBooking TO 'patient'@'localhost';

