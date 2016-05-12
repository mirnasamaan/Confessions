package com.example.marvoot.testingandroid.ViewModel;


import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.marvoot.testingandroid.ConfApplication;
import com.example.marvoot.testingandroid.Model.ConfessionService;
import com.example.marvoot.testingandroid.Model.UserData;
import com.example.marvoot.testingandroid.Model.UserDataFilter;
import com.example.marvoot.testingandroid.PublicFunctions.AlertMessages;
import com.example.marvoot.testingandroid.PublicFunctions.CheckInternetConnection;
import com.example.marvoot.testingandroid.PublicFunctions.PopupMessages;
import com.example.marvoot.testingandroid.PublicFunctions.SessionManagement;
import com.example.marvoot.testingandroid.R;
import com.example.marvoot.testingandroid.View.ConfessionActivity;
import com.example.marvoot.testingandroid.View.RegisterActivity;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;


public class RegisterViewModel implements ViewModel {


    private Context context;
    private Subscription subscription;
    private DataListener dataListener;
    private UserData userdata;
    private String editTextUsernameValue;
    private String editTextPasswordValue;
    private String editTextGenderValue;
    private String GenderValue;
    public ObservableField<String> username_field_error;
    public ObservableField<String> password_field_error;
    public ObservableField<String> gender_field_error;
    public ObservableField<Drawable> username_field_icon;
    public ObservableField<Drawable> password_field_icon;
    public ObservableField<Drawable> gender_field_icon_right;
    public ObservableField<Drawable> gender_field_icon_left;
    public ObservableInt genderButtonVisibility;
    public ObservableField<String> gender_text;
    AlertMessages alertMessages;


    public RegisterViewModel(Context context, DataListener dataListener) {
        this.context = context;
        this.dataListener = dataListener;
        this.username_field_error = new ObservableField<>(null);
        this.password_field_error = new ObservableField<>(null);
        this.gender_field_error = new ObservableField<>(null);
        this.username_field_icon = new ObservableField<>(null);
        this.password_field_icon = new ObservableField<>(null);
        this.gender_field_icon_right = new ObservableField<>(null);
        this.gender_field_icon_left = new ObservableField<>(null);
        Drawable username_icon = context.getResources().getDrawable(R.drawable.people);
        Drawable password_icon = context.getResources().getDrawable(R.drawable.tool);
        Drawable gender_right_icon = context.getResources().getDrawable(R.drawable.man);
        Drawable gender_left_icon = context.getResources().getDrawable(R.drawable.arrows);
        username_field_icon.set(username_icon);
        password_field_icon.set(password_icon);
        gender_field_icon_right.set(gender_right_icon);
        gender_field_icon_left.set(gender_left_icon);
        genderButtonVisibility = new ObservableInt(View.GONE);
        gender_text = new ObservableField<>("");
        alertMessages = new AlertMessages();
    }


    public void setDataListener(DataListener dataListener) {
        this.dataListener = dataListener;
    }


    @Override
    public void destroy() {
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        subscription = null;
        context = null;
        dataListener = null;
    }


    public interface DataListener {
        void onUserDataChanged(UserData userdata);
    }


    public void loadUserData(String username, String password, String gender) {
        ConfApplication application = ConfApplication.get(context);
        final ConfessionService confessionService = application.getConfessionService();
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        UserDataFilter filter = new UserDataFilter(username, password, gender);
        subscription = confessionService.Register(filter)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe(new Subscriber<UserData>() {


                    @Override
                    public void onCompleted() {
                        alertMessages.HideAlert();
                        if (dataListener != null)
                            dataListener.onUserDataChanged(userdata);
                        if (userdata != null) {
                            if (userdata.username != null && userdata.password != null && userdata.gender != null)
                            {
                                SessionManagement new_session = new SessionManagement(context);
                                new_session.logoutUser();
                                String userID = Integer.toString(userdata.userId);
                                new_session.createLoginSession(userID, userdata.username, userdata.password);
                                /*CharSequence text = "You should be directed to the confessions list";
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();*/
                                UserData userData = new UserData();
                                userData.username = userdata.username;
                                userData.password = userdata.password;
                                context.startActivity(ConfessionActivity.newIntent(context, userData));
                                ((Activity) context).finish();
                            }
                            else
                            {
                                PopupMessages popup = new PopupMessages();
                                String error_msg = "User already exists";
                                popup.showAlertDialog(context, "Sorry", error_msg, null);
                            }
                        }
                        else
                        {
                            PopupMessages popup = new PopupMessages();
                            String error_msg = "User already exists";
                            popup.showAlertDialog(context, "Sorry", error_msg, null);
                        }
                    }


                    @Override
                    public void onError(Throwable error) {
                        alertMessages.HideAlert();
                        PopupMessages popup = new PopupMessages();
                        String error_msg = "Something went wrong..Please try again later";
                        popup.showAlertDialog(context, "Sorry", error_msg, null);
                    }


                    @Override
                    public void onNext(UserData userdata) {
                        RegisterViewModel.this.userdata = userdata;
                    }
                });
    }


