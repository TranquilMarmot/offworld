{"resources":	{
	"textures":	[
		{
			"name":	"tut",
			"path":	"rooms/tut.png",
			"minFilter":	"nearest",
			"magFilter":	"nearest"
		},
		{
			"name":	"tutback",
			"path":	"rooms/back.png",
			"minFilter":	"nearest",
			"magFilter":	"nearest"
		},
		{
			"name":	"background",
			"path":	"rooms/background.png",
			"minFilter":	"nearest",
			"magFilter":	"linear"
		},
		{
			"name": "rocks",
			"path": "entities/rocks/rocks.png",
			"minFilter":	"nearest",
			"magFilter":	"nearest"
		}
	],
	
	"polygons": [
		{
			"name":	"tut1",
			"render":	"rooms/tut.obj",
			"geom":	"rooms/tut-collision.obj",
			"type":	"loop",
			"texture":	"tut",
			"scale":	4.0
		},
	
		{
			"name":	"tut2",
			"render":	"rooms/tut2.obj",
			"geom":	"rooms/tut2-collision.obj",
			"type":	"loop",
			"texture":	"tut",
			"scale":	4.0
		}
	],
	
	"entities":	[
		{
			"name":	"tut1",
			"fixture":	{
				"shape":	{
					"type":	"polygon",
					"polyName":	"tut1"
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
		},
		{
			"name":	"tut2",
			"fixture":	{
				"shape":	{
					"type":	"polygon",
					"polyName":	"tut2"
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