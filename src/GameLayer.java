import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnd;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.FloatBuffer;

public class GameLayer extends GameManager
{
	float pos = 0;
	int vertexBuffer = 0;
	Shader shader = new Shader();
	Camera camera = new Camera();
	int u_viewProjection;
	int vao;
	
	public void gameInit()
	{
		
		try
		{
			shader.loadShaderFromFile("resources//vert.vert", "resources//frag.frag");
			u_viewProjection = GL30.glGetUniformLocation(shader.id, "u_viewProjection");
		}
		catch(Exception e){
			System.out.println("shader error" + e);
		}
		
		shader.bind();
		
		vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);
		
		float bufferData[] = {0.f, 1.f, -1.f, -1.f, 1.f, -1.f};
		
		vertexBuffer = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vertexBuffer);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, bufferData, GL30.GL_STATIC_DRAW);
		
		GL30.glEnableVertexAttribArray(0);
		GL30.glVertexAttribPointer(0, 2, GL13.GL_FLOAT, false, 0, 0);
		//GL30.glVertexAttrib3f(1, 1,1,1);
		
		GL30.glBindVertexArray(0);
		
	}
	
	float rotation = 0.f;
	
	float lastMouseX = getMousePosX();
	float lastMouseY = getMousePosY();
	
	public void gameUpdate()
	{
		int w = getWindowW();
		int h = getWindowH();
		
		glViewport(0, 0, w, h);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
		
		//input
		{
			Vector3f move = new Vector3f(0,0,0);
			float speed = 10 * getDeltaTime();
			
			if(isKeyHeld(GLFW_KEY_W))
			{
				move.z -= speed;
			}
			if(isKeyHeld(GLFW_KEY_S))
			{
				move.z += speed;
			}
			
			if(isKeyHeld(GLFW_KEY_A))
			{
				move.x -= speed;
			}
			if(isKeyHeld(GLFW_KEY_D))
			{
				move.x += speed;
			}
			
			if(isKeyHeld(GLFW_KEY_Q))
			{
				move.y -= speed;
			}
			if(isKeyHeld(GLFW_KEY_E))
			{
				move.y += speed;
			}
			
			if(isRightMouseButtonHeld())
			{
				Vector2f delta = new Vector2f(getMousePosX(), getMousePosY());
				delta.x -= lastMouseX;
				delta.y -= lastMouseY;
				delta.mul(getDeltaTime());
				camera.rotateCamera(delta);
			}

			lastMouseX = getMousePosX();
			lastMouseY = getMousePosY();
			
			camera.moveFPS(move);
			
		}
		
		camera.updateAspectRation(w, h);
		
		GL30.glBindVertexArray(vao);
		
		
		shader.bind();
		try (MemoryStack stack = MemoryStack.stackPush()) {
			
			FloatBuffer fb = camera.getViewProjectionMatrix().get(stack.mallocFloat(16));
			//Vector3f rotate = new Vector3f(0.0f, 0.0f, 1.f);
			//FloatBuffer fb = new Matrix4f().rotate(rotation * (float)Math.PI, rotate).get(stack.mallocFloat(16));
			
			rotation += getDeltaTime();
			
			GL30.glUniformMatrix4fv(u_viewProjection, false,
					fb);
		}
		
		GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, 3);
		
		GL30.glBindVertexArray(0);
		
		
	}
	
	public void gameClose()
	{
	
	
	}
	
	
}
