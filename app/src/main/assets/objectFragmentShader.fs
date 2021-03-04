#version 300 es
struct Material {
    sampler2D diffuse;      //漫反射贴图
    sampler2D specular;     //镜面光贴图
    sampler2D emission;     //放射光贴图
    float shininess;        //反光度
};

struct Light {
    vec3 position;
    //vec3 direction;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;

    float constant;
    float linear;
    float quadratic;
};

out vec4 FragColor;

in vec3 Normal;
in vec3 FragPos;
in vec2 TexCoords;

uniform vec3 viewPos;

uniform Material material;
uniform Light light;

void main() {
    //衰减值计算
    float distanc = length(light.position - FragPos);
    float attenuation = 1.0 / (light.constant + light.linear * distanc + light.quadratic * (distanc * distanc));

    //环境光
    vec3 ambient = light.ambient * vec3(texture(material.diffuse, TexCoords));

    //diffuse 漫反射
    vec3 norm = normalize(Normal);
    vec3 lightDir = normalize(light.position - FragPos);
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = light.diffuse * diff * vec3(texture(material.diffuse, TexCoords));

    //specular 镜面光
    vec3 viewDir = normalize(viewPos - FragPos);
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    vec3 specular = light.specular * spec * vec3(texture(material.specular, TexCoords));

    ambient *= attenuation;
    diffuse *= attenuation;
    specular *= attenuation;

    vec3 result = ambient + diffuse + specular;
    FragColor = vec4(result, 1.0);
    //放射光贴图效果
    //FragColor = vec4(result + vec3(texture(material.emission, TexCoords)), 1.0);
}