import { NativeModules } from 'react-native';
import PushUtil from './lib/pushUtil';

const { RnUmengModule, UMShareModule, UMPushModule } = NativeModules;

export { PushUtil, UMShareModule, UMPushModule };
export default RnUmengModule;
