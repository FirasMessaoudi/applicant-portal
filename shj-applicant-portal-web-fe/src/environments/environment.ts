// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.

// `.env.ts` is generated by the `npm run env` command
import {env} from './.env';

export const environment = {
  production: false,
  dev: true,
  mock: false,
  recaptchaSiteKey: '6LdzHFQUAAAAAKbbaojHVswZvdHgPuzC71VCIUlb',
  invisibleRecaptchaSiteKey: '6Le30G0UAAAAAM3Kwdf4V_feWJ-zD7OFAjxqO3Vo',
  version: env.npm_package_version + '-dev',
  backendMappings: [{mapping:'/core', url:'http://localhost:8080/shj-applicant/'}, {mapping:'/engine',url:'http://localhost:8080/dcc-engines-filescan/'}],
  websocketConnection: [{url: ''}],
  defaultLanguage: 'ar-SA',
  termsAndConditionsUrl: '',
  supportedLanguages: [
    'en-US',
    'ar-SA'
  ],
  mockData: [
    {url: '/api/auth/login', data: '{"authorities":[],"details":null,"authenticated":false,"principal":"1234567897","credentials":null,"otpRequired":true,"firstName":"سعد","lastName":"الغامدي","email":"sg***@el****.sa","mobileNumber":"*******678","otpExpiryMinutes":5,"name":"1234567897"}'},
    {url: '/api/auth/otp', data: '{"authorities":[{"authority":"ROLE_MANAGEMENT"},{"authority":"ADD_USER"},{"authority":"EDIT_USER"},{"authority":"USER_MANAGEMENT"},{"authority":"CHANGE_ROLE_STATUS"},{"authority":"DELETE_ROLE"},{"authority":"CHANGE_USER_STATUS"},{"authority":"RESET_USER_PASSWORD"},{"authority":"EDIT_ROLE"},{"authority":"DELETE_USER"},{"authority":"RESET_PASSWORD"},{"authority":"ADD_ROLE"},{"authority":"ADMIN_DASHBOARD"}],"details":null,"authenticated":true,"principal":"1234567897","credentials":null,"token":null,"tokenExpirationDate":0,"passwordExpired":false,"userRoles":[{"id":1,"role":{"id":1,"nameArabic":"مشرف النظام","nameEnglish":"System Admin","deleted":false,"activated":true,"roleAuthorities":null,"creationDate":1616492160000,"updateDate":null},"mainRole":true,"creationDate":1616596260000}],"firstName":"سعد","lastName":"الغامدي","name":"1234567897"}'},
    {url: '/api/dashboard', data: '{"totalNumberOfUsers":2,"totalNumberOfCitizenActiveUsers":2,"totalNumberOfResidentActiveUsers":0,"totalNumberOfActiveUsers":2,"totalNumberOfInactiveUsers":0,"totalNumberOfDeletedUsers":2,"totalNumberOfRoles":12,"totalNumberOfActiveRoles":12,"totalNumberOfInactiveRoles":0,"totalUsersWithDashboardAccess":0,"totalUsersWithUserManagementAccess":0,"totalUsersWithRoleManagementAccess":0,"usersByParentAuthorityCountVoList":[{"label":"إدارة الأدوار","labelNumber":0,"count":1},{"label":"إدارة المستخدمين","labelNumber":0,"count":2},{"label":"إدارة المعرفات الرقمية","labelNumber":0,"count":1},{"label":"لوحة المعلومات","labelNumber":0,"count":2}],"createdUsersCountVoList":[],"activeUsersCountVoList":[],"inactiveUsersCountVoList":[],"deletedUsersCountVoList":[]}'},
    {url: '/api/dashboard/period/D', data: '{"totalNumberOfUsers":0,"totalNumberOfCitizenActiveUsers":0,"totalNumberOfResidentActiveUsers":0,"totalNumberOfActiveUsers":0,"totalNumberOfInactiveUsers":0,"totalNumberOfDeletedUsers":0,"totalNumberOfRoles":0,"totalNumberOfActiveRoles":0,"totalNumberOfInactiveRoles":0,"totalUsersWithDashboardAccess":0,"totalUsersWithUserManagementAccess":0,"totalUsersWithRoleManagementAccess":0,"usersByParentAuthorityCountVoList":null,"createdUsersCountVoList":[],"activeUsersCountVoList":[],"inactiveUsersCountVoList":[],"deletedUsersCountVoList":[]}'},
    {url: '/api/users/list', data: '{"content":[{"id":2,"dateOfBirthGregorian":510440400000,"dateOfBirthHijri":14060625,"email":"a@elm.sa","familyName":"فليفل","firstName":"شادي","gender":"M","grandFatherName":"","fatherName":"","lastLoginDate":1616395620000,"mobileNumber":550000000,"nin":1234567896,"avatarFile":null,"avatar":null,"numberOfTries":0,"password":"<CONFIDENTIAL>","passwordHash":"<CONFIDENTIAL>","preferredLanguage":null,"updateDate":1616395620000,"deleted":false,"activated":true,"blockDate":null,"blocked":false,"changePasswordRequired":false,"creationDate":1616395380000,"userRoles":[],"tokenExpiryDate":null},{"id":3,"dateOfBirthGregorian":794091600000,"dateOfBirthHijri":14151001,"email":"ahmad@elm.sa","familyName":"flaifel","firstName":"ahmad","gender":"M","grandFatherName":"","fatherName":"","lastLoginDate":null,"mobileNumber":555555555,"nin":1234555555,"avatarFile":null,"avatar":null,"numberOfTries":0,"password":"<CONFIDENTIAL>","passwordHash":"<CONFIDENTIAL>","preferredLanguage":null,"updateDate":null,"deleted":false,"activated":true,"blockDate":null,"blocked":false,"changePasswordRequired":true,"creationDate":1616579100000,"userRoles":[],"tokenExpiryDate":null},{"id":4,"dateOfBirthGregorian":826146000000,"dateOfBirthHijri":14161018,"email":"samer@elm.sa","familyName":"momani","firstName":"samer","gender":"M","grandFatherName":"","fatherName":"","lastLoginDate":null,"mobileNumber":575444444,"nin":1235478888,"avatarFile":null,"avatar":null,"numberOfTries":0,"password":"<CONFIDENTIAL>","passwordHash":"<CONFIDENTIAL>","preferredLanguage":null,"updateDate":null,"deleted":false,"activated":true,"blockDate":null,"blocked":false,"changePasswordRequired":true,"creationDate":1616579400000,"userRoles":[],"tokenExpiryDate":null},{"id":13,"dateOfBirthGregorian":478990800000,"dateOfBirthHijri":14050616,"email":"ah@elm.sa","familyName":"amin","firstName":"ahmad","gender":"M","grandFatherName":"","fatherName":"","lastLoginDate":null,"mobileNumber":555555555,"nin":1222222222,"avatarFile":null,"avatar":null,"numberOfTries":0,"password":"<CONFIDENTIAL>","passwordHash":"<CONFIDENTIAL>","preferredLanguage":null,"updateDate":null,"deleted":false,"activated":true,"blockDate":null,"blocked":false,"changePasswordRequired":true,"creationDate":1616667420000,"userRoles":[{"id":10,"role":{"id":3,"nameArabic":"مشرف التسجيل","nameEnglish":"Enrollment Officer Supervisor","deleted":false,"activated":true,"roleAuthorities":[{"id":24,"authority":{"id":16,"nameArabic":"تعديل معلومات ضيف الرحمن","nameEnglish":"Update Applicant Profile","code":"UPDATE_APPLICANT_PROFILE","creationDate":1616394300000,"parentId":14,"children":[]},"creationDate":1616394300000},{"id":20,"authority":{"id":6,"nameArabic":"إعادة تعيين كلمة المرور","nameEnglish":"Reset Password","code":"RESET_PASSWORD","creationDate":1616394300000,"parentId":2,"children":[]},"creationDate":1616394300000},{"id":21,"authority":{"id":7,"nameArabic":"حذف مستخدم","nameEnglish":"Delete User","code":"DELETE_USER","creationDate":1616394300000,"parentId":2,"children":[]},"creationDate":1616394300000},{"id":23,"authority":{"id":15,"nameArabic":"عرض معلومات ضيف الرحمن","nameEnglish":"View Applicant Profile","code":"VIEW_APPLICANT_PROFILE","creationDate":1616394300000,"parentId":14,"children":[]},"creationDate":1616394300000},{"id":29,"authority":{"id":31,"nameArabic":"تعديل الملف الشخصي","nameEnglish":"Update My Profile","code":"UPDATE_MY_PROFILE","creationDate":1616394300000,"parentId":2,"children":[]},"creationDate":1616394300000},{"id":19,"authority":{"id":5,"nameArabic":"تغيير حالة مستخدم","nameEnglish":"Change User Status","code":"CHANGE_USER_STATUS","creationDate":1616394300000,"parentId":2,"children":[]},"creationDate":1616394300000},{"id":16,"authority":{"id":2,"nameArabic":"إدارة المستخدمين","nameEnglish":"User Management","code":"USER_MANAGEMENT","creationDate":1616394300000,"parentId":null,"children":[{"id":30,"nameArabic":"عرض الملف الشخصي","nameEnglish":"View My Profile","code":"VIEW_MY_PROFILE","creationDate":1616394300000,"parentId":2,"children":[]},{"id":13,"nameArabic":"إعادة تعيين كلمة مرور المستخدم","nameEnglish":"Reset User Password","code":"RESET_USER_PASSWORD","creationDate":1616394300000,"parentId":2,"children":[]},{"id":7,"nameArabic":"حذف مستخدم","nameEnglish":"Delete User","code":"DELETE_USER","creationDate":1616394300000,"parentId":2,"children":[]},{"id":3,"nameArabic":"إضافة مستخدم","nameEnglish":"Add User","code":"ADD_USER","creationDate":1616394300000,"parentId":2,"children":[]},{"id":6,"nameArabic":"إعادة تعيين كلمة المرور","nameEnglish":"Reset Password","code":"RESET_PASSWORD","creationDate":1616394300000,"parentId":2,"children":[]},{"id":4,"nameArabic":"تعديل مستخدم","nameEnglish":"Edit User","code":"EDIT_USER","creationDate":1616394300000,"parentId":2,"children":[]},{"id":31,"nameArabic":"تعديل الملف الشخصي","nameEnglish":"Update My Profile","code":"UPDATE_MY_PROFILE","creationDate":1616394300000,"parentId":2,"children":[]},{"id":5,"nameArabic":"تغيير حالة مستخدم","nameEnglish":"Change User Status","code":"CHANGE_USER_STATUS","creationDate":1616394300000,"parentId":2,"children":[]}]},"creationDate":1616394300000},{"id":27,"authority":{"id":19,"nameArabic":"إيقاف بطاقة تعريف ضيف الرحمن","nameEnglish":"Suspend Applicant Card","code":"SUSPEND_APPLICANT_CARD","creationDate":1616394300000,"parentId":14,"children":[]},"creationDate":1616394300000},{"id":15,"authority":{"id":1,"nameArabic":"لوحة المعلومات","nameEnglish":"Admin Dashboard","code":"ADMIN_DASHBOARD","creationDate":1616394300000,"parentId":null,"children":[]},"creationDate":1616394300000},{"id":26,"authority":{"id":18,"nameArabic":"إلغاء بطاقة تعريف ضيف الرحمن","nameEnglish":"Cancel Applicant Card","code":"CANCEL_APPLICANT_CARD","creationDate":1616394300000,"parentId":14,"children":[]},"creationDate":1616394300000},{"id":22,"authority":{"id":14,"nameArabic":"إدارة المعرفات الرقمية","nameEnglish":"Manage Digital ID","code":"MANAGE_DIGITAL_ID","creationDate":1616394300000,"parentId":null,"children":[{"id":22,"nameArabic":"إنشاء معلومات ضيف الرحمن","nameEnglish":"Add Applicant Profile","code":"ADD_APPLICANT_PROFILE","creationDate":1616394300000,"parentId":14,"children":[]},{"id":16,"nameArabic":"تعديل معلومات ضيف الرحمن","nameEnglish":"Update Applicant Profile","code":"UPDATE_APPLICANT_PROFILE","creationDate":1616394300000,"parentId":14,"children":[]},{"id":15,"nameArabic":"عرض معلومات ضيف الرحمن","nameEnglish":"View Applicant Profile","code":"VIEW_APPLICANT_PROFILE","creationDate":1616394300000,"parentId":14,"children":[]},{"id":20,"nameArabic":"إعادة إصدار بطاقة تعريف ضيف الرحمن","nameEnglish":"Reissue Applicant Card","code":"REISSUE_APPLICANT_CARD","creationDate":1616394300000,"parentId":14,"children":[]},{"id":21,"nameArabic":"إضافة رقم تعريف ضيف الرحمن","nameEnglish":"Add Digital ID","code":"ADD_DIGITAL_ID","creationDate":1616394300000,"parentId":14,"children":[]},{"id":17,"nameArabic":"تفعيل بطاقة تعريف ضيف الرحمن","nameEnglish":"Activate Applicant Card","code":"ACTIVATE_APPLICANT_CARD","creationDate":1616394300000,"parentId":14,"children":[]},{"id":18,"nameArabic":"إلغاء بطاقة تعريف ضيف الرحمن","nameEnglish":"Cancel Applicant Card","code":"CANCEL_APPLICANT_CARD","creationDate":1616394300000,"parentId":14,"children":[]},{"id":19,"nameArabic":"إيقاف بطاقة تعريف ضيف الرحمن","nameEnglish":"Suspend Applicant Card","code":"SUSPEND_APPLICANT_CARD","creationDate":1616394300000,"parentId":14,"children":[]},{"id":23,"nameArabic":"موافقة إنشاء معلومات ضيف الرحمن","nameEnglish":"Approve Applicant Profile","code":"APPROVE_APPLICANT_PROFILE","creationDate":1616394300000,"parentId":14,"children":[]}]},"creationDate":1616394300000},{"id":17,"authority":{"id":3,"nameArabic":"إضافة مستخدم","nameEnglish":"Add User","code":"ADD_USER","creationDate":1616394300000,"parentId":2,"children":[]},"creationDate":1616394300000},{"id":18,"authority":{"id":4,"nameArabic":"تعديل مستخدم","nameEnglish":"Edit User","code":"EDIT_USER","creationDate":1616394300000,"parentId":2,"children":[]},"creationDate":1616394300000},{"id":28,"authority":{"id":30,"nameArabic":"عرض الملف الشخصي","nameEnglish":"View My Profile","code":"VIEW_MY_PROFILE","creationDate":1616394300000,"parentId":2,"children":[]},"creationDate":1616394300000},{"id":25,"authority":{"id":17,"nameArabic":"تفعيل بطاقة تعريف ضيف الرحمن","nameEnglish":"Activate Applicant Card","code":"ACTIVATE_APPLICANT_CARD","creationDate":1616394300000,"parentId":14,"children":[]},"creationDate":1616394300000}],"creationDate":1616394300000,"updateDate":null},"mainRole":false,"creationDate":1616667420000}],"tokenExpiryDate":null},{"id":15,"dateOfBirthGregorian":541976400000,"dateOfBirthHijri":14070706,"email":"ay@elm.sa","familyName":"dhaoui","firstName":"ayman","gender":"M","grandFatherName":"","fatherName":"","lastLoginDate":null,"mobileNumber":555555555,"nin":1234777888,"avatarFile":null,"avatar":null,"numberOfTries":0,"password":"<CONFIDENTIAL>","passwordHash":"<CONFIDENTIAL>","preferredLanguage":null,"updateDate":null,"deleted":false,"activated":true,"blockDate":null,"blocked":false,"changePasswordRequired":true,"creationDate":1616681160000,"userRoles":[{"id":13,"role":{"id":9,"nameArabic":"مشرف الحملة","nameEnglish":"Hamlah Owner","deleted":false,"activated":true,"roleAuthorities":[{"id":99,"authority":{"id":15,"nameArabic":"عرض معلومات ضيف الرحمن","nameEnglish":"View Applicant Profile","code":"VIEW_APPLICANT_PROFILE","creationDate":1616394300000,"parentId":14,"children":[]},"creationDate":1616394300000},{"id":104,"authority":{"id":32,"nameArabic":"تسجيل حملة","nameEnglish":"Register Hamlah","code":"REGISTER_HAMLAH","creationDate":1616394300000,"parentId":null,"children":[{"id":33,"nameArabic":"استعلام طلب تسجيل حملة","nameEnglish":"Enquiry Hamlah Registration","code":"ENQUIRY_HAMLAH_REGISTRATION","creationDate":1616394300000,"parentId":32,"children":[]}]},"creationDate":1616394300000},{"id":93,"authority":{"id":3,"nameArabic":"إضافة مستخدم","nameEnglish":"Add User","code":"ADD_USER","creationDate":1616394300000,"parentId":2,"children":[]},"creationDate":1616394300000},{"id":98,"authority":{"id":14,"nameArabic":"إدارة المعرفات الرقمية","nameEnglish":"Manage Digital ID","code":"MANAGE_DIGITAL_ID","creationDate":1616394300000,"parentId":null,"children":[{"id":22,"nameArabic":"إنشاء معلومات ضيف الرحمن","nameEnglish":"Add Applicant Profile","code":"ADD_APPLICANT_PROFILE","creationDate":1616394300000,"parentId":14,"children":[]},{"id":16,"nameArabic":"تعديل معلومات ضيف الرحمن","nameEnglish":"Update Applicant Profile","code":"UPDATE_APPLICANT_PROFILE","creationDate":1616394300000,"parentId":14,"children":[]},{"id":15,"nameArabic":"عرض معلومات ضيف الرحمن","nameEnglish":"View Applicant Profile","code":"VIEW_APPLICANT_PROFILE","creationDate":1616394300000,"parentId":14,"children":[]},{"id":20,"nameArabic":"إعادة إصدار بطاقة تعريف ضيف الرحمن","nameEnglish":"Reissue Applicant Card","code":"REISSUE_APPLICANT_CARD","creationDate":1616394300000,"parentId":14,"children":[]},{"id":21,"nameArabic":"إضافة رقم تعريف ضيف الرحمن","nameEnglish":"Add Digital ID","code":"ADD_DIGITAL_ID","creationDate":1616394300000,"parentId":14,"children":[]},{"id":17,"nameArabic":"تفعيل بطاقة تعريف ضيف الرحمن","nameEnglish":"Activate Applicant Card","code":"ACTIVATE_APPLICANT_CARD","creationDate":1616394300000,"parentId":14,"children":[]},{"id":18,"nameArabic":"إلغاء بطاقة تعريف ضيف الرحمن","nameEnglish":"Cancel Applicant Card","code":"CANCEL_APPLICANT_CARD","creationDate":1616394300000,"parentId":14,"children":[]},{"id":19,"nameArabic":"إيقاف بطاقة تعريف ضيف الرحمن","nameEnglish":"Suspend Applicant Card","code":"SUSPEND_APPLICANT_CARD","creationDate":1616394300000,"parentId":14,"children":[]},{"id":23,"nameArabic":"موافقة إنشاء معلومات ضيف الرحمن","nameEnglish":"Approve Applicant Profile","code":"APPROVE_APPLICANT_PROFILE","creationDate":1616394300000,"parentId":14,"children":[]}]},"creationDate":1616394300000},{"id":102,"authority":{"id":30,"nameArabic":"عرض الملف الشخصي","nameEnglish":"View My Profile","code":"VIEW_MY_PROFILE","creationDate":1616394300000,"parentId":2,"children":[]},"creationDate":1616394300000},{"id":92,"authority":{"id":2,"nameArabic":"إدارة المستخدمين","nameEnglish":"User Management","code":"USER_MANAGEMENT","creationDate":1616394300000,"parentId":null,"children":[{"id":30,"nameArabic":"عرض الملف الشخصي","nameEnglish":"View My Profile","code":"VIEW_MY_PROFILE","creationDate":1616394300000,"parentId":2,"children":[]},{"id":13,"nameArabic":"إعادة تعيين كلمة مرور المستخدم","nameEnglish":"Reset User Password","code":"RESET_USER_PASSWORD","creationDate":1616394300000,"parentId":2,"children":[]},{"id":7,"nameArabic":"حذف مستخدم","nameEnglish":"Delete User","code":"DELETE_USER","creationDate":1616394300000,"parentId":2,"children":[]},{"id":3,"nameArabic":"إضافة مستخدم","nameEnglish":"Add User","code":"ADD_USER","creationDate":1616394300000,"parentId":2,"children":[]},{"id":6,"nameArabic":"إعادة تعيين كلمة المرور","nameEnglish":"Reset Password","code":"RESET_PASSWORD","creationDate":1616394300000,"parentId":2,"children":[]},{"id":4,"nameArabic":"تعديل مستخدم","nameEnglish":"Edit User","code":"EDIT_USER","creationDate":1616394300000,"parentId":2,"children":[]},{"id":31,"nameArabic":"تعديل الملف الشخصي","nameEnglish":"Update My Profile","code":"UPDATE_MY_PROFILE","creationDate":1616394300000,"parentId":2,"children":[]},{"id":5,"nameArabic":"تغيير حالة مستخدم","nameEnglish":"Change User Status","code":"CHANGE_USER_STATUS","creationDate":1616394300000,"parentId":2,"children":[]}]},"creationDate":1616394300000},{"id":95,"authority":{"id":5,"nameArabic":"تغيير حالة مستخدم","nameEnglish":"Change User Status","code":"CHANGE_USER_STATUS","creationDate":1616394300000,"parentId":2,"children":[]},"creationDate":1616394300000},{"id":97,"authority":{"id":7,"nameArabic":"حذف مستخدم","nameEnglish":"Delete User","code":"DELETE_USER","creationDate":1616394300000,"parentId":2,"children":[]},"creationDate":1616394300000},{"id":91,"authority":{"id":1,"nameArabic":"لوحة المعلومات","nameEnglish":"Admin Dashboard","code":"ADMIN_DASHBOARD","creationDate":1616394300000,"parentId":null,"children":[]},"creationDate":1616394300000},{"id":96,"authority":{"id":6,"nameArabic":"إعادة تعيين كلمة المرور","nameEnglish":"Reset Password","code":"RESET_PASSWORD","creationDate":1616394300000,"parentId":2,"children":[]},"creationDate":1616394300000},{"id":105,"authority":{"id":33,"nameArabic":"استعلام طلب تسجيل حملة","nameEnglish":"Enquiry Hamlah Registration","code":"ENQUIRY_HAMLAH_REGISTRATION","creationDate":1616394300000,"parentId":32,"children":[]},"creationDate":1616394300000},{"id":100,"authority":{"id":16,"nameArabic":"تعديل معلومات ضيف الرحمن","nameEnglish":"Update Applicant Profile","code":"UPDATE_APPLICANT_PROFILE","creationDate":1616394300000,"parentId":14,"children":[]},"creationDate":1616394300000},{"id":103,"authority":{"id":31,"nameArabic":"تعديل الملف الشخصي","nameEnglish":"Update My Profile","code":"UPDATE_MY_PROFILE","creationDate":1616394300000,"parentId":2,"children":[]},"creationDate":1616394300000},{"id":94,"authority":{"id":4,"nameArabic":"تعديل مستخدم","nameEnglish":"Edit User","code":"EDIT_USER","creationDate":1616394300000,"parentId":2,"children":[]},"creationDate":1616394300000},{"id":101,"authority":{"id":17,"nameArabic":"تفعيل بطاقة تعريف ضيف الرحمن","nameEnglish":"Activate Applicant Card","code":"ACTIVATE_APPLICANT_CARD","creationDate":1616394300000,"parentId":14,"children":[]},"creationDate":1616394300000}],"creationDate":1616394300000,"updateDate":null},"mainRole":false,"creationDate":1616681160000},{"id":14,"role":{"id":8,"nameArabic":"موظف الطباعة","nameEnglish":"Printing User","deleted":false,"activated":true,"roleAuthorities":[{"id":87,"authority":{"id":25,"nameArabic":"عرض تفاصيل طلب الطباعة","nameEnglish":"View Printing Request Details","code":"VIEW_PRINTING_REQUEST_DETAILS","creationDate":1616394300000,"parentId":24,"children":[]},"creationDate":1616394300000},{"id":85,"authority":{"id":6,"nameArabic":"إعادة تعيين كلمة المرور","nameEnglish":"Reset Password","code":"RESET_PASSWORD","creationDate":1616394300000,"parentId":2,"children":[]},"creationDate":1616394300000},{"id":90,"authority":{"id":31,"nameArabic":"تعديل الملف الشخصي","nameEnglish":"Update My Profile","code":"UPDATE_MY_PROFILE","creationDate":1616394300000,"parentId":2,"children":[]},"creationDate":1616394300000},{"id":84,"authority":{"id":1,"nameArabic":"لوحة المعلومات","nameEnglish":"Admin Dashboard","code":"ADMIN_DASHBOARD","creationDate":1616394300000,"parentId":null,"children":[]},"creationDate":1616394300000},{"id":88,"authority":{"id":26,"nameArabic":"إضافة طلب طباعة","nameEnglish":"Add Printing Request","code":"ADD_PRINTING_REQUEST","creationDate":1616394300000,"parentId":24,"children":[]},"creationDate":1616394300000},{"id":89,"authority":{"id":30,"nameArabic":"عرض الملف الشخصي","nameEnglish":"View My Profile","code":"VIEW_MY_PROFILE","creationDate":1616394300000,"parentId":2,"children":[]},"creationDate":1616394300000},{"id":86,"authority":{"id":24,"nameArabic":"إدارة طلبات الطباعة","nameEnglish":"Manage Printing Requests","code":"MANAGE_PRINTING_REQUEST","creationDate":1616394300000,"parentId":null,"children":[{"id":26,"nameArabic":"إضافة طلب طباعة","nameEnglish":"Add Printing Request","code":"ADD_PRINTING_REQUEST","creationDate":1616394300000,"parentId":24,"children":[]},{"id":25,"nameArabic":"عرض تفاصيل طلب الطباعة","nameEnglish":"View Printing Request Details","code":"VIEW_PRINTING_REQUEST_DETAILS","creationDate":1616394300000,"parentId":24,"children":[]}]},"creationDate":1616394300000}],"creationDate":1616394300000,"updateDate":null},"mainRole":false,"creationDate":1616681160000}],"tokenExpiryDate":null}],"pageable":{"sort":{"sorted":false,"unsorted":true,"empty":true},"offset":0,"pageSize":20,"pageNumber":0,"paged":true,"unpaged":false},"totalElements":5,"totalPages":1,"last":true,"size":20,"number":0,"sort":{"sorted":false,"unsorted":true,"empty":true},"numberOfElements":5,"first":true,"empty":false}'},
    {url: '/api/users/find/2', data: '{"id":2,"dateOfBirthGregorian":510440400000,"dateOfBirthHijri":14060625,"email":"a@elm.sa","familyName":"فليفل","firstName":"شادي","gender":"M","grandFatherName":"","fatherName":"","lastLoginDate":1616395620000,"mobileNumber":550000000,"nin":1234567896,"avatarFile":null,"avatar":null,"numberOfTries":0,"password":"<CONFIDENTIAL>","passwordHash":"<CONFIDENTIAL>","preferredLanguage":null,"updateDate":1616395620000,"deleted":false,"activated":true,"blockDate":null,"blocked":false,"changePasswordRequired":false,"creationDate":1616395380000,"userRoles":[],"tokenExpiryDate":null}'},
  ]
};
