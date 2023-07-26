package sg.edu.np.mad.team5MADAssignmentOnTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;


public class FriendViewModel extends ViewModel {
    private MutableLiveData<List<User>> userListLiveData = new MutableLiveData<>();
    private MutableLiveData<List<String>> friendsSetLiveData = new MutableLiveData<>();

    private static FriendViewModel instance;

    public FriendViewModel() {
        friendsSetLiveData.setValue(new ArrayList<>());
    }

    public static FriendViewModel getInstance() {
        if (instance == null) {
            instance = new FriendViewModel();
        }
        return instance;
    }

    public void setUserList(List<User> userList) {
        userListLiveData.setValue(userList);
    }

    public MutableLiveData<List<User>> getUserListLiveData() {
        return userListLiveData;
    }

    public void setFriendsSet(List<String> friendsSet) {
        friendsSetLiveData.setValue(friendsSet);
    }
    public MutableLiveData<List<String>> getFriendsSetLiveData() {
        return friendsSetLiveData;}

    public static class Factory implements ViewModelProvider.Factory {
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(FriendViewModel.class)) {
                return (T) getInstance();
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
