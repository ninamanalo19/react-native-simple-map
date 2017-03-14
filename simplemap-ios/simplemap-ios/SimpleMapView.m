//
//  SimpleMapView.m
//  test
//
//  Created by Nina Manalo on 11/03/2017.
//  Copyright © 2017 Facebook. All rights reserved.
//

#import "SimpleMapView.h"

@implementation SimpleMapView

RCT_EXPORT_MODULE()

- (UIView *)view {
  SimpleMap *map = [[SimpleMap alloc] init];
  return map;
}

RCT_EXPORT_VIEW_PROPERTY(showUserLocation, BOOL)
RCT_EXPORT_VIEW_PROPERTY(markers, NSDictionaryArray);




@end
