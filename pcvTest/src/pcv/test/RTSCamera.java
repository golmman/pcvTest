package pcv.test;

import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

class CameraInput {
	public final static String RTSCAM_LEFT 			= "RTSCAM_Left";
	public final static String RTSCAM_RIGHT 		= "RTSCAM_Right";
	public final static String RTSCAM_UP 			= "RTSCAM_Up";
	public final static String RTSCAM_DOWN 			= "RTSCAM_Down";
	public final static String RTSCAM_RISE 			= "RTSCAM_Rise";
	public final static String RTSCAM_LOWER 		= "RTSCAM_Lower";
	public final static String RTSCAM_MOVEDRAG 		= "RTSCAM_MoveDrag";
	public final static String RTSCAM_ROTATEDRAG 	= "RTSCAM_RotateDrag";
}

/**
 * A first person view camera controller. After creation, you must register the
 * camera controller with the dispatcher using #registerWithDispatcher().
 *
 * Controls: - Move the mouse to rotate the camera - Mouse wheel for zooming in
 * or out - WASD keys for moving forward/backward and strafing - QZ keys raise
 * or lower the camera
 */
public class RTSCamera implements AnalogListener, ActionListener {

	private static String[] mappings = new String[] { 
			CameraInput.RTSCAM_LEFT, 
			CameraInput.RTSCAM_RIGHT, 
			CameraInput.RTSCAM_UP, 
			CameraInput.RTSCAM_DOWN,
			CameraInput.RTSCAM_RISE, 
			CameraInput.RTSCAM_LOWER,
			CameraInput.RTSCAM_MOVEDRAG,
			CameraInput.RTSCAM_ROTATEDRAG };

	protected Camera cam;
	protected Vector3f initialBackVec;
	protected float rotationSpeed = 3.0f;
	protected float moveSpeedBase = 3.0f;
	protected float moveSpeed = 3.0f;
	protected boolean enabled = true;
	protected boolean canMove = false;
	protected boolean canRotate = false;
	protected InputManager inputManager;

	/**
	 * Creates a new FlyByCamera to control the given Camera object.
	 * 
	 * @param cam
	 */
	public RTSCamera(Camera cam) {
		this.cam = cam;
		initialBackVec = cam.getDirection().clone().mult(-1.0f);
		
		// adjust moveSpeed to camera height
		float h = cam.getFrustumTop();
		float near = cam.getFrustumNear();
		float z = cam.getLocation().z;
		moveSpeed = 2.0f * z * h/near;
	}

	/**
	 * Sets the move speed. The speed is given in world units per second.
	 * 
	 * @param moveSpeed
	 */
	public void setMoveSpeedBase(float moveSpeed) {
		this.moveSpeedBase = moveSpeed;
	}

	/**
	 * Gets the move speed. The speed is given in world units per second.
	 * 
	 * @return moveSpeed
	 */
	public float getMoveSpeedBase() {
		return moveSpeedBase;
	}

	/**
	 * Sets the rotation speed.
	 * 
	 * @param rotationSpeed
	 */
	public void setRotationSpeed(float rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
	}

	/**
	 * Gets the move speed. The speed is given in world units per second.
	 * 
	 * @return rotationSpeed
	 */
	public float getRotationSpeed() {
		return rotationSpeed;
	}

	/**
	 * @param enable
	 *            If false, the camera will ignore input.
	 */
	public void setEnabled(boolean enable) {
		if (enabled && !enable) {
			if (inputManager != null) {
				inputManager.setCursorVisible(true);
			}
		}
		enabled = enable;
	}

	/**
	 * @return If enabled
	 * @see FlyByCamera#setEnabled(boolean)
	 */
	public boolean isEnabled() {
		return enabled;
	}


