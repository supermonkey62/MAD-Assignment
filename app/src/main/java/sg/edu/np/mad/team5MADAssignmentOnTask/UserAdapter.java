package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;
    private List<User> selectedUsers;

    public UserAdapter(List<User> userList) {
        this.userList = userList;
        this.selectedUsers = new ArrayList<>();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public List<User> getSelectedUsers() {
        return selectedUsers;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView displayNameTextView, usernameTextView;
        private CheckBox userCheckbox;
        private ImageView profileImageView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            displayNameTextView = itemView.findViewById(R.id.displayNameTextView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            userCheckbox = itemView.findViewById(R.id.userCheckbox);
            profileImageView = itemView.findViewById(R.id.profileImageView);
        }

        public void bind(User user) {
            displayNameTextView.setText(user.getDisplayname());
            usernameTextView.setText(user.getUsername());
            Uri uri = Uri.parse(user.getImageURI());
            profileImageView.setImageURI(uri);


            userCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedUsers.add(user);
                } else {
                    selectedUsers.remove(user);
                }
            });
        }
    }

    public void setFilteredList(List<User> filteredList) {
        userList = filteredList;
        notifyDataSetChanged();
    }
}


