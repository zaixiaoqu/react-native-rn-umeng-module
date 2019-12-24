//
//  pushListener.m
//  szlanlingtong
//
//  Created by juddy on 2019/6/13.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import "pushListener.h"
static NSString * const recivePushNoti = @"recivePushNoti";
static NSString * const openPushNoti = @"openPushNoti";
static NSString * const DidReceiveMessage = @"didReceiveMessage";
static NSString * const DidOpenMessage = @"didOpenMessage";
// 如果通知已经注册了，告诉系统，我的通知已经注册 (注册一个全局变量来保存时间注册状态)
static BOOL IsAddObserverUmengPushEvent = NO;
// 最后push的信息(还未被RN读取到的信息)
static NSDictionary * LastPushWaitMessage = NULL;

@implementation pushListener
{
  bool _hasListeners;
}
RCT_EXPORT_MODULE();
//rn代理
- (NSArray<NSString *> *)supportedEvents
{
  return @[DidReceiveMessage,DidOpenMessage];
}

-(instancetype)init{
  self = [super init];
  if (self) {
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(didReceivNoti:)
                                                 name:recivePushNoti
                                               object:nil];
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(openMsg:) name:openPushNoti object:nil];
    // 如果通知已经注册了，告诉系统，我的通知已经注册
    [pushListener setIsAddObserver:YES];
  }
  return self;
}

// 设置RN通知事件是否已经注册
+ (void)setIsAddObserver:(BOOL)isAddObserver
{
    IsAddObserverUmengPushEvent = isAddObserver;
}
// 读取RN通知事件是否已经注册
+ (BOOL)getIsAddObserver
{
    return IsAddObserverUmengPushEvent;
}

// 设置最后push的信息(还未被RN读取到的信息)
+ (void)setLastPushWaitMessage:(NSDictionary *)msg
{
    LastPushWaitMessage = msg;
}
// q读取最后push的信息(还未被RN读取到的信息)
+ (NSDictionary *)getLastPushWaitMessage
{
    return LastPushWaitMessage;
}


+ (BOOL)requiresMainQueueSetup
{
    return YES;
}

- (void)startObserving
{
  _hasListeners = YES;
}
- (void)stopObserving
{
  
  _hasListeners = NO;
}

-(void)didReceivNoti:(NSNotification *)notification{
  
  if (_hasListeners) {
    [self sendEventWithName:DidReceiveMessage
                       body:notification.userInfo];
  }
}

-(void)openMsg:(NSNotification *)notification{
  
  if (_hasListeners) {
    [self sendEventWithName:DidOpenMessage body:notification.userInfo];
  }
}

+(BOOL)runPushWaitMessage {
    NSDictionary *waitMessage = [pushListener getLastPushWaitMessage];
    if (nil == waitMessage || NULL == waitMessage || [waitMessage isEqual:[NSNull null]]) {
        return NO;
    }
    @try {
        [pushListener setIsAddObserver:YES];
        //关闭友盟自带的弹出框
        [UMessage setAutoAlert:NO];
        [UMessage didReceiveRemoteNotification:waitMessage];
        [[NSNotificationCenter defaultCenter]postNotificationName:openPushNoti object:nil userInfo:waitMessage];
        [pushListener setLastPushWaitMessage:NULL];
        return YES;
    } @catch (NSException *exception) {
    } @finally {
    }
    return NO;
}

@end
