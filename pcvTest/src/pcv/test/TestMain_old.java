package pcv.test;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Line;


public class TestMain_old extends SimpleApplication {

	public static void main(String[] args) {
		TestMain_old app = new TestMain_old();

		app.setShowSettings(false);

		app.start();

	}

	@Override
	public void simpleInitApp() {
		flyCam.setMoveSpeed(4.0f);
		flyCam.setRotationSpeed(2.0f);
		
		Box b = new Box(1, 1, 1);
		Geometry geom = new Geometry("Box", b);
		Geometry line = new Geometry("Line", new Line(new Vector3f(2, 2, 0), new Vector3f(0, 0, 0)));
		Geometry line2 = new Geometry("Line", new Line(new Vector3f(-2, 2, 0), new Vector3f(0, 0, 0)));
		

		Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");// "Common/MatDefs/Misc/Unshaded.j3md");
		// mat.setColor("Color", ColorRGBA.White);
		mat.setBoolean("UseMaterialColors", true); // Set some parameters, e.g. blue
		mat.setColor("Ambient", ColorRGBA.Blue); // ... color of this object
		mat.setColor("Diffuse", ColorRGBA.Blue); // ... color of light being reflected
		
		geom.setMaterial(mat);
		line.setMaterial(mat);
		line2.setMaterial(mat);

		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(0.2f));

		DirectionalLight sun = new DirectionalLight();
		sun.setColor(ColorRGBA.White);
		sun.setDirection(new Vector3f(-0.25f, -0.3f, -0.5f).normalizeLocal());

		rootNode.addLight(sun);
		rootNode.addLight(al);
		
		rootNode.attachChild(geom);
		rootNode.attachChild(line);
		rootNode.attachChild(line2);
	}

	@Override
	public void simpleUpdate(float tpf) {
		// TODO: add update code
	}

	@Override
	public void simpleRender(RenderManager rm) {
		// TODO: add render code
	}
}