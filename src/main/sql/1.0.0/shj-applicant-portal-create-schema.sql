create DATABASE "sha_portal";
GO
USE sha_portal
GO
create SCHEMA "sha_portal";
GO

/*--------------------------------------------------------
--  ddl for user and authorities tables
--------------------------------------------------------*/
if not exists (select * from sys.tables where name = 'sha_user')
create table sha_portal.sha_user
  (
    id 						    int not null primary key identity(1,1),
    user_name 				    nvarchar(50) not null,
    full_name_ar                nvarchar(150),
    full_name_en                varchar(150),
    uin                         bigint  not null,
    nin 						bigint ,
    gender 					   nvarchar(1) ,
    mobile_number 			int not null,
    date_of_birth_gregorian 	date,
    date_of_birth_hijri 		int null default 0,
    password_hash				nvarchar(256) not null,
    email 					nvarchar(256),
    subtribe_name 			nvarchar(100) null default '',
    activated       			bit default 0,
    deleted       			bit default 0,
    blocked       			bit default 0,
    block_date 				smalldatetime null,
    number_of_tries       	bit default 0,
    preferred_language       	nvarchar(2) default 'en',
    change_password_required  bit default 0,
    last_login_date 			smalldatetime null,
    creation_date 			smalldatetime not null default current_timestamp,
    update_date 				smalldatetime null,
    avatar					nvarchar(max),
    constraint sha_user_user_name_unique unique (user_name),
    constraint sha_user_nin_unique unique (uin)
  );


GO
if not exists (select * from sys.tables where name = 'sha_user_authorities')
create table sha_portal.sha_user_authorities
  (
    id 		int not null primary key identity(1,1),
    user_id 	int not null,
    authority nvarchar(50) not null,
    constraint fk_sha_authorities_users foreign key(user_id) references sha_portal.sha_user(id)
  );
GO
/*---------------------------------------------------
--  ddl for sha_config table
---------------------------------------------------*/
if not exists (select * from sys.tables where name = 'sha_config')
create table sha_portal.sha_config
  (
    id            int not null primary key identity(1,1),
    conf_key    	nvarchar(250) not null,
    conf_value    nvarchar(250) not null,
    creation_date smalldatetime not null default current_timestamp,
    update_date 	smalldatetime null,
    constraint sha_config_key_unique unique (conf_key)
  );
GO
-- update script for sha_portal aash version 1.2.0
USE sha_portal
GO
ALTER TABLE sha_portal.sha_user ADD token nvarchar(255) null;
GO
-- update script for sha_portal aash version 1.3.0
USE sha_portal
GO
if not exists (select * from sys.tables where name = 'sha_user_password_history')
create table sha_portal.sha_user_password_history
(
    id                int PRIMARY KEY                         NOT NULL identity(1,1),
    user_id           int                                     NOT NULL,
    old_password_hash nvarchar(256) NOT NULL,
    creation_date     smalldatetime DEFAULT current_timestamp NOT NULL,
    CONSTRAINT fk_sha_password_history_user FOREIGN KEY (user_id) REFERENCES sha_portal.sha_user (id)
);
GO
ALTER TABLE sha_portal.sha_user DROP COLUMN token;
GO
ALTER TABLE sha_portal.sha_user
    ADD token_expiry_date smalldatetime NULL;

ALTER TABLE sha_portal.sha_user
ALTER
COLUMN mobile_number nvarchar(30);

ALTER TABLE sha_portal.sha_user
    ADD country_phone_prefix nvarchar(10);
GO


GO
ALTER TABLE sha_portal.sha_user
    ADD country_code nvarchar(10);
GO



GO
drop table if exists sha_portal.sha_audit_event_data;
drop table if exists sha_portal.sha_audit_event;
GO

declare
@schema_name nvarchar(256)
declare
@table_name nvarchar(256)
declare
@col_name nvarchar(256)
declare
@Command  nvarchar(1000)
set @schema_name = N'sha_portal'
set @table_name = N'sha_user'
set @col_name = N'number_of_tries'
select @Command = 'ALTER TABLE ' + @schema_name + '.[' + @table_name + '] DROP CONSTRAINT ' + d.name
from sys.tables t
         join sys.default_constraints d on d.parent_object_id = t.object_id
         join sys.columns c on c.object_id = t.object_id and c.column_id = d.parent_column_id
where t.name = @table_name and t.schema_id = schema_id(@schema_name) and c.name = @col_name
execute (@Command)

ALTER TABLE sha_portal.sha_user ALTER COLUMN number_of_tries int;
GO

CREATE INDEX ix_sha_user_nin ON sha_portal.sha_user(nin);
GO

if not exists (select * from sys.tables where name = 'sha_user_role')
create table sha_portal.sha_user_role
(
    id int PRIMARY KEY NOT NULL identity(1,1),
    name_arabic nvarchar(50) NOT NULL,
    name_english nvarchar(50) NOT NULL,
    creation_date smalldatetime not null default current_timestamp
);
GO
--------------------------------------------------------

EXEC sp_rename 'sha_portal.sha_user_authorities', 'sha_user_roles';
EXEC sp_rename 'sha_portal.sha_user_role', 'sha_user_role_lk';
ALTER TABLE sha_portal.sha_user_roles DROP COLUMN authority;
ALTER TABLE sha_portal.sha_user_roles ADD role_id int;
ALTER TABLE sha_portal.sha_user_roles ADD CONSTRAINT fk_sha_user_roles_role_lk FOREIGN KEY (role_id) REFERENCES sha_portal.sha_user_role_lk (id);
EXEC sp_rename 'sha_portal.fk_sha_authorities_users', 'fk_sha_user_roles_user', 'OBJECT';
GO

