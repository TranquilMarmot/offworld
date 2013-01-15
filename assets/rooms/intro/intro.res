{"resources":	{
	"textures":	[
		{
			"name":	"intro-00x00",
			"path":	"rooms/intro/00x00.png",
			"minFilter":	"nearest",
			"magFilter":	"nearest"
		},
		{
			"name":	"intro-00x01",
			"path":	"rooms/intro/00x01.png",
			"minFilter":	"nearest",
			"magFilter":	"nearest"
		},
		{
			"name":	"intro-00x02",
			"path":	"rooms/intro/00x02.png",
			"minFilter":	"nearest",
			"magFilter":	"nearest"
		},
		{
			"name":	"intro-01x00",
			"path":	"rooms/intro/01x00.png",
			"minFilter":	"nearest",
			"magFilter":	"nearest"
		},
		{
			"name":	"intro-01x01",
			"path":	"rooms/intro/01x01.png",
			"minFilter":	"nearest",
			"magFilter":	"nearest"
		},
		{
			"name":	"intro-01x02",
			"path":	"rooms/intro/01x02.png",
			"minFilter":	"nearest",
			"magFilter":	"nearest"
		},
		{
			"name":	"intro-02x02",
			"path":	"rooms/intro/02x02.png",
			"minFilter":	"nearest",
			"magFilter":	"nearest"
		},
		{
			"name":	"intro-03x02",
			"path":	"rooms/intro/03x02.png",
			"minFilter":	"nearest",
			"magFilter":	"nearest"
		},
		{
			"name":	"intro-04x02",
			"path":	"rooms/intro/04x02.png",
			"minFilter":	"nearest",
			"magFilter":	"nearest"
		},
		{
			"name":	"intro-05x02",
			"path":	"rooms/intro/05x02.png",
			"minFilter":	"nearest",
			"magFilter":	"nearest"
		}
	],
	
	"polygons": [
		{
			"name":	"intro",
			"render":	[
				{
					"render":	"rooms/intro/00x00.obj",
					"texture":	"intro-00x00"
				},
				{
					"render":	"rooms/intro/00x01.obj",
					"texture":	"intro-00x01"
				},
				{
					"render":	"rooms/intro/00x02.obj",
					"texture":	"intro-00x02"
				},
				{
					"render":	"rooms/intro/01x00.obj",
					"texture":	"intro-01x00"
				},
				{
					"render":	"rooms/intro/01x01.obj",
					"texture":	"intro-01x01"
				},
				{
					"render":	"rooms/intro/01x02.obj",
					"texture":	"intro-01x02"
				},
				{
					"render":	"rooms/intro/02x02.obj",
					"texture":	"intro-02x02"
				},
				{
					"render":	"rooms/intro/03x02.obj",
					"texture":	"intro-03x02"
				},
				{
					"render":	"rooms/intro/04x02.obj",
					"texture":	"intro-04x02"
				},
				{
					"render":	"rooms/intro/05x02.obj",
					"texture":	"intro-05x02"
				}
			],
			"geom":	"rooms/intro/seg1-collision.obj",
			"debugRender":	"rooms/intro/seg1-debug.obj",
			"type":	"loop",
			"scale":	8.0
		}
	],
	
	"entities":	[
		{
			"name":	"intro",
			"fixture":	{
				"shape":	{
					"type":	"polygon",
					"polyName":	"intro"
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