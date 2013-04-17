varying vec3 Position;
varying vec3 Normal;
varying vec2 TexCoord;

uniform sampler2D Tex1;

struct LightInfo {
  vec4 LightPosition;  // Light position in eye coords.
  vec3 LightIntensity; // A,D,S intensity
  bool LightEnabled;
};
uniform LightInfo Light;

struct MaterialInfo {
  vec3 Ka;            // Ambient reflectivity
  vec3 Kd;            // Diffuse reflectivity
  vec3 Ks;            // Specular reflectivity
  float Shininess;    // Specular shininess factor
};
uniform MaterialInfo Material;

void phongModel( vec3 pos, vec3 norm, out vec3 ambAndDiff, out vec3 spec ) {
    vec3 s = normalize(vec3(Light.LightPosition));
    vec3 v = normalize(-pos.xyz);
    vec3 r = reflect(-s, norm);
    vec3 ambient = Light.LightIntensity * Material.Ka;
    float sDotN = max(dot(s,norm), 0.0);
    vec3 diffuse = Light.LightIntensity * Material.Kd * sDotN;
    spec = vec3(0.0);
    if(sDotN > 0.0)
        spec = Light.LightIntensity * Material.Ks *
               pow(max(dot(r,v), 0.0), Material.Shininess);
    ambAndDiff = ambient + diffuse;
}

void main() {
    vec4 texColor = texture2D(Tex1, TexCoord);
    	
    if(Light.LightEnabled){
	    vec3 ambAndDiff, spec;
	    phongModel( Position, Normal, ambAndDiff, spec );
	    gl_FragColor = (vec4( ambAndDiff, 1.0 ) * texColor) + vec4(spec,1.0);
    } else{
    	/*
    	 * For efficiency, only check for alpha discards
    	 * when lighitng is turned off
    	 * (otherwise we just assume we don't want
    	 * transparency)
    	 */
        if(texColor.w == 0.0)
    		discard;
    	gl_FragColor = (texColor);
    }
}