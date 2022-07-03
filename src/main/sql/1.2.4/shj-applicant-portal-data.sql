USE sha_portal
UPDATE sha_portal.sha_config SET conf_key = 'medical.emergency.phone.number', conf_value = '937' WHERE conf_key = 'emergency.phone.number'
GO
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('security.emergency.phone.number', '911');
GO
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('mohu.emergency.phone.number', '920002814');
GO
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('holy.mosque.emergency.phone.number', '1966');
GO
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('ethrai.url', 'https://ethrai.com/en/');
GO
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('adahi.url', 'https://www.adahi.org/');
GO
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('awareness.url', 'https://guide.haj.gov.sa');
GO
INSERT INTO sha_portal.sha_config (conf_key, conf_value) VALUES ('fatwas.phone.number', '8002451000');
GO