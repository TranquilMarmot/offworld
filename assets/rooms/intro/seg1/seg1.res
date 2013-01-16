{"resources":	{
	"textures":	[
		{
			"name":	"intro-seg1-0",
			"path":	"rooms/intro/seg1/0.png",
			"minFilter":	"linear",
			"magFilter":	"linear"
		},
		{
			"name":	"intro-seg1-1",
			"path":	"rooms/intro/seg1/1.png",
			"minFilter":	"linear",
			"magFilter":	"linear"
		},
		{
			"name":	"intro-seg1-2",
			"path":	"rooms/intro/seg1/2.png",
			"minFilter":	"linear",
			"magFilter":	"linear"
		},
		{
			"name":	"intro-seg1-3",
			"path":	"rooms/intro/seg1/3.png",
			"minFilter":	"linear",
			"magFilter":	"linear"
		},
		{
			"name":	"intro-seg1-4",
			"path":	"rooms/intro/seg1/4.png",
			"minFilter":	"linear",
			"magFilter":	"linear"
		},
		{
			"name":	"intro-seg1-5",
			"path":	"rooms/intro/seg1/5.png",
			"minFilter":	"linear",
			"magFilter":	"linear"
		}
	],
	
	"polygons": [
		{
			"name":	"intro-seg1",
			"render":	[
				{
					"render":	"rooms/intro/seg1/0.obj",
					"texture":	"intro-seg1-0"
				},
				{
					"render":	"rooms/intro/seg1/1.obj",
					"texture":	"intro-seg1-1"
				},
				{
					"render":	"rooms/intro/seg1/2.obj",
					"texture":	"intro-seg1-2"
				},
				{
					"render":	"rooms/intro/seg1/3.obj",
					"texture":	"intro-seg1-3"
				},
				{
					"render":	"rooms/intro/seg1/4.obj",
					"texture":	"intro-seg1-4"
				},
				{
					"render":	"rooms/intro/seg1/5.obj",
					"texture":	"intro-seg1-5"
				}
			],
			"geom":	"rooms/intro/seg1/geom.obj",
			"debug":	"rooms/intro/seg1/debug.obj",
			"type":	"loop",
			"scale":	16.0
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