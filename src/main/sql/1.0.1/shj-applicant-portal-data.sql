USE sha_portal
GO

INSERT INTO sha_portal.sha_config (conf_key, conf_value)
VALUES ('sms.api.token', 'D60A13C6105742AD99FEDD608632A157');
INSERT INTO sha_portal.sha_config (conf_key, conf_value)
VALUES ('sms.api.url', 'https://172.16.72.65/api/SmartCards/smsService');

GO

UPDATE sha_portal.sha_config
SET conf_key = 'huic.sms.api.token'
WHERE conf_key = 'sms.api.token';

UPDATE sha_portal.sha_config
SET conf_key = 'huic.sms.api.url'
WHERE conf_key = 'sms.api.url';

INSERT INTO sha_portal.sha_config(conf_key, conf_value)
VALUES('huic.sms.api.mock.enabled','true');
GO
