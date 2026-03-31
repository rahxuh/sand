package sh.raza.sand.sa.rw.tex;

public class Raster {
	private int platformId;
	private int filterFlags;
	private String name;
	private String maskName;
	
	private int rasterFormat; // alpha flags
	private int d3dFormat;
	
	private int[] widths;
	private int[] heights;
	private int depth;
	private int mipmapCount; // numLevels
	
	private int imgProps;
	
	public Raster(int pId, int fFlags, String nm, String mask, int rF, int dF) {
		platformId = pId;
		filterFlags = fFlags;
		name = nm;
		maskName = mask;
		rasterFormat = rF;
		d3dFormat = dF;
	}
	
	public int getPlatformId() {
		return platformId;
	}
	
	public int getFilterFlags() {
		return filterFlags;
	}
	
	// 61:71, https://github.com/Timic3/rw-parser/blob/47f9ab58f554a96dbd4500e679f8300b849ae447/src/renderware/txd/TxdParser.ts#L70
	public int getFilterMode() {
		return (filterFlags & 0xFF);
	}
	
	public int getUAddressing() {
		return (filterFlags & 0xF00) >> 8;
	}
	
	public int getVAddressing() {
		return (filterFlags & 0xF000) >> 12;
	}
	
	public String getName() {
		return name;
	}
	
	public String getMaskName() {
		return maskName;
	}
	
	public int getRasterFormat() {
		return rasterFormat;
	}
	
	// just to conform to old naming 
	public int getAlphaFlags() {
		return rasterFormat;
	}
	
	public int getD3DFormat() {
		return d3dFormat;
	}
	
	public String getD3DFormatStylized() {
		// TODO: finish this
		return "";
	}
	
	public int[] getWidths() {
		return widths;
	}
	
	public int getWidth(int idx) {
		return widths[idx];
	}
	
	public int[] getHeights() {
		return heights;
	}
	
	public int getHeight(int idx) {
		return heights[idx];
	}
	
	public int getDepth() {
		return depth;
	}
	
	public void setDepth(int d) {
		depth = d; 
	}
	
	// again, just to conform to old naming (this name is better idk why they changed it in doc)
	public int getMipmapCount() {
		return mipmapCount;
	}
	
	public int getNumLevels() {
		return mipmapCount;
	}
	
	public void setMipmapCount(int mC) {
		mipmapCount = mC;
	}
	
	public int getImageProps() {
		return imgProps;
	}
	
	public void setImageProps(int props) {
		imgProps = props;
	}
	
	// 147:161,
	// https://github.com/Timic3/rw-parser/blob/47f9ab58f554a96dbd4500e679f8300b849ae447/src/renderware/txd/TxdParser.ts#L91
	// https://github.com/Parik27/DragonFF/blob/ead5572256a609a21210e242cc2e9e7ca870829f/gtaLib/txd.py#L60
	public boolean hasAlpha() {
		return (imgProps & 0b0001) != 0;
	}
	
	public boolean hasCubeTexture() {
		return (imgProps & 0b0010) != 0;
	}
	
	public boolean hasAutoMipMaps() {
		return (imgProps & 0b0100) != 0;
	}
	
	public boolean isCompressed() {
		return (imgProps & 0b1000) != 0;
	}	
}
