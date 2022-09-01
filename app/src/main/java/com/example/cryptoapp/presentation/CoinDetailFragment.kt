package com.example.cryptoapp.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cryptoapp.databinding.FragmentCoinDetailBinding
import com.squareup.picasso.Picasso
import javax.inject.Inject

class CoinDetailFragment : Fragment() {

    private var _binding: FragmentCoinDetailBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: CoinViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[CoinViewModel::class.java]
    }

    private val component by lazy {
        (requireActivity().application as CoinApp).component
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoinDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fromSymbol = getSymbol()
        viewModel.getDetailInfo(fromSymbol).observe(viewLifecycleOwner) {
            binding.tvPrice.text = it.price.toString()
            binding.tvMinPrice.text = it.lowDay.toString()
            binding.tvMaxPrice.text = it.highDay.toString()
            binding.tvLastMarket.text = it.lastMarket
            binding.tvLastUpdate.text = it.lastUpdate
            binding.tvFromSymbol.text = it.fromSymbol
            binding.tvToSymbol.text = it.toSymbol
            Picasso.get().load(it.imageUrl).into(binding.ivLogoCoin)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getSymbol(): String {
        val args = requireArguments()

        if (!args.containsKey(EXTRA_FROM_SYMBOL)) {
            throw RuntimeException("Param coin info from symbol is absent")
        }
        return args.getString(EXTRA_FROM_SYMBOL, "")
    }

    companion object {

        private const val EXTRA_FROM_SYMBOL = "fSym"

        fun newInstance(fSym: String): CoinDetailFragment {
            return CoinDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_FROM_SYMBOL, fSym)
                }
            }
        }
    }
}