package pcv.algo;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class Pipeline extends PipelineNative implements Callable<double[]> {

	public static final ArrayList<String> FILTER_NAMES = getFilterNames();
	
	private String start;
	private String end;
	private int flags;
	
	private int startIndex;
	private int endIndex;
	
	public Pipeline(String start, String end, int flags) {
		this.start = start;
		this.end = end;
		this.flags = flags;
		startIndex = FILTER_NAMES.indexOf(start);
		endIndex = FILTER_NAMES.indexOf(end);
		
		if (startIndex == -1) {
			throw new IllegalArgumentException("Start filter '" + start + "' does not exist.");
		}
		if (endIndex == -1) {
			throw new IllegalArgumentException("End filter '" + end + "' does not exist.");
		}
		if (startIndex > endIndex) {
			throw new IllegalStateException("End filter must not be before start filter.");
		}
		
		init(start, end, flags);
	}

	private static ArrayList<String> getFilterNames() {
		ArrayList<String> result = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		
		char[] chars = initFilterNames();
		
		for (int k = 0; k < chars.length; ++k) {
			if (chars[k] == 0) {
				result.add(sb.toString());
				sb = new StringBuilder();
			} else {
				sb.append(chars[k]);
			}
		}
		
		return result;
	}
	
	@Override
	public double[] call() throws Exception {
		return execute();
	}

	public String getStart() {
		return start;
	}

	public String getEnd() {
		return end;
	}

	public int getFlags() {
		return flags;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

}
