USE sha_portal
GO
if not exists (select * from sys.tables where name = 'sha_otp_cache')
create table sha_portal.sha_otp_cache
(
    id int PRIMARY KEY NOT NULL identity(1,1),
    principle nvarchar(50) NOT NULL,
    otp nvarchar(50) NOT NULL,
    creation_date smalldatetime not null default current_timestamp
);
GO