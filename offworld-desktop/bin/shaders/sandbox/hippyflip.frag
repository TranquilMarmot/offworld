precision mediump float;
 
uniform float time;
uniform vec2 resolution;
 
float scale = 1.0;
 
void main(void) {
	vec2 uPos = ( gl_FragCoord.xy / resolution.xy );//normalize wrt y axis
	uPos.x += time / 10.0;
	uPos.y -= time / 10.0;
	
	// background color
	//vec3 color = vec3(-time / 100.0, (time / 7500.0) - 0.5, time / 25000.0);
	vec3 color = vec3(1.0,1.0,1.0);
	
	for(float i = 0.0; i < 15.0; ++i) {
		//uPos.x += cos(time / 10.0 + uPos.y) / 25.0;
		//uPos.y += sin(uPos.x * (i + 12.0) + (time / 100.0)) / 2.0;
		
		uPos.x += cos((time / 10.0) + uPos.y);
		uPos.y += sin((time / 10.0) + uPos.x);
		
		uPos.x *= scale + (i / 10.0);
		uPos.y *= scale + (i / 10.0);
		
		float fTemp = abs(((uPos.x * 0.5) / (uPos.y * 1.0)) / 150.0);
		float r = fTemp * (sin(uPos.x * 0.5) * 100.0) - 0.02;
		float g = fTemp * (cos(uPos.y * 0.5) * 100.0) + 0.02;
		float b = fTemp * (cos(-uPos.x * 0.5) * 100.0) - 0.02;
		
		color += vec3(r,g,b);
	}
	
	gl_FragColor = vec4(color.r / 5.0, color.g / 5.0, color.b / 5.0, 0.5);
}