package com.example.emergencyjoadminnew

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.navigation.NavigationView

import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.zeugmasolutions.localehelper.Locales
import kotlinx.android.synthetic.main.activity_add_car.*
import kotlinx.android.synthetic.main.activity_add_car.nav_side_list_id

class ActivityAddCar : BaseActivity(),TextWatcher,NavigationView.OnNavigationItemSelectedListener {

    //global variables

    private lateinit var mRefCar: DatabaseReference                      //initialize reference
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_car)

        toolbar = findViewById(R.id.header_id)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        connectActionbar()

        connectDatabase()
        et_car_number_add_id.addTextChangedListener(this)



        btn_add_information_id.setOnClickListener()
        {

            if(!Exp.expCarNumber.matches(et_car_number_add_id.text.toString()))
            {
                showMessageError(et_car_number_add_id,"Enter a car number valid")
            }
            else if(!Exp.expCarName.matches(et_car_name_add_id.text.toString()))
            {
                showMessageError(et_car_name_add_id,"Enter a car name valid")
            }
            else if(!Exp.expCarModel.matches(et_car_model_add_id.text.toString()))
            {
                showMessageError(et_car_model_add_id,"Enter a car model valid")
            }
            else if(!Exp.expMilitaryName.matches(et_military_name_add_id.text.toString()))
            {
                showMessageError(et_military_name_add_id,"Enter a name valid")
            }
            else  if(!Exp.expMilitaryNumber.matches(et_military_number_add_id.text.toString()))
            {
                showMessageError(et_military_number_add_id,"Enter a Military number valid")
            }

            else if(!Exp.expPassword.matches(et_password_add_id.text.toString()))
            {
                showMessageError(et_password_add_id,"Enter a Password ( 8> , A , a , $)")
            }


            else if (et_password_add_id.text.toString()!=et_re_password_add_id.text.toString())
            {
                et_re_password_layout_form_add_id.error="Password Not Match"
            }

            else {


                mRefCar.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        if(snapshot.child(et_car_number_add_id.text.toString()).exists())
                        {
                            showAlertFound()
                        }

                        else
                        {
                            mRefCar.child(tv_show_username_add_id.text.toString()).setValue(
                                DatabaseCar(
                                    et_car_number_add_id.text.toString(),
                                    et_car_name_add_id.text.toString(),
                                    et_car_model_add_id.text.toString(),
                                    et_military_name_add_id.text.toString(),
                                    et_military_number_add_id.text.toString(),
                                    tv_show_username_add_id.text.toString(),
                                    et_password_add_id.text.toString(),
                                    sp_type_car_add_id.selectedItem.toString(),
                                    "Available"
                                )
                            )
                            showAlertDone()
                            emptyFields()
                        }// end else
                    }//end data change
                    override fun onCancelled(error: DatabaseError) {}
                }) // end add value listener

            }//end else
        }//end btn add user



    } // end onCreate

    private fun connectDatabase() {
        val database= Firebase.database
        mRefCar=database.getReference("Car")
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//        btn_add_user_id.isEnabled =
//            (et_car_number_add_id.text.trim().isNotEmpty()&&et_car_name_add_id.text.trim().isNotEmpty()&&et_car_model_add_id.text.trim().isNotEmpty()
//                    &&et_military_name_add_id.text.trim().isNotEmpty()&&et_military_number_add_id.text.trim().isNotEmpty()
//                    &&et_password_add_id.text.trim().isNotEmpty()&&et_re_password_add_id.text.trim().isNotEmpty())

    }

    override fun afterTextChanged(s: Editable?) {
        tv_show_username_add_id.text=et_car_number_add_id.text.toString()
        if(et_car_number_add_id.text.length==2)
            et_car_number_add_id.append("-")
    }

    private fun emptyFields() {
        et_car_number_add_id.setText("")
        et_car_name_add_id.setText("")
        et_car_model_add_id.setText("")
        et_military_name_add_id.setText("")
        et_military_number_add_id.setText("")
        tv_show_username_add_id.text=""
        et_password_add_id.setText("")
        et_re_password_add_id.setText("")
    }

    private fun showMessageError(item: EditText, message:String)
    {
        item.error=message
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    }
    private fun showAlertFound() {
        val alertDialog= AlertDialog.Builder(this)
        alertDialog.setTitle(R.string.title_message_found)
        alertDialog.setMessage(R.string.message_account_found)
        alertDialog.setPositiveButton("Ok",null)

        val alert=alertDialog.create()
        alert.show()
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener()
        {
            alert.dismiss()
        }
    }

    private fun showAlertDone() {
        val alertDialog= AlertDialog.Builder(this)
        alertDialog.setTitle(R.string.title_message_done)

        alertDialog.setPositiveButton("Ok",null)

        val alert=alertDialog.create()
        alert.show()
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener()
        {
            alert.dismiss()
        }
    }

    /******************************** for side bar ********************************/
    private fun connectActionbar()
    {
        val actionToggle= ActionBarDrawerToggle(this,dr_add_car_id,toolbar,R.string.drawer_open,R.string.drawer_close)
        dr_add_car_id.addDrawerListener(actionToggle)
        actionToggle.syncState()
        nav_side_list_id.setNavigationItemSelectedListener(this)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId)
        {

            R.id.btn_request_id->
            {
                val goToRequest=Intent(this,ActivityMain::class.java)
                startActivity(goToRequest)
                finish()
            }
            R.id.btn_add_car_id->
            {
                val goToAddCar=Intent(this,ActivityAddCar::class.java)
                startActivity(goToAddCar)
                finish()
            }
            R.id.btn_edit_car_id->
            {
                val goToEditCar=Intent(this,ActivityEditCar::class.java)
                startActivity(goToEditCar)
                finish()
            }
            R.id.btn_remove_car_id->
            {
                val goToRemoveCar=Intent(this,ActivityRemoveCar::class.java)
                startActivity(goToRemoveCar)
                finish()
            }

            R.id.btn_car_info_id->
            {
                val goToCarInfo=Intent(this,ActivityCarInfo::class.java)
                startActivity(goToCarInfo)
                finish()
            }
            R.id.btn_common_cases_id->
            {
                val goToCommonCase=Intent(this,ActivityCommonCase::class.java)
                startActivity(goToCommonCase)
                finish()
            }
            R.id.btn_accepted_request_id->
            {
                val goToAcceptedRequest=Intent(this,ActivityAcceptedRequest::class.java)
                startActivity(goToAcceptedRequest)
                finish()
            }
            R.id.btn_false_request_id->
            {
                val goToFalseRequest=Intent(this,ActivityFalseRequest::class.java)
                startActivity(goToFalseRequest)
                finish()
            }
            R.id.en_id->
            {
                updateLocale(Locales.English)
            }
            R.id.ar_id->
            {
                updateLocale(Locales.Arabic)
            }
            R.id.btn_logout_id->{

                val user: FirebaseAuth = FirebaseAuth.getInstance()

                user.signOut()
                startActivity(
                    Intent(this,ActivityLogin::class.java)
                )
                finish()
            }

        }
        closeDrawer()
        return true
    }

    private fun closeDrawer()
    {
        dr_add_car_id.closeDrawer(GravityCompat.START)
    }

    override fun onBackPressed() {
        if(dr_add_car_id.isDrawerOpen(GravityCompat.START))
            closeDrawer()
        else
            super.onBackPressed()
    }

    /****************************************************************************************/
}