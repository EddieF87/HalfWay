package xyz.eddief.halfway.data.repository

import com.google.gson.Gson
import xyz.eddief.halfway.data.models.DistanceResult
import xyz.eddief.halfway.data.models.GeoCode
import xyz.eddief.halfway.data.models.NearbyPlacesResult

class MapsRepositoryMock : MapsRepository {

    override suspend fun getNearbyPlacesByType(
        location: String,
        placeToMeet: String,
        isKeyword: Boolean,
        openNow: Boolean
    ): NearbyPlacesResult {
        return Gson().fromJson(placesJson1, NearbyPlacesResult::class.java)
    }

    override suspend fun getGeocode(latLng: String): GeoCode {
        return GeoCode(
            results = listOf(
                GeoCode.Result(
                    address_components = listOf(),
                    formatted_address = "",
                    geometry = GeoCode.Geometry(
                        location = GeoCode.Location(0.0, 0.0),
                        location_type = "",
                        viewport = GeoCode.Viewport(
                            GeoCode.Northeast(0.0, 0.0),
                            GeoCode.Southwest(0.0, 0.0)
                        )
                    ),
                    place_id = "",
                    types = listOf()
                )
            )
        )
    }

    override suspend fun getDistance(
        units: String,
        origins: String,
        destinations: String
    ): DistanceResult {
        return DistanceResult(
            destination_addresses = listOf(),
            origin_addresses = listOf(),
            error_message = "",
            rows = listOf(),
            status = ""
        )
    }


