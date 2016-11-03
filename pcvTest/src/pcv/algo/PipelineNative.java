package pcv.algo;

public class PipelineNative {

	protected PipelineNative() {}
	protected static native char[] initFilterNames();
	protected static native void init(String start, String end, int flags);
	protected static native double[] execute();
	//protected static native String fetchString();
	//protected static native int[] fetchIntArray();

}
