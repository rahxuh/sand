package sh.raza.sand.sa.rw;

import java.util.List;

import sh.raza.sand.sa.rw.extension.Extension;
import sh.raza.sand.sa.rw.extension.NodeName;
import sh.raza.sand.sa.rw.extension.plg.HAnim;

public class Frame extends ExtChunk {
	private float[][] matrix;
	private float[] position;
	private int frameIndex;
	private int flags;
	private float[] ltm;
	
	public Frame() {
		matrix = new float[3][3];
		position = new float[3];
		frameIndex = -2;
		flags = -1;
	}
	
	public float[][] getRotation() {
		return matrix;
	}
	
	public float[] getRotationAs1d() {
		float[] mat = new float[9];
		int idx = 0;
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				mat[idx] = matrix[i][j];
				idx++;
			}
		}
		
		return mat;
	}
	
	public float[] getPosition() {
		return position;
	}
	
	public int getFrameIndex() {
		return frameIndex;
	}
	
	public int getFlags() {
		return flags;
	}
	
	public HAnim getHAnim() {
		List<Extension> exts = getExtensions();
		
		for (int i = 0; i < exts.size(); i++) {
			if (exts.get(i).getExtensionType().equals("HANIM"))
				return (HAnim)exts.get(i);
		}
		
		return null;
	}
	
	public String getName() {
		List<Extension> exts = getExtensions();
		
		for (int i = 0; i < exts.size(); i++) {
			if (exts.get(i).getExtensionType().equals("FRAME"))
				return ((NodeName)exts.get(i)).getName();
		}
		
		return "NO_NAME";
	}
	
	public void addVector(int idx, float x, float y, float z) {
		matrix[idx][0] = x;
		matrix[idx][1] = y;
		matrix[idx][2] = z;
	}
	
	public void setPosition(float x, float y, float z) { 
		position[0] = x;
		position[1] = y;
		position[2] = z;
	}
	
	public void setFlags(int flag) {
		flags = flag;
	}
	
	public void setFrameIndex(int idx) {
		frameIndex = idx;
	}
	
	public float[] getMM() {
		float[] rot = getRotationAs1d();
		
		float[] transform = new float[] {
				rot[0], rot[1], rot[2], 0,
				rot[3], rot[4], rot[5], 0,
				rot[6], rot[7], rot[8], 0,
				position[0], position[1], position[2], 1
		};

		return transform; 
	}
	
	public float[] getLTM() {
		return ltm;
	}
	
	public void setLTM(float[] lcltfm) {
		ltm = lcltfm;
	}
}
