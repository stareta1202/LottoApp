package com.yongjun.lottoapp.ui.dashboard

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yongjun.lottoapp.LottoRepository.LottoRepository
import com.yongjun.lottoapp.R
import com.yongjun.lottoapp.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private lateinit var button: Button
    private lateinit var edtTextView: TextView
    private lateinit var recomendationTextView: TextView
    private var lottoMap = mutableMapOf(1 to 0)
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        for(i in 1..45) {
            lottoMap[i] = 0
        }
        val dashboardView = inflater.inflate(R.layout.fragment_dashboard, container, false)
        button = dashboardView.findViewById(R.id.dashboardSearchbutton)
        recomendationTextView = dashboardView.findViewById(R.id.recomendationTextView)
        edtTextView = dashboardView.findViewById(R.id.editTextNumber)
        if (container != null) {
            setupButton(container.context)
        }

        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)


        val root: View = binding.root

        return dashboardView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupButton(context: Context) {
        var tempLottoMap = lottoMap
        button.setOnClickListener {
            if (isOnline(context)) {


                for (i in 1..45) {
                    tempLottoMap[i] = 0
                }
                if (LottoRepository.lottoList.count() < LottoRepository.recentLottoOrder) {
                    Log.d("????", "It is small")
                    android.widget.Toast.makeText(context, "?????? ??? ?????? ??????????????????", Toast.LENGTH_LONG)
                        .show()
                }
                android.widget.Toast.makeText(context, "waitwait", 2)
                var a = edtTextView.text.toString().toInt()
                var gap = LottoRepository.recentLottoOrder - a
                Log.e("????", "${LottoRepository.lottoList}")
                Thread.sleep(500)
                for (i in gap..LottoRepository.recentLottoOrder - 1) {
                    Log.d("????", " loop ${i} ${LottoRepository.lottoList[i]}")
                    var lotto = LottoRepository.lottoList[i]

                    tempLottoMap[lotto.drwtNo1!!] = (tempLottoMap[lotto.drwtNo1!!] ?: 0) + 1
                    tempLottoMap[lotto.drwtNo2!!] = (tempLottoMap[lotto.drwtNo2!!] ?: 0) + 1
                    tempLottoMap[lotto.drwtNo3!!] = (tempLottoMap[lotto.drwtNo3!!] ?: 0) + 1
                    tempLottoMap[lotto.drwtNo4!!] = (tempLottoMap[lotto.drwtNo4!!] ?: 0) + 1
                    tempLottoMap[lotto.drwtNo5!!] = (tempLottoMap[lotto.drwtNo5!!] ?: 0) + 1
                    tempLottoMap[lotto.drwtNo6!!] = (tempLottoMap[lotto.drwtNo6!!] ?: 0) + 1
                    tempLottoMap[lotto.bnusNo!!] = (tempLottoMap[lotto.bnusNo!!] ?: 0) + 1
                }
                var sortedByValue =
                    tempLottoMap.toList().sortedWith(compareByDescending({ it.second })).toMap()
                var _sortedByValueList =
                    tempLottoMap.toList().sortedWith(compareByDescending({ it.second }))
                recomendationTextView.text = "" +
                        "${gap + 1} ~ ${LottoRepository.recentLottoOrder} (${edtTextView.text}) ?????? \n\n" +
                        " ${_sortedByValueList[0].first} ???   ${_sortedByValueList[0].second} ???  ??????!  \n" +
                        " ${_sortedByValueList[1].first} ???   ${_sortedByValueList[1].second} ???  ??????!  \n" +
                        " ${_sortedByValueList[2].first} ???   ${_sortedByValueList[2].second} ???  ??????!  \n" +
                        " ${_sortedByValueList[3].first} ???   ${_sortedByValueList[3].second} ???  ??????!  \n" +
                        " ${_sortedByValueList[4].first} ???   ${_sortedByValueList[4].second} ???  ??????!  \n" +
                        " ${_sortedByValueList[5].first} ???   ${_sortedByValueList[5].second} ???  ??????!  \n" +
                        " ${_sortedByValueList[6].first} ???   ${_sortedByValueList[6].second} ???  ??????!  \n" +
                        " ${_sortedByValueList[7].first} ???   ${_sortedByValueList[7].second} ???  ??????!  \n" +
                        " ${_sortedByValueList[8].first} ???   ${_sortedByValueList[8].second} ???  ??????!  \n" +
                        " ${_sortedByValueList[9].first} ???   ${_sortedByValueList[9].second} ???  ??????!  \n"
            } else {
                Toast.makeText(context, "????????? ?????????, ????????? ??????????????? ??????????????????", 2000).show()
            }
        }
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
}