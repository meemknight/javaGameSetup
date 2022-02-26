import org.lwjgl.opengl.GL30;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Shader
{
	
	int id = 0;
	
	String readEntireFile(String file) throws IOException
	{
		return new String(Files.readAllBytes(Paths.get(file)));
	}
	
	private int loadShaderComponent(int type, String text)
	{
		int id = GL30.glCreateShader(type);
		GL30.glShaderSource(id, text);
		
		if(GL30.glGetShaderi(id, GL30.GL_COMPILE_STATUS) == 0)
		{
			int messageSize[] = new int[1];
			messageSize[0] = GL30.glGetShaderi(id, GL30.GL_INFO_LOG_LENGTH);
			
			ByteBuffer buffer = ByteBuffer.allocate(messageSize[0]);
			
			GL30.glGetShaderInfoLog(id, messageSize, buffer);
			
			System.out.println("error: " + StandardCharsets.UTF_8.decode(buffer).toString());
			return 0;
		}
		
		return id;
	}
	
	public void loadShaderFromFile(String vertexShader, String fragmentShader) throws IOException
	{
		loadShaderFromMemory(readEntireFile(vertexShader), readEntireFile(fragmentShader));
	}
	
	public void loadShaderFromMemory(String vertexShader, String fragmentShader)
	{
		int vs = loadShaderComponent(GL30.GL_VERTEX_SHADER, vertexShader);
		int fs = loadShaderComponent(GL30.GL_FRAGMENT_SHADER, vertexShader);
		
		
	}
	
}
