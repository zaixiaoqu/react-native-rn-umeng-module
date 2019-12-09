import {
  NativeModules,
  Platform,
  DeviceEventEmitter,
  NativeEventEmitter,
  AppState
} from 'react-native';

const PushUtil = {

  /**
   * 得到设备token
   */
  getDeviceToken() {
    return NativeModules.UMPushModule.getDeviceToken();
  },

  didReceiveMessage(callbackEvent = () => {}) {
    return new Promise((resolve, reject) => {
      this._addEventListener('didReceiveMessage', message => {
        //处于后台时，拦截收到的消息
        if (AppState.currentState === 'background') {
          return;
        }
        if (typeof  callbackEvent == 'function') {
          callbackEvent(message);
        }
        resolve(message);
      });
      NativeModules.UMPushModule.awakenWaitPush().then((s) => {
      });
    });
  },

  didOpenMessage(callbackEvent = () => {}) {
    return new Promise((resolve, reject) => {
      this._addEventListener('didOpenMessage', message => {
        if (typeof  callbackEvent == 'function') {
          callbackEvent(message);
        }
        resolve(message);
      });
      NativeModules.UMPushModule.awakenWaitPush().then((s) => {
      });
    });
  },

  _addEventListener(eventName, handler) {
    if (Platform.OS === 'android') {
      return (new NativeEventEmitter(NativeModules.UMPushModule)).addListener(eventName, event => {
        handler(event);
      });
    } else {
      return (new NativeEventEmitter(NativeModules.pushListener)).addListener(eventName, event => {
        handler(event);
      });
    }
  }
};
export default PushUtil;
