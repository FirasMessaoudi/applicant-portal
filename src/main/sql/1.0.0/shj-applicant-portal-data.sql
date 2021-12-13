USE sha_portal
GO
-- sha_user data
SET IDENTITY_INSERT sha_portal.sha_user ON;
insert into sha_portal.sha_user (id, nin,uin, gender , mobile_number, date_of_birth_gregorian,
                                 password_hash,full_name_ar, full_name_en , activated, creation_date) values ('1', 1234567897,1234567897, 'M',
                                                                                                              512345678, convert(date, '14/02/1972', 103), '$2a$10$A81/FuMFJWcxaJhUcL8isuVeKKa.hk7GVzTVTyf7xe/XoMVWuKckK',  N'سعد الغامدي' , 'Saad El Ghamdy', 1, current_timestamp);
SET IDENTITY_INSERT sha_portal.sha_user OFF;

-- sha_config data
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.cache.read.timeout', 500);
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.cache.write.timeout', 1000);
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.cache.delete.timeout', 1000);

INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.yakeen.username', 'changeit');
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.yakeen.password', 'changeit');
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.yakeen.chargecode', 'changeit');
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.yakeen.url', 'changeit');
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.yakeen.connect.timeout', 500);
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.yakeen.receive.timeout', 500);
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.yakeen.mock.enabled', 'true');

INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.email.host', '192.168.0.200');
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.email.port', 25);
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.email.username', '');
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.email.password', '');
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.email.from.address', 'no-reply@elm.sa');
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.email.from.name', 'Elm Product');
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.email.smtp.auth', 'false');
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.email.mock.enabled', 'false');

INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.smsgateway.url', 'http://te1-iqa-rv-sg:8080/ElmSMSGatewayEJB/ElmSMSGatewayWS');
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.smsgateway.connect.timeout', 500);
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.smsgateway.receive.timeout', 500);
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.smsgateway.username', 'changeit');
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.smsgateway.password', 'changeit');
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.smsgateway.sendername', 'changeit');
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.smsgateway.mock.enabled', 'true');

INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.recaptcha.key.site', '6Le30G0UAAAAAM3Kwdf4V_feWJ-zD7OFAjxqO3Vo');
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.recaptcha.key.secret', '6Le30G0UAAAAAIBhhUZ-TtNdmbCRqzoxNftB5W1w');
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.recaptcha.verify.url', 'https://www.google.com/recaptcha/api/siteverify');
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.recaptcha.mock.enabled', 'false');
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.recaptcha.connection.timeout', 500);
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.recaptcha.read.timeout', 500);

INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('login.failed.max.attempts', '3');

INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.filescan.mock.enabled', 'false');
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.filescan.read.timeout', 10000);
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.filescan.connection.timeout', 10000);
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.filescan.rest.url', 'http://localhost:8080/sha_portal-engines-filescan/scan-file');
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.filescan.username', 'sha_portalfilescan-username');
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.filescan.password', 'sha_portalfilescan-password');

INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.engines.filescan.host', '192.168.46.145');
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.engines.filescan.port', '1344');
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.engines.filescan.username', 'sha_portalfilescan-username');
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.engines.filescan.password', 'sha_portalfilescan-password');
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('admin.portal.url', 'http://localhost:8085/shj-admin/api');
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('google.map.key', 'AIzaSyAC78ugAlOF9B2YK8-ukki2IQTyNAgUSO0');

GO
-- update sha_user data in 1.2.0 version

GO
UPDATE sha_portal.sha_user SET password_hash = '$2a$10$A81/FuMFJWcxaJhUcL8isuVeKKa.hk7GVzTVTyf7xe/XoMVWuKckK'
where id = 1;

INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.recaptcha.invisible.key.site', '6Le30G0UAAAAAM3Kwdf4V_feWJ-zD7OFAjxqO3Vo');
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.recaptcha.invisible.key.secret', '6Le30G0UAAAAAIBhhUZ-TtNdmbCRqzoxNftB5W1w');
GO

GO
INSERT INTO sha_portal.sha_user_password_history (user_id, old_password_hash) values (1, '$2a$10$MLt2QkqgBSo5WdVu5UJXjunvi0t/h.BKDJQWzO2tyrQKBysLmc9ou');

DELETE FROM sha_portal.sha_config where conf_key = 'elm.engines.filescan.host';
DELETE FROM sha_portal.sha_config where conf_key = 'elm.engines.filescan.port';
GO

