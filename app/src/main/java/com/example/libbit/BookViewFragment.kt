package com.example.libbit

import android.content.ContentValues.TAG
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil.isValidUrl
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.example.libbit.databinding.FragmentBookViewBinding
import com.example.libbit.databinding.FragmentMakeReservationBinding
import com.example.libbit.model.Book
import com.example.libbit.model.ReservationStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar
import com.github.barteksc.pdfviewer.PDFView
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class BookViewFragment : Fragment() {

    private var _binding: FragmentBookViewBinding? = null
    private val binding get() = _binding!!

    private lateinit var pdfView: PDFView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pdfView = binding.pdfView

        //Retrieve Book object from other fragment
        val book: Book? = arguments?.getParcelable("book")
        book?.let {
            RetrievePDFFromURL(pdfView).execute(it.bookUrl)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class RetrievePDFFromURL(private val pdfView: PDFView) :
        AsyncTask<String, Void, InputStream>() {

        override fun doInBackground(vararg params: String?): InputStream? {
            var inputStream: InputStream? = null
            try {
                val url = URL(params[0])
                val urlConnection: HttpURLConnection = url.openConnection() as HttpsURLConnection
                if (urlConnection.responseCode == 200) {
                    inputStream = BufferedInputStream(urlConnection.inputStream)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return inputStream
        }

        override fun onPostExecute(result: InputStream?) {
            pdfView.fromStream(result).load()
        }
    }
}