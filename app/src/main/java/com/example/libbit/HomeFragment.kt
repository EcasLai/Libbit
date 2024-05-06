package com.example.libbit

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libbit.adapter.BookAdapter
import com.example.libbit.adapter.BookSavedAdapter
import com.example.libbit.databinding.FragmentHomeBinding
import com.example.libbit.model.Book
import com.example.libbit.util.FirestoreUtil
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var bookAdapter: BookAdapter
    private lateinit var bookPopularAdapter: BookSavedAdapter
    private val executor = Executors.newSingleThreadScheduledExecutor()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                isGranted:Boolean ->
            if (isGranted){
                showCamera()
            }
        }

    private val scanLauncher =
        registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
            run {
                if (result.contents == null){
                    Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show()
                } else{
                    Toast.makeText(context, "succesful", Toast.LENGTH_SHORT).show()
                    setResult(result.contents)
                    handleQRContent(result.contents)
                }
            }
        }

    private fun showCamera() {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        options.setPrompt("Set QR Code")
        options.setCameraId(0)
        options.setBeepEnabled(false)
        options.setBarcodeImageEnabled(true)
        options.setOrientationLocked(false)

        scanLauncher.launch(options)
    }

    private fun setResult(string: String) {
        binding.tvDiscoverViewAll.text = string
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        initView()

        executor.scheduleAtFixedRate({
            val dateFormat = SimpleDateFormat("EEEE dd/MM/yyyy", Locale.ENGLISH)
            val calendar = Calendar.getInstance()

            val day = calendar.get(Calendar.DAY_OF_MONTH).toString()
            val weekday = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH).toString()
            val month = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH).toString()
            val year = calendar.get(Calendar.YEAR).toString()

            // Update the time display on the UI thread
            activity?.runOnUiThread {
                binding.tvDate.text = day
                binding.tvDay.text = weekday
                binding.tvMonthYear.text = "$month $year"
            }
        }, 0, 1, TimeUnit.HOURS)

        val carouselViewPager = binding.carouselViewPager

        //Define clickListener
        val itemClickListener = object : BookAdapter.OnItemClickListener {
            override fun onItemClick(book: Book) {
                // Handle item click event here, e.g., navigate to BookDetailFragment
                val bundle = Bundle().apply {
                    putParcelable("book", book)
                }
                val navController = findNavController()
                navController.navigate(R.id.action_homeFragment_to_bookDetailFragment, bundle)
            }
        }

        val itemClickListenerPopular = object : BookSavedAdapter.OnItemClickListener {
            override fun onItemClick(book: Book) {
                // Handle item click event here, e.g., navigate to BookDetailFragment
                val bundle = Bundle().apply {
                    putParcelable("book", book)
                }
                val navController = findNavController()
                navController.navigate(R.id.action_homeFragment_to_bookDetailFragment, bundle)
            }
        }

        binding.progressBarDiscover.visibility = View.VISIBLE

        bookAdapter = BookAdapter(ArrayList(), itemClickListener)

        bookPopularAdapter = BookSavedAdapter(ArrayList(), itemClickListenerPopular)

        FirestoreUtil.getBooks("books",
            onSuccess = { bookList ->
                activity?.runOnUiThread {
                    bookAdapter.updateData(bookList)
                    binding.progressBarDiscover.visibility = View.GONE
                }
            },
            onFailure = { exception ->
                // Handle any errors
                // You may want to display a message to the user
                binding.progressBarDiscover.visibility = View.GONE
            }
        )

        FirestoreUtil.getBooks("books",
            onSuccess = { bookList ->
                activity?.runOnUiThread {
                    bookPopularAdapter.updateData(bookList)
                    binding.progressBarDiscover.visibility = View.GONE
                }
            },
            onFailure = { exception ->
                // Handle any errors
                // You may want to display a message to the user
                binding.progressBarDiscover.visibility = View.GONE
            }
        )

        binding.rvBookHot.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = bookAdapter
        }

        binding.rvBookPopular.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = bookPopularAdapter
        }

        binding.imgSearch.setOnClickListener{
            val navController = findNavController()
            navController.navigate(R.id.action_homeFragment_to_searchFragment)
        }

        binding.imgQrScanner.setOnClickListener{
            checkPermissionCamera(requireContext())
        }
    }

    // Handle the result of requesting permissions
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted, initiate QR code scanning
                showCamera()
            } else {
                // Camera permission denied, show a message or handle accordingly
                Toast.makeText(context, "CAMERA permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermissionCamera(context: Context) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            showCamera()
        } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
            Toast.makeText(context, "CAMERA permission required", Toast.LENGTH_SHORT).show()
        } else {
            // Request camera permissions
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    private fun handleQRContent(contents: String){
        val (reservationId, userId) = parseQRContent(contents)

        if (reservationId != null && userId != null) {
            val bundle = Bundle().apply {
                putString("qrContent", contents)
            }
            findNavController().navigate(R.id.action_homeFragment_to_reservationApprovementFragment, bundle)

        } else {
            // Display an error message for invalid QR code content
            Toast.makeText(requireContext(), "Invalid QR code content", Toast.LENGTH_SHORT).show()
        }
    }


    //Extract QR Content
    private fun parseQRContent(contents: String): Pair<String?, String?> {

        val parts = contents.split("-")
        if (parts.size == 3 && parts[0] == "RES") {
            val reservationId = parts[1]
            val userId = parts[2]
            return Pair(reservationId, userId)
        }

        return Pair(null, null)
    }


    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
    }

}