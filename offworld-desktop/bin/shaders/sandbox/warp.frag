//Set the controls for the heart of the sun

#ifdef GL_ES
precision mediump float;
#endif

uniform float time;
uniform vec2 resolution;

const float PI = 3.14159265358979323;
const float TWOPI = PI*2.0;

float osc1 = sin(time)*0.5+0.5;
//float env1 = mod(time*0.25, 4.0);
float env1 = fract(time*0.25);




vec2 rotated(float a, float r) {
	
	return vec2(cos(a*TWOPI)*r, sin(a*TWOPI)*r);
}

float circle(vec2 pos, float radius, float smooth) {
	
	float len = length( pos );
	float d = smoothstep( radius-smooth, radius+smooth, len );
	return d;
}

float unatan(vec2 p, float start)
{
	float a= atan(p.x, p.y);
	a/=TWOPI;
	a += 0.5;
	a = fract(a+start);
	return a;
}

vec3 ring01(vec2 p)
{
	
	//vec2 gr = rotated(0.5, 1.0);
	float shade = unatan(p, -time + pow(length(p), 4.0) );
	float shade2 = unatan(p, time * 1.3 + pow(length(p), 2.0) );
	vec3 a = vec3(max(shade2, max(0.0, 2.0 - pow(length(p), 2.0))) * 0.3 + sin(time) * 0.1 + 0.2, 0.0, 0.0);
	vec3 b = vec3(1.0, 0.8, 0.0);
	//shade *= max(g, dot(p,rotated(g,1.0)));
	//shade *= 1.0-length(p);
	vec3 clr = a + b * shade;
	return clr;

}

void main( void ) {

	float speed = time *0.5;
	float aspect = resolution.x / resolution.y;
	vec2 unipos = ( gl_FragCoord.xy / resolution );
	vec2 pos = unipos*2.0-1.0;
	pos.x *=aspect;
	
	float sint = sin(time);
	float usint = sint*0.5+0.5;


	vec3 clr = ring01(pos);
    
    gl_FragColor = vec4(clr,1.0);

}