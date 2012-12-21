uniform mat4 ModelView;
uniform mat4 Projection;

precision highp float;
attribute vec4 vPosition;
attribute vec2 vTexCoord;

varying vec2 vTexCoordOut;

void main() {
	mat4 mvp =   Projection * ModelView; 
	gl_Position = mvp * vPosition;
	vTexCoordOut = vTexCoord;
}