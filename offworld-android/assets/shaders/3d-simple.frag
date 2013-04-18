varying vec2 TexCoord;

uniform sampler2D Tex1;

void main() {
    vec4 texColor = texture2D(Tex1, TexCoord);
    	
	if(texColor.w == 0.0)
		discard;
	gl_FragColor = (texColor);
}