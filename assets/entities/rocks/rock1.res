{"resources":	{
	"textures":	[
		{
			"name": "rocks",
			"path": "entities/rocks/rocks.png",
			"minFilter":	"nearest",
			"magFilter":	"nearest"
		}
	],
	
	"polygons": [
		{
			"name":	"rock1",
			"render":	"entities/rocks/rock1-render.obj",
			"geom":	"entities/rocks/rock1-geom.obj",
			"type":	"polygon",
			"texture":	"rocks",
			"scale":	4.0
		}
	],
	
	"entities":	[
		{
			"name":	"rock1",
			"fixture":	{
				"shape":	{
					"type":	"polygon",
					"polyName":	"rock1"
				},
				"density":	1.0,
				"friction":	0.5,
				"restitution":	0.0,
				"isSensor":	false,
				"groupIndex":	5,
				"category":	"entity",
				"mask":	"everything"
			},
			"body":	{
				"allowSleep":	true,
				"bullet":	false,
				"fixedRotation":	false,
				"gravityScale":	1.0,
				"linearDamping":	0.0,
				"type":	"dynamic"
			}
		}
	]
}}