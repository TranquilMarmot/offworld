{"resources":	{
	"polygons": [
		{
			"name":	"cave-1-platform2",
			"geom":	"rooms/cave-1/platform2/geom.obj",
			"debug": "rooms/cave-1/platform2/render.obj",
			"render":	[
				{
					"render":	"rooms/cave-1/platform2/render.obj",
					"texture":	"cave-1-tile-1"
				}
			],
			"type":	"loop",
			"scale": 3.0
		}
	],
	
	"entities":	[
		{
			"name":	"cave-1-platform2",
			"fixture":	{
				"shape":	{
					"type":	"polygon",
					"polyName":	"cave-1-platform2"
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