ALTER TABLE sha_portal.sha_user_role_lk ADD code nvarchar(50);
DELETE FROM sha_portal.sha_user_role_lk WHERE id > 0;
ALTER TABLE sha_portal.sha_user_role_lk ADD level int;
GO

ALTER TABLE sha_portal.sha_user_role_lk ADD sub_roles varchar(50);
GO

declare @schema_name nvarchar(256)
declare @table_name nvarchar(256)
declare @col_name nvarchar(256)
declare @Command  nvarchar(1000)
set @schema_name = N'sha_portal'
set @table_name = N'sha_user'
set @col_name = N'subtribe_name'
select @Command = 'ALTER TABLE ' + @schema_name + '.[' + @table_name + '] DROP CONSTRAINT ' + d.name
from sys.tables t
         join sys.default_constraints d on d.parent_object_id = t.object_id
         join sys.columns c on c.object_id = t.object_id and c.column_id = d.parent_column_id
where t.name = @table_name and t.schema_id = schema_id(@schema_name) and c.name = @col_name
execute (@Command)

ALTER TABLE sha_portal.sha_user DROP COLUMN subtribe_name;
GO


-- script for role and authority new implementation
if not exists (select * from sys.tables where name = 'sha_authority_lk')
create table sha_portal.sha_authority_lk
(
    id int PRIMARY KEY NOT NULL identity(1,1),
    label_ar nvarchar(50) NOT NULL,
    label_en nvarchar(50) NOT NULL,
    code nvarchar(50) NOT NULL,
    parent_id int,
    creation_date smalldatetime not null default current_timestamp
);
GO

if not exists (select * from sys.tables where name = 'sha_role')
create table sha_portal.sha_role
(
    id int PRIMARY KEY NOT NULL identity(1,1),
    label_ar nvarchar(50) NOT NULL,
    label_en nvarchar(50) NOT NULL,
    deleted bit NOT NULL default 0,
    activated bit NOT NULL default 0,
    creation_date smalldatetime not null default current_timestamp,
    update_date smalldatetime null
);
GO

if not exists (select * from sys.tables where name = 'sha_role_authority')
create table sha_portal.sha_role_authority
(
    id int PRIMARY KEY NOT NULL identity(1,1),
    role_id int NOT NULL,
    authority_id int NOT NULL,
    creation_date smalldatetime not null default current_timestamp,
    CONSTRAINT fk_sha_role_authority_role FOREIGN KEY (role_id) REFERENCES sha_portal.sha_role (id),
    CONSTRAINT fk_sha_role_authority_authority_lk FOREIGN KEY (authority_id) REFERENCES sha_portal.sha_authority_lk (id)
);
GO

drop table if exists sha_portal.sha_user_roles;
drop table if exists sha_portal.sha_user_role_lk;

ALTER TABLE sha_portal.sha_user ADD role_id int;
ALTER TABLE sha_portal.sha_user ADD CONSTRAINT fk_sha_user_role FOREIGN KEY (role_id) REFERENCES sha_portal.sha_role (id);

ALTER TABLE sha_portal.sha_user ADD action_date smalldatetime NULL;

-- Dropping unused column
ALTER TABLE sha_portal.sha_user DROP CONSTRAINT sha_user_user_name_unique;
ALTER TABLE sha_portal.sha_user DROP COLUMN user_name;
GO

if not exists(select * from sys.tables where name = 'sha_audit_log')
create table sha_portal.sha_audit_log
(
    id              int            not null primary key identity (1,1),
    user_id_number  int            not null,
    handler         varchar(100)   not null,
    action          varchar(100)   not null,
    params          nvarchar(1000) null,
    host            varchar(100)   not null,
    origin          nvarchar(256)  not null,
    start_time      smalldatetime  not null,
    processing_time int            not null,
    channel         varchar(256)   null,
    http_status     int            not null,
    error_details   nvarchar(512)  null
);
GO

if not exists (select * from sys.tables where name = 'sha_user_role')
create table sha_portal.sha_user_role
(
    id int PRIMARY KEY NOT NULL identity(1,1),
    user_id int NOT NULL,
    role_id int NOT NULL,
    is_main_role bit NOT NULL default 0,
    creation_date smalldatetime not null default current_timestamp,
    CONSTRAINT fk_sha_user_role_user FOREIGN KEY (user_id) REFERENCES sha_portal.sha_user (id),
    CONSTRAINT fk_sha_user_role_role FOREIGN KEY (role_id) REFERENCES sha_portal.sha_role (id)
);
GO

ALTER TABLE sha_portal.sha_user DROP CONSTRAINT fk_sha_user_role;
ALTER TABLE sha_portal.sha_user DROP COLUMN role_id;
GO

if not exists(select * from sys.tables where name = 'sha_scheduled_tasks_lock')
create table sha_portal.sha_scheduled_tasks_lock
(
    name       varchar(255) NOT NULL,
    lock_until datetime     NULL,
    locked_at  datetime     NULL,
    locked_by  varchar(255) NOT NULL,
);

GO
if not exists (select * from sys.tables where name = 'sha_user_location')
create table sha_portal.sha_user_location
(
id int PRIMARY KEY NOT NULL identity(1,1),
user_id int NOT NULL,
latitude DECIMAL(10, 8) ,
longitude DECIMAL(11, 8) ,
altitude DECIMAL(10, 5),
heading DECIMAL(10, 5),
speed DECIMAL(7, 4),
gps_time smalldatetime null,
creation_date smalldatetime not null default current_timestamp,
update_date smalldatetime null,
CONSTRAINT fk_sha_location_user FOREIGN KEY (user_id) REFERENCES sha_portal.sha_user (id)
);
GO




