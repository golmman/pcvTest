package pcv.algo;


public class LinePicker {

	private LinePicker() {}
	
	public static native void init(int[] lines, int imgW, int imgH, int gridSize);
	public static native void release();
	public static native int[] pick(int x, int y, float hotSpotSize);

}
