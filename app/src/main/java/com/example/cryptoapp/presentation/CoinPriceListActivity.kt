package com.example.cryptoapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.ActivityCoinPriceListBinding
import com.example.cryptoapp.domain.CoinInfo
import com.example.cryptoapp.presentation.adapters.CoinInfoAdapter
import javax.inject.Inject

class CoinPriceListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCoinPriceListBinding
    private lateinit var viewModel: CoinViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (application as CoinApp).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityCoinPriceListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = CoinInfoAdapter(this@CoinPriceListActivity)
        adapter.onCoinClickListener = object : CoinInfoAdapter.OnCoinClickListener {
            override fun onCoinClick(coinInfo: CoinInfo) {
                if (binding.coinInfoContainer == null) {
                    val intent = CoinDetailActivity.newIntent(
                        this@CoinPriceListActivity,
                        coinInfo.fromSymbol
                    )
                    startActivity(intent)
                } else {
                    launchFragment(CoinDetailFragment.newInstance(coinInfo.fromSymbol))
                }
            }

        }
        binding.rvCoinPriceList.adapter = adapter
        binding.rvCoinPriceList.itemAnimator = null

        viewModel = ViewModelProvider(this, viewModelFactory)[CoinViewModel::class.java]

        viewModel.coinInfoList.observe(this) {
            adapter.submitList(it)
        }
    }

    private fun launchFragment(fragment: CoinDetailFragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.coinInfoContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
}