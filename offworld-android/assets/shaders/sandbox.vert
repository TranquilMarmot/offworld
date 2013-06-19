attribute vec3 position;

uniform mat4 MVP;

void main() {
	gl_Position = vec4(position, 1.0);
}