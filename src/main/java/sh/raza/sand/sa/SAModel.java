package sh.raza.sand.sa;

import net.minecraft.util.math.vector.Matrix4f;
import sh.raza.sand.sa.rw.Atomic;
import sh.raza.sand.sa.rw.Frame;
import sh.raza.sand.sa.rw.extension.plg.Skin;
import sh.raza.sand.sa.rw.geom.Geometry;
import java.util.Arrays;
import java.util.stream.IntStream;

public class SAModel {
	private Frame[] frameList;
	private Geometry[] geomList;
	private Atomic[] atomList;
	
	public Frame[] getFrameList() {
		return frameList;
	}
	
	public Geometry[] getGeometryList() {
		return geomList;
	}
	
	public Atomic[] getAtomicList() {
		return atomList;
	}
	
	public void setFrameList(Frame[] frames) {
		frameList = frames;
		createLTMs();
	}
	
	public void setGeometryList(Geometry[] geoms) {
		geomList = geoms;
	}
	
	public void setAtomicList(Atomic[] atoms) { 
		atomList = atoms;
	}
	
	public void createLTMs() {
		for (int i = 0; i < frameList.length; i++) {
			Frame frame = frameList[i];

			if (frame.getFrameIndex() == -1) {
				frame.setLTM(frame.getMM());
				continue;
			}

			Frame parent = frameList[frame.getFrameIndex()];
			float[] mm = frame.getMM();
			float[] pltm = parent.getLTM();

			float[] ltm = multMatrix(mm, pltm);

			frame.setLTM(ltm);
		}
	}

	// TODO: move this to util or just use Matrix4f
	public float[] multMatrix(float[] mat1, float[] mat2) {
		float f0 = mat1[0]*mat2[0]+mat1[1]*mat2[4]+mat1[2]*mat2[8]+mat1[3]*mat2[12];
		float f1 = mat1[0]*mat2[1]+mat1[1]*mat2[5]+mat1[2]*mat2[9]+mat1[3]*mat2[13];
		float f2 = mat1[0]*mat2[2]+mat1[1]*mat2[6]+mat1[2]*mat2[10]+mat1[3]*mat2[14];
		float f3 = mat1[0]*mat2[3]+mat1[1]*mat2[7]+mat1[2]*mat2[11]+mat1[3]*mat2[15];
		float f4 = mat1[4]*mat2[0]+mat1[5]*mat2[4]+mat1[6]*mat2[8]+mat1[7]*mat2[12];
		float f5 = mat1[4]*mat2[1]+mat1[5]*mat2[5]+mat1[6]*mat2[9]+mat1[7]*mat2[13];
		float f6 = mat1[4]*mat2[2]+mat1[5]*mat2[6]+mat1[6]*mat2[10]+mat1[7]*mat2[14];
		float f7 = mat1[4]*mat2[3]+mat1[5]*mat2[7]+mat1[6]*mat2[11]+mat1[7]*mat2[15];
		float f8 = mat1[8]*mat2[0]+mat1[9]*mat2[4]+mat1[10]*mat2[8]+mat1[11]*mat2[12];
		float f9 = mat1[8]*mat2[1]+mat1[9]*mat2[5]+mat1[10]*mat2[9]+mat1[11]*mat2[13];
		float f10 = mat1[8]*mat2[2]+mat1[9]*mat2[6]+mat1[10]*mat2[10]+mat1[11]*mat2[14];
		float f11 = mat1[8]*mat2[3]+mat1[9]*mat2[7]+mat1[10]*mat2[11]+mat1[11]*mat2[15];
		float f12 = mat1[12]*mat2[0]+mat1[13]*mat2[4]+mat1[14]*mat2[8]+mat1[15]*mat2[12];
		float f13 = mat1[12]*mat2[1]+mat1[13]*mat2[5]+mat1[14]*mat2[9]+mat1[15]*mat2[13];
		float f14 = mat1[12]*mat2[2]+mat1[13]*mat2[6]+mat1[14]*mat2[10]+mat1[15]*mat2[14];
		float f15 = mat1[12]*mat2[3]+mat1[13]*mat2[7]+mat1[14]*mat2[11]+mat1[15]*mat2[15];
		
		return new float[] {
			f0, f1, f2, f3,
			f4, f5, f6, f7,
			f8, f9, f10, f11,
			f12, f13, f14, f15
		};
	}
}