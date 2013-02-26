#ifdef GL_ES
precision mediump float;
#endif

uniform float time;
uniform vec2 mouse;
uniform vec2 resolution;

void main( void ) {

	vec2 position = ( gl_FragCoord.xy - resolution.xy*vec2(.5,.15) )/resolution.y;
	float angle = atan(position.y,position.x) + (0.1 * sin(time));
	float rad = (1.+.9*cos(8.*angle))*(1.+0.1*cos(24.*angle))*(0.9+0.05*cos(200.*angle))*(1.+sin(angle))*.2;
	
	gl_FragColor = step(sqrt(dot(position,position)),rad)*vec4(0.5 + sin(position.x),0.8 - sin(position.y),0.2 - sin(time),0)+vec4(0,0,0,1);
}