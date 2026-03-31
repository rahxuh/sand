package sh.raza.sand.sa.rw.anim;

public class IFP {
	private String identifier;
	private int offset;
	private String name;
	private int numAnimations;
	
	private Animation[] anims;
	
	public IFP(String id, int off, String internal, int numAnims) {
		identifier = id;
		offset = off;
		name = internal;
		numAnimations = numAnims;
		anims = new Animation[numAnims];
	}
	
	public String getIdentifier() {
		return identifier; // should always be ANP3
	}
	
	public int getOffset() {
		return offset;
	}
	
	public String getName() {
		return name;
	}
	
	public int getNumAnimations() {
		return numAnimations;
	}
	
	public Animation[] getAnimations() {
		return anims;
	}
	
	public void addAnimation(int idx, Animation anim) {
		anims[idx] = anim;
	}
}
