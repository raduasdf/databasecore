DO $body$
DECLARE
	current_user_id bigint;
	current_role_id bigint;
	
BEGIN

	-
	-- INSERTING SOME DEFAULT VALUES
	
 	INSERT INTO nom_types (name, description, code, start_date) VALUES ('Gender', 'something', 'GENDER', now());
 	INSERT INTO nom_items (code, type_id, value, start_date) VALUES ('MALE', 7, 'Male', now());
	INSERT INTO nom_items (code, type_id, value, start_date) VALUES ('FEMALE', 7, 'Female', now());
	
END$body$ language plpgsql;
