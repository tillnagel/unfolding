uniform sampler2D src_tex_unit0;
uniform sampler2D src_tex_unit1;


void main(void) {
    vec4 src_color = texture2D(src_tex_unit0, gl_TexCoord[0].st).rgba;
    vec4 mask_color = texture2D(src_tex_unit1, gl_TexCoord[1].st).rgba;
    
	gl_FragColor =  mix(src_color, 0.0*mask_color, mask_color.r);
	
	//gl_FragColor = vec4(1.0, 0.0, 0.0, 0.0);
    //gl_FragColor = vec4(src_color.r, src_color.g, src_color.b, mask_color.a);
} 
