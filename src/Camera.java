
import com.sun.javafx.geom.*;
import org.lwjgl.ovr.OVRMatrix4f;

import java.io.BufferedReader;
import org.joml.*;

public class Camera
{
	
	public Vector3f position = new Vector3f(0,0,1.f);
	public Vector3f viewDirection = new Vector3f(0,0,-1.f);
	public Vector3f up = new Vector3f(0,1,0);
	
	public float closePlane = 0.01f;
	public float farPlane  = 200.f;
	
	public float aspectRatio = 1.f;
	public float fovRadians = GameMath.toRadians(60.f);
	
	public Matrix4f getProjectionMatrix()
	{
		return new Matrix4f()
				.perspective(fovRadians, aspectRatio, closePlane, farPlane);
	}
	
	public Matrix4f getViewMatrix()
	{
		return new Matrix4f().lookAt(position, position.add(viewDirection), up);
	}
	
	public Matrix4f getViewProjectionMatrix()
	{
		return getProjectionMatrix().mul(getViewMatrix());
	}
	
}
