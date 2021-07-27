

INSERT INTO sha_authority_lk (id, label_ar, label_en, code, parent_id, creation_date) VALUES (1, N'لوحة المعلومات', N'Admin Dashboard', N'ADMIN_DASHBOARD', null, N'2020-07-27 11:44:00');
INSERT INTO sha_authority_lk (id, label_ar, label_en, code, parent_id, creation_date) VALUES (2, N'ادارة المستخدمين', N'User Management', N'USER_MANAGEMENT', null, N'2020-07-27 11:44:00');
INSERT INTO sha_authority_lk (id, label_ar, label_en, code, parent_id, creation_date) VALUES (3, N'اضا�?ة مستخدم', N'Add User', N'ADD_USER', 2, N'2020-07-27 11:44:00');
INSERT INTO sha_authority_lk (id, label_ar, label_en, code, parent_id, creation_date) VALUES (4, N'تعديل مستخدم', N'Edit User', N'EDIT_USER', 2, N'2020-07-27 11:44:00');
INSERT INTO sha_authority_lk (id, label_ar, label_en, code, parent_id, creation_date) VALUES (5, N'تغيير حالة مستخدم', N'Change User Status', N'CHANGE_USER_STATUS', 2, N'2020-07-27 11:44:00');
INSERT INTO sha_authority_lk (id, label_ar, label_en, code, parent_id, creation_date) VALUES (6, N'إعادة تعيين كلمة المرور', N'Reset Password', N'RESET_PASSWORD', 2, N'2020-07-27 11:44:00');
INSERT INTO sha_authority_lk (id, label_ar, label_en, code, parent_id, creation_date) VALUES (7, N'حذ�? مستخدم', N'Delete User', N'DELETE_USER', 2, N'2020-07-27 11:44:00');
INSERT INTO sha_authority_lk (id, label_ar, label_en, code, parent_id, creation_date) VALUES (8, N'ادارة الأدوار', N'Role Management', N'ROLE_MANAGEMENT', null, N'2020-07-27 11:44:00');
INSERT INTO sha_authority_lk (id, label_ar, label_en, code, parent_id, creation_date) VALUES (9, N'اضا�?ة دور', N'Add Role', N'ADD_ROLE', 8, N'2020-07-27 11:44:00');
INSERT INTO sha_authority_lk (id, label_ar, label_en, code, parent_id, creation_date) VALUES (10, N'تعديل دور', N'Edit Role', N'EDIT_ROLE', 8, N'2020-07-27 11:44:00');
INSERT INTO sha_authority_lk (id, label_ar, label_en, code, parent_id, creation_date) VALUES (11, N'حذ�? دور', N'Delete Role', N'DELETE_ROLE', 8, N'2020-07-27 11:44:00');
INSERT INTO sha_authority_lk (id, label_ar, label_en, code, parent_id, creation_date) VALUES (12, N'تغيير حالة دور', N'Change Role Status', N'CHANGE_ROLE_STATUS', 8, N'2020-08-12 12:59:00');
INSERT INTO sha_authority_lk (id, label_ar, label_en, code, parent_id, creation_date) VALUES (13, N'إعادة تعيين كلمة مرور المستخدم', N'Reset User Password', N'RESET_USER_PASSWORD', 2, N'2020-09-07 11:04:00');



INSERT INTO sha_role (id, label_ar, label_en, deleted, activated, creation_date, update_date) VALUES (1, N'مشر�? النظام', N'System Admin', 0, 1, N'2020-07-27 11:44:00', null);
INSERT INTO sha_role (id, label_ar, label_en, deleted, activated, creation_date, update_date) VALUES (2, N'مستخدم النظام', N'System User', 0, 1, N'2020-09-07 11:04:00', null);

INSERT INTO sha_user (id, uin, nin, gender, mobile_number, date_of_birth_gregorian, date_of_birth_hijri, password_hash, email, full_name_ar,full_name_en, activated, deleted, blocked, block_date, number_of_tries, preferred_language, change_password_required, last_login_date, creation_date, update_date, avatar, token_expiry_date, action_date) VALUES (1, 223456789, 1234567897, N'M', 512345678, N'1972-02-14', 14400505, N'$2a$10$A81/FuMFJWcxaJhUcL8isuVeKKa.hk7GVzTVTyf7xe/XoMVWuKckK', N'sgh@elm.sa', N'سعد الغامدي', N'saad elghmdy', 1, 0, 0, null, 0, N'en', 0, N'2020-09-28 16:19:00', N'2020-07-21 16:01:00', N'2020-09-28 16:19:00', null, N'2020-09-28 16:34:00', null);
INSERT INTO sha_user (id, uin, nin, gender, mobile_number, date_of_birth_gregorian, date_of_birth_hijri, password_hash, email, full_name_ar,full_name_en, activated, deleted, blocked, block_date, number_of_tries, preferred_language, change_password_required, last_login_date, creation_date, update_date, avatar, token_expiry_date, action_date) VALUES (2, 2234567892, 1234567892, N'M', 512345678, N'2004-08-06', 14250620, N'$2a$10$A81/FuMFJWcxaJhUcL8isuVeKKa.hk7GVzTVTyf7xe/XoMVWuKckK', N'mail@company.com', N' ايمن ضاوي', N'Aymen Dawi', 1, 0, 0, null, 0, null, 1, N'2020-09-20 16:34:00', N'2020-08-25 13:38:00', N'2020-09-20 16:34:00', null, N'2020-09-20 16:49:00', N'2020-09-09 13:04:00');
INSERT INTO sha_user (id, uin, nin, gender, mobile_number, date_of_birth_gregorian, date_of_birth_hijri, password_hash, email, full_name_ar,full_name_en, activated, deleted, blocked, block_date, number_of_tries, preferred_language, change_password_required, last_login_date, creation_date, update_date, avatar, token_expiry_date, action_date) VALUES (3, 2000000164, 1000000164, N'F', 512345678, N'2004-08-12', 14250625, N'$2a$10$A81/FuMFJWcxaJhUcL8isuVeKKa.hk7GVzTVTyf7xe/XoMVWuKckK', N'maram@company.com', N'مرام ضاوي', N'Mram Dawi',  1, 1, 0, null, 0, null, 1, null, N'2020-08-25 16:41:00', null, null, null, null);


INSERT INTO sha_user_role(id, user_id, role_id, is_main_role, creation_date) VALUES (1, 1, 1, 1, N'2004-08-12');