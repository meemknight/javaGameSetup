import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnd;

import org.lwjgl.opengl.*;

public class GameLayer extends GameManager
{
	float pos = 0;
	int vertexBuffer = 0;
	
	public void gameInit()
	{
		
		//vertexBuffer = GL30.glGenBuffers();
		
		
	}
	
	public void gameUpdate()
	{
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
		
		glBegin(GL_TRIANGLES);
		glColor3f(1.f, 0.f, 0.f);
		glVertex2f(0.f, 1.f);
		
		glColor3f(0.f, 1.f, 0.f);
		glVertex2f(-1.f, -1.f);
		
		glColor3f(0.f, 0.f, 1.f);
		glVertex2f(1.f, -1.f);
		
		glEnd();
		
		if(isLeftMouseButtonReleased())
			System.out.println(getMousePosX() + " " + getMousePosY());
		
	}
	
	public void gameClose()
	{
	
	
	}
	
	
}
