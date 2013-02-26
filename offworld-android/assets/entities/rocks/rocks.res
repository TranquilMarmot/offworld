{"resources":	{
	"textures":	[
		{
			"name": "rocks",
			"path": "entities/rocks/rocks.png",
			"minFilter":	"linear",
			"magFilter":	"linear"
		}
	],
	
	"polygons": [
		{
			"name":	"rock1",
			"render":	"entities/rocks/rock1-render.obj",
			"geom":	"entities/rocks/rock1-geom.obj",
			"debug":	"entities/rocks/rock1-debug.obj",
			"type":	"polygon",
			"texture":	"rocks",
			"scale":	4.0
		},
		{
			"name":	"rock2",
			"render":	"entities/rocks/rock2-render.obj",
			"geom":	"entities/rocks/rock2-geom.obj",
			"debug":	"entities/rocks/rock2-debug.obj",
			"type":	"polygon",
			"texture":	"rocks",
			"scale":	4.0
		},
		{
			"name":	"rock3",
			"render":	"entities/rocks/rock3-render.obj",
			"geom":	"entities/rocks/rock3-geom.obj",
			"debug":	"entities/rocks/rock3-debug.obj",
			"type":	"polygon",
			"texture":	"rocks",
			"scale":	4.0
		},
		{
			"name":	"rock4",
			"render":	"entities/rocks/rock4-render.obj",
			"geom":	"entities/rocks/rock4-geom.obj",
			"debug":	"entities/rocks/rock4-debug.obj",
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
		},
		{
			"name":	"rock2",
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
		},
		{
			"name":	"rock3",
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
		},
		{
			"name":	"rock4",
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