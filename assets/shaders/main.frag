#define TRANSPARENCY_MASK vec3(1.0, 0.0, 1.0)

precision highp float;
uniform vec4 vColor;

varying lowp vec2 vTexCoordOut;
uniform sampler2D Texture;

void main() {
	vec4 texColor = texture2D(Texture, vTexCoordOut);

	if(texColor.xyz == TRANSPARENCY_MASK || texColor.w == 0.0)
		discard;
	else
		gl_FragColor = vColor * texColor;
}