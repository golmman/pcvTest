package pcv.test;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.FlyByCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.debug.WireBox;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Line;
import com.jme3.scene.shape.Quad;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;


public class TestMain extends SimpleApplication {

	
	private Node shootables;
	private ActionListener actionListener = new ActionListener() {

		public void onAction(String name, boolean keyPressed, float tpf) {
			
			
			if (name.equals("Pick") && !keyPressed) {
				
				CollisionResults results = new CollisionResults();
				Ray ray = new Ray(cam.getLocation(), cam.getDirection());
				shootables.collideWith(ray, results);
				
				System.out.println("----- Collisions? " + results.size() + "-----");
				for (int i = 0; i < results.size(); i++) {
					float dist = results.getCollision(i).getDistance();
					Vector3f pt = results.getCollision(i).getContactPoint();
					String hit = results.getCollision(i).getGeometry().getName();
					System.out.println("* Collision #" + i);
					System.out.println("  You shot " + hit + " at " + pt + ", " + dist + " wu away.");
				}
				
				if (results.size() > 0) {
					
				} else {
					
				}
			}
		}
	};
	
	
	public static void main(String[] args) {
		AppSettings settings = new AppSettings(true);
		settings.setResolution(640, 480);
		settings.setTitle("pcvTest");
		
		TestMain app = new TestMain();

		app.setShowSettings(false);
		app.setSettings(settings);

		app.start();

	}

	@Override
	public void simpleInitApp() {
		// initialise input
		inputManager.addMapping("Pick", new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addListener(actionListener, "Pick");
		
		// load stuff
		Material matImage = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");//"Common/MatDefs/Light/Lighting.j3md");
		Material matRed = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		Material matBlue = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		Material matGreen = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		matRed.setColor("Color", ColorRGBA.Red);
		matBlue.setColor("Color", ColorRGBA.Blue);
		matGreen.setColor("Color", ColorRGBA.Green);
		assetManager.registerLocator("C:/Program Files (x86)/eclipse/workspace/pcvTest/", FileLocator.class);
		Texture image = assetManager.loadTexture("test.png");
		int imgW = image.getImage().getWidth();
		int imgH = image.getImage().getHeight();
		
		guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
		BitmapText ch = new BitmapText(guiFont, false);
		ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
		ch.setText("+"); // crosshairs
		ch.setLocalTranslation(settings.getWidth() / 2 - ch.getLineWidth() / 2, settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
		guiNode.attachChild(ch);
		
		// initialise camera
		flyCam.setEnabled(false);
		
		cam.setLocation(new Vector3f(0.5f*imgW, 0.5f*imgH, 2.0f*Math.max(imgW, imgH)));
		cam.setFrustumFar(10000000.0f);
		
		RTSCamera testCam = new RTSCamera(cam);
		RTSCameraAppState testCamAppState = new RTSCameraAppState(testCam);
		
		stateManager.detach(stateManager.getState(FlyCamAppState.class));
		stateManager.attach(testCamAppState);
		
		viewPort.setBackgroundColor(new ColorRGBA(240, 248, 255, 255));
		
		
		// create geometries
		Quad quad = new Quad(imgW, imgH);
		Line lineTest0 = new Line(new Vector3f(200, 300, 0), new Vector3f(0, 0, 0));
		Line lineTest1 = new Line(new Vector3f(-100, 300, 0), new Vector3f(0, 0, 0));
		lineTest1.setLineWidth(10.0f);
		
		Geometry geomQuad = new Geometry("Quad", quad);
		geomQuad.setLocalTranslation(0.0f, 0.0f, -0.1f);
		
		Geometry geomArrowX = new Geometry("ArrowX", new Arrow(new Vector3f(1.0f, 0.0f, 0.0f)));
		Geometry geomArrowY = new Geometry("ArrowY", new Arrow(new Vector3f(0.0f, 1.0f, 0.0f)));
		Geometry geomArrowZ = new Geometry("ArrowZ", new Arrow(new Vector3f(0.0f, 0.0f, 1.0f)));
		
		Geometry line = new Geometry("Line0", lineTest0);
		Geometry line2 = new Geometry("Line1", lineTest1);
		
		
		matImage.setTexture("ColorMap", image);
		
		
		//mat.setBoolean("UseMaterialColors", true); // Set some parameters, e.g. blue
		//mat.setColor("Ambient", ColorRGBA.Blue); // ... color of this object
		//mat.setColor("Diffuse", ColorRGBA.Blue); // ... color of light being reflected
		
		geomQuad.setMaterial(matImage);
		geomArrowX.setMaterial(matRed);
		geomArrowY.setMaterial(matGreen);
		geomArrowZ.setMaterial(matBlue);
		
		line.setMaterial(matBlue);
		line2.setMaterial(matBlue);

		//AmbientLight al = new AmbientLight();
		//al.setColor(ColorRGBA.White.mult(0.2f));

		//DirectionalLight sun = new DirectionalLight();
		//sun.setColor(ColorRGBA.White);
		//sun.setDirection(new Vector3f(-0.25f, -0.3f, -0.5f).normalizeLocal());

		//rootNode.addLight(sun);
		//rootNode.addLight(al);
		
		shootables = new Node("Pickables");
		rootNode.attachChild(shootables);
		
		shootables.attachChild(geomQuad);
		shootables.attachChild(geomArrowX);
		shootables.attachChild(geomArrowY);
		shootables.attachChild(geomArrowZ);
		
		shootables.attachChild(line);
		shootables.attachChild(line2);
	}

	@Override
	public void simpleUpdate(float tpf) {
		
	}

	@Override
	public void simpleRender(RenderManager rm) {
		
	}
}