{"resources":	{
	"polygons": [
		{
			"name":	"cave-1-platform-3",
			"geom":	"rooms/cave-1/platform-3/geom.obj",
			"debug": "rooms/cave-1/platform-3/debug.obj",
			"type":	"loop",
			"scale": 3.0
		}
	],
	
	"entities":	[
		{
			"name":	"cave-1-platform-3",
			"fixture":	{
				"shape":	{
					"type":	"polygon",
					"polyName":	"cave-1-platform-3"
				},
				"density":	0.0,
				"friction":	1.0,
				"restitution":	0.01,
				"isSensor":	false,
				"groupIndex":	5,
				"category":	"ground",
				"mask":	"everything"
			},
			"body":	{
				"allowSleep":	true,
				"bullet":	false,
				"fixedRotation":	true,
				"gravityScale":	1.0,
				"linearDamping":	0.0,
				"type":	"static"
			}
		}
	]
}}