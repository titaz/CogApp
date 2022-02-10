package com.example.cogapp.ui.main;

import static com.example.cogapp.Utils.AndroidUtils.NEWLINE;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cogapp.Adapters.ContactAdapter;
import com.example.cogapp.MainActivity;
import com.example.cogapp.Model.ContactModel;
import com.example.cogapp.R;
import com.example.cogapp.Utils.AndroidUtils;
import com.example.cogapp.Utils.PermissionUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;

    Uri uri = ContactsContract.Contacts.CONTENT_URI;

    private static ListView contact_listview;
    private static ArrayList<ContactModel> arrayList;
    private static ContactAdapter adapter;

    private static ProgressDialog pd;




    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {



        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        contact_listview = (ListView) view.findViewById(R.id.contact_listview);

        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
//        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult result) {
//                        if (result.getResultCode() == Activity.RESULT_OK) {
//                            // There are no request codes
//                            Intent data = result.getData();
//
//                            Uri contactUri = data.getData();
//                            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
//                            Cursor cursor = getContext().getContentResolver().query(contactUri, projection,
//                                    null, null, null);
//                            // If the cursor returned is valid, get the phone number
//                            if (cursor != null && cursor.moveToFirst()) {
//                                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
//                                String number = cursor.getString(numberIndex);
//                                // Do something with the phone number
//                            }
//                        }
//                    }
//                });

//        mViewModel.openSomeActivityForResult(someActivityResultLauncher);

        //request permissions
         PermissionUtils permissionUtils =PermissionUtils.getInstance(getActivity()).setCallback(new PermissionUtils.PermissionCallback() {
             @Override
             public void success() {
             loadContacts();
             }

             @Override
             public void fail() {
                 AndroidUtils.makeText(getContext(),"No permmisions");

             }
         });

        permissionUtils.requestPermission(Manifest.permission.READ_CONTACTS);




    }
    private void loadContacts(){
        new LoadContacts().execute();// Execute the async task
    }

    // Async task to load contacts
    private class LoadContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            arrayList = readContacts();// Get contacts array list from this
            // method
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            // If array list is not null and is contains value
            if (arrayList != null && arrayList.size() > 0) {

                // then set total contacts to subtitle
//                getSupportActionBar().setSubtitle(arrayList.size() + " Contacts");
                adapter = null;
                if (adapter == null) {
                    adapter = new ContactAdapter(getContext(), arrayList);
                    contact_listview.setAdapter(adapter);// set adapter
                }
                adapter.notifyDataSetChanged();
            } else {

                // If adapter is null then show toast
                Toast.makeText(getContext(), "There are no contacts.",
                        Toast.LENGTH_LONG).show();
            }

            // Hide dialog if showing
            if (pd.isShowing())
                pd.dismiss();

        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            // Show Dialog
            pd = ProgressDialog.show(getContext(), "Loading Contacts",
                    "Please Wait...");
        }

    }

    // Method that return all contact details in array format
    private ArrayList<ContactModel> readContacts() {
        ArrayList<ContactModel> contactList = new ArrayList<ContactModel>();

        Context applicationContext = MainActivity.getContextOfApplication();
        applicationContext.getContentResolver();

        Uri uri = ContactsContract.Contacts.CONTENT_URI; // Contact URI
        Cursor contactsCursor = applicationContext.getContentResolver().query(uri, null, null,
                null, ContactsContract.Contacts.DISPLAY_NAME + " ASC "); // Return
        // all
        // contacts
        // name
        // containing
        // in
        // URI
        // in
        // ascending
        // order
        // Move cursor at starting
        if (contactsCursor.moveToFirst()) {
            do {
//                long contctId = contactsCursor.getLong(contactsCursor.getColumnIndex("_ID")); // Get contact ID
                long contctId = contactsCursor.getLong(contactsCursor.getColumnIndexOrThrow("_ID")); // Get contact ID
                Uri dataUri = ContactsContract.Data.CONTENT_URI; // URI to get
                // data of
                // contacts
                Cursor dataCursor = getActivity().getContentResolver().query(dataUri, null,
                        ContactsContract.Data.CONTACT_ID + " = " + contctId,
                        null, null);// Retrun data cusror represntative to
                // contact ID

                // Strings to get all details
                String displayName = "";
                String nickName = "";
                String homePhone = "";
                String mobilePhone = "";
                String workPhone = "";
                String photoPath = "" + R.drawable.ic_person; // Photo path
                byte[] photoByte = null;// Byte to get photo since it will come
                // in BLOB
                String homeEmail = "";
                String workEmail = "";
                String companyName = "";
                String title = "";

                // This strings stores all contact numbers, email and other
                // details like nick name, company etc.
                String contactNumbers = "";
                String contactEmailAddresses = "";
                String contactOtherDetails = "";

                // Now start the cusrsor
                if (dataCursor.moveToFirst()) {

//                    displayName = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));// get
                    displayName = dataCursor.getString(dataCursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));// get
                    // the
                    // contact
                    // name
                    do {
                        if (dataCursor.getString(dataCursor.getColumnIndexOrThrow("mimetype"))
                                .equals(ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE)) {
                            nickName = dataCursor.getString(dataCursor
                                    .getColumnIndexOrThrow("data1")); // Get Nick Name
                            contactOtherDetails += "NickName : " + nickName
                                    + NEWLINE;// Add the nick name to string

                        }

                        if (dataCursor
                                .getString(
                                        dataCursor.getColumnIndexOrThrow("mimetype"))
                                .equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {

                            // In this get All contact numbers like home,
                            // mobile, work, etc and add them to numbers string
                            switch (dataCursor.getInt(dataCursor
                                    .getColumnIndexOrThrow("data2"))) {
                                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                    homePhone = dataCursor.getString(dataCursor
                                            .getColumnIndexOrThrow("data1"));
                                    contactNumbers += "Home Phone : " + homePhone
                                            + NEWLINE;
                                    break;

                                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                    workPhone = dataCursor.getString(dataCursor
                                            .getColumnIndexOrThrow("data1"));
                                    contactNumbers += "Work Phone : " + workPhone
                                            + NEWLINE;
                                    break;

                                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                    mobilePhone = dataCursor.getString(dataCursor
                                            .getColumnIndexOrThrow("data1"));
                                    contactNumbers += "Mobile Phone : "
                                            + mobilePhone + NEWLINE;
                                    break;

                            }
                        }
                        if (dataCursor.getString(
                                        dataCursor.getColumnIndexOrThrow("mimetype")).equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {

                            // In this get all Emails like home, work etc and
                            // add them to email string
                            switch (dataCursor.getInt(dataCursor.getColumnIndexOrThrow("data2"))) {
                                case ContactsContract.CommonDataKinds.Email.TYPE_HOME: homeEmail = dataCursor.getString(
                                        dataCursor.getColumnIndexOrThrow("data1"));
                                    contactEmailAddresses += "Home Email : "
                                            + homeEmail + NEWLINE;
                                    break;
                                case ContactsContract.CommonDataKinds.Email.TYPE_WORK:
                                    workEmail = dataCursor.getString(dataCursor
                                            .getColumnIndexOrThrow("data1"));
                                    contactEmailAddresses += "Work Email : "
                                            + workEmail + NEWLINE;
                                    break;

                            }
                        }

                        if (dataCursor
                                .getString(
                                        dataCursor.getColumnIndexOrThrow("mimetype"))
                                .equals(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)) {
                            companyName = dataCursor.getString(dataCursor
                                    .getColumnIndexOrThrow("data1"));// get company
                            // name
                            contactOtherDetails += "Coompany Name : "
                                    + companyName + NEWLINE;
                            title = dataCursor.getString(dataCursor
                                    .getColumnIndexOrThrow("data4"));// get Company
                            // title
                            contactOtherDetails += "Title : " + title + NEWLINE;

                        }

                        if (dataCursor
                                .getString(
                                        dataCursor.getColumnIndexOrThrow("mimetype"))
                                .equals(ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)) {
                            photoByte = dataCursor.getBlob(dataCursor
                                    .getColumnIndexOrThrow("data15")); // get photo in
                            // byte

                            if (photoByte != null) {

                                // Now make a cache folder in file manager to
                                // make cache of contacts images and save them
                                // in .png
                                Bitmap bitmap = BitmapFactory.decodeByteArray(
                                        photoByte, 0, photoByte.length);
                                File cacheDirectory = getActivity().getBaseContext()
                                        .getCacheDir();
                                File tmp = new File(cacheDirectory.getPath()
                                        + "/_androhub" + contctId + ".png");
                                try {
                                    FileOutputStream fileOutputStream = new FileOutputStream(
                                            tmp);
                                    bitmap.compress(Bitmap.CompressFormat.PNG,
                                            100, fileOutputStream);
                                    fileOutputStream.flush();
                                    fileOutputStream.close();
                                } catch (Exception e) {
                                    // TODO: handle exception
                                    e.printStackTrace();
                                }
                                photoPath = tmp.getPath();// finally get the
                                // saved path of
                                // image
                            }

                        }

                    } while (dataCursor.moveToNext()); // Now move to next
                    // cursor

                    contactList.add(new ContactModel(Long.toString(contctId),
                            displayName, contactNumbers, contactEmailAddresses,
                            photoPath, contactOtherDetails));// Finally add
                    // items to
                    // array list
                }

            } while (contactsCursor.moveToNext());
        }
        return contactList;
    }





}