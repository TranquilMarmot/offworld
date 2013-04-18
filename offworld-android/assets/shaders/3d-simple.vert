attribute vec3 VertexPosition;
attribute vec2 VertexTexCoord;

varying vec2 TexCoord;

uniform mat4 ModelViewMatrix;
uniform mat4 ProjectionMatrix;

void main()
{
    TexCoord = vec2(VertexTexCoord.x, VertexTexCoord.y);

	mat4 MVP = ProjectionMatrix * ModelViewMatrix;
    gl_Position = MVP * vec4(VertexPosition,1.0);
}