	/**
	 * Registers the FlyByCamera to receive input events from the provided
	 * Dispatcher.
	 * 
	 * @param inputManager
	 */
	public void registerWithInput(InputManager inputManager) {
		this.inputManager = inputManager;

		inputManager.addMapping(CameraInput.RTSCAM_LEFT, new MouseAxisTrigger(MouseInput.AXIS_X, true));
		inputManager.addMapping(CameraInput.RTSCAM_RIGHT, new MouseAxisTrigger(MouseInput.AXIS_X, false));
		inputManager.addMapping(CameraInput.RTSCAM_UP, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
		inputManager.addMapping(CameraInput.RTSCAM_DOWN, new MouseAxisTrigger(MouseInput.AXIS_Y, true));
		inputManager.addMapping(CameraInput.RTSCAM_MOVEDRAG, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addMapping(CameraInput.RTSCAM_ROTATEDRAG, new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
		inputManager.addMapping(CameraInput.RTSCAM_RISE, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
		inputManager.addMapping(CameraInput.RTSCAM_LOWER, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));

		inputManager.addListener(this, mappings);

		inputManager.setCursorVisible(true);
	}

	/**
	 * Unregisters the FlyByCamera from the event Dispatcher.
	 */
	public void unregisterInput() {
		if (inputManager == null) {
			return;
		}

		for (String s : mappings) {
			if (inputManager.hasMapping(s)) {
				inputManager.deleteMapping(s);
			}
		}

		inputManager.removeListener(this);
		inputManager.setCursorVisible(true);
	}

	protected void rotateCamera(float value, Vector3f axis) {
		if (!canRotate || canMove) return;

		Matrix3f mat = new Matrix3f();
		mat.fromAngleNormalAxis(rotationSpeed * value, axis);

		Vector3f up = cam.getUp();
		Vector3f left = cam.getLeft();
		Vector3f dir = cam.getDirection();

		mat.mult(up, up);
		mat.mult(left, left);
		mat.mult(dir, dir);

		Quaternion q = new Quaternion();
		q.fromAxes(left, up, dir);
		q.normalizeLocal();

		cam.setAxes(q);
	}

	protected void riseCamera(float value) {
		
		float h = cam.getFrustumTop();
		float near = cam.getFrustumNear();
		float v = 0.1f * value * cam.getLocation().z;
		float z = cam.getLocation().z + v;
		
		// adjust moveSpeed to camera height
		moveSpeed = 2.0f * z * h/near;
		
		Vector3f vel = new Vector3f(0, 0, v);//value * moveSpeed);
		Vector3f pos = cam.getLocation().clone();

		pos.addLocal(vel);

		cam.setLocation(pos);
	}

	protected void moveCamera(float value, boolean sideways) {
		if (!canMove || canRotate) return;

		Vector3f vel = new Vector3f();
		Vector3f pos = cam.getLocation().clone();

		if (sideways) {
			cam.getLeft(vel);
		} else {
			// cam.getDirection(vel);
			cam.getUp(vel);
		}
		vel.multLocal(value * moveSpeed);

		pos.addLocal(vel);

		cam.setLocation(pos);
	}

	public void onAnalog(String name, float value, float tpf) {
		if (!enabled)
			return;

		if (name.equals(CameraInput.RTSCAM_LEFT)) {
			rotateCamera(value, initialBackVec);
			moveCamera(-value, true);
		} else if (name.equals(CameraInput.RTSCAM_RIGHT)) {
			rotateCamera(-value, initialBackVec);
			moveCamera(value, true);
		} else if (name.equals(CameraInput.RTSCAM_UP)) {
			moveCamera(-value, false);
		} else if (name.equals(CameraInput.RTSCAM_DOWN)) {
			moveCamera(value, false);
		} else if (name.equals(CameraInput.RTSCAM_RISE)) {
			riseCamera(value);
		} else if (name.equals(CameraInput.RTSCAM_LOWER)) {
			riseCamera(-value);
		}
	}

	public void onAction(String name, boolean value, float tpf) {
		if (!enabled)
			return;

		if (name.equals(CameraInput.RTSCAM_MOVEDRAG)) {
			canMove = value;
			//inputManager.setCursorVisible(!value);
		} else if (name.equals(CameraInput.RTSCAM_ROTATEDRAG)) {
			canRotate = value;
			//inputManager.setCursorVisible(!value);
		}
	}

}
