import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnd;

import org.lwjgl.opengl.*;

import java.io.IOException;

public class GameLayer extends GameManager
{
	float pos = 0;
	int vertexBuffer = 0;
	Shader shader = new Shader();
	
	public void gameInit()
	{
		
		try
		{
			shader.loadShaderFromFile("resources//vert.vert", "resources//frag.frag");
		}
		catch(Exception e){
			System.out.println("shader error" + e);
		}
		
		shader.bind();
		
		float bufferData[] = {0.f, 1.f, -1.f, -1.f, 1.f, -1.f};
		
		vertexBuffer = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vertexBuffer);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, bufferData, GL30.GL_STATIC_DRAW);
		
		GL30.glEnableVertexAttribArray(0);
		GL30.glVertexAttribPointer(0, 2, GL13.GL_FLOAT, false, 0, 0);
		//GL30.glVertexAttrib3f(1, 1,1,1);
		
	}
	
	public void gameUpdate()
	{
		int w = getWindowW();
		int h = getWindowH();
		
		glViewport(0, 0, w, h);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
		
		/*
		glBegin(GL_TRIANGLES);
		glColor3f(1.f, 0.f, 0.f);
		glVertex2f(0.f, 1.f);
		
		glColor3f(0.f, 1.f, 0.f);
		glVertex2f(-1.f, -1.f);
		
		glColor3f(0.f, 0.f, 1.f);
		glVertex2f(1.f, -1.f);
		
		glEnd();
		 */
		
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vertexBuffer);
		GL30.glEnableVertexAttribArray(0);
		GL30.glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);

		shader.bind();
		
		GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, 3);
		
		if(isLeftMouseButtonReleased())
			System.out.println(getMousePosX() + " " + getMousePosY());
		
	}
	
	public void gameClose()
	{
	
	
	}
	
	
}
