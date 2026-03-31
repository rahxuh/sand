package sh.raza.sand.sa.rw.anim;

public class AnimObject {
	private String objName;
	private int type;
	private int numFrames;
	private int boneId;
	
	private AnimFrame[] frames;
	
	public AnimObject(String name, int t, int nF, int bI) {
		objName = name;
		type = t;
		numFrames = nF;
		boneId = bI;
		frames = new AnimFrame[numFrames];
	}
	
	public String getName() {
		return objName;
	}
	
	public int getType() {
		return type; // child = 3, root = 4
	}
	
	public int getNumFrames() {
		return numFrames;
	}
	
	public int getBoneId() {
		return boneId;
	}
	
	public AnimFrame[] getFrames() {
		return frames;
	}
	
	public void addFrame(int idx, AnimFrame frame) {
		frames[idx] = frame;
	}
}
