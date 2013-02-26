{"resources":	{
	"polygons": [
		{
			"name":	"intro-seg1",
			"render":	[
				{
					"render":	"rooms/intro/seg1/0.obj",
					"texture":	"intro-0"
				},
				{
					"render":	"rooms/intro/seg1/1.obj",
					"texture":	"intro-1"
				},
				{
					"render":	"rooms/intro/seg1/2.obj",
					"texture":	"intro-2"
				}
			],
			"geom":	"rooms/intro/seg1/geom.obj",
			"debug":	"rooms/intro/seg1/debug.obj",
			"type":	"loop",
			"scale":	32.0
		}
	],
	
	"entities":	[
		{
			"name":	"intro-seg1",
			"fixture":	{
				"shape":	{
					"type":	"polygon",
					"polyName":	"intro-seg1"
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