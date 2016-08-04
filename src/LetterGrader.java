import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LetterGrader {
	private String mInputFileName = null;
	private String mOutputFileName = null;
	private ArrayList<InputData> mInputList = new ArrayList<>();
	private ArrayList<GradeInfo> mGradeList = new ArrayList<>();
	private AvgData mAvgData = new AvgData();
	private BufferedReader mBufferedReader;
	
	public LetterGrader() {
		mBufferedReader= new BufferedReader(new InputStreamReader(System.in));
	}
	
	private void closeBufferedReader() {
		try {
			mBufferedReader .close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Prints the averages to the console
	 */
	public void printAverages() {
		System.out.println("Here is the class averages");
		final Object[][] table = new String[4][];
		table[0] = new String[] { " ", "Q1", "Q2", "Q3","Q4","Mid1","Mid2","Final" };
		table[1] = new String[] { "Average:", ""+mAvgData.getmQ1Avg(), ""+mAvgData.getmQ2Avg(),""+mAvgData.getmQ3Avg(),""+mAvgData.getmQ4Avg(),""+mAvgData.getmM1Avg(),""+mAvgData.getmM2Avg(),""+mAvgData.getmFinalAvg() };
		table[2] = new String[] { "Minimum:", ""+mAvgData.getmQ1Min(), ""+mAvgData.getmQ2Min(),""+mAvgData.getmQ3Min(),""+mAvgData.getmQ4Min(),""+mAvgData.getmM1Min(),""+mAvgData.getmM2Min(),""+mAvgData.getmFinalMin()  };
		table[3] = new String[] { "Maximum:", ""+mAvgData.getmQ1Max(), ""+mAvgData.getmQ2Max(),""+mAvgData.getmQ3Max(),""+mAvgData.getmQ4Max(),""+mAvgData.getmM1Max(),""+mAvgData.getmM2Max(),""+mAvgData.getmFinalMax() };

		for (final Object[] row : table) {
			System.out.format("%15s%15s%15s%15s%15s%15s%15s%15s\n", row);
		}
		
	}
	
	/**
	 * calculates the averages for each exam
	 */
	public void CalcAverages() {
		
		mAvgData.setmQ1Avg(mAvgData.getmQ1Total()*1.0/mInputList.size());
		mAvgData.setmQ2Avg(mAvgData.getmQ2Total()*1.0/mInputList.size());
		mAvgData.setmQ3Avg(mAvgData.getmQ3Total()*1.0/mInputList.size());
		mAvgData.setmQ4Avg(mAvgData.getmQ4Total()*1.0/mInputList.size());
		mAvgData.setmM1Avg(mAvgData.getmM1Total()*1.0/mInputList.size());
		mAvgData.setmM2Avg(mAvgData.getmM2Total()*1.0/mInputList.size());
		mAvgData.setmFinalAvg(mAvgData.getmFinalTotal()*1.0/mInputList.size());
	}
	
	
	/**
	 * Writes the grade information to the specified file
	 */
	public void printGrades() {
		PrintWriter writer;
		try {
			writer = new PrintWriter(mOutputFileName, "UTF-8");
			for(int i=0; i<mGradeList.size(); i++){
				writer.println(mGradeList.get(i).getmName()+":       "+mGradeList.get(i).getmGrade());
			}
			
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Calculates the grades information from the parsed data
	 */
	public void calcLetterGrade() {
		for(int i=0; i<mInputList.size(); i++){
			int lFinalScore = (int) (mInputList.get(i).getmQuiz1()*.10
									+mInputList.get(i).getmQuiz2()*.10
									+mInputList.get(i).getmQuiz3()*.10
									+mInputList.get(i).getmQuiz4()*.10
									+mInputList.get(i).getmMidi1()*.20
									+mInputList.get(i).getmMidi2()*.15
									+mInputList.get(i).getmFinal()*.25
									);
			GradeInfo lLetterGrade = new GradeInfo();
			lLetterGrade.setmName(mInputList.get(i).getmName());
			
			if(lFinalScore >= 90){
				lLetterGrade.setmGrade("A");
			}else if(lFinalScore >= 80){
				lLetterGrade.setmGrade("B");
			}else if(lFinalScore >= 70){
				lLetterGrade.setmGrade("C");
			}else if(lFinalScore >= 60){
				lLetterGrade.setmGrade("D");
			}else{
				lLetterGrade.setmGrade("F");
			}
			mGradeList.add(lLetterGrade);
		}
		Collections.sort(mGradeList);
	}
	
	/**
	 * Parses input file to read score and saves them in the memory
	 * @return true if successful
	 */
	public boolean readScrores() {

        File file = new File(mInputFileName);
        try {
            Scanner input = new Scanner(file);
            while (input.hasNextLine()) {
            	String lStudenetData = input.nextLine().trim().replaceAll("\\s", "");
                String[] line = lStudenetData.split(",");
                if(line.length != 8){
                	//something wrong with the data so skip this line
                	continue;
                }
                InputData lInputData = new InputData();
                for (int i = 0; i < line.length; i++) {
                	switch (i) {
					case 0:
						lInputData.setmName(line[i]);
						break;
					case 1:
						int lQ1Score = Integer.parseInt(line[i]);
						lInputData.setmQuiz1(lQ1Score);
						
						mAvgData.setmQ1Total(mAvgData.getmQ1Total()+lQ1Score);
						if(lQ1Score>mAvgData.getmQ1Max()){
							//new high score
							mAvgData.setmQ1Max(lQ1Score);
						}
						
						if(mAvgData.getmQ1Min() > lQ1Score||mAvgData.getmQ1Min() ==0) {
							//new lowest score 
							mAvgData.setmQ1Min(lQ1Score);
						}
						break;
					case 2:
						int lQ2Score = Integer.parseInt(line[i]);
						lInputData.setmQuiz2(lQ2Score);
						
						mAvgData.setmQ2Total(mAvgData.getmQ2Total()+lQ2Score);
						if(lQ2Score>mAvgData.getmQ2Max()){
							//new high score
							mAvgData.setmQ2Max(lQ2Score);
						}
						
						if(mAvgData.getmQ2Min() > lQ2Score||mAvgData.getmQ2Min()==0) {
							//new lowest score 
							mAvgData.setmQ2Min(lQ2Score);
						}
						break;
					case 3:
						int lQ3Score = Integer.parseInt(line[i]);
						lInputData.setmQuiz3(lQ3Score);
						
						mAvgData.setmQ3Total(mAvgData.getmQ3Total()+lQ3Score);
						if(lQ3Score>mAvgData.getmQ3Max()){
							//new high score
							mAvgData.setmQ3Max(lQ3Score);
						}
						
						if(mAvgData.getmQ3Min() > lQ3Score || mAvgData.getmQ3Min()==0) {
							//new lowest score 
							mAvgData.setmQ3Min(lQ3Score);
						}
						break;
					case 4:
						int lQ4Score = Integer.parseInt(line[i]);
						lInputData.setmQuiz4(lQ4Score);
						
						mAvgData.setmQ4Total(mAvgData.getmQ4Total()+lQ4Score);
						if(lQ4Score>mAvgData.getmQ4Max()){
							//new high score
							mAvgData.setmQ4Max(lQ4Score);
						}
						
						if(mAvgData.getmQ4Min() > lQ4Score || mAvgData.getmQ4Min() ==0) {
							//new lowest score 
							mAvgData.setmQ4Min(lQ4Score);
						}
						break;
					case 5:
						int lM1Score = Integer.parseInt(line[i]);
						lInputData.setmMidi1(lM1Score);
						
						mAvgData.setmM1Total(mAvgData.getmM1Total()+lM1Score);
						if(lM1Score>mAvgData.getmM1Max()){
							//new high score
							mAvgData.setmM1Max(lM1Score);
						}
						
						if(mAvgData.getmM1Min() > lM1Score || mAvgData.getmM1Min()==0) {
							//new lowest score 
							mAvgData.setmM1Min(lM1Score);
						}
						break;
					case 6:
						int lM2Score = Integer.parseInt(line[i]);
						lInputData.setmMidi2(lM2Score);
						
						mAvgData.setmM2Total(mAvgData.getmM2Total()+lM2Score);
						if(lM2Score>mAvgData.getmM2Max()){
							//new high score
							mAvgData.setmM2Max(lM2Score);
						}
						
						if(mAvgData.getmM2Min() > lM2Score || mAvgData.getmM2Min()==0) {
							//new lowest score 
							mAvgData.setmM2Min(lM2Score);
						}
						break;
					case 7:
						int lFinalScore = Integer.parseInt(line[i]);
						lInputData.setmFinal(lFinalScore);
						
						mAvgData.setmFinalTotal(mAvgData.getmFinalTotal()+lFinalScore);
						if(lFinalScore>mAvgData.getmFinalMax()){
							//new high score
							mAvgData.setmFinalMax(lFinalScore);
						}
						
						if(mAvgData.getmFinalMin() > lFinalScore || mAvgData.getmFinalMin() == 0) {
							//new lowest score 
							mAvgData.setmFinalMin(lFinalScore);
						}
						break;

					default:
						break;
					}
                	
                }
                mInputList.add(lInputData);
            }
            input.close();
        } catch (FileNotFoundException x) {
            System.out.println(x.getMessage());
            return false;
        }
        return true;

    }
	
	/**
	 * checks for the input and output file names and requests the user to provide them if he did not already
	 * @param args main args
	 */
	public boolean retrieveInput(String[] args) {
		int lNumOfAttempts =0;
		while(!validateFiles(args) && lNumOfAttempts <= 3){
				args = askForInput(lNumOfAttempts);
				lNumOfAttempts++;
		};
		closeBufferedReader();
		if(mInputFileName == null || mOutputFileName == null){
			System.out.println("\n Too Many Attempts , Exiting the Program ");
			return false;
		}
		return true;
	}
	
	/**
	 * File names could be provided while running application
	 * if not ask for input and out filenames
	 * @param args file names provided if any
	 */
	private boolean validateFiles(String[] args) {
		if(args !=null && args.length >= 2){
			//user provided the file names and we only care about first two names
			String lInputFileName = args[0];
			String lOutPutFileName = args[1];
			//make sure he gave txt file names
			if(!isValidName(lInputFileName) || !isValidName(lOutPutFileName)) {
				System.out.println("\n INVALID FILE NAME(S) ");
				return false;
			}
			
			//check if the input file exists
			File lFile = new File(lInputFileName);
			if(!lFile.exists() || lFile.isDirectory()) {
				//huh you lied to me
				System.out.println("\n The input file doesnt exists ");
				return false;
			}
			
			//check if the output file exists
			File lOutputFile = new File(lOutPutFileName);
			if(lOutputFile.exists() && !lOutputFile.isDirectory()) {
				//The output file already exists
				System.out.println("\n The "+lOutPutFileName+" file already exists, Would you like to overrite it? \n  ");
				String lAnswer;
				try {
					lAnswer = mBufferedReader.readLine().toLowerCase().trim();
					if(lAnswer.equals("y") ||lAnswer.equals("yes")){
						 //do nothing
					 }else{
						 return false;
					 }
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			
			
			mInputFileName = lInputFileName;
			mOutputFileName = lOutPutFileName;
			return true;
		}
		return false;
	}
	
	/**
	 * asks the user to provide both input and output file names, 
	 * incase if he didnt provided them already
	 * @return
	 */
	private String[] askForInput(int pNumOfAttemps) {
		System.out.println("\n Please enter Input and output filenames separated by space. Attempts Left: "+(3-pNumOfAttemps));
		  
		 try {
			String lInput = mBufferedReader.readLine();
			String[] lInputArgs = lInput.split(" ");
			return lInputArgs;
		} catch (IOException e) {
			e.printStackTrace();
		}
		 return null;
	}
	
	
	/**
	 * verifies the accepted file names in windows 
	 * and also checks for the .txt extension
	 * @param text
	 * @return
	 */
	public boolean isValidName(String text){
	    Pattern pattern = Pattern.compile(
	        "^                                \n" +
	        "(?!                              \n" +
	        "  (?:                            \n" +
	        "    CON|PRN|AUX|NUL|             \n" +
	        "    COM[1-9]|LPT[1-9]            \n" +
	        "  )                              \n" +
	        "  (?:\\.[^.]*)?                  \n" +
	        "  $                              \n" +
	        ")                                \n" +
	        "[^<>:\"/\\\\|?*\\x00-\\x1F]*     \n" +
	        "[^<>:\"/\\\\|?*\\x00-\\x1F\\ .]  \n" +
	        "$                                ", 
	        Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.COMMENTS);
	    Matcher matcher = pattern.matcher(text);
	    boolean isMatch = matcher.matches();
	    
	    String extension = "";
	    int i = text.lastIndexOf('.');
	    if (i > 0) {
	        extension = text.substring(i+1);
	    }
	    if(extension.toLowerCase().equals("txt") && isMatch) {
	    	return true;
	    }
	    return false;
	}
}
