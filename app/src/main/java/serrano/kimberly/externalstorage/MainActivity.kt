package serrano.kimberly.externalstorage

import android.app.Activity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.io.BufferedReader
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader


class MainActivity : Activity() {
    var inputText: EditText? = null
    var response: TextView? = null
    var saveButton: Button? = null
    var readButton: Button? = null
    private val filename = "SampleFile.txt"
    private val filepath = "MyFileStorage"
    var myExternalFile: File? = null
    var myData = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inputText = findViewById<View>(R.id.myInputText) as EditText
        response = findViewById<View>(R.id.response) as TextView
        saveButton = findViewById<View>(R.id.saveExternalStorage) as Button
        saveButton!!.setOnClickListener {
            try {
                val fos = FileOutputStream(myExternalFile)
                fos.write(inputText!!.text.toString().toByteArray())
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            inputText!!.setText("")
            response!!.text = "SampleFile.txt saved to External Storage..."
        }
        readButton = findViewById<View>(R.id.getExternalStorage) as Button
        readButton!!.setOnClickListener {
            try {
                val fis = FileInputStream(myExternalFile)
                val `in` = DataInputStream(fis)
                val br = BufferedReader(InputStreamReader(`in`))
                var strLine: String
                while (br.readLine().also { strLine = it } != null) {
                    myData = myData + strLine
                }
                `in`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            inputText!!.setText(myData)
            response!!.text = "SampleFile.txt data retrieved from Internal Storage..."
        }
        if (!isExternalStorageAvailable || isExternalStorageReadOnly) {
            saveButton!!.isEnabled = false
        } else {
            myExternalFile = File(getExternalFilesDir(filepath), filename)
        }
    }

    companion object {
        private val isExternalStorageReadOnly: Boolean
            private get() {
                val extStorageState = Environment.getExternalStorageState()
                return if (Environment.MEDIA_MOUNTED_READ_ONLY == extStorageState) {
                    true
                } else false
            }
        private val isExternalStorageAvailable: Boolean
            private get() {
                val extStorageState = Environment.getExternalStorageState()
                return if (Environment.MEDIA_MOUNTED == extStorageState) {
                    true
                } else false
            }
    }
}