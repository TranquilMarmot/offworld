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
			"name":	"rocks",
			"path":	"entities/rocks/rocks.png",
			"minFilter": "nearest",
			"magFilter":	"nearest"
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
		},
		{
			"name": "rock1",
			"render":	"entities/rocks/rock1-render.obj",
			"geom":	"entities/rocks/rock1-geom.obj",
			"texture":	"rocks",
			"scale":	4.0
		}
	]
}}