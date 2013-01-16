{"resources":	{
	"textures":	[
		{
			"name":	"intro-seg3-0",
			"path":	"rooms/intro/seg3/0.png",
			"minFilter":	"linear",
			"magFilter":	"linear"
		},
		{
			"name":	"intro-seg3-1",
			"path":	"rooms/intro/seg3/1.png",
			"minFilter":	"linear",
			"magFilter":	"linear"
		},
		{
			"name":	"intro-seg3-2",
			"path":	"rooms/intro/seg3/2.png",
			"minFilter":	"linear",
			"magFilter":	"linear"
		},
		{
			"name":	"intro-seg3-3",
			"path":	"rooms/intro/seg3/3.png",
			"minFilter":	"linear",
			"magFilter":	"linear"
		},
		{
			"name":	"intro-seg3-4",
			"path":	"rooms/intro/seg3/4.png",
			"minFilter":	"linear",
			"magFilter":	"linear"
		},
		{
			"name":	"intro-seg3-5",
			"path":	"rooms/intro/seg3/5.png",
			"minFilter":	"linear",
			"magFilter":	"linear"
		},
		{
			"name":	"intro-seg3-6",
			"path":	"rooms/intro/seg3/6.png",
			"minFilter":	"linear",
			"magFilter":	"linear"
		},
		{
			"name":	"intro-seg3-7",
			"path":	"rooms/intro/seg3/7.png",
			"minFilter":	"linear",
			"magFilter":	"linear"
		}
	],
	
	"polygons": [
		{
			"name":	"intro-seg3",
			"render":	[
				{
					"render":	"rooms/intro/seg3/0.obj",
					"texture":	"intro-seg3-0"
				},
				{
					"render":	"rooms/intro/seg3/1.obj",
					"texture":	"intro-seg3-1"
				},
				{
					"render":	"rooms/intro/seg3/2.obj",
					"texture":	"intro-seg3-2"
				},
				{
					"render":	"rooms/intro/seg3/3.obj",
					"texture":	"intro-seg3-3"
				},
				{
					"render":	"rooms/intro/seg3/4.obj",
					"texture":	"intro-seg3-4"
				},
				{
					"render":	"rooms/intro/seg3/5.obj",
					"texture":	"intro-seg3-5"
				},
				{
					"render":	"rooms/intro/seg3/6.obj",
					"texture":	"intro-seg3-6"
				},
				{
					"render":	"rooms/intro/seg3/6.obj",
					"texture":	"intro-seg3-6"
				}
			],
			"geom":	"rooms/intro/seg3/geom.obj",
			"debug":	"rooms/intro/seg3/debug.obj",
			"type":	"loop",
			"scale":	16.0
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