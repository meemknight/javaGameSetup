
import com.sun.javafx.geom.*;
import org.lwjgl.ovr.OVRMatrix4f;

import java.io.BufferedReader;
import java.lang.Math;
import java.util.Vector;

import org.joml.*;

public class Camera
{
	
	public Vector3f position = new Vector3f(0,0,5.f);
	public Vector3f viewDirection = new Vector3f(0,0,-1.f);
	public Vector3f up = new Vector3f(0,1,0);
	
	public float closePlane = 0.01f;
	public float farPlane  = 100.f;
	
	public float aspectRatio = 1.f;
	public float fovRadians = GameMath.toRadians(60.f);
	
	public Matrix4f getProjectionMatrix()
	{
		return new Matrix4f()
				.perspective(fovRadians, aspectRatio, closePlane, farPlane);
	}
	
	public Matrix4f getViewMatrix()
	{
		Vector3f newVector = new Vector3f();
		position.add(viewDirection, newVector);
		return new Matrix4f().lookAt(position, newVector, up);
	}
	
	public Matrix4f getViewProjectionMatrix()
	{
		//Matrix4f m = new Matrix4f()
		//		.perspective((float) GameMath.toRadians(45.0f), 1.0f, 0.01f, 100.0f)
		//		.lookAt(0.0f, 0.0f, 5.0f,
		//				0.0f, 0.0f, 0.0f,
		//				0.0f, 1.0f, 0.0f);
		//return m;
		//Matrix4f ret = new Matrix4f();
		return getProjectionMatrix().mul(getViewMatrix());
		//return ret;
	}
	
}
