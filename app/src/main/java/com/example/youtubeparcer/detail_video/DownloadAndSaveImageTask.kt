//package com.example.youtubeparcer.detail_video
//
//import android.content.Context
//import android.graphics.Bitmap
//import android.util.Log
//import java.io.File
//import java.io.FileOutputStream
//
//class DownloadAndSaveImageTask(context: Context) : AsyncTask<String, Unit, Unit>() {
//    private var mContext: WeakReference<Context> = WeakReference(context)
//
//    override fun doInBackground(vararg params: String?) {
//        val url = params[0]
//        val requestOptions = RequestOptions().override(100)
//            .downsample(DownsampleStrategy.CENTER_INSIDE)
//            .skipMemoryCache(true)
//            .diskCacheStrategy(DiskCacheStrategy.NONE)
//
//        mContext.get()?.let {
//            val bitmap = Glide.with(it)
//                .asBitmap()
//                .load(url)
//                .apply(requestOptions)
//                .submit()
//                .get()
//
//            try {
//                var file = it.getDir("Images", Context.MODE_PRIVATE)
//                file = File(file, "img.jpg")
//                val out = FileOutputStream(file)
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
//                out.flush()
//                out.close()
//                Log.i("Seiggailion", "Image saved.")
//            } catch (e: Exception) {
//                Log.i("Seiggailion", "Failed to save image.")
//            }
//        }
//    }
//}