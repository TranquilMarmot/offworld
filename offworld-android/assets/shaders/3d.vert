attribute vec3 VertexPosition;
attribute vec3 VertexNormal;
attribute vec2 VertexTexCoord;

// position in world coordinates (for lighting)
varying vec3 Position;
varying vec3 Normal;
varying vec2 TexCoord;

uniform mat4 ModelViewMatrix;
uniform mat4 ProjectionMatrix;
uniform mat3 NormalMatrix;

void main()
{
    TexCoord = vec2(VertexTexCoord.x, VertexTexCoord.y);
    Normal = normalize(NormalMatrix * VertexNormal);
    Position = vec3(ModelViewMatrix * vec4(VertexPosition,1.0));

	mat4 MVP = ProjectionMatrix * ModelViewMatrix;
    gl_Position = MVP * vec4(VertexPosition,1.0);
}