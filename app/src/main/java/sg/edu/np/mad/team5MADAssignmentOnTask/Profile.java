package sg.edu.np.mad.team5MADAssignmentOnTask;

public class Profile {
    private String ProfileURI, BannerURI;

    public Profile(String profileURI, String bannerURI) {
        ProfileURI = profileURI;
        BannerURI = bannerURI;
    }

    public Profile() {
    }

    public String getProfileURI() {
        return ProfileURI;
    }

    public void setProfileURI(String profileURI) {
        ProfileURI = profileURI;
    }

    public String getBannerURI() {
        return BannerURI;
    }

    public void setBannerURI(String bannerURI) {
        BannerURI = bannerURI;
    }
}
