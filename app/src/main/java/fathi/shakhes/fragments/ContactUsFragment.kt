package fathi.shakhes.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import fathi.shakhes.MainApplication.Companion.getInstance
import fathi.shakhes.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_contact_us.*
import shakhes.R

class ContactUsFragment : BaseFragment(R.layout.fragment_contact_us) {
    var url = "https://dining.sharif.ir/api/contactus"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setLogo(R.drawable.shariflogo);
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        View v = LayoutInflater
//                .from(getSupportActionBar().getThemedContext())
//                .inflate(R.layout.action_bar, null);
//        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
//                ActionBar.LayoutParams.MATCH_PARENT,
//                ActionBar.LayoutParams.MATCH_PARENT);
//        getSupportActionBar().setCustomView(v, params);
//
//        TextView myTextView2a = (TextView) findViewById(R.id.text2);
//        myTextView2a.setText("ارتباط با ما");
        submitButton.setOnClickListener { submit() }
    }

    private fun submit() {
        if (mobile.text.toString().length != 11) {
            mobile.error =
                "شماره موبایل را به درستی وارد کنید."
            mobile.requestFocus()
            return
        }
        if (!email.text.toString().contains("@")) {
            email.error = "ایمیل را به درستی وارد کنید."
            email.requestFocus()
            return
        }
        if (body.text.length < 5) {
            body.error = "متن را به درستی وارد کنید."
            body.requestFocus()
            return
        }
        var tmp =
            body.text.toString().replace("\n".toRegex(), "-")
        tmp = tmp.replace(" ".toRegex(), "%20")
        url += """
               ?phone=${mobile.text}&email=${
            email.text
        }&body=$tmp
               From Shakhes App
               """.trimIndent()
        val jsonObjReq: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST,
            url, null,
            Response.Listener { response ->
                try {
                    if (response.getString("success") == "true") {
                        Toast.makeText(context, "پیام شما ارسال شد", Toast.LENGTH_SHORT).show()
                        mobile.setText("")
                        email.setText("")
                        body.setText("")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, R.string.sending_message_error, Toast.LENGTH_SHORT)
                        .show()
                }
            }, Response.ErrorListener { error ->
                error.printStackTrace()
                Toast.makeText(context, R.string.sending_message_error, Toast.LENGTH_SHORT).show()
                mobile.setText("")
                email.setText("")
                body.setText("")
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders() = hashMapOf(
                "Content-type" to "application/json"
            )
        }
        getInstance().addToRequestQueue(jsonObjReq)
    }
}