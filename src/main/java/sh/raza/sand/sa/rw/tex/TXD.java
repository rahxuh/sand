package sh.raza.sand.sa.rw.tex;

public class TXD {
	private Raster[] rasters;
	private int deviceId;
	private int textureCount;
	
	public TXD(int tC) {
		textureCount = tC;
		deviceId = 0;
	}
	
	public TXD(int tC, int dI) {
		textureCount = tC;
		deviceId = dI;
	}
	
	public int getTextureCount() {
		return textureCount;
	}
	
	public int getDeviceId() {
		return deviceId;
	}
}
