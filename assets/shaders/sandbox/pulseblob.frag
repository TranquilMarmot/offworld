#ifdef GL_ES
precision mediump float;
#endif
 
// Posted by Trisomie21
 
uniform float time;
uniform vec2 mouse;
uniform vec2 resolution;
 
void main( void ) {
 
  float scale = resolution.y / 20.0;
	float ring = 50.0;
	float radius = resolution.x*1.2;
	float gap = scale*0.3;
	vec2 pos = gl_FragCoord.xy - resolution.xy * 0.2 + vec2(300.0, 225.0);
	
	float d = length(pos) * 1.0;
	
	// Create the wiggle
	d += (sin(pos.y*2.0/scale+time)*sin(pos.x*2.0/scale+time*.5))*scale * 4.0;
	
	// Compute the distance to the closest ring
	float v = mod(d + radius/(ring*3.0), radius/ring);
	v = abs(v - radius/(ring*10.0));
	
	v = clamp(v-gap, 0.0, 2.0);
	
	d /= radius + (cos(sin(time*1.0))*sin(time*0.5) * 50.0);
	vec3 m = fract((d-1.0)*vec3(ring*-.5, -ring, ring*.25)*0.5);
	
	gl_FragColor = vec4( m*v / 4.20, 1.0 );
}