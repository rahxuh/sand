package sh.raza.sand.sa.loaders;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import io.github.memo33.jsquish.Squish;
import io.github.memo33.jsquish.Squish.CompressionType;
import sh.raza.sand.sa.rw.Header;
import sh.raza.sand.sa.rw.RWFile;
import sh.raza.sand.util.FileData;

public class TXDLoader extends RWFile {
	// projects that helped and txd documentation:
	// -- https://gtamods.com/wiki/RenderWare_binary_stream_file#TXD
	// -- https://github.com/Parik27/DragonFF/blob/master/gtaLib/txd.py
	// -- https://github.com/aap/rwtools/blob/master/src/txdread.cpp
	// -- https://github.com/Timic3/rw-parser/blob/master/src/renderware/txd/TxdParser.ts
	// -- https://github.com/aap/librw/blob/05db81b92f30484a536990a6b792166102405c51/src/d3d/d3d9.cpp#L704
	// --> these links also contributed to the rw/tex/* files
	private DataInputStream stream;
	private TXDLoader instance = new TXDLoader();
	
	public TXDLoader getInstance() {
		return instance;
	}
	
	public DataInputStream getStream() {
		return stream;
	}
	
	public void parseTexture(String filename) {
		// TODO: create central parse function
		try {
			stream = new DataInputStream(new FileInputStream(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace(System.out);
		}
	}
	
	public void readTextureDictionary() throws IOException {
		// texture dictionary struct:
		// -- Renderware 3.0 - 3.5
		// struct rwTexDictionary {
	    // unsigned int textureCount;    // determines count of Raster sections
	    // };
		// -- Renderware 3.6+
		// struct rwTexDictionary {
	    // unsigned short textureCount;   // determines count of Rastersections
	    // unsigned short deviceId;       // 1 for D3D8, 2 for D3D9, 6 for PlayStation 2, 8 for XBOX
	    // };
		Header struct = readHeader(stream);
		int textureCount = 0;
		int deviceId = 0;
		
		if (struct.getVersion() < 0x36000) {
			textureCount = FileData.readUInt32(stream);
		} else {
			textureCount = FileData.readUInt16(stream);
			deviceId = FileData.readUInt16(stream);
		}
		
		for (int i = 0; i < textureCount; i++) {
			// raster header
			readHeader(stream);
			readRaster();
		}
	}
	
	public void readRaster() throws IOException {
		// Raster:
		// -- TextureFormat:
		// struct {
        // unsigned int platformId;
        // unsigned int filterMode : 8;
        // unsigned int uAddressing : 4;
        // unsigned int vAddressing : 4;
        // unsigned int pad : 16;        // should be zeroed out for sanity
        // char name[32];
        // char maskName[32];
        // } 
		Header struct = readHeader(stream);
		int platformId = FileData.readUInt32(stream);
		int filterFlags = FileData.readUInt32(stream);
		// https://github.com/Timic3/rw-parser/blob/47f9ab58f554a96dbd4500e679f8300b849ae447/src/renderware/txd/TxdParser.ts#L70
		int filterMode = (filterFlags & 0xFF);
		int uAddressing = (filterFlags & 0xF00) >> 8;
		int vAddressing = (filterFlags & 0xF000) >> 12;
		String name = FileData.readString(stream, 32);
		String maskName = FileData.readString(stream, 32);
		
		// Raster Format (alpha flags?)
		int rasterFormat = FileData.readUInt32(stream);
		
		int d3dFormat = 0;
		// union {
        // D3DFORMAT d3dFormat; // SA, see D3DFORMAT on MSDN
        // unsigned int hasAlpha // GTA3 & VC
    	// };
		// --
		// check is only here to agree with the doc -- platform will always be SA
		if (platformId == 9) {
			// SA
			d3dFormat = FileData.readUInt32(stream);	
		} else {
			// GTA3/VC -- never going to occur
			FileData.readUInt32(stream);
		}
		
		int width = FileData.readUInt16(stream);
		int height = FileData.readUInt16(stream);
		int depth = FileData.readUInt8(stream);
		int numLevels = FileData.readUInt8(stream); // mipmap count
		int rasterType = FileData.readUInt8(stream); // always 4
		
		int props = FileData.readUInt8(stream);
		// https://github.com/Timic3/rw-parser/blob/47f9ab58f554a96dbd4500e679f8300b849ae447/src/renderware/txd/TxdParser.ts#L91
		// https://github.com/Parik27/DragonFF/blob/ead5572256a609a21210e242cc2e9e7ca870829f/gtaLib/txd.py#L60
		boolean alpha = (props & 0b0001) != 0;
		boolean cubeTexture = (props & 0b0010) != 0;
		boolean autoMipMaps = (props & 0b0100) != 0;
		boolean compressed = (props & 0b1000) != 0;
		
		if ((rasterFormat & 0x2000) != 0) {
			// "// - 256 for FORMAT_EXT_PAL8 on any configuration"
			// pc only txds so no ps2/xbox support yet :(
			
			// TODO: finish rasters with palletes 
			
			int[][] pallete = new int[256][4];
			
			for (int i = 0; i < 256*4; i++) { 
				pallete[i][0] = FileData.readUInt8(stream);
				pallete[i][1] = FileData.readUInt8(stream);
				pallete[i][2] = FileData.readUInt8(stream);
				pallete[i][3] = FileData.readUInt8(stream);
			}
		} else {
			// repeated for each mipmap:
			// {
			//     // mipWidth is the texture width, halfed for each level
			//     // mipHeight is the texture height, halfed for each level

			//     unsigned int rasterSize;  // size of raster data in bytes, see Texture Data Layout section
			//     colorType_t colors[ mipWidth * mipHeight ];
			// }
			int mipW = width;
			int mipH = height;
			//ArrayList<int[][]> rasters = new ArrayList<int[][]>();
			ArrayList<byte[]> rasters = new ArrayList<byte[]>();

			for (int m = 0; m < numLevels; m++) {
				int rasterSize = FileData.readUInt32(stream);
				//int[][] raster = new int[mipW*mipH][4];
				byte[] raster = new byte[rasterSize];

				for (int i = 0; i < rasterSize; i++) {
					raster[i] = stream.readByte();
				}
				
				// for (int d = 0; d < rasterSize/4; d++) {
				// 	raster[d][0] = FileData.readUInt8(stream);
				// 	raster[d][1] = FileData.readUInt8(stream);
				// 	raster[d][2] = FileData.readUInt8(stream);
				// 	raster[d][3] = FileData.readUInt8(stream);
				// }
				
				rasters.add(raster);
				mipW /= 2;
				mipH /= 2;
			}
			
			// TODO: fix spaghetti code
			if (compressed) {
				int num = ((int)Math.ceil((double)width/4))*((int)Math.ceil((double)height/4));
				byte[] blocks = new byte[num*8];
				System.arraycopy(rasters.get(0), 0, blocks, 0, blocks.length);
				byte[] b = Squish.decompressImage(rasters.get(0), width, height, blocks, CompressionType.DXT1);
				rasters.set(0, b);
			}

			BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			//int[][] raster = rasters.get(0);
			byte[] raster = rasters.get(0);
			int k = 0;
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					Color c = new Color(raster[k] & 0xFF, raster[k+1] & 0xFF, raster[k+2] & 0xFF);
					
					bi.setRGB(j, i, c.getRGB());
					k+=4;
				}
			}
			
			File f = new File("out.png");
			ImageIO.write(bi, "png", f);
		}
			
	}
}
