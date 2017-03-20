package parser;

public class IndexRange {
	public int startIndex;
	public int endIndex;
	
	public IndexRange(int startIndex, int endIndex) {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}
	
	public IndexRange() {
		this.startIndex = 0;
		this.endIndex = 0;
	}
}
