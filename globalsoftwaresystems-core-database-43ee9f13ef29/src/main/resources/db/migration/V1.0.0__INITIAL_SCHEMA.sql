DO $body$
DECLARE
	current_user_id bigint;
	current_role_id bigint;
	
BEGIN

	-- Sequence: public.nom_items_id_seq
	CREATE SEQUENCE public.nom_items_id_seq
	  INCREMENT 1
	  MINVALUE 1
	  MAXVALUE 9223372036854775807
	  START 1
	  CACHE 1;
	ALTER TABLE public.nom_items_id_seq
	  OWNER TO postgres;
	
	-- Sequence: public.nom_types_id_seq
	CREATE SEQUENCE public.nom_types_id_seq
	  INCREMENT 1
	  MINVALUE 1
	  MAXVALUE 9223372036854775807
	  START 4
	  CACHE 1;
	ALTER TABLE public.nom_types_id_seq
	  OWNER TO postgres;
	
	-- Sequence: public.roles_id_seq
	CREATE SEQUENCE public.roles_id_seq
	  INCREMENT 1
	  MINVALUE 1
	  MAXVALUE 9223372036854775807
	  START 1
	  CACHE 1;
	ALTER TABLE public.roles_id_seq
	  OWNER TO postgres;
	
	-- Sequence: public.sys_parameters_id_seq
	CREATE SEQUENCE public.sys_parameters_id_seq
	  INCREMENT 1
	  MINVALUE 1
	  MAXVALUE 9223372036854775807
	  START 1
	  CACHE 1;
	ALTER TABLE public.sys_parameters_id_seq
	  OWNER TO postgres;
	
	-- Sequence: public.user_roles_id_seq
	CREATE SEQUENCE public.user_roles_id_seq
	  INCREMENT 1
	  MINVALUE 1
	  MAXVALUE 9223372036854775807
	  START 1
	  CACHE 1;
	ALTER TABLE public.user_roles_id_seq
	  OWNER TO postgres;
	
	-- Sequence: public.users_id_seq
	CREATE SEQUENCE public.users_id_seq
	  INCREMENT 1
	  MINVALUE 1
	  MAXVALUE 9223372036854775807
	  START 1
	  CACHE 1;
	ALTER TABLE public.users_id_seq
	  OWNER TO postgres;	

	-- Sequence: public.my_users_id_seq
	CREATE SEQUENCE public.my_users_id_seq
	  INCREMENT 1
	  MINVALUE 1
	  MAXVALUE 9223372036854775807
	  START 1
	  CACHE 1;
	ALTER TABLE public.my_users_id_seq
	  OWNER TO postgres;
	
	  
	
	-- TABLES
	
	-- Table: public.sys_parameters
	CREATE TABLE public.sys_parameters
	(
	  id bigint NOT NULL DEFAULT nextval('sys_parameters_id_seq'::regclass),
	  code character varying(100) NOT NULL,
	  string_value character varying(100),
	  number_value bigint,
	  float_value double precision,
	  CONSTRAINT sys_parameter_pk PRIMARY KEY (id),
	  CONSTRAINT at_least_one_check CHECK (string_value IS NOT NULL OR number_value IS NOT NULL OR float_value IS NOT NULL)
	)
	WITH (
	  OIDS=FALSE
	);
	ALTER TABLE public.sys_parameters
	  OWNER TO postgres;
	
	-- Table: public.users
	CREATE TABLE public.users
	(
	  id bigint NOT NULL DEFAULT nextval('users_id_seq'::regclass),
	  login character varying(100) NOT NULL,
	  start_date timestamp with time zone NOT NULL,
	  end_date timestamp with time zone,
	  password character varying(60) NOT NULL,
	  CONSTRAINT user_pk PRIMARY KEY (id),
	  CONSTRAINT user_login_unique UNIQUE (login),
	  CONSTRAINT password_length_check CHECK (char_length(password::text) = 60)
	)
	WITH (
	  OIDS=FALSE
	);
	ALTER TABLE public.users
	  OWNER TO postgres;
	
	-- Table: public.roles
	CREATE TABLE public.roles
	(
	  id bigint NOT NULL DEFAULT nextval('roles_id_seq'::regclass),
	  code character varying(100) NOT NULL,
	  start_date timestamp with time zone NOT NULL,
	  end_date timestamp with time zone,
	  CONSTRAINT role_pk PRIMARY KEY (id)
	)
	WITH (
	  OIDS=FALSE
	);
	ALTER TABLE public.roles
	  OWNER TO postgres;
	
	-- Table: public.user_roles
	CREATE TABLE public.user_roles
	(
	  id bigint NOT NULL DEFAULT nextval('user_roles_id_seq'::regclass),
	  user_id bigint NOT NULL,
	  role_id bigint NOT NULL,
	  start_date timestamp with time zone NOT NULL,
	  end_date timestamp with time zone,
	  CONSTRAINT user_role_pk PRIMARY KEY (id),
	  CONSTRAINT user_role_role_fk FOREIGN KEY (role_id)
	      REFERENCES public.roles (id) MATCH SIMPLE
	      ON UPDATE NO ACTION ON DELETE NO ACTION,
	  CONSTRAINT user_role_user_fk FOREIGN KEY (user_id)
	      REFERENCES public.users (id) MATCH SIMPLE
	      ON UPDATE NO ACTION ON DELETE NO ACTION
	)
	WITH (
	  OIDS=FALSE
	);
	ALTER TABLE public.user_roles
	  OWNER TO postgres;
	
	-- Table: public.nom_types
	CREATE TABLE public.nom_types
	(
	  id bigint NOT NULL DEFAULT nextval('nom_types_id_seq'::regclass),
	  name character varying(150),
	  description character varying(500),
	  start_date timestamp with time zone NOT NULL,
	  end_date timestamp with time zone,
	  code character varying(100) NOT NULL,
	  CONSTRAINT nom_type_pk PRIMARY KEY (id)
	)
	WITH (
	  OIDS=FALSE
	);
	ALTER TABLE public.nom_types
	  OWNER TO postgres;
	
	-- Table: public.nom_items
	CREATE TABLE public.nom_items
	(
	  id bigint NOT NULL DEFAULT nextval('nom_items_id_seq'::regclass),
	  code character varying(100) NOT NULL,
	  type_id bigint NOT NULL,
	  start_date timestamp with time zone NOT NULL,
	  end_date timestamp with time zone,
	  value character varying(200) NOT NULL,
	  CONSTRAINT nom_item_pk PRIMARY KEY (id),
	  CONSTRAINT nom_item_nom_type_fk FOREIGN KEY (type_id)
	      REFERENCES public.nom_types (id) MATCH SIMPLE
	      ON UPDATE NO ACTION ON DELETE NO ACTION
	)
	WITH (
	  OIDS=FALSE
	);
	ALTER TABLE public.nom_items
	  OWNER TO postgres;
	
	-- Table: public.my_users
	CREATE TABLE public.my_users
	(
	  id bigint NOT NULL DEFAULT nextval('my_users_id_seq'::regclass),
	  user_name character varying(100) NOT NULL,
	  password character varying(60) NOT NULL,
	  email character varying(60) NOT NULL,
	  address character varying(60) NOT NULL,
	  CONSTRAINT my_user_pk PRIMARY KEY (id),
	  CONSTRAINT my_user_login_unique UNIQUE (user_name)
	)
	WITH (
	  OIDS=FALSE
	);
	ALTER TABLE public.my_users
	  OWNER TO postgres;

	
	-- INSERTING SOME DEFAULT VALUES
	
	-- roles
	INSERT INTO roles (code, start_date)
		VALUES ('ROLE_ADMIN', now());
	INSERT INTO roles (code, start_date) 
		VALUES ('ROLE_USER', now());
	INSERT INTO users (login, start_date, password) 
		VALUES ('admin', now(), '$2a$06$sSVblin0KnVdLtlcpffG.OGj7EKiMqpi5cI3eUYMhEBkl92S.uzFe'); --password: admin (bcrypt)
	
	SELECT id into current_user_id from users where login='admin';
	SELECT id into current_role_id from roles where code='ROLE_USER';
	INSERT INTO user_roles (user_id, role_id, start_date) 
		VALUES (current_user_id, current_role_id, now());
		
	SELECT id into current_role_id from roles where code='ROLE_ADMIN';
	INSERT INTO user_roles (user_id, role_id, start_date) 
		VALUES (current_user_id, current_role_id, now());
	
END$body$ language plpgsql;