    public void onClickSearch(View view) {
        username_field_error.set(null);
        password_field_error.set(null);
        gender_field_error.set(null);
        String empty = "";
        if( editTextUsernameValue == null || editTextUsernameValue.equals(empty))
            username_field_error.set("Username field can't be empty");
        else
        if( editTextPasswordValue == null || editTextPasswordValue.equals(empty))
            password_field_error.set("Password field can't be empty");
        else
        if( editTextGenderValue == null || editTextGenderValue.equals(empty))
            gender_field_error.set("Gender field can't be empty");
        else {
            username_field_error.set(null);
            password_field_error.set(null);
            gender_field_error.set(null);
            alertMessages.ShowAlert(context, "Checking Network", "Loading..",false);
            if (CheckInternetConnection.getInstance(context).isOnline())
            {
                //try{ Thread.sleep(3000); }catch(InterruptedException e){ }
                alertMessages.HideAlert();
                alertMessages.ShowAlert(context, "Please wait", "Loading..", false);
                loadUserData(editTextUsernameValue, editTextPasswordValue, GenderValue);
            }
            else
            {
                alertMessages.HideAlert();
                PopupMessages popup = new PopupMessages();
                String error = "You are offline";
                popup.showAlertDialog(context, "Sorry", error, null);
            }
        }
    }


    public void onClickGender(View view) {
        hideKeyboard(view);
        genderButtonVisibility.set(View.VISIBLE);
    }


    public void onClickFemale(View view) {
        genderButtonVisibility.set(View.GONE);
        gender_text.set("   انثى");
        gender_field_error.set(null);
        gender_field_icon_right.set(null);
        gender_field_icon_left.set(null);
        GenderValue = "f";
    }


    public void onClickMale(View view) {
        genderButtonVisibility.set(View.GONE);
        gender_text.set("   ذكر");
        gender_field_error.set(null);
        gender_field_icon_right.set(null);
        gender_field_icon_left.set(null);
        GenderValue = "m";
    }


    public void onClickGenderMenu(View view) {

    }


    public TextWatcher getUsernameEditTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                editTextUsernameValue = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String UsernameValue = editable.toString();
                if (UsernameValue.length() > 0) {
                    username_field_icon.set(null);
                } else {
                    //Assign your image again to the view, otherwise it will always be gone even if the text is 0 again.
                    Drawable username_icon = context.getResources().getDrawable(R.drawable.people);
                    username_field_icon.set(username_icon);
                }
            }
        };
    }


    public TextWatcher getPasswordEditTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                editTextPasswordValue = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String PasswordValue = editable.toString();
                if (PasswordValue.length() > 0) {
                    password_field_icon.set(null);
                } else {
                    //Assign your image again to the view, otherwise it will always be gone even if the text is 0 again.
                    Drawable password_icon = context.getResources().getDrawable(R.drawable.tool);
                    password_field_icon.set(password_icon);
                }
            }
        };
    }


    public TextWatcher getGenderEditTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                editTextGenderValue = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
    }


    public View.OnFocusChangeListener getUsernameEditFocusListner() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(view);
                }
            }
        };
    }


    public View.OnFocusChangeListener getPasswordEditFocusListner() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(view);
                }
            }
        };
    }



    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}