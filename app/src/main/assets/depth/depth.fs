#version 300 es
out vec4 FragColor;

in vec2 TexCoords;

uniform sampler2D texture1;

uniform float mVisible;
void main() {
    FragColor = texture(texture1, TexCoords);
}