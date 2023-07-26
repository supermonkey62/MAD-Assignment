package sg.edu.np.mad.team5MADAssignmentOnTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class FriendViewModelFactory implements ViewModelProvider.Factory {
    private FriendViewModel mFriendViewModel;

    public FriendViewModelFactory(FriendViewModel friendViewModel) {
        mFriendViewModel = friendViewModel;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FriendViewModel.class)) {
            return (T) mFriendViewModel;
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

