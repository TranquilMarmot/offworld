precision mediump float;

uniform float time;
uniform vec2 resolution;

void main(void) {
	vec2 uPos = ( gl_FragCoord.xy / resolution.xy );//normalize wrt y axis
	uPos -= .5;
	vec3 color = vec3(0.0);
	float vertColor = 0.0;
	for( float i = 0.; i < 7.; ++i ) {
		uPos.y += sin( uPos.x*(i+1.0) + (time * 1.9)   +i/2.0 ) * 0.1;
		float fTemp = abs(1.0 / uPos.y / 100.0);
		vertColor += fTemp;
		color += vec3( fTemp*(10.0-i)/10.0, fTemp*i/10.0, pow(fTemp,0.99)*1.5 );
	}
	gl_FragColor = vec4(color, 1.0);
}