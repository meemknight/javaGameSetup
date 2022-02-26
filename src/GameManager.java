import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public abstract class GameManager
{
	
	// The window handle
	public long window;
	public GameLayer gameLayer;
	
	private byte keysPressed[] = new byte[GLFW_KEY_LAST + 1];
	
	public boolean isKeyPressed(int key)
	{
		return (keysPressed[key] & 0b001) !=0;
	}
	
	public boolean isKeyHeld(int key)
	{
		return (keysPressed[key] & 0b010) !=0;
	}
	
	public boolean isKeyReleased(int key)
	{
		return (keysPressed[key] & 0b100) !=0;
	}
	
	private enum KeyStates
	{
		EraseState,
		Pressed,
		Released
	}
	
	//  from msb to lsb last 3 bits: released, held, pressed
	private void changeKeyState(int index, KeyStates state)
	{
	
		switch(state)
		{
			case EraseState:
				keysPressed[index] = 0;
				break;
				
			case Pressed:
				keysPressed[index] = 0b11;
				break;
				
			case Released:
				keysPressed[index] = 0b110;
				break;
			
		}
	
	}
	
	
	public abstract void gameInit();
	
	public abstract void gameUpdate();
	
	public abstract void gameClose();
	
	
	public void freeResources()
	{
		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		
		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	public void init()
	{
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();
		
		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if(!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");
		
		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
		
		// Create the window
		window = glfwCreateWindow(300, 300, "geam", NULL, NULL);
		if(window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");
		
		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) ->
		{
			
			if(action == GLFW_PRESS)
			{
				changeKeyState(key, KeyStates.Pressed);
			}else if(action == GLFW_RELEASE)
			{
				changeKeyState(key, KeyStates.Released);
			}

			if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		});
		
		// Get the thread stack and push a new frame
		//try ( MemoryStack stack = stackPush() )
		//{
		//	IntBuffer pWidth = stack.mallocInt(1); // int*
		//	IntBuffer pHeight = stack.mallocInt(1); // int*
		//
		//	// Get the window size passed to glfwCreateWindow
		//	glfwGetWindowSize(window, pWidth, pHeight);
		//
		//	// Get the resolution of the primary monitor
		//	GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		//
		//	// Center the window
		//	glfwSetWindowPos(
		//			window,
		//			(vidmode.width() - pWidth.get(0)) / 2,
		//			(vidmode.height() - pHeight.get(0)) / 2
		//	);
		//} // the stack frame is popped automatically
		
		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);
		
		// Make the window visible
		glfwShowWindow(window);
		
		gameInit();
		
	}
	
	private void updateInput()
	{
		for(int i =0; i < keysPressed.length; i++)
		{
			switch(keysPressed[i])
			{
				case 0b000:
					break;	//do nothing
				case 0b010:
					break;	//key is being held, do nothing
				case 0b011:	//pressed
					keysPressed[i] = 0b010; //go to held
					break;
				case 0b110:	//released
					keysPressed[i] = 0b000; //go to nothing
					break;
				
			}
		}
		
	}
	
	public void loop()
	{
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();
		
		// Set the clear color
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while(!glfwWindowShouldClose(window))
		{
			
			gameUpdate();
			
			glfwSwapBuffers(window); // swap the color buffers
			
			updateInput();
			glfwPollEvents();
		}
	}
	
	public void close()
	{
		
		gameClose();
		
	}
	
	
}