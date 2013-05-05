uniform float time;
uniform vec2 resolution;

void main( void ) {
	// background thing
	vec2 position = ( gl_FragCoord.xy / resolution.xy * 2.0) - 1.0;
	vec4 veca = vec4((mod(atan(position.x ,position.y) / 1.6, 0.1) * 15.0 + 0.5) * 0.6);
	vec4 vecb = vec4(0.2, 0.5, 1.0, 1.0) * (1.5- length(position));
	float l = (length(vec2(position.x , position.y)));
	vec4 color = veca * vecb * l;
	
	// spinny thing
	float th = atan(position.y, position.x) + time*0.5;
	th = mod(th, 3.14159*0.125);
	
	// color things
	float rmod = th * 0.2;
	float gmod = th * 0.5;
	float bmod = th * 1.0;
	
	gl_FragColor = vec4(color.x + rmod, color.y + gmod, color.z + bmod, 1.0);

}