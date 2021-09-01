
// ROLE_USER - regular user
// ROLE_ADMIN - client admin
// ROLE_SYS_ADMIN - super admin

import {AppConst} from "./app/@core/helpers/app-const";

export const config = {
  apiUrl: AppConst.serverPath,
  // authRoles: {
  //   sa: ['SA'], // Only Super Admin has access
  //   admin: ['SA', 'Admin'], // Only SA & Admin has access
  //   editor: ['SA', 'Admin', 'Editor'], // Only SA & Admin & Editor has access
  //   user: ['SA', 'Admin', 'Editor', 'User'], // Only SA & Admin & Editor & User has access
  //   guest: ['SA', 'Admin', 'Editor', 'User', 'Guest'] // Everyone has access
  // }


  authRoles: {
    SYS_ADMIN: ['ROLE_SYS_ADMIN', 'ROLE_USER'], // Only Super Admin has access
    ADMIN: ['ROLE_ADMIN', 'ROLE_USER'], // Only SYS_ADMIN and ADMIN has access
    USER: ['ROLE_USER'], // Only SYS_ADMIN, ADMIN and USER has access
  }

  // authRoles: {
  //   SYS_ADMIN: ['ROLE_SYS_ADMIN'], // Only Super Admin has access
  //   ADMIN: ['ROLE_ADMIN'], // Only ADMIN has access
  //   USER: ['ROLE_User'], // Only USER has access
  // }
}
