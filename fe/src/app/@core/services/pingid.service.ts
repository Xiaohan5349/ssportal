import { Injectable } from '@angular/core';
import {AppConst} from "../utils/app-const";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class PingIdService {
  BACKEND_URL: string = AppConst.SERVER_PATH;

  constructor(
    private http: HttpClient
  ) { }

  unpairDevice(deviceId, username) {
    const url = this.BACKEND_URL + '/pingid/unpairDevice';
    const payload = {
      deviceId: deviceId,
      username: username
    }
    const httpOptions = {
      headers: new HttpHeaders({'Content-Type': 'application/json'})
    };
    return this.http.post(url, payload, httpOptions);
  }

  getActivationCode(type, username) {
    const url = this.BACKEND_URL + '/pingid/getActivationCode';
    const payload = {
      type: type,
      username: username

    }
    const httpOptions = {
      headers: new HttpHeaders({'Content-Type': 'application/json'})
    };
    return this.http.post(url, payload, httpOptions);
  }

  AuthenticatorAppStartPairing(username) {
    const url = this.BACKEND_URL + '/pingid/AuthenticatorAppStartPairing';
    const payload = {
      username: username

    }
    const httpOptions = {
     headers: new HttpHeaders({'Content-Type': 'application/json'})
    };
    return this.http.post(url, payload, httpOptions);
  }

  AuthenticatorAppFinishPairing(sessionId, otp) {
    const url = this.BACKEND_URL + '/pingid/AuthenticatorAppFinishPairing';
    const payload = {
      sessionId: sessionId,
      otp: otp

    }
    const httpOptions = {
     headers: new HttpHeaders({'Content-Type': 'application/json'})
    };
    return this.http.post(url, payload, httpOptions);
  }

  yubikeyPairing(username, otp) {
    const url = this.BACKEND_URL + '/pingid/pairYubiKey';
    const payload = {
      username: username,
      otp: otp

    }
    const httpOptions = {
     headers: new HttpHeaders({'Content-Type': 'application/json'})
    };
    return this.http.post(url, payload, httpOptions);
  }


  checkPairingStatus(activationCode, username) {
    const url = this.BACKEND_URL + '/pingid/getPairingStatus';
    const payload = {
      activationCode: activationCode,
      username: username

    }
    const httpOptions = {
      headers: new HttpHeaders({'Content-Type': 'application/json'})
    };
    return this.http.post(url, payload, httpOptions);
  }

  testMFA(deviceId, username) {
    const url = this.BACKEND_URL + '/pingid/authenticateOnline';
    const payload = {
      deviceId: deviceId,
      username: username

    }
    const httpOptions = {
      headers: new HttpHeaders({'Content-Type': 'application/json'})
    };
    return this.http.post(url, payload, httpOptions);
  }

  makeDevicePrimary(deviceId, username) {
    const url = this.BACKEND_URL + '/pingid/makeDevicePrimary';
    const payload = {
      deviceId: deviceId,
      username: username

    }
    const httpOptions = {
      headers: new HttpHeaders({'Content-Type': 'application/json'})
    };
    return this.http.post(url, payload, httpOptions);
  }

  testWebMFA(deviceId, username) {
    const url = this.BACKEND_URL + '/pingid/webAuthnStartAuth';
    const payload = {
      deviceId: deviceId,
      username: username
    }
    const httpOptions = {
      headers: new HttpHeaders({'Content-Type': 'application/json'})
    };
    return this.http.post(url, payload, httpOptions);
  }

  ToggleUserBypass(username) {
    const url = this.BACKEND_URL + '/pingid/ToggleUserBypass';
    const payload = {
      username: username

    }
    const httpOptions = {
     headers: new HttpHeaders({'Content-Type': 'application/json'})
    };
    return this.http.post(url, payload, httpOptions);
  }

  SuspendUser(username) {
    const url = this.BACKEND_URL + '/pingid/SuspendUser';
    const payload = {
      username: username

    }
    const httpOptions = {
     headers: new HttpHeaders({'Content-Type': 'application/json'})
    };
    return this.http.post(url, payload, httpOptions);
  }

  ActivateUser(username) {
    const url = this.BACKEND_URL + '/pingid/ActivateUser';
    const payload = {
      username: username

    }
    const httpOptions = {
     headers: new HttpHeaders({'Content-Type': 'application/json'})
    };
    return this.http.post(url, payload, httpOptions);
  }

  startOfflineAuth(deviceId,username) {
    const url = this.BACKEND_URL + '/pingid/startOfflineAuth';
    const payload = {
      deviceId: deviceId,
      username: username

    }
    const httpOptions = {
     headers: new HttpHeaders({'Content-Type': 'application/json'})
    };
    return this.http.post(url, payload, httpOptions);

  }

  finalOfflineAuth(sessionId,username,otp) {
    const url = this.BACKEND_URL + '/pingid/finalOfflineAuth';
    const payload = {
      username: username,
      sessionId: sessionId,
      otp: otp

    }
    const httpOptions = {
     headers: new HttpHeaders({'Content-Type': 'application/json'})
    };
    return this.http.post(url, payload, httpOptions);

  }


}
