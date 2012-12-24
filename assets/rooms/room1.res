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
		}
	],
	
	"polygons": [
		{
			"name":	"tut1",
			"render":	"rooms/tut.obj",
			"geom":	"rooms/tut-collision.obj",
			"texture":	"tut",
			"scale":	4.0
		},
	
		{
			"name":	"tut2",
			"render":	"rooms/tut2.obj",
			"geom":	"rooms/tut2-collision.obj",
			"texture":	"tut",
			"scale":	4.0
		}
	]
}}