USE sha_portal
GO

UPDATE sha_portal.sha_config SET conf_key = 'huic.sms.api.token' WHERE conf_key = 'sms.api.token';
UPDATE sha_portal.sha_config SET conf_key = 'huic.sms.api.url' WHERE conf_key = 'sms.api.url';

INSERT INTO sha_portal.sha_config(conf_key, conf_value) VALUES('huic.sms.api.mock.enabled','true');
GO
