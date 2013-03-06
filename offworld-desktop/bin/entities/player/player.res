{"resources": {
	"textures": [
		{
			"name":"player-arm",
			"path":"entities/player/arm.png",
			"minFilter": "linear",
			"magFilter": "linear"
		},
		{
			"name": "particle-fire",
			"path": "particles/fire.png",
			"minFilter": "nearest",
			"magFilter": "nearest"
		},
		{
			"name": "particle-blur",
			"path": "particles/blur.png",
			"minFilter": "linear",
			"magFilter": "linear"
		}
	],

	"resources": [
		{
			"name": "player-body-anim",
			"path": "entities/player/body-anim.res"
		}
	],
	
	"sheets": [
		{
			"path": "entities/player/body-inair.png",
			"minFilter":	"linear",
			"magFilter":	"linear",
			"sprites":	[
			{
				"name":	"player-body-inair-l",
				"x":	43,
				"y":	13,
				"w":	167,
				"h":	517
			},
			{
				"name":	"player-body-jump",
				"x":	431,
				"y":	13,
				"w":	138,
				"h":	520
			},
			{
				"name":	"player-body-inair-r",
				"x":	814,
				"y":	13,
				"w":	154,
				"h":	523
			}
			]
		}
	]
}}
