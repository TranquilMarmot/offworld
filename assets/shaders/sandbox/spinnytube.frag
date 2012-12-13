#ifdef GL_ES
precision mediump float;
#endif

#define PI 3.14159265359
#define SPACING 0.8

uniform float time;
uniform vec2 resolution;

bool f(float t, vec2 pos) {
	vec2 pos_ = pos * 2.0 - vec2(1.0);
	
	return abs(pos_.y - sin(pos_.x + t * 0.5)) < 0.5;
}

vec3 spin(float angle, vec2 position) {
	
	float width = abs(sin(angle));
	
	if (abs(position.y * 2.0 - 1.0) < width) {
		if (mod(angle, 2.0 * PI) < PI) {
			return vec3(245.0 / 255.0, 1.0, 59.0 / 255.0);
		}
		else {
			return vec3(0.2);
		}
	}
	else {
		return vec3(0);
	}
}

void main( void ) {
	vec2 position = gl_FragCoord.xy / resolution;
	float cylWidth = 0.5;
	
	vec3 color;
	float cylPos = position.x - 0.5;
	if (abs(cylPos) < cylWidth / 2.0) {
		float cylAngle = asin(cylPos / 0.5 * 2.0);
		position.x = (cylAngle + time * 0.5) / PI + 1.0;
		
		vec2 res = floor(resolution / 20.0);
		
		vec2 position_ = mod(position * res, 1.0);
		
		if (position_.x < SPACING && position_.y < SPACING) {
			vec2 tilePos = floor(position * res);
			
			float t = time * 0.5;
			
			bool now  = f(floor(t), tilePos / res);
			bool next = f(floor(t) + 1.0, tilePos / res);
			
			float baseAngle = (now ? 0.5 : 1.5) * PI;
			float nextAngle = (next ? 0.5 : 1.5) * PI;
			
			color = spin(baseAngle + (nextAngle - baseAngle) * mod(t, 1.0), position_ / SPACING);
			color *= cos(cylAngle);
		}
	}
	
	gl_FragColor = vec4(color, 1.0);
}