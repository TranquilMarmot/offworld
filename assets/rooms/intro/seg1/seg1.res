{"resources":	{
	"textures":	[
		{
			"name":	"intro-seg1-0",
			"path":	"rooms/intro/seg1/0.png"
		},
		{
			"name":	"intro-seg1-1",
			"path":	"rooms/intro/seg1/1.png"
		},
		{
			"name":	"intro-seg1-3",
			"path":	"rooms/intro/seg1/3.png"
		},
		{
			"name":	"intro-seg1-4",
			"path":	"rooms/intro/seg1/4.png"
		},
		{
			"name":	"intro-seg1-5",
			"path":	"rooms/intro/seg1/5.png"
		},
		{
			"name":	"intro-seg1-6",
			"path":	"rooms/intro/seg1/6.png"
		},
		{
			"name":	"intro-seg1-7",
			"path":	"rooms/intro/seg1/7.png"
		},
		{
			"name":	"intro-seg1-8",
			"path":	"rooms/intro/seg1/8.png"
		},
		{
			"name":	"intro-seg1-9",
			"path":	"rooms/intro/seg1/9.png"
		},
		{
			"name":	"intro-seg1-10",
			"path":	"rooms/intro/seg1/10.png"
		},
		{
			"name":	"intro-seg1-11",
			"path":	"rooms/intro/seg1/11.png"
		},
		{
			"name":	"intro-seg1-12",
			"path":	"rooms/intro/seg1/12.png"
		},
		{
			"name":	"intro-seg1-13",
			"path":	"rooms/intro/seg1/13.png"
		},
		{
			"name":	"intro-seg1-14",
			"path":	"rooms/intro/seg1/14.png"
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
				},
				{
					"render":	"rooms/intro/seg1/6.obj",
					"texture":	"intro-seg1-6"
				},
				{
					"render":	"rooms/intro/seg1/7.obj",
					"texture":	"intro-seg1-7"
				},
				{
					"render":	"rooms/intro/seg1/8.obj",
					"texture":	"intro-seg1-8"
				},
				{
					"render":	"rooms/intro/seg1/9.obj",
					"texture":	"intro-seg1-9"
				},
				{
					"render":	"rooms/intro/seg1/10.obj",
					"texture":	"intro-seg1-10"
				},
				{
					"render":	"rooms/intro/seg1/11.obj",
					"texture":	"intro-seg1-11"
				},
				{
					"render":	"rooms/intro/seg1/12.obj",
					"texture":	"intro-seg1-12"
				},
				{
					"render":	"rooms/intro/seg1/13.obj",
					"texture":	"intro-seg1-13"
				},
				{
					"render":	"rooms/intro/seg1/14.obj",
					"texture":	"intro-seg1-14"
				}
			],
			"geom":	"rooms/intro/seg1/geom.obj",
			"debug":	"rooms/intro/seg1/debug.obj",
			"type":	"loop",
			"scale":	8.0
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