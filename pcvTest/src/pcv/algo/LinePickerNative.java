package pcv.algo;

public class LinePickerNative {

	protected LinePickerNative() {}
	protected static native void init(int[] lines, int imgW, int imgH, int gridSize);
	protected static native void release();
	protected static native int[] pick(int x, int y, float hotSpotSize);

}
