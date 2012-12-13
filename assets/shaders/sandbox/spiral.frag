#ifdef GL_ES
precision mediump float;
#endif

uniform float time;
uniform vec2 resolution;

#define size 0.1
#define stroke 0.075

void main( void ) {

	vec2 position = 10.0 * ( gl_FragCoord.xy / resolution.xy - vec2( 0.5, 0.5 ) );
	
	vec3 color = vec3( 0.0 );

	float r = length( position );
	float a = ( atan( position.y, position.x ) ) / 3.14;
	
	float diff = ( a  + 0.1 * time ) - r;
	float m = mod( diff, size );
	if( m < size && m > size - stroke ) {
		color = vec3( 0.5 * m / size, 0.0, r );
	}
	
	gl_FragColor = vec4( color, 7.0 );

}