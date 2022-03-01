#version 330

layout (location = 0) in vec3 pos;
layout (location = 1) in vec3 normal;
layout (location = 2) in vec2 uv;

uniform mat4 u_viewProjection;
out vec3 v_normal;

void main()
{
    v_normal = normal;
    gl_Position = u_viewProjection * vec4(pos, 1);
}