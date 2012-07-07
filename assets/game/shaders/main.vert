uniform mat4 ModelView;
uniform mat4 Projection;

attribute vec4 vPosition;

void main() {
	mat4 mvp =   Projection * ModelView; 
	gl_Position = mvp * vPosition;
}