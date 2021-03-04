#version 300 es
out vec4 FragColor;
in vec4 ourColor;
in vec2 TexCoord;
uniform sampler2D texture1;
uniform sampler2D texture2;
uniform float mVisible;
void main() {
    FragColor = mix(texture(texture1, TexCoord), texture(texture2, TexCoord), mVisible);
}