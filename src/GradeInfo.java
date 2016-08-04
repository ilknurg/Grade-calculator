
public class GradeInfo implements Comparable<GradeInfo>{

	private String mName;
	private String mGrade;
	
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	public String getmGrade() {
		return mGrade;
	}
	public void setmGrade(String mGrade) {
		this.mGrade = mGrade;
	}
	
	public int compareTo(GradeInfo pLetterGrade) {
        return mName.compareTo(pLetterGrade.mName);
    }
}
