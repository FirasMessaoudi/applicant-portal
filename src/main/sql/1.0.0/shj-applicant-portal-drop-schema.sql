
/*--------------------------------------------------------
--  ddl for to drop tables
--------------------------------------------------------*/

USE
sha_portal
GO
drop table if exists sha_portal.sha_user_role;
drop table if exists sha_portal.sha_role_authority;
drop table if exists sha_portal.sha_user_authorities;
drop table if exists sha_portal.sha_user_password_history;
drop table if exists sha_user_location;
drop table if exists sha_portal.sha_user;
drop table if exists sha_portal.sha_role;
drop table if exists sha_portal.sha_user_role_lk;
drop table if exists sha_portal.sha_config;
drop table if exists sha_portal.sha_audit_log;
drop table if exists sha_portal.sha_applicant_relative;

drop table if exists sha_portal.sha_authority_lk;
drop table if exists sha_portal.sha_scheduled_tasks_lock;
GO
