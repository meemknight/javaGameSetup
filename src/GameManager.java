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
	
	//last 2 positions are for mouse
	private byte keysPressed[] = new byte[GLFW_KEY_LAST + 3];
	private boolean focus = true;
	
	public boolean isFocused()
	{
		return focus;
	}
	
	public boolean isKeyPressed(int key)
	{
		return (keysPressed[key] & 0b001) != 0;
	}
	
	public boolean isKeyHeld(int key)
	{
		return (keysPressed[key] & 0b010) != 0;
	}
	
	public boolean isKeyReleased(int key)
	{
		return (keysPressed[key] & 0b100) != 0;
	}
	
	public boolean isLeftMouseButtonPressed()
	{
		return isKeyPressed(keysPressed.length - 2);
	}
	
	public boolean isLeftMouseButtonHeld()
	{
		return isKeyHeld(keysPressed.length - 2);
	}
	
	public boolean isLeftMouseButtonReleased()
	{
		return isKeyReleased(keysPressed.length - 2);
	}
	
	public boolean isRightMouseButtonPressed()
	{
		return isKeyPressed(keysPressed.length - 1);
	}
	
	public boolean isRightMouseButtonHeld()
	{
		return isKeyHeld(keysPressed.length - 1);
	}
	
	public boolean isRightMouseButtonReleased()
	{
		return isKeyReleased(keysPressed.length - 1);
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
	
	
	private void changeAllKeyState(KeyStates state)
	{
		for(int i = 0; i < keysPressed.length; i++)
		{
			changeKeyState(i, state);
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
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 4);
		
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
			}
			else if(action == GLFW_RELEASE)
			{
				changeKeyState(key, KeyStates.Released);
			}
			
		});
		
		glfwSetWindowFocusCallback(window, (window, f) ->
		{
			focus = f;
		});
		
		glfwSetMouseButtonCallback(window, (window, key, action, mods) ->
		{
			int k = 0;
			
			if(key == GLFW_MOUSE_BUTTON_LEFT)
			{
				k = keysPressed.length - 2;
			}
			else if(key == GLFW_MOUSE_BUTTON_RIGHT)
			{
				k = keysPressed.length - 1;
			}
			
			if(k != 0)
			{
				if(action == GLFW_PRESS)
				{
					changeKeyState(k, KeyStates.Pressed);
				}
				else if(action == GLFW_RELEASE)
				{
					changeKeyState(k, KeyStates.Released);
				}
			}
			
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
		//glfwSwapInterval(1);
		
		// Make the window visible
		glfwShowWindow(window);
		
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();
		
		gameInit();
		
	}
	
	private double[] mouseX = new double[1];
	private double[] mouseY = new double[1];
	
	public int getMousePosX()
	{
		return (int) mouseX[0];
	}
	
	public int getMousePosY()
	{
		return (int) mouseY[0];
	}
	
	public void setMousePosition(int x, int y)
	{
		glfwSetCursorPos(window, x, y);
	}
	
	private void updateInput()
	{
		for(int i = 0; i < keysPressed.length; i++)
		{
			switch(keysPressed[i])
			{
				case 0b000:
					break;    //do nothing
				case 0b010:
					break;    //key is being held, do nothing
				case 0b011:    //pressed
					keysPressed[i] = 0b010; //go to held
					break;
				case 0b110:    //released
					keysPressed[i] = 0b000; //go to nothing
					break;
			}
		}
		
		glfwGetCursorPos(window, mouseX, mouseY);
		
	}
	
	private int[] windowX = new int[1];
	private int[] windowY = new int[1];
	
	public int getWindowW()
	{
		return windowX[0];
	}
	
	public int getWindowH()
	{
		return windowY[0];
	}
	
	public void loop()
	{
		
		// Set the clear color
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while(!glfwWindowShouldClose(window))
		{
			if(!focus)
			{
				changeAllKeyState(KeyStates.EraseState);
			}
			
			gameUpdate();
			
			glfwSwapBuffers(window); // swap the color buffers
			
			glfwGetWindowSize(window, windowX, windowY);
			
			updateInput();
			glfwPollEvents();
		}
	}
	
	public void close()
	{
		
		gameClose();
		
	}
	
	
}