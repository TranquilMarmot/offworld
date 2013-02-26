{"resources":	{
	"polygons": [
		{
			"name":	"intro-seg2",
			"render":	[
				{
					"render":	"rooms/intro/seg2/0.obj",
					"texture":	"intro-1"
				},
				{
					"render":	"rooms/intro/seg2/1.obj",
					"texture":	"intro-2"
				},
				{
					"render":	"rooms/intro/seg2/2.obj",
					"texture":	"intro-3"
				},
				{
					"render":	"rooms/intro/seg2/3.obj",
					"texture":	"intro-4"
				}
			],
			"geom":	"rooms/intro/seg2/geom.obj",
			"debug":	"rooms/intro/seg2/debug.obj",
			"type":	"loop",
			"scale":	32.0
		}
	],
	
	"entities":	[
		{
			"name":	"intro-seg2",
			"fixture":	{
				"shape":	{
					"type":	"polygon",
					"polyName":	"intro-seg2"
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