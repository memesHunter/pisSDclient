package ru.pis.sdclient

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.os.LocaleList
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

var lastFragment: Fragment? = null
lateinit var sharedPreferences: SharedPreferences
lateinit var defaultPreferences: SharedPreferences
val sampler_options = listOf("Euler a", "DPM++ SDE Karras", "DDIM", "UniPC")
fun savePrefString(key: String, value: String) {
    sharedPreferences.edit().putString(key, value).apply()
}
fun switchToFragment(bundle: Bundle?, activity: FragmentActivity, fragment: Fragment) {
    lastFragment = fragment
    fragment.arguments = bundle

    val fragmentManager = activity.supportFragmentManager
    val fragmentTransaction = fragmentManager.beginTransaction()

    fragmentTransaction.replace(R.id.fragment_container, fragment)

    fragmentTransaction.addToBackStack(null)
    fragmentTransaction.commit()
}

fun changeNavBarTitle(activity: FragmentActivity, itemID: Int, title: String, icon: Int) {
    val navView = (activity as AppCompatActivity).findViewById<BottomNavigationView>(R.id.bottom_nav_view).menu.findItem(itemID)
    navView.title = title
    navView.setIcon(icon)
}

fun saveBitmapToFile(bitmap: Bitmap, context: Context): File? {
    val fileDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val fileName = "IMG_$timeStamp.jpg"
    val file = File(fileDir, fileName)

    try {
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        return file
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return null
}

fun saveImageToHistory(context: Context, bundle: Bundle) {
    val db = AppDatabase.getInstance(context)
    db.imageDao().insert(
        HistoryImage(
            uid = 0,
            filename = bundle.getString("imageFilePath"),
            prompt = bundle.getString("prompt") ?: "null",
            negative = bundle.getString("negative") ?: "null",
            steps = bundle.getInt("steps"),
            sampler = bundle.getString("sampler") ?: "null",
            width = bundle.getInt("width"),
            height = bundle.getInt("height")
        ))
}

fun trySaveFile(filename: String, context: Context) {
    try {
        val imageFile = File(filename)

        val saveDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "sdClient"
        )
        if (!saveDir.exists()) {
            saveDir.mkdirs()
        }
        val saveFile = File(saveDir, imageFile.name)

        // Copy the image file to the specified directory
        val inputStream = FileInputStream(imageFile)
        val outputStream = FileOutputStream(saveFile)
        inputStream.copyTo(outputStream)
        inputStream.close()
        outputStream.close()

        // Notify the gallery about the new image
        MediaScannerConnection.scanFile(
            context,
            arrayOf(saveFile.toString()),
            null
        ) { _, _ -> }

        // Notify the user that the image has been saved
        Toast.makeText(context, context.getString(R.string.toast_img_saved), Toast.LENGTH_SHORT).show()
    } catch (e: IOException) {
        e.printStackTrace()
        // Handle the error
        Toast.makeText(context, context.getString(R.string.toast_gen_error), Toast.LENGTH_SHORT).show()
    }
}
fun saveTxt2imgPrompt(payload: Txt2ImgPayload) {
    savePrefString("txt_prompt", payload.prompt)
    savePrefString("txt_negative_prompt", payload.negative_prompt)
    sharedPreferences.edit().putInt("img_sampler", sampler_options.indexOf(payload.sampler_name)).apply()
    sharedPreferences.edit().putInt("txt_steps", payload.steps).apply()
}
fun saveImg2imgPrompt(payload: Img2ImgPayload) {
    savePrefString("img_prompt", payload.prompt)
    savePrefString("img_negative_prompt", payload.negative_prompt)
    sharedPreferences.edit().putInt("img_sampler", sampler_options.indexOf(payload.sampler_name)).apply()
    sharedPreferences.edit().putInt("img_steps", payload.steps).apply()
    sharedPreferences.edit().putInt("img_denoise", (payload.denoising_strength * 100).toInt()).apply()
}

fun setLocale(localeToSet: String, resources: Resources) {
    val localeListToSet = LocaleList(Locale(localeToSet))
    LocaleList.setDefault(localeListToSet)
    resources.configuration.setLocales(localeListToSet)
    @Suppress("DEPRECATION")
    resources.updateConfiguration(resources.configuration, resources.displayMetrics)
}