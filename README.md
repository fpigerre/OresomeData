OresomeData
===========

OresomeData is a tool initially created for the [OresomeCraft Network](http://oresomecraft.com) to harness the potential of large, extensive data sets.
Using OresomeData, appropriate parties would be able to view instantaneous representations of data about certain aspects of OresomeCraft.
That data would be potentially extensive, depending on the interface used for the representations of data

### MongoDB
OresomeData uses MongoDB (a NoSQL database) to store and manage large amounts of data.
Using MongoDB comes with several advantages, notably:
- Data is usually represented as a JSON-type 'Document' allowing for tiered data types and usage of non-primitive data types
- Documents can be nested within one another, allowing for the nesting of more complex data inside an 'umbrella' document; for example, an 'OresomeBattles Map' document could be nested under a 'Player' or 'Author' document
- MongoDB is horizontally scalable, allowing for extra database servers to be created or retired as OresomeCraft's average player count fluctuates
- MongoDB data schemas can be updated as new tools are integrated with OresomeCraft; this means database conversions and transfers are seldom needed

### Example Documents/Schema

```json
{
    "_id": "5063114bd386d8fadbd6b004",
    "uuid": "d6938711-6efc-4057-97e5-86aba8f6ed2b",
    "name": "psgs",
    "coins": 10,
    "stats": {
        "kills": 6621,
        "deaths": 6352,
        "kd": 1.04,
        "ffawins": 5,
        "infectionwins": 36,
        "highestkillstreak": 11
    },
    "raidhouse": {
        "recognition": 560,
        "tier": 2,
        "totalraids": 182,
        "successfulraids": 158,
        "failedraids": 24,
        "rerollamount": 11
    },
    "forums": {
        "messages": 179,
        "likes": 103,
        "trophypoints": 43
    },
    "maps": [
        {
            "ElementsII": {
                "name": "elements2",
                "fullName": "Elements II",
                "creators": ["_Moist", "psgs", "broddikill"],
                "gamemodes": ["KOTH"],
                "inrotation": false
            },
            "TheBowl": {
                "name": "bowl",
                "fullName": "The Bowl",
                "creators": ["_Moist", "psgs", "niceman506", "broddikill"],
                "gamemodes": ["INFECTION", "KOTH"],
                "inrotation": false
            },
            "TropicalPaths": {
                "name": "tropical",
                "fullName": "Tropical Paths",
                "creators": ["_Moist", "psgs", "Evil_Emo"],
                "gamemodes": ["TDM", "CTF", "KOTH"],
                "inrotation": false
            },
            "WarTrauma": {
                "name": "trauma",
                "fullName": "War Trauma",
                "creators": ["_Moist", "niceman506", "psgs"],
                "gamemodes": ["TDM"],
                "inrotation": false
            }
        }
    ],
    "mapsplugin": {
        "commits": 162,
        "additions": 10493,
        "deletions": 4626
    },
    "firstjoin": "09/06/2012",
    "smphours": 271
}
```