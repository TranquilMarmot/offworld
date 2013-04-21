{ "resources": {
	"textures": [
		{
			"name": "diamond",
			"path": "entities/diamond/diamond.png",
			"minFilter": "linear",
			"magFilter": "linear"
		}
	],
	
	"models": [
		{
			"name": "diamond",
			"texture": "diamond",
			"dir": "entities/diamond",
			"xScale": 4.0,
			"yScale": 4.0,
			"zScale": 4.0
		}
	],
	
	"polygons": [
		{
			"name": "diamond",
			"model": "diamond",
			"geom": "entities/diamond/diamond-geom.obj",
			"debug": "entities/diamond/diamond-debug.obj",
			"type": "polygon",
			"texture": "diamond",
			"scale": 4.0
		}
	],
	
	"entities":	[
		{
			"name":	"diamond",
			"fixture":	{
				"shape":	{
					"type":	"polygon",
					"polyName":	"diamond"
				},
				"density":	1.0,
				"friction":	0.2,
				"restitution":	0.7,
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