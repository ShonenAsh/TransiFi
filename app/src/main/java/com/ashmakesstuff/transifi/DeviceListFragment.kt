package com.ashmakesstuff.transifi

import android.content.Context
import android.net.Uri
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager.PeerListListener
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.ListFragment


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DeviceListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DeviceListFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DeviceListFragment : ListFragment(), PeerListListener {
    override fun onPeersAvailable(peers: WifiP2pDeviceList?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private var peers = ArrayList<WifiP2pDevice>()
    private lateinit var device: WifiP2pDevice

    /**
     * Array adapter for ListFragment that maintains WifiP2pDevice list.
     */
    private inner class WiFiPeerListAdapter
    /**
     * @param context
     * @param textViewResourceId
     * @param objects
     */
        (
        context: Context, textViewResourceId: Int,
        private val items: List<WifiP2pDevice>
    ) : ArrayAdapter<WifiP2pDevice>(context, textViewResourceId, items) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
            var v = convertView
            if (v == null) {
                val vi = activity.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE
                ) as LayoutInflater
                v = vi.inflate(R.layout.row_devices, null)
            }
            val device = items[position]
            if (device != null) {
                val top = v!!.findViewById(R.id.device_name) as TextView
                val bottom = v.findViewById(R.id.device_details) as TextView
                if (top != null) {
                    top.text = device.deviceName
                }
                bottom.text = getDeviceStatus(device.status)
            }

            return v

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        this.listAdapter = WiFiPeerListAdapter(context!!, R.layout.row_devices, peers)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_device_list, container, false)
    }

    /**
     * @return this device
     */
    fun getDevice(): WifiP2pDevice {
        return device
    }

    private fun getDeviceStatus(deviceStatus: Int): String {
        Log.d(MainActivity.TAG, "Peer status :$deviceStatus")
        when (deviceStatus) {
            WifiP2pDevice.AVAILABLE -> return "Available"
            WifiP2pDevice.INVITED -> return "Invited"
            WifiP2pDevice.CONNECTED -> return "Connected"
            WifiP2pDevice.FAILED -> return "Failed"
            WifiP2pDevice.UNAVAILABLE -> return "Unavailable"
            else -> return "Unknown"
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)

        fun showDetails(device: WifiP2pDevice)

        fun cancelDisconnect()

        fun connect(config: WifiP2pConfig)

        fun disconnect()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DeviceListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DeviceListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
