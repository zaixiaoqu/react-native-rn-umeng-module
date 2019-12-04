//
//  pushListener.h
//  szlanlingtong
//
//  Created by juddy on 2019/6/13.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>
#import <UMPush/UMessage.h>

@interface pushListener : RCTEventEmitter<RCTBridgeModule>

+ (void)setIsAddObserver:(BOOL)isAddObserver;
+ (BOOL)getIsAddObserver;
+ (void)setLastPushWaitMessage:(NSDictionary *)msg;
+ (NSDictionary *)getLastPushWaitMessage;

@end
