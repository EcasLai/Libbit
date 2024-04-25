package com.example.libbit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.libbit.databinding.BottomsheetOrderBinding
import com.example.libbit.model.Book
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.stripe.android.ApiResultCallback
import com.stripe.android.PaymentConfiguration
import com.stripe.android.Stripe
import com.stripe.android.model.PaymentMethod
import com.stripe.android.model.PaymentMethodCreateParams
import com.stripe.android.view.CardInputWidget

class OrderBottomSheetFragment: BottomSheetDialogFragment() {
    private var _binding: BottomsheetOrderBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomsheetOrderBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        PaymentConfiguration.init(requireContext(), "pk_test_51NlDZ7EjnEdR9NEnpOtkvyacYViV1Ve0RunnLFLNQZ7tGPoeOyL5b1YX7ZYY67sUpg4WfFniNpFtlSI2N5GZy6vQ00m2q5PETm")
//
//        val cardInputWidgetTest = binding.cardInputWidget
//
//        val stripe = Stripe(requireContext(), "pk_test_51NlDZ7EjnEdR9NEnpOtkvyacYViV1Ve0RunnLFLNQZ7tGPoeOyL5b1YX7ZYY67sUpg4WfFniNpFtlSI2N5GZy6vQ00m2q5PETm")



        val book: Book? = arguments?.getParcelable("book")

        if (book != null) {
            // Set the book details
            binding.tvBsTitle.text = book.title
//            binding.tvBsPayAmount.text = book.author
            Glide.with(requireContext())
                .load(book.bookImage)
                .into(binding.ivBsBook)
        }

        binding.btnBsPayment.setOnClickListener{
//            val cardInputParams = cardInputWidgetTest.cardParams
//            if (cardInputParams != null) {
//                val paymentMethodParamsCard = PaymentMethodCreateParams.Card.create(cardInputParams.toString())
//                val paymentMethodParams = PaymentMethodCreateParams.create(card = paymentMethodParamsCard)
//
//                stripe.createPaymentMethod(
//                    paymentMethodParams,
//                    null, // Idempotency key, can be null
//                    null, // Stripe Account ID, can be null
//                    object : ApiResultCallback<PaymentMethod> {
//                        override fun onSuccess(result: PaymentMethod) {
//                            // Handle successful payment method creation
//                            // Now you can use this payment method to confirm a PaymentIntent
//                        }
//
//                        override fun onError(e: Exception) {
//                            // Handle error
//                        }
//                    }
//                )
//            } else {
//                // Handle invalid card details
//            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}