    private val placesJson1 = """
{
  "html_attributions": [],
  "results": [
    {
      "geometry": {
        "location": {
          "lat": -34.08652470000001,
          "lng": 151.1457628
        },
        "viewport": {
          "northeast": {
            "lat": -34.0852310197085,
            "lng": 151.1474899802915
          },
          "southwest": {
            "lat": -34.0879289802915,
            "lng": 151.1447920197085
          }
        }
      },
      "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/generic_business-71.png",
      "name": "Bundeena Bowling and Sports Club",
      "opening_hours": {
        "open_now": true
      },
      "place_id": "ChIJPeWDnITIEmsR5y99eg9FxOo",
      "reference": "ChIJPeWDnITIEmsR5y99eg9FxOo",
      "types": [
        "parking",
        "restaurant",
        "food",
        "point_of_interest",
        "establishment"
      ],
      "vicinity": "49 Liverpool Street, Bundeena"
    },
    {
      "geometry": {
        "location": {
          "lat": -34.0852267,
          "lng": 151.1515988
        },
        "viewport": {
          "northeast": {
            "lat": -34.0838688697085,
            "lng": 151.1527604802915
          },
          "southwest": {
            "lat": -34.0865668302915,
            "lng": 151.1500625197085
          }
        }
      },
      "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
      "name": "Vinegar and Brown Paper",
      "opening_hours": {
        "open_now": false
      },
      "place_id": "ChIJ_Xxbgn3JEmsRYAlD3YOEb1k",
      "reference": "ChIJ_Xxbgn3JEmsRYAlD3YOEb1k",
      "types": [
        "meal_takeaway",
        "restaurant",
        "food",
        "point_of_interest",
        "establishment"
      ],
      "vicinity": "46 Brighton Street, Bundeena"
    },
    {
      "geometry": {
        "location": {
          "lat": -34.0852267,
          "lng": 151.1515988
        },
        "viewport": {
          "northeast": {
            "lat": -34.0838688697085,
            "lng": 151.1527604802915
          },
          "southwest": {
            "lat": -34.0865668302915,
            "lng": 151.1500625197085
          }
        }
      },
      "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
      "name": "P\u0026P Kitchen",
      "place_id": "ChIJqTrdydjJEmsR989wUzVvw9Q",
      "reference": "ChIJqTrdydjJEmsR989wUzVvw9Q",
      "types": [
        "meal_takeaway",
        "restaurant",
        "food",
        "point_of_interest",
        "establishment"
      ],
      "vicinity": "Shop 2/48 Brighton Street, Bundeena"
    },
    {
      "geometry": {
        "location": {
          "lat": -34.0850994,
          "lng": 151.1516275
        },
        "viewport": {
          "northeast": {
            "lat": -34.0836716697085,
            "lng": 151.1528074802915
          },
          "southwest": {
            "lat": -34.0863696302915,
            "lng": 151.1501095197085
          }
        }
      },
      "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
      "name": "I Grill",
      "opening_hours": {
        "open_now": false
      },
      "place_id": "ChIJn3CL8Z7JEmsRVnQZb-aabDo",
      "reference": "ChIJn3CL8Z7JEmsRVnQZb-aabDo",
      "types": [
        "bar",
        "restaurant",
        "food",
        "point_of_interest",
        "establishment"
      ],
      "vicinity": "42 Brighton Street, Bundeena"
    },
    {
      "geometry": {
        "location": {
          "lat": -34.0850434,
          "lng": 151.15146
        },
        "viewport": {
          "northeast": {
            "lat": -34.0836510197085,
            "lng": 151.1527185802915
          },
          "southwest": {
            "lat": -34.0863489802915,
            "lng": 151.1500206197085
          }
        }
      },
      "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
      "name": "Bundeena pizza",
      "opening_hours": {
        "open_now": false
      },
      "place_id": "ChIJcxvlu8_JEmsRpdzZtbIBw6s",
      "reference": "ChIJcxvlu8_JEmsRpdzZtbIBw6s",
      "types": [
        "restaurant",
        "food",
        "point_of_interest",
        "establishment"
      ],
      "vicinity": "44 Brighton Street, Bundeena"
    },
    {
      "geometry": {
        "location": {
          "lat": -34.0848811,
          "lng": 151.1515038
        },
        "viewport": {
          "northeast": {
            "lat": -34.0835164197085,
            "lng": 151.1527627302915
          },
          "southwest": {
            "lat": -34.0862143802915,
            "lng": 151.1500647697085
          }
        }
      },
      "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/cafe-71.png",
      "name": "Driftwood Cafe Bundeena",
      "opening_hours": {
        "open_now": false
      },
      "place_id": "ChIJ8cyn9oXIEmsR0E2mVTJ-khU",
      "reference": "ChIJ8cyn9oXIEmsR0E2mVTJ-khU",
      "types": [
        "cafe",
        "restaurant",
        "food",
        "health",
        "point_of_interest",
        "store",
        "establishment"
      ],
      "vicinity": "1/36-40 Brighton Street, Bundeena"
    },
    {
      "geometry": {
        "location": {
          "lat": -34.0681752,
          "lng": 151.1550664
        },
        "viewport": {
          "northeast": {
            "lat": -34.0668386697085,
            "lng": 151.1564679802915
          },
          "southwest": {
            "lat": -34.0695366302915,
            "lng": 151.1537700197085
          }
        }
      },
      "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
      "name": "Little Parrot",
      "opening_hours": {
        "open_now": false
      },
      "place_id": "ChIJxSN4oG_IEmsR2I_0Gd4Mw2Q",
      "reference": "ChIJxSN4oG_IEmsR2I_0Gd4Mw2Q",
      "types": [
        "restaurant",
        "food",
        "point_of_interest",
        "establishment"
      ],
      "vicinity": "147 Ewos Parade, Cronulla"
    },
    {
      "geometry": {
        "location": {
          "lat": -34.0644084,
          "lng": 151.1534521
        },
        "viewport": {
          "northeast": {
            "lat": -34.06301956970849,
            "lng": 151.1549904302915
          },
          "southwest": {
            "lat": -34.06571753029149,
            "lng": 151.1522924697085
          }
        }
      },
      "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
      "name": "The Nuns\u0027 Pool",
      "opening_hours": {
        "open_now": false
      },
      "place_id": "ChIJTQlidG7IEmsRYlxLkkmZV64",
      "reference": "ChIJTQlidG7IEmsRYlxLkkmZV64",
      "types": [
        "restaurant",
        "food",
        "point_of_interest",
        "establishment"
      ],
      "vicinity": "103 Ewos Parade, Cronulla"
    },
    {
      "geometry": {
        "location": {
          "lat": -34.0622369,
          "lng": 151.1396816
        },
        "viewport": {
          "northeast": {
            "lat": -34.0609061197085,
            "lng": 151.1410313802915
          },
          "southwest": {
            "lat": -34.0636040802915,
            "lng": 151.1383334197085
          }
        }
      },
      "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
      "name": "Yachties Bistro",
      "opening_hours": {
        "open_now": true
      },
      "place_id": "ChIJMdRpmtjHEmsRCtFj_RVRznQ",
      "reference": "ChIJMdRpmtjHEmsRCtFj_RVRznQ",
      "types": [
        "restaurant",
        "food",
        "point_of_interest",
        "establishment"
      ],
      "vicinity": "232 Woolooware Road, Burraneer"
    },
    {
      "geometry": {
        "location": {
          "lat": -34.0569444,
          "lng": 151.1533333
        },
        "viewport": {
          "northeast": {
            "lat": -34.0554809697085,
            "lng": 151.1547954802915
          },
          "southwest": {
            "lat": -34.0581789302915,
            "lng": 151.1520975197085
          }
        }
      },
      "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
      "name": "Little Italy Restaurant Cronulla",
      "opening_hours": {
        "open_now": true
      },
      "place_id": "ChIJM5i6khLIEmsR0zd3ve-c7s0",
      "reference": "ChIJM5i6khLIEmsR0zd3ve-c7s0",
      "types": [
        "meal_delivery",
        "meal_takeaway",
        "restaurant",
        "food",
        "point_of_interest",
        "establishment"
      ],
      "vicinity": "99 Gerrale Street, Cronulla"
    },
    {
      "geometry": {
        "location": {
          "lat": -34.0567641,
          "lng": 151.153352
        },
        "viewport": {
          "northeast": {
            "lat": -34.0553684697085,
            "lng": 151.1547810802915
          },
          "southwest": {
            "lat": -34.0580664302915,
            "lng": 151.1520831197085
          }
        }
      },
      "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
      "name": "Pilgrims Cronulla",
      "opening_hours": {
        "open_now": false
      },
      "place_id": "ChIJ8weBkhLIEmsRZGr8PAoYBGE",
      "reference": "ChIJ8weBkhLIEmsRZGr8PAoYBGE",
      "types": [
        "restaurant",
        "food",
        "point_of_interest",
        "store",
        "establishment"
      ],
      "vicinity": "97 Gerrale Street, Cronulla"
    },
    {
      "geometry": {
        "location": {
          "lat": -34.0639437,
          "lng": 151.1203632
        },
        "viewport": {
          "northeast": {
            "lat": -34.0625272697085,
            "lng": 151.1217733302915
          },
          "southwest": {
            "lat": -34.0652252302915,
            "lng": 151.1190753697085
          }
        }
      },
      "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
      "name": "Bellissimo Pizza, Pasta and Grill",
      "opening_hours": {
        "open_now": true
      },
      "place_id": "ChIJr32a6iLHEmsRaT_mDNMzP-4",
      "reference": "ChIJr32a6iLHEmsRaT_mDNMzP-4",
      "types": [
        "restaurant",
        "food",
        "point_of_interest",
        "establishment"
      ],
      "vicinity": "Shop 1/623 Port Hacking Road, Lilli Pilli"
    },
    {
      "geometry": {
        "location": {
          "lat": -34.0564159,
          "lng": 151.1546376
        },
        "viewport": {
          "northeast": {
            "lat": -34.0553780197085,
            "lng": 151.1557015802915
          },
          "southwest": {
            "lat": -34.0580759802915,
            "lng": 151.1530036197085
          }
        }
      },
      "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
      "name": "Watergrill Restaurant",
      "opening_hours": {
        "open_now": true
      },
      "place_id": "ChIJaWH7VxLIEmsREkEGXqe-n3w",
      "reference": "ChIJaWH7VxLIEmsREkEGXqe-n3w",
      "types": [
        "restaurant",
        "bar",
        "food",
        "point_of_interest",
        "establishment"
      ],
      "vicinity": "RSL, Level 4/38 Gerrale Street, Cronulla"
    },
    {
      "geometry": {
        "location": {
          "lat": -34.0564446,
          "lng": 151.1553467
        },
        "viewport": {
          "northeast": {
            "lat": -34.05538526970851,
            "lng": 151.1568514802915
          },
          "southwest": {
            "lat": -34.05808323029151,
            "lng": 151.1541535197085
          }
        }
      },
      "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
      "name": "Zimzala Restaurant Cronulla",
      "opening_hours": {
        "open_now": true
      },
      "place_id": "ChIJN2boqRPIEmsRw6Gpe5apgQ8",
      "reference": "ChIJN2boqRPIEmsRw6Gpe5apgQ8",
      "types": [
        "restaurant",
        "food",
        "point_of_interest",
        "establishment"
      ],
      "vicinity": "The Esplanade, Cronulla"
    },
    {
      "geometry": {
        "location": {
          "lat": -34.0638003,
          "lng": 151.120191
        },
        "viewport": {
          "northeast": {
            "lat": -34.0623953697085,
            "lng": 151.1215910802915
          },
          "southwest": {
            "lat": -34.0650933302915,
            "lng": 151.1188931197085
          }
        }
      },
      "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/cafe-71.png",
      "name": "Vitalo",
      "opening_hours": {
        "open_now": false
      },
      "place_id": "ChIJ4wy_CMnHEmsRgnNWyZX-jfI",
      "reference": "ChIJ4wy_CMnHEmsRgnNWyZX-jfI",
      "types": [
        "cafe",
        "restaurant",
        "food",
        "point_of_interest",
        "store",
        "establishment"
      ],
      "vicinity": "621 Port Hacking Road, Lilli Pilli"
    },
    {
      "geometry": {
        "location": {
          "lat": -34.0563103,
          "lng": 151.1551813
        },
        "viewport": {
          "northeast": {
            "lat": -34.0549613197085,
            "lng": 151.1565302802915
          },
          "southwest": {
            "lat": -34.0576592802915,
            "lng": 151.1538323197085
          }
        }
      },
      "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/cafe-71.png",
      "name": "Barefoot on the Beach Cafe",
      "opening_hours": {
        "open_now": false
      },
      "place_id": "ChIJq6qqRhLIEmsRtWDWOR_H8E0",
      "reference": "ChIJq6qqRhLIEmsRtWDWOR_H8E0",
      "types": [
        "cafe",
        "meal_takeaway",
        "restaurant",
        "food",
        "point_of_interest",
        "store",
        "establishment"
      ],
      "vicinity": "30 Gerrale Street, Cronulla"
    },
    {
      "geometry": {
        "location": {
          "lat": -34.06374499999999,
          "lng": 151.1199321
        },
        "viewport": {
          "northeast": {
            "lat": -34.0623060697085,
            "lng": 151.1213632302915
          },
          "southwest": {
            "lat": -34.0650040302915,
            "lng": 151.1186652697085
          }
        }
      },
      "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/cafe-71.png",
      "name": "D\u0027Lish On Port",
      "opening_hours": {
        "open_now": false
      },
      "place_id": "ChIJ54P4B8nHEmsRQb810GwKQTk",
      "reference": "ChIJ54P4B8nHEmsRQb810GwKQTk",
      "types": [
        "cafe",
        "restaurant",
        "food",
        "point_of_interest",
        "establishment"
      ],
      "vicinity": "1/617 Port Hacking Road, Lilli Pilli"
    },
    {
      "geometry": {
        "location": {
          "lat": -34.0636315,
          "lng": 151.1199136
        },
        "viewport": {
          "northeast": {
            "lat": -34.0622249697085,
            "lng": 151.1213151302915
          },
          "southwest": {
            "lat": -34.06492293029149,
            "lng": 151.1186171697085
          }
        }
      },
      "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
      "name": "Tuk Tuk Thai Take Away",
      "opening_hours": {
        "open_now": true
      },
      "place_id": "ChIJi6VDRcbHEmsRhTGHDN4HXR0",
      "reference": "ChIJi6VDRcbHEmsRhTGHDN4HXR0",
      "types": [
        "meal_takeaway",
        "restaurant",
        "food",
        "point_of_interest",
        "establishment"
      ],
      "vicinity": "4/617 Port Hacking Road, Lilli Pilli"
    },
    {
      "geometry": {
        "location": {
          "lat": -34.05575049999999,
          "lng": 151.1503208
        },
        "viewport": {
          "northeast": {
            "lat": -34.05441456970851,
            "lng": 151.1516776302915
          },
          "southwest": {
            "lat": -34.0571125302915,
            "lng": 151.1489796697085
          }
        }
      },
      "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/generic_business-71.png",
      "name": "Cronulla Cruises",
      "place_id": "ChIJb5wHaA3IEmsR6CTR-s3WX7o",
      "reference": "ChIJb5wHaA3IEmsR6CTR-s3WX7o",
      "types": [
        "restaurant",
        "food",
        "point_of_interest",
        "establishment"
      ],
      "vicinity": "2 Tonkin Street, Cronulla"
    },
    {
      "geometry": {
        "location": {
          "lat": -34.0556953,
          "lng": 151.1521665
        },
        "viewport": {
          "northeast": {
            "lat": -34.0543463197085,
            "lng": 151.1535154802915
          },
          "southwest": {
            "lat": -34.0570442802915,
            "lng": 151.1508175197085
          }
        }
      },
      "icon": "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
      "name": "D.Bowls",
      "opening_hours": {
        "open_now": false
      },
      "place_id": "ChIJe5S4_EfJEmsRBNv2tI0cj4w",
      "reference": "ChIJe5S4_EfJEmsRBNv2tI0cj4w",
      "types": [
        "meal_takeaway",
        "restaurant",
        "food",
        "point_of_interest",
        "establishment"
      ],
      "vicinity": "Shop, Cronulla"
    }
  ],
  "status": "OK"
}
""".trim()
}