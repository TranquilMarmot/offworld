// 
// by @hektor41 
// 21/06/12

// doublehelix 


#ifdef GL_ES
precision mediump float;
#endif

uniform float time;
uniform vec2 resolution;


float f(float x, float y) {
	//x = (mouse.x*10.0)*x; //Interactive
	
	float nx = x + cos(time*x+ 2.0*sin(time*y + 0.1*x*x*sin(time)));
	float ny = y + 2.0*sin(time*y + 0.1*x*x*sin(time) + y + 2.0*sin(time*y + 0.1*x*x*sin(time)));
	
	float formula = 0.2/( sin( x*1.5 + time + y + 1.0*sin(time*y + 0.1*x*x*sin(time)) ) + 0.05 - y );
	float formula_effects = formula/sqrt(ny*sin(time));
	
	return formula_effects;
}



void main( void ) {
	
	// CONTROLS
	 float zoom =10.0;
	 float cameraX = (zoom*5.0)/10.0;
	 float cameraY = (zoom*3.0)/10.0-0.5;
	 
	// ^ ^ ^ ^ 
	
	zoom += 0.5;
	vec2 p = ( gl_FragCoord.xy /(resolution.x * 1.0/zoom));
	float x = p.x - cameraX;
	float y = p.y - cameraY;

	float a = f(x,y);
	
	gl_FragColor = vec4(sqrt(a)/9.0,  sqrt(a)/6.0,  sqrt(a)/10.0,  1.0);

}