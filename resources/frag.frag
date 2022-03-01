#version 330

layout (location = 0) out vec4 color;

void main()
{
    //color = vec4(0.5,0.6,0.9,1);
    //color = (gl_FragCoord.x<25.0) ? vec4(1.0, 0.0, 0.0, 1.0) : vec4(0.0, 1.0, 0.0, 1.0);
    color = vec4(gl_FragCoord.xy/1000.f,0.9,1);
}