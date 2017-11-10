package com.devilucky.placeapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devilucky.placeapp.InfoActivity;
import com.devilucky.placeapp.R;
import com.devilucky.placeapp.adapters.AdaptadorComentarios;
import com.devilucky.placeapp.adapters.AdaptadorComidas;
import com.devilucky.placeapp.adapters.AdaptadorProductos;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

/**
 * Created by Hakos on 11/06/2017.
 */

public class FragmentoProductos extends Fragment {
    public static PayPalConfiguration payPalConfiguration;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private AdaptadorProductos adapter;
    Activity activity;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle saved){
        View v=inflater.inflate(R.layout.fragmento_productos,parent,false);
        payPalConfiguration = new PayPalConfiguration();
        payPalConfiguration.environment(payPalConfiguration.ENVIRONMENT_NO_NETWORK);
        payPalConfiguration.clientId("");
        Intent intent = new Intent(getActivity(), PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,payPalConfiguration);
        getActivity().startService(intent);
        recyclerView = (RecyclerView)v.findViewById(R.id.reciclador);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AdaptadorProductos(getActivity());
        recyclerView.setAdapter(adapter);
        return v;
    }
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        this.activity = activity;
    }
    public void comprarProducto(){
        PayPalPayment payment = new PayPalPayment(new BigDecimal("18"), "USD", "sample", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(activity, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, FragmentoProductos.payPalConfiguration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payment);
        getActivity().startActivityForResult(intent,0);
    }
    public void onDestroy(){
        getActivity().stopService(new Intent(getActivity(),PayPalService.class));
        super.onDestroy();
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));

                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }

}