GO
UPDATE sha_portal.sha_config SET conf_value='http://192.168.2.149:8080/sha_portal-engines-filescan/scan-file'
where conf_key = 'elm.providers.filescan.rest.url';
GO
UPDATE sha_portal.sha_user SET date_of_birth_hijri='14400505' where id = 1;
GO

DELETE FROM sha_portal.sha_config where conf_key = 'elm.engines.filescan.username';
DELETE FROM sha_portal.sha_config where conf_key = 'elm.engines.filescan.password';
GO

INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.email.ssl.enabled', 'false');
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.email.read.timeout', 5000);
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.providers.email.connection.timeout', 5000);
GO
-- update script for sha_portal aash version 1.7.1

GO
UPDATE sha_portal.sha_user SET email = 'sgh@elm.sa' where id = 1;
GO

INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('login.simultaneous.enabled', 'false');
GO
-- update script for sha_portal aash version 1.8.0

GO
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.commons.web.cors.allowed_origins', 'http://localhost:8080,http://localhost:4200,http://127.0.0.1:4200,http://localhost:8200,http://127.0.0.1:8200,http://ci-sha_portal.elm.com.sa:8080');
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('elm.commons.web.cors.allowed_methods', 'GET,POST,PUT,OPTIONS');
GO

UPDATE sha_portal.sha_config SET conf_value = '5000' WHERE conf_key = 'elm.providers.cache.read.timeout';
UPDATE sha_portal.sha_config
SET conf_value = '5000'
WHERE conf_key = 'elm.providers.yakeen.connect.timeout';
UPDATE sha_portal.sha_config
SET conf_value = '5000'
WHERE conf_key = 'elm.providers.yakeen.receive.timeout';
UPDATE sha_portal.sha_config
SET conf_value = '5000'
WHERE conf_key = 'elm.providers.smsgateway.connect.timeout';
UPDATE sha_portal.sha_config
SET conf_value = '5000'
WHERE conf_key = 'elm.providers.smsgateway.receive.timeout';
UPDATE sha_portal.sha_config
SET conf_value = '5000'
WHERE conf_key = 'elm.providers.recaptcha.connection.timeout';
UPDATE sha_portal.sha_config
SET conf_value = '5000'
WHERE conf_key = 'elm.providers.recaptcha.read.timeout';
GO

-- insert authorities
SET IDENTITY_INSERT sha_portal.sha_authority_lk ON;
INSERT INTO sha_portal.sha_authority_lk(id, label_ar, label_en, code, parent_id)
VALUES (1, N'صلاحيات ضيف الرحمن', 'applicant privilege', 'APPLICANT_PRIVILEGE', NULL);

SET
IDENTITY_INSERT sha_portal.sha_authority_lk OFF;
GO

SET IDENTITY_INSERT sha_portal.sha_role ON;
INSERT INTO sha_portal.sha_role(id, label_ar, label_en, deleted, activated)
VALUES (1, N'ضيف الرحمن', 'applicant', 0, 1);
SET
IDENTITY_INSERT sha_portal.sha_role OFF;
GO

INSERT INTO sha_portal.sha_role_authority(role_id, authority_id) VALUES (1, 1);
GO

UPDATE sha_portal.sha_user SET number_of_tries = 0 WHERE number_of_tries IS NULL;
GO

SET IDENTITY_INSERT sha_portal.sha_user_role ON;
INSERT INTO sha_portal.sha_user_role(id, user_id, role_id, is_main_role) VALUES (1, 1, 1, 1);
SET IDENTITY_INSERT sha_portal.sha_user_role OFF;
GO

-- add otp config
INSERT INTO sha_portal.sha_config (conf_key, conf_value)
VALUES ('otp.expiry.minutes', '5'),
       ('otp.pin.length', '4'),
       ('otp.mock.enabled', 'true');
GO

-- add lookup loading scheduler cron
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('scheduler.load.lookups.cron', '0 0/45 * * * *');
GO

-- add  password expiry notification scheduler cron
GO
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('scheduler.password.expiry.notification.cron', '0 0 1 ? * *');
GO
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('password.expiry.notification.period.in.days', 5);
GO

INSERT INTO sha_portal.sha_config (conf_key,conf_value) VALUES ('send.user.location.distance' ,'10')
GO

INSERT INTO sha_portal.sha_config (conf_key,conf_value) VALUES ('send.user.location.batch.size' ,'10')
GO
