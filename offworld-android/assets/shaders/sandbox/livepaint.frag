uniform float time;
uniform vec2 resolution;

const int complexity = 7;
const float zoom = 0.2;
const float speed1 = 0.2;
const float speed2 = 0.2;
const float color_intensity = 0.05;

void main() 
{
	vec2 p=((1.0 + zoom) * gl_FragCoord.xy-resolution)/max(resolution.x,resolution.y);
	for(int i= 1;i<(complexity + 6);i++) {
		vec2 newp=p;
		newp.x+= 0.6/float(i) * cos(float(i * (i/2))*p.y + (time * speed1)) + (time * speed1 * 0.001);
		newp.y+= 0.6/float(i) * sin(float(i * (i/2))*p.x + (time * speed1)) + (time * speed1 * 0.001) + 1.5;
		p=newp; 
	}
	gl_FragColor = vec4(vec3((0.4*sin(3.0*(p.x+0.5))+color_intensity),(0.4*sin(3.0*p.y + (time * speed2))+color_intensity),sin(p.x+p.y)), 1.0); 
}