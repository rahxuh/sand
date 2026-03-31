package sh.raza.sand.sa.loaders;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import sh.raza.sand.sa.rw.anim.AnimFrame;
import sh.raza.sand.sa.rw.anim.AnimObject;
import sh.raza.sand.sa.rw.anim.Animation;
import sh.raza.sand.sa.rw.anim.IFP;
import sh.raza.sand.util.FileData;

public class IFPLoader {
	private DataInputStream stream;
	
	public IFP parseAnimation(String filename) {
		try {
			stream = new DataInputStream(new FileInputStream(filename));
			
			// header -- IFP header is completely different from dffs or txds
			String fourcc = FileData.readString(stream, 4);
			int offset = FileData.readUInt32(stream);
			String internalName = FileData.readString(stream, 24);
			int numAnims = FileData.readUInt32(stream);
			
			IFP ifp = new IFP(fourcc, offset, internalName, numAnims);
			
			for (int a = 0; a < numAnims; a++) {
				Animation anim = readAnimation(stream);
				ifp.addAnimation(a, anim);
			}
			
			return ifp;
		} catch (FileNotFoundException e) {
			e.printStackTrace(System.out);
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
		
		return null;
	}
	
	public Animation readAnimation(DataInputStream stream) throws IOException {
		String animName = FileData.readString(stream, 24);
		int numObjs = FileData.readUInt32(stream);
		int frameDataSize = FileData.readUInt32(stream);
		FileData.readUInt32(stream); // unknown, always 1
		
		Animation anim = new Animation(animName, numObjs, frameDataSize);
		
		for (int o = 0; o < numObjs; o++) {
			AnimObject obj = readObject(stream);
			anim.addObject(o, obj);
		}
		
		return anim;
	}
	
	public AnimObject readObject(DataInputStream stream) throws IOException {
		String objName = FileData.readString(stream, 24);
		int frameType = FileData.readUInt32(stream); // child = 3, root = 4
		int numFrames = FileData.readUInt32(stream);
		int boneId = FileData.readUInt32(stream);
		
		AnimObject obj = new AnimObject(objName, frameType, numFrames, boneId);
		
		for (int f = 0; f < numFrames; f++) {
			AnimFrame frame = readFrame(stream, frameType);
			obj.addFrame(f, frame);
		}
		
		return obj;
		
	}
	
	public AnimFrame readFrame(DataInputStream stream, int type) throws IOException {
		AnimFrame frame; 
		
		if (type == 4) {
			// root structure
			int qX = FileData.readUInt16(stream); // quaternion X
			int qY = FileData.readUInt16(stream); // quaternion Y
			int qZ = FileData.readUInt16(stream); // quaternion Z
			int qW = FileData.readUInt16(stream); // quaternion W

			int time = FileData.readUInt16(stream); // in seconds

			int tX = FileData.readUInt16(stream); // translation X
			int tY = FileData.readUInt16(stream); // translation Y
			int tZ = FileData.readUInt16(stream); // translation Z
			
			frame = new AnimFrame(time, qX, qY, qZ, qW, tX, tY, tZ);
		} else {
			int qX = FileData.readUInt16(stream); // quaternion X
			int qY = FileData.readUInt16(stream); // quaternion Y
			int qZ = FileData.readUInt16(stream); // quaternion Z
			int qW = FileData.readUInt16(stream); // quaternion W

			int time = FileData.readUInt16(stream); // in seconds
			
			frame = new AnimFrame(time, qX, qY, qZ, qW);
		}
		
		return frame;
	}
}
