{"resources":	{
	"polygons": [
		{
			"name":	"intro-seg3",
			"render":	[
				{
					"render":	"rooms/intro/seg3/0.obj",
					"texture":	"intro-2"
				},
				{
					"render":	"rooms/intro/seg3/1.obj",
					"texture":	"intro-3"
				},
				{
					"render":	"rooms/intro/seg3/2.obj",
					"texture":	"intro-4"
				},
				{
					"render":	"rooms/intro/seg3/3.obj",
					"texture":	"intro-5"
				},
				{
					"render":	"rooms/intro/seg3/4.obj",
					"texture":	"intro-6"
				}
			],
			"geom":	"rooms/intro/seg3/geom.obj",
			"debug":	"rooms/intro/seg3/debug.obj",
			"type":	"loop",
			"scale":	32.0
		}
	],
	
	"entities":	[
		{
			"name":	"intro-seg3",
			"fixture":	{
				"shape":	{
					"type":	"polygon",
					"polyName":	"intro-seg3"
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