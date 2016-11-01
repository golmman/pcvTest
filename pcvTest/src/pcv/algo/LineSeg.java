package pcv.algo;

public class LineSeg {

	int sx, sy, ex, ey;
	
	public LineSeg() {
		sx = 0;
		sy = 0;
		ex = 0;
		ey = 0;
	}

	public LineSeg(int x0, int y0, int x1, int y1) {
		sx = x0;
		sy = y0;
		ex = x1;
		ey = y1;
	}
	
	public LineSeg(LineSeg line) {
		sx = line.sx;
		sy = line.sy;
		ex = line.ex;
		ey = line.ey;
	}
}
