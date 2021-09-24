package com.rodrigo.mezclado

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.facebook.login.LoginManager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header.*
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import android.R.attr.label
import android.content.ClipData
import android.content.ContentValues.TAG
import android.util.Log
import androidx.core.view.GravityCompat
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import java.lang.IndexOutOfBoundsException


enum class ProviderType{
    BASIC,
    GOOGLE,
    FACEBOOK
}
class MainActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    val listOfId = mutableListOf<String>()
    val listOfTitulos = mutableListOf<String>()
    var conexion = true
    var conexionant = true

    lateinit var toggle: ActionBarDrawerToggle // Para hamburger

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT in 19..20) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
        super.onCreate(savedInstanceState)
        database = Firebase.database.reference

        //Datos de Auth
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")

        FirebaseCrashlytics.getInstance().setUserId(email!!)// OBTENER CORREO EN CRASH
        FirebaseCrashlytics.getInstance().setCustomKey("provider", provider!!)
        FirebaseCrashlytics.getInstance().log("UN LOG")
        FirebaseMessaging.getInstance().subscribeToTopic("tutorial")//SUSCRIBIRSE A TOPIC O "CASA"

        val testString: String = "example only"
        var bundlea = Bundle()
        bundlea.putString("ARG_PARAM1", testString)
        var frag = Fragment1()
        frag.arguments = bundlea

        // Guardado de Datos
        val prefs =
            getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.putString("provider", provider)
        prefs.apply()

        // Cerrar Sesion
        //val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        //prefs.clear()
        //prefs.apply()
        //logOutButton.setOnCliclListener{
        //FirebaseAuth.getInstance().signOut()
        //onBackPressed()

        // Aca pa abajo nuevo side
        setContentView(R.layout.activity_main)
        lista()
        sync()

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            findViewById(R.id.toolbar),
            R.string.open,
            R.string.close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        var togglea = toggle

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setSupportActionBar(findViewById(R.id.toolbar))
        toggle.isDrawerIndicatorEnabled = true
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.file1 -> {
                    title = "PRUEBA 1"
                    Toast.makeText(applicationContext, "PRUEBA 1 elegido", Toast.LENGTH_SHORT).show()
                }
                R.id.file2 -> Toast.makeText(applicationContext, "Clicked Message", Toast.LENGTH_SHORT).show()
                R.id.file3 -> Toast.makeText(applicationContext, "Clicked Sync", Toast.LENGTH_SHORT).show()
                R.id.file4 -> Toast.makeText(applicationContext, "Clicked Delete", Toast.LENGTH_SHORT).show()
                R.id.file5 -> Toast.makeText(applicationContext, "Clicked Setting", Toast.LENGTH_SHORT).show()

                R.id.logOut -> {
                    prefs.clear()
                    prefs.apply()
                    FirebaseAuth.getInstance().signOut()
                    if (provider == ProviderType.FACEBOOK.name) LoginManager.getInstance().logOut()
                    startActivity(Intent(this, AuthActivity::class.java))
                }

                R.id.copyUID -> {
                    Toast.makeText(applicationContext, "Clicked Rate us", Toast.LENGTH_SHORT).show()
                    val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText(label.toString(), user_email.text)
                    clipboard.setPrimaryClip(clip)
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Aca pa abajo nuevo tab
        viewPager.adapter = PageAdapter(this, supportFragmentManager)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Alarma"
                1 -> tab.text = "Control Salidas"
                2 -> tab.text = "Historial"
                3 -> tab.text = "Sensores"
                4 -> tab.text = "Camaras"
            }
        }.attach()

    }

    private fun sync() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (conexion !=conexionant){
                    conexion = conexionant
                }
                // Get Post object and use the values to update the UI
                try {
                    textView.text = listOfId.get(0)
                    textView.text = listOfTitulos.get(0)
                } catch (error: java.lang.IndexOutOfBoundsException) {
                    textView.text =  "LA PTM"
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        database.child("lists").addValueEventListener(postListener)

        try {
            textView.text = listOfId.get(0)
            textView.text = listOfTitulos.get(0)
        } catch (error: java.lang.IndexOutOfBoundsException) {
            textView.text =  "LA PTM"
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        sync()
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //menuInflater.inflate(R.menu.nav_menu, menu)
        copyUID.setOnClickListener {
            Toast.makeText(applicationContext, "Copiado al portapapeles", Toast.LENGTH_SHORT).show()
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(label.toString(), user_email.text)
            clipboard.setPrimaryClip(clip)
        }

        user_name.text = intent.extras?.getString("name")
        user_email.text = intent.extras?.getString("uid")

        if (user_name.text != "") { Picasso.get().load(intent.extras?.getString("photoUrl")).into(perfil) } else {
            user_name.text = intent.extras?.getString("email")

            val imageView = findViewById<ImageView>(R.id.perfil)
            var imgResId = R.drawable.ic_launcher_background
            imageView.setImageResource(imgResId)
            imgResId = if (imgResId == R.drawable.ic_launcher_background) R.mipmap.ic_launcher_round else R.drawable.ic_launcher_background
            imageView.setImageResource(imgResId)
        }

        return true
    }

    private fun lista() {
        // Obtener UID admin y titulo
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) { // OBTENGO UID
                    val uids = ds.key.toString()
//--------------------------------------
                    val valueEventListener = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (dss in dataSnapshot.children) { // OBTENGO UID
                                if (dss.child("miembros").child(intent.extras?.getString("uid").toString()).exists()){
                                    listOfId.add(uids)
                                    listOfTitulos.add(dss.key.toString())
                                }
                            }
                        }
                        override fun onCancelled(databaseError: DatabaseError) {
                            textView.text = "Error getting data"
                        }
                    }
                    database.child("lists").child(uids).child("items").addListenerForSingleValueEvent(valueEventListener)
//--------------------------------------
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        database.child("lists").addListenerForSingleValueEvent(valueEventListener)

    }

    //fun onGroupItemClick(item: MenuItem) {
        // One of the group items (using the onClick attribute) was clicked
        // The item parameter passed here indicates which item it is
        // All other menu item clicks are handled by <code><a href="/reference/android/app/Activity.html#onOptionsItemSelected(android.view.MenuItem)">onOptionsItemSelected()</a></code>
    //}
}