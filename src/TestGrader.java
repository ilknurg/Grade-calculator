
public class TestGrader {

	public static void main(String[] args) {
		LetterGrader lLetterGrader = new LetterGrader();
		
		boolean lGetFileNames = lLetterGrader.retrieveInput(args);
		if(lGetFileNames && lLetterGrader.readScrores()) {
			lLetterGrader.calcLetterGrade();
			lLetterGrader.printGrades();
			lLetterGrader.CalcAverages();
			lLetterGrader.printAverages();
		}
		
	}
	
	
}
