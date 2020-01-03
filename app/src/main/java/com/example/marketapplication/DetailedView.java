package com.example.marketapplication;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailedView extends AppCompatActivity
{
    final int UPI_PAYMENT = 0;
    ImageView detailedImage;
    TextView price;
    String price_of_product;
    String url;
    Button buyButton;

    //Internet Connectivity Check
    public static boolean isConnectionAvailable(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable())
            {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detailed_view);
        buyButton = findViewById(R.id.buyNow);
        detailedImage = findViewById(R.id.delatiledImage);
        price = findViewById(R.id.priceOfProduct);
        price_of_product = getIntent().getStringExtra("priceOfProduct");
        url = getIntent().getStringExtra("delatiledImage");
        price.setText(price_of_product);
        Picasso.get().load(url).into(detailedImage);

        buyButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                payUsingUpi(price_of_product, "9849608827@ybl", price_of_product, price.getText().toString());

            }
        });

    }

    // Payment Method
    void payUsingUpi(String name, String upiId, String note, String amount)
    {
        Log.e("main ", "name " + name + "--up--" + upiId + "--" + note + "--" + amount);
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();
        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);
        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        // check if intent resolves
        if (null != chooser.resolveActivity(getPackageManager()))
        {
            startActivityForResult(chooser, UPI_PAYMENT);
        }
        else
        {
            Toast.makeText(DetailedView.this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
//        requestCode=0, resultCode=-1
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("main ", "response " + resultCode);  //       E/main: response -1
        System.out.println(UPI_PAYMENT);
        if (requestCode == UPI_PAYMENT)
        {
            if ((RESULT_OK == resultCode) || (resultCode == 11))
            {
                if (data != null)
                {
                    System.out.println(data);
                    String trxt = data.getStringExtra("response");
                    System.out.println(trxt);
                    Log.e("UPI", "onActivityResult: " + trxt);  //E/UPI: onActivityResult: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add(trxt);
                    upiPaymentDataOperation(dataList);
                }
                else
                {
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
            }
            else
            {
                //when user simply back without payment
                Log.e("UPI", "onActivityResult: " + "Return data is null");
                ArrayList<String> dataList = new ArrayList<>();
                dataList.add("nothing");
                upiPaymentDataOperation(dataList);
            }
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data)
    {
        if (isConnectionAvailable(DetailedView.this))
        {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: " + str); // E/UPIPAY: upiPaymentDataOperation: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
            String paymentCancel = "";
            if (str == null)
            {
                str = "discard";
            }
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++)
            {
                String equalStr[] = response[i].split("=");
                if (equalStr.length >= 2)
                {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase()))
                    {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase()))
                    {
                        approvalRefNo = equalStr[1];
                    }
                }
                else
                {
                    paymentCancel = "Payment cancelled by user.";
                }
            }
            if (status.equals("success"))
            {
                //Code to handle successful transaction here.
                Toast.makeText(DetailedView.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "payment successful: " + approvalRefNo); //E/UPI: payment successfull: 922118921612
            }
            else if ("Payment cancelled by user.".equals(paymentCancel))
            {
                Toast.makeText(DetailedView.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: " + approvalRefNo);
            }
            else
            {
                Toast.makeText(DetailedView.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: " + approvalRefNo);
            }
        }
        else
        {
            Log.e("UPI", "Internet issue: ");
            Toast.makeText(DetailedView.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

}
