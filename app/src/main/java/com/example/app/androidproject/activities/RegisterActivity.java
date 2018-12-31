package com.example.app.androidproject.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.app.androidproject.utils.Constants;
import com.example.app.androidproject.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
public class RegisterActivity extends AppCompatActivity  {
    ImageView logo;
    EditText input_username,input_last_name, input_name,  input_password, input_reEnterPassword, input_address, input_mobile, input_email;
    Button btn_create_account, btn_upload_img, btn_date, upload;
    TextView date_field, already_have_account, link_main;
    String username, lastname, name, email, password, dateNiss, adress, numtel_str, rePassword;
    Bitmap cropped;
    ImageView imageView;
    private int mYear, mMonth, mDay, mHour, mMinute, numtel;
    DatePicker datePicker;
    private RequestQueue mQueue;
    /******IMAGE UPLOAD*******/

    private String UPLOAD_URL = Constants.WEBSERVICE_URL+"/mdw/uploadimage/upload.php", image_extension;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";
    private Boolean image_changed = false;
    /***************/
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mQueue = Volley.newRequestQueue(getApplicationContext());
        input_username = findViewById(R.id.input_username);
        input_name = findViewById(R.id.input_name);
        input_reEnterPassword = findViewById(R.id.input_reEnterPassword);
        input_last_name = findViewById(R.id.input_lastName);
        input_address = findViewById(R.id.input_address);
        input_email = findViewById(R.id.input_email);
        input_mobile = findViewById(R.id.input_mobile);
        input_password = findViewById(R.id.input_password);
        input_reEnterPassword = findViewById(R.id.input_reEnterPassword);
        imageView = findViewById(R.id.imageView_profile);
        btn_create_account = findViewById(R.id.btn_signup);
        date_field = findViewById(R.id.date_field);
        btn_date = findViewById(R.id.btn_date);
        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getApplicationContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                date_field.setText(day + "/" + (month + 1) + "/" + year);


                            }
                        }, 0, 0, 0);
                datePickerDialog.show();
            }
        });

        already_have_account = findViewById(R.id.link_login);
        link_main = findViewById(R.id.link_main);
        link_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                cropped.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                byte[] byteArray = stream.toByteArray();
//                intent.putExtra("picture", byteArray);
                startActivity(intent);
                finish();
            }
        });
        btn_upload_img = findViewById(R.id.upload_img);
        btn_upload_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        btn_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                username = input_username.getText().toString().trim();
                name = input_name.getText().toString().trim();
                lastname = input_last_name.getText().toString().trim();
                dateNiss = date_field.getText().toString();
                email = input_email.getText().toString().trim();
                password = input_password.getText().toString();
                adress = input_address.getText().toString().trim();
                numtel_str = input_mobile.getText().toString();
                rePassword = input_reEnterPassword.getText().toString();
                if (validate()) {
                    Toast.makeText(RegisterActivity.this, "Creating account ...", Toast.LENGTH_LONG).show();
                    uploadImage();
                    registerRequest(username,name,lastname,adress,dateNiss,email,numtel_str,password);
                }
                else  Toast.makeText(RegisterActivity.this, "Something went wrong :(", Toast.LENGTH_LONG).show();
            }
        });
        already_have_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(RegisterActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public boolean validate() {
        boolean valid = true;


        if (username.isEmpty() || username.length() < 3) {
            input_username.setError("at least 3 characters");
            valid = false;
        } else {
            input_username.setError(null);
        }
        if (lastname.isEmpty() || lastname.length() < 3) {
            input_last_name.setError("at least 3 characters");
            valid = false;
        } else {
            input_last_name.setError(null);
        }
        if (name.isEmpty() || name.length() < 3) {
            input_name.setError("at least 3 characters");
            valid = false;
        } else {
            input_name.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            input_email.setError("enter a valid email address");
            valid = false;
        } else {
            input_email.setError(null);
        }
        if(String.valueOf(input_mobile.getText().toString()).length() != 8){
            input_mobile.setError("at least 8 numbers");
            valid = false;
        }
        else
            input_mobile.setError(null);
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            input_password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            input_password.setError(null);
        }
        if (rePassword.isEmpty() || password.length() < 4 || !rePassword.equals(password)) {
            input_reEnterPassword.setError("Password doesn't match");
            valid = false;
        } else {
            input_reEnterPassword.setError(null);
        }

        if(!image_changed){
            Toast.makeText(getApplicationContext(), "No image found !", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        return valid;
    }
    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        date_field.setText(sdf.format(myCalendar.getTime()));
    }

    public void registerRequest (final String username, final String name , final String lastname, final String adress, final String dateNiss, final String email, final String numtel, final String password ){
        String url =  Constants.WEBSERVICE_URL+"/mdw/v1/register";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response

                        Toast.makeText(getApplication(),response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(getApplication(),error+"", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("username", username);
                params.put("name", name);
                params.put("last_name", lastname);
                params.put("email", email);
                params.put("password", password);
                params.put("numtel", numtel);
                params.put("adresse", adress);
                params.put("datenaiss", dateNiss);

                return params;
            }
        };
        mQueue.add(postRequest);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(RegisterActivity.this, s , Toast.LENGTH_LONG).show();
                        Log.d("error_image", s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(RegisterActivity.this, "error", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String

                String image = getStringImage(bitmap);

                //Getting Image Name
                String name = input_username.getText().toString().trim()+"-img."+image_extension;

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("image", image);
                params.put("filename", name);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(RegisterActivity.this, getStringImage(bitmap), Toast.LENGTH_SHORT).show();
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                image_extension = getImageExtension(getApplicationContext(), filePath);
                image_changed = true;
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public  String getImageExtension(Context context, Uri uri) {
        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        return extension;
    }
}