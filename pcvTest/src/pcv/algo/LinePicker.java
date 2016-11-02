package pcv.algo;

import java.util.ArrayList;

import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Line;

import pcv.test.RTSCamera;
import pcv.test.TestMain;

public class LinePicker {

	private LinePicker() {}
	private static native void init(int[] lines, int imgW, int imgH, int gridSize);
	public static native void release(); // TODO: rename to close()
	public static native int[] pick(int x, int y, float hotSpotSize);
	
	
	
	
	private static final float LINE_Z = 0.0f;
	private static final String PICK_MAPPING = "CursorPick";
	
	private static RTSCamera rtsCam;
	
	private static Node nodeLines;
	private static Material matDormant;
	private static Material matSelected;
	
	private static ArrayList<Geometry> geomLines;
	private static ArrayList<Geometry> geomLinesSelected;
	
	private static ActionListener actionListener = new ActionListener() {

		@Override
		public void onAction(String name, boolean isPressed, float tpf) {
			if (name.equals(PICK_MAPPING) && !isPressed) {
				
				for (Geometry line : geomLinesSelected) {
					line.setMaterial(matDormant);
				}
				geomLinesSelected.clear();
				
				Vector3f plane = rtsCam.getPlaneAtCursor();
				int[] lines = LinePicker.pick((int)plane.x, (int)plane.y, rtsCam.getHotspotSize());
				
				for (int j = 0; j < lines.length; j++) {
					Geometry line = geomLines.get(lines[j]);
					
					line.setMaterial(matSelected);
					geomLinesSelected.add(line);
					
					System.out.println(line.getName());
				}
				
			}
		}
		
	};
	
	
	public static void init(TestMain app, int[] lines, int imgW, int imgH, int gridSize) {
		init(lines, imgW, imgH, gridSize);
		
		rtsCam = app.getRtsCam();
		
		app.getInputManager().addMapping(PICK_MAPPING, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		app.getInputManager().addListener(actionListener, PICK_MAPPING);
		
		matDormant = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		matDormant.setColor("Color", ColorRGBA.Blue);
		
		matSelected = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		matSelected.setColor("Color", ColorRGBA.Red);
		
		
		geomLinesSelected = new ArrayList<Geometry>();
		
		nodeLines = new Node("nodeLines");
		
		geomLines = linesToGeometry(lines);
		
		app.getRootNode().attachChild(nodeLines);
	}
	
	private static ArrayList<Geometry> linesToGeometry(int[] lines) {
		ArrayList<Geometry> result = new ArrayList<Geometry>();
		
		for (int i = 0; i < lines.length; i += 4) {
			Line line = new Line(
					new Vector3f(lines[i], lines[i+1], LINE_Z), 
					new Vector3f(lines[i+2], lines[i+3], LINE_Z));
			line.setLineWidth(2.0f);
			
			Geometry geomLine = new Geometry("line" + i/4, line);
			geomLine.setMaterial(matDormant);
			
			nodeLines.attachChild(geomLine);
			result.add(geomLine);
		}
		return result;
	}
	
	

}
