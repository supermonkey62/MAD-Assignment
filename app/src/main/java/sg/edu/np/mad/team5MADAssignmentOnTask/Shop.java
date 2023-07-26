package sg.edu.np.mad.team5MADAssignmentOnTask;

import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;

public class Shop {

    private int cost;
    private String cardimage;
    private String itemtype;
    private boolean boughted;

    public Shop() {
        // Empty constructor required for Firebase
        // Make sure you don't perform any initialization in this constructor
    }

    public Shop(int cost, String cardimage, String itemtype, boolean boughted) {
        this.cost = cost;
        this.cardimage = cardimage;
        this.itemtype = itemtype;
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

    public String getItemtype() {
        return itemtype;
    }

    public void setItemtype(String itemtype) {
        this.itemtype = itemtype;
    }

    public boolean isBoughted() {
        return boughted;
    }

    public void setBoughted(boolean boughted) {
        this.boughted = boughted;
    }
}
