DO $body$
DECLARE
BEGIN

	
	-- INSERTING SOME DEFAULT VALUES
	
 	--INSERT INTO nom_types (name, description, code, start_date) VALUES ('Gender', 'something', 'GENDER', now());
 	--INSERT INTO nom_items (code, type_id, value, start_date) VALUES ('MALE', 7, 'Male', now());
	--INSERT INTO nom_items (code, type_id, value, start_date) VALUES ('FEMALE', 7, 'Female', now());
	
	
	ALTER TABLE public.my_users 
		ADD COLUMN nom_item_id bigint;
	ALTER TABLE public.my_users 
		ADD CONSTRAINT nom_item_fk FOREIGN KEY (nom_item_id)
		REFERENCES public.nom_items (id) MATCH SIMPLE
	     	ON UPDATE NO ACTION ON DELETE NO ACTION;

END$body$ language plpgsql;
