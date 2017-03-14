//
//  SimpleMap.h
//  test
//
//  Created by Nina Manalo on 12/03/2017.
//  Copyright © 2017 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <MapKit/MapKit.h>
#import <SDWebImage/MKAnnotationView+WebCache.h>

@class RCTEventDispatcher;

@interface SimpleMap : MKMapView<CLLocationManagerDelegate, MKMapViewDelegate> {
  CLLocationManager *locationManager;
  NSMutableArray *allMarkers;
}

@property (nonatomic) BOOL *showUserLocation;
@property (nonatomic, copy) NSArray *markers;

@end
