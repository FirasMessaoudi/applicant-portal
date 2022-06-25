USE sha_portal
GO
UPDATE sha_portal.sha_config SET conf_value = 'Hajj App' WHERE conf_key = 'elm.providers.email.from.name';
INSERT INTO sha_portal.sha_config(conf_key, conf_value) VALUES('notification.count.refresh.interval', 600000);
GO

UPDATE sha_portal.sha_user SET email = 'aelsayed@elm.sa' WHERE nin in ('1234567897');
GO
