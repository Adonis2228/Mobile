package viewmodel;

import android.app.Application;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import model.User;

public class AuthViewModel extends AndroidViewModel {

    public MutableLiveData<User> authUser;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        authUser = new MutableLiveData<>();
    }

    public MutableLiveData<User> getUser() {
        return authUser;
    }
}
