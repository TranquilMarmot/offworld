precision highp float;
uniform vec4 vColor;

varying lowp vec2 vTexCoordOut;
uniform sampler2D Texture;

void main() {
	vec4 texColor = texture2D(Texture, vTexCoordOut);

	if(texColor.w == 0.0)
		discard;
	else
		gl_FragColor = vColor * texColor;
}