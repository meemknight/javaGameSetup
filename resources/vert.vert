#version 330

layout (location = 0) in vec2 pos;

uniform mat4 u_viewProjection;

void main()
{
    gl_Position = vec4(pos, 0.5, 1) * u_viewProjection;
}