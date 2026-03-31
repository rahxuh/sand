package sh.raza.sand.sa.rw.anim;

public class AnimFrame {
	private int time;
	private int qX;
	private int qY;
	private int qZ;
	private int qW;
	
	// root only
	private int tX;
	private int tY;
	private int tZ;
	
	public AnimFrame(int t, int qx, int qy, int qz, int qw) {
		time = t;
		
		qX = qx;
		qY = qy;
		qZ = qz;
		qW = qw;
		
		tX = -1;
		tY = -1;
		tZ = -1;
	}
	
	public AnimFrame(int t, int qx, int qy, int qz, int qw, int tx, int ty, int tz) {
		time = t;
		
		qX = qx;
		qY = qy;
		qZ = qz;
		qW = qw;
		
		tX = tx;
		tY = ty;
		tZ = tz;
	}
	
	public int[] getQuaternionCoords() {
		return new int[] {qX, qY, qZ, qW};
	}
	
	public int[] getTranslationCoords() {
		return new int[] {tX, tY, tZ};
	}
	
	public int getTime() {
		return time;
	}
}
