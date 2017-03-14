//
//  SimpleMap.m
//  test
//
//  Created by Nina Manalo on 12/03/2017.
//  Copyright © 2017 Facebook. All rights reserved.
//

#import "SimpleMap.h"

@implementation SimpleMap

- (id)init {
  if (self = [super init]) {
    locationManager = [[CLLocationManager alloc] init];
    allMarkers = [[NSMutableArray alloc] init];
    locationManager.delegate = self;
    self.delegate = self;
    [locationManager requestAlwaysAuthorization];
    [locationManager startUpdatingLocation];
  }
  return self;
}

-(void)mapView:(MKMapView *)mapView didUpdateUserLocation:(MKUserLocation *)userLocation {
}

- (void)setShowUserLocation:(BOOL*)showUserLocation {
  NSLog(@"setShowUserLocation() -- showUserLocation=%@", showUserLocation ? @"YES" : @"NO");
  self.showsUserLocation = showUserLocation;
}

- (void)setMarkers:(NSArray *)markers {
  NSLog(@"setMarkers() -- markers=%@", markers);
  for (int i = 0; i < [markers count]; i++) {
    NSDictionary *marker = [markers objectAtIndex:i];
    NSNumber *lat = [marker valueForKey:@"lat"];
    NSNumber *lng = [marker valueForKey:@"lng"];
    NSString *title = [marker valueForKey:@"id"];
    
    MKPointAnnotation *annotation = [[MKPointAnnotation alloc] init];
    CLLocationCoordinate2D loc1;
    loc1.latitude = (CLLocationDegrees)[lat doubleValue];
    loc1.longitude = (CLLocationDegrees)[lng doubleValue];;
    [annotation setTitle:title];
    [annotation setCoordinate:loc1];
    [self addAnnotation:annotation];
    [allMarkers addObject:marker];
  }
  
}

-(MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id<MKAnnotation>)annotation {
    NSLog(@"viewForAnnotation() -- all markers size=%lu", (unsigned long)[allMarkers count]);
    NSString *annotationTitle = [annotation title];
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"%K == %@", @"id", annotationTitle];
    NSArray *filteredMarkers = [allMarkers filteredArrayUsingPredicate:predicate];
    NSLog(@"viewForAnnotation() -- filteredMarkers=%@", filteredMarkers);
    MKAnnotationView *pinView = nil;
    if ([filteredMarkers count] > 0) {
        NSDictionary *marker = [filteredMarkers objectAtIndex:0];
        NSString *markerTitle = [marker valueForKey:@"id"];
        NSString *markerIcon = [marker valueForKey:@"icon"];
        
        
        NSLog(@"viewForAnnotation() -- title=%@", markerTitle);
        if (annotation != mapView.userLocation || [annotationTitle isEqualToString:markerTitle]) {
            pinView = [[MKAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:@"CustomPin"];
            NSURL *imageURL = [[NSURL alloc] initWithString:markerIcon];
            [pinView sd_setImageWithURL:imageURL completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
            }];
        }
        
    }
    return pinView;
}

@end
