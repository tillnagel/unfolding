uniform sampler2D src_tex_unit0;
uniform sampler2D src_tex_unit1;
uniform float mask_factor;

void main(void) {
    vec4 src_color = texture2D(src_tex_unit0, gl_TexCoord[0].st).rgba;
    vec4 mask_color = texture2D(src_tex_unit1, gl_TexCoord[1].st).rgba;
    
    gl_FragColor =  mix(src_color, mask_factor * mask_color, mask_color.a);
} 
