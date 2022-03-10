export class AppConst {

// bbb
//   public static SERVER_PATH = 'https://mfaqa.bedbath.com/sso';
  public static SERVER_PATH = 'http://localhost:8080/sso';
//   public static SERVER_PATH = 'http://172.24.160.53:8080/sso';

  public static LOGIN_PATH = 'https://ssoqa.bedbath.com/idp/startSSO.ping?PartnerSpId=https%3A%2F%2Fssoqa.bedbath.com';


  public static JWT_STORAGE_NAME = 'SSPORTAL_JWT_TOKEN'
  public static APP_USER_STORAGE_NAME = 'SSPORTAL_APP_USER'
  public static APP_ADMIN_STORAGE_NAME = 'SSPORTAL_APP_ADMIN'

  public static MAIL_TASK_selfPair = "pairdeviceself"
  public static MAIL_TASK_selfUnPair = "unpairdeviceself"
  public static MAIL_TASK_helpDeskPair = "pairdevice"
  public static MAIL_TASK_helpDeskUnPair = "unpairdevice"
  public static MAIL_TASK_bypass = "bypass"
  public static MAIL_TASK_enable = "enable"
  public static MAIL_TASK_disable = "disable"

}
