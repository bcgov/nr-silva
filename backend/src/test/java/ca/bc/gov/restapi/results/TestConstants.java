package ca.bc.gov.restapi.results;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestConstants {

  public static final String WFS_OPENING = """
        {
          "type": "FeatureCollection",
          "features": [
              {
                  "type": "Feature",
                  "id": "WHSE_FOREST_VEGETATION.RSLT_OPENING_SVW.fid1",
                  "geometry": {
                      "type": "Polygon",
                      "coordinates": [
                          [
                              [
                                  -125.26247,
                                  50.20025
                              ],
                              [
                                  -125.26242,
                                  50.2
                              ],
                              [
                                  -125.26236,
                                  50.19951
                              ]
                          ],
                          [
                              [
                                  -125.2619,
                                  50.20063
                              ],
                              [
                                  -125.26188,
                                  50.20064
                              ],
                              [
                                  -125.26184,
                                  50.20068
                              ],
                              [
                                  -125.26188,
                                  50.20064
                              ],
                              [
                                  -125.2619,
                                  50.20063
                              ]
                          ]
                      ]
                  },
                  "geometry_name": "GEOMETRY",
                  "properties": {
                      "OPENING_ID": 123,
                      "OPENING_CATEGORY_CODE": "FTML",
                      "OPENING_STATUS_CODE": "FG",
                      "REGION_CODE": "RWC",
                      "REGION_NAME": "West Coast Natural Resource Region",
                      "DISTRICT_CODE": "DCR",
                      "DISTRICT_NAME": "Campbell River Natural Resource District",
                      "CLIENT_NAME": "MY CORP",
                      "CLIENT_NUMBER": "00000001",
                      "OPENING_WHO_CREATED": "EXTSRV",
                      "OPENING_WHEN_CREATED": "2001-06-07Z",
                      "OPENING_WHO_UPDATED": "USRUPDAT",
                      "OPENING_WHEN_UPDATED": "2014-04-02Z",
                      "OBJECTID": 3905591
                  },
                  "bbox": [
                      -125.27047,
                      50.19608,
                      -125.25889,
                      50.20249
                  ]
              }
          ],
          "totalFeatures": 1,
          "numberMatched": 1,
          "numberReturned": 1,
          "timeStamp": "2025-02-19T00:00:22.712Z",
          "crs": {
              "type": "name",
              "properties": {
                  "name": "urn:ogc:def:crs:EPSG::4326"
              }
          },
          "bbox": [
              -125.27047,
              50.19608,
              -125.25889,
              50.20249
          ]
      }""";

}
