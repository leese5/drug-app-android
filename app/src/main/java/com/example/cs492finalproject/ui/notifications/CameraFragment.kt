package com.example.cs492finalproject.ui.notifications

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.cs492finalproject.R
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.DecoratedBarcodeView

class CameraFragment : Fragment(R.layout.fragment_notifications) {
    private val scanBarcodeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val barcode = data?.getStringExtra("SCAN_RESULT")
            barcode?.let {
                // Handle the scanned barcode data here
                Log.d("ScanBarcode", barcode)
                val setid = barcode
                val baseUrl = "https://dailymed.nlm.nih.gov/dailymed/search.cfm?labeltype=all&query="
                val url = baseUrl + setid
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                try {
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {

                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scanBtn: Button = view.findViewById(R.id.camera_button)
        scanBtn.setOnClickListener {
            // Start barcode scanning activity
            val intent = Intent(requireContext(), CaptureActivity::class.java)
            scanBarcodeLauncher.launch(intent)
        }
    }
}