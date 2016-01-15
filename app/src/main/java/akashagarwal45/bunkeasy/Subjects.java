package akashagarwal45.bunkeasy;

public class Subjects {
    private String subName;
    private int missClass;
    private int totClass;
    static int shortPercent;

    public Subjects(){
        this.missClass=0;
        this.totClass=0;
    }

    public Subjects(String subName){
        this.subName=subName;
        this.missClass=0;
        this.totClass=0;
    }

    public String getSubName() {
        return subName;
    }

    public int getTotClass() {
        return totClass;
    }

    public int getMissClass() {
        return missClass;
    }

    public void setMissClass(int missClass) {
        this.missClass = missClass;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public void setTotClass(int totClass) {
        this.totClass = totClass;
    }

    public float calPercent(){
        return (float) ((totClass-missClass)/totClass);
    }

    public int presentClass(){
        return (totClass-missClass);
    }
}
