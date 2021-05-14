package com.thepyprogrammer.greenpass.ui.image

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.thepyprogrammer.greenpass.R
import com.thepyprogrammer.greenpass.model.firebase.FirebaseUtil
import com.thepyprogrammer.greenpass.ui.main.MainActivity
import com.thepyprogrammer.greenpass.ui.main.MainViewModel
import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.util.*


class ImageDetailsActivity : AppCompatActivity() {
    private var REQUEST_IMAGE = 2169
    var imageView: ImageView? = null
    var imageInfoFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_details)


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title
        toolbar.inflateMenu(R.menu.image_bar_menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setTitleTextColor(Color.WHITE)



        imageView = findViewById(R.id.imageDetailsImageView)
        imageInfoFile = File(filesDir, "profileImageURI.txt")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.image_bar_menu, menu)
        return true
    }

    override fun onStart() {
        super.onStart()
        loadImage()
    }


    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String?>,
            grantResults: IntArray
    ) {
        super
                .onRequestPermissionsResult(
                        requestCode,
                        permissions,
                        grantResults
                )
        // Checking whether user granted the permission or not.
        if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            return
        } else {
            Toast.makeText(
                    this,
                    "To select an icon, these permissions are required.",
                    Toast.LENGTH_SHORT
            )
                    .show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                val uri: Uri? = data?.getParcelableExtra("path")
                try {
                    if (uri != null) {
                        FirebaseUtil.uploadImage(uri)
                    }
                    // You can update this bitmap to your server
                    MediaStore.Images.Media.getBitmap(this.contentResolver, uri)

                    //save uri to internal storage
                    writeData(uri.toString())

                    // loading profile image from local cache
                    loadImage()

                    //todo fix this one loadProfile(uri.toString())
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun writeData(s: String) {
        val output = PrintWriter(imageInfoFile)
        output.println(s)
        output.close()
        println(s)
    }

    private fun readData(): String {
        if (!imageInfoFile!!.exists()) {
            return ""
        }
        val scanner = Scanner(imageInfoFile)
        val string = StringBuilder(scanner.nextLine())

        while (scanner.hasNextLine())
            string.append("\n" + scanner.nextLine())


        scanner.close()
        return string.toString()
    }

    private fun loadImage() {
        val string: String = readData()
        if (string.isNotEmpty()) {
            imageView!!.setImageURI(Uri.parse(readData()))
        } else {
            imageView!!.setImageResource(R.drawable.face_trans)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                android.R.id.home -> {
                    // This ID represents the Home or Up button. In the case of this
                    // activity, the Up button is shown. For
                    // more details, see the Navigation pattern on Android Design:
                    //
                    // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                    val toast = Toast.makeText(
                            applicationContext,
                            "Moving back to Main Page",
                            Toast.LENGTH_LONG
                    )
                    toast.show()
                    navigateUpTo(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.action_edit -> {
                    EditImage(this, this)
                    true
                }

                else -> super.onOptionsItemSelected(item)
            }

}