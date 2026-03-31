package sh.raza.sand.sa.rw.anim;

public class Animation {
	private String animName;
	private int numObjects;
	private int frameDataSize;
	
	private AnimObject[] objects;
	
	public Animation(String name, int objs, int size) {
		animName = name;
		numObjects = objs;
		frameDataSize = size;
		objects = new AnimObject[numObjects];
	}
	
	public String getAnimationName() {
		return animName;
	}
	
	public int getNumObjects() {
		return numObjects;
	}
	
	public int getFrameDataSize() {
		return frameDataSize;
	}
	
	public AnimObject[] getObjects() {
		return objects;
	}
	
	public void addObject(int idx, AnimObject object) {
		objects[idx] = object;
	}
	
}
