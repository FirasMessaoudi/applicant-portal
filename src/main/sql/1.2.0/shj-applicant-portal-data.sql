USE sha_portal
GO
UPDATE sha_portal.sha_config SET conf_value = 'Hajj App' WHERE conf_key = 'elm.providers.email.from.name';
INSERT INTO sha_portal.sha_config(conf_key, conf_value) VALUES('notification.count.interval',600000);
GO
