uniform mat4 MVP;

attribute vec4 vPosition;
attribute vec2 vTexCoord;

varying vec2 vTexCoordOut;

void main() {
	gl_Position = MVP * vPosition;
	vTexCoordOut = vTexCoord;
}