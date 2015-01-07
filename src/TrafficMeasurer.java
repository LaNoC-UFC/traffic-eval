
public class TrafficMeasurer {
	public static void main(String[] args) {
		String path = "./evaluate";
		int dimX = 5, dimY = 5;
		
		if (args.length == 1) {
			path = "./" + args[0];
		}
		
		System.out.println("Evaluating traffic from " + path);
		Evaluation eval = new Evaluation("", path, path, dimX, dimY);
		eval.makeCNFs();
		//eval.makeDistris();
		eval.makeRelats();
	}
}
