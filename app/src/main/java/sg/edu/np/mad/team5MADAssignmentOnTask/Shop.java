package sg.edu.np.mad.team5MADAssignmentOnTask;

public class Shop {

    private int cost;
    private String cardimage;
    private String fonttype;
    private boolean boughted;

    public Shop() {
        // Empty constructor required for Firebase
        // Make sure you don't perform any initialization in this constructor
    }

    public Shop(int cost, String cardimage, String fonttype, boolean boughted) {
        this.cost = cost;
        this.cardimage = cardimage;
        this.fonttype = fonttype;
        this.boughted = boughted;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getCardimage() {
        return cardimage;
    }

    public void setCardimage(String cardimage) {
        this.cardimage = cardimage;
    }

    public String getFonttype() {
        return fonttype;
    }

    public void setFonttype(String fonttype) {
        this.fonttype = fonttype;
    }

    public boolean isBoughted() {
        return boughted;
    }

    public void setBoughted(boolean boughted) {
        this.boughted = boughted;
    }